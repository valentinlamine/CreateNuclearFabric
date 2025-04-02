package net.nuclearteam.createnuclear.utils;

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
import net.minecraft.world.level.Level;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.material.FluidState;
import net.nuclearteam.createnuclear.CreateNuclear;
import net.nuclearteam.createnuclear.effects.CNEffects;
import net.nuclearteam.createnuclear.fluid.CNFluids;
import net.minecraft.world.level.material.Fluid;
import net.nuclearteam.createnuclear.tags.CNTag;

public class CNClientEvent {

    private static float irradiatedVisionAlpha = 0.0f; // Variable to manage alpha

    public static void register() {
        ClientEvents.ModBusEvents.registerClientReloadListeners();

        FogEvents.SET_COLOR.register(CNClientEvent::getForColor);
        HudRenderCallback.EVENT.register(CNClientEvent::hudRender);
    }

    private static void getForColor(ColorData event, float partialTicks) {
        Camera info = event.getCamera();
        Level level = Minecraft.getInstance().level;
        BlockPos blockPos = info.getBlockPosition();
        FluidState fluidState = level.getFluidState(blockPos);
        if (info.getPosition().y > blockPos.getY() + fluidState.getHeight(level, blockPos)) return;

        Fluid fluid = fluidState.getType();

        if (CNFluids.URANIUM.get().isSame(fluid)){
            event.setRed(56 / 255F);
            event.setGreen(255 / 255F);
            event.setBlue(8 / 255F);
        }
    }

    private static void hudRender(GuiGraphics graphics, float partialTicks) {
        ResourceLocation IRRADIATED_VISION = CreateNuclear.asResource("textures/misc/irradiated_vision.png");
        ResourceLocation HELMETTEST = CreateNuclear.asResource("textures/misc/test_texture.png");

        LocalPlayer localPlayer = Minecraft.getInstance().player;
        RenderSystem.enableBlend();

        if (localPlayer.getEffect(CNEffects.RADIATION.get()) != null) {
            if (irradiatedVisionAlpha < 1.0f) {
                irradiatedVisionAlpha += 0.01f; // Increment alpha gradually
            }
        } else {
            if (irradiatedVisionAlpha > 0.0f) {
                irradiatedVisionAlpha -= 0.01f; // Decrement alpha gradually
            }
        }

        if (irradiatedVisionAlpha > 0.0f) {
            renderTextureOverlay(graphics, IRRADIATED_VISION, irradiatedVisionAlpha);
        }

        if (localPlayer.getInventory().getArmor(3).is(CNTag.ItemTags.ANTI_RADIATION_HELMET_FULL_DYE.tag)) {
            renderTextureOverlay(graphics, HELMETTEST, 1f);
            Minecraft.getInstance().gui.renderHotbar(12f, graphics);
        }
    }

    private static void renderTextureOverlay(GuiGraphics guiGraphics, ResourceLocation shaderLocation, float alpha) {
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        guiGraphics.setColor(1.0f, 1.0f, 1.0f, alpha);
        guiGraphics.blit(shaderLocation, 0, 0, -90, 0.0f, 0.0f, guiGraphics.guiWidth(), guiGraphics.guiHeight(), guiGraphics.guiWidth(), guiGraphics.guiHeight());
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        guiGraphics.setColor(1.0f, 1.0f, 1.0f, 1.0f);
    }
}
