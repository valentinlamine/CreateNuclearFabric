package net.nuclearteam.createnuclear.utils;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.simibubi.create.foundation.events.ClientEvents;
import io.github.fabricators_of_create.porting_lib.event.client.FogEvents;
import io.github.fabricators_of_create.porting_lib.event.client.FogEvents.ColorData;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.Camera;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.LavaFluid;
import net.nuclearteam.createnuclear.CreateNuclear;
import net.nuclearteam.createnuclear.effects.CNEffects;
import net.nuclearteam.createnuclear.fluid.CNFluids;
import net.minecraft.world.level.material.Fluid;
import net.nuclearteam.createnuclear.tags.CNTag;

public class CNClientEvent {

    public static void register() {
        ClientEvents.ModBusEvents.registerClientReloadListeners();

        FogEvents.SET_COLOR.register(CNClientEvent::getForColor);
        HudRenderCallback.EVENT.register(CNClientEvent::test);
    }

    private static void getForColor(ColorData event, float partialTicks) {
        Camera info = event.getCamera();
        Level level = Minecraft.getInstance().level;
        BlockPos blockPos = info.getBlockPosition();
        FluidState fluidState = level.getFluidState(blockPos);
        if (info.getPosition().y > blockPos.getY() + fluidState.getHeight(level, blockPos)) return;

        Fluid fluid = fluidState.getType();

        if (CNFluids.URANIUM.get().isSame(fluid)){
            event.setRed(56/ 255F);
            event.setGreen(255/ 255F);
            event.setBlue(8/ 255F);
        }
    }

    private static void test(GuiGraphics graphics, float partialTicks) {
        Window window = Minecraft.getInstance().getWindow();
        ResourceLocation POWDER_SNOW_OUTLINE_LOCATION = new ResourceLocation("textures/misc/powder_snow_outline.png");

        ResourceLocation PUMPKIN_BLUR_LOCATION = new ResourceLocation("textures/misc/pumpkinblur.png");
       

        LocalPlayer localPlayer = Minecraft.getInstance().player;
        RenderSystem.enableBlend();
        float scopeScale = 0.5f;

        float f2 = Minecraft.getInstance().getDeltaFrameTime();
        scopeScale = Mth.lerp(0.5f * f2, scopeScale, 1.125f);

        float f;
        float g = f = (float)Math.min(window.getScreenWidth(), window.getScreenHeight());
        float h = (Math.min((float)window.getScreenWidth() / f, (float)window.getScreenHeight() / g) * scopeScale);
        int i = Mth.floor(f * h);
        int j = Mth.floor(g * h);
        int k = (window.getScreenWidth() - i) / 2;
        int l = (window.getScreenHeight() - j) / 2;
        int m = k + i;
        int n = l + j;
        if (localPlayer.getEffect(CNEffects.RADIATION.get()) != null) {
            graphics.blit(POWDER_SNOW_OUTLINE_LOCATION, k, l, -90, 0.0f, 0.0f, i, j, i, j);
        }

        if (localPlayer.getInventory().getArmor(3).is(CNTag.ItemTags.ANTI_RADIATION_HELMET_DYE.tag)) {
            graphics.blit(PUMPKIN_BLUR_LOCATION, k, l, -90, 0.0f, 0.0f, i, j, i, j);
        }

        /*RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        graphics.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        graphics.blit(POWDER_SNOW_OUTLINE_LOCATION, 0, 0, -90, 0.0f, 0.0f, window.getWidth(), window.getHeight(), window.getWidth(), window.getHeight());
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        graphics.setColor(1.0f, 1.0f, 1.0f, 1.0f);*/
    }
}
