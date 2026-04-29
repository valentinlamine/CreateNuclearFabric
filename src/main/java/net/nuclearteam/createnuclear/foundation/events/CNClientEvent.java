package net.nuclearteam.createnuclear.foundation.events;

import com.mojang.blaze3d.systems.RenderSystem;
import com.simibubi.create.foundation.events.ClientEvents;
import io.github.fabricators_of_create.porting_lib.event.client.ClientWorldEvents;
import io.github.fabricators_of_create.porting_lib.event.client.FogEvents;
import io.github.fabricators_of_create.porting_lib.event.client.FogEvents.ColorData;
import io.github.fabricators_of_create.porting_lib.event.client.ParticleManagerRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.Camera;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.level.Level;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.material.FluidState;
import net.nuclearteam.createnuclear.CNEffects;
import net.nuclearteam.createnuclear.CNFluids;
import net.nuclearteam.createnuclear.CNParticleTypes;
import net.nuclearteam.createnuclear.CreateNuclear;
import net.minecraft.world.level.material.Fluid;

public class CNClientEvent {

    private static float irradiatedVisionAlpha = 0.0f; // Variable to manage alpha <- Put it in the player to have it per character and not globally.

    private static final HudRenderer HUD_RENDERER = new HudRenderer();

    public static void register() {
        ClientEvents.ModBusEvents.registerClientReloadListeners();

        FogEvents.SET_COLOR.register(CNClientEvent::getForColor);
        HudRenderCallback.EVENT.register(HUD_RENDERER::onHudRender);
        HudRenderCallback.EVENT.register(CNClientEvent::radiationOverlay);

        ParticleManagerRegistrationCallback.EVENT.register(CNParticleTypes::registerFactories);

        ClientWorldEvents.LOAD.register(CommonEvents::onLoadWorld);
        ClientWorldEvents.UNLOAD.register(CommonEvents::onUnloadWorld);
    }

    private static void getForColor(ColorData event, float partialTicks) {
        Camera info = event.getCamera();
        Level level = Minecraft.getInstance().level;
        BlockPos blockPos = info.getBlockPosition();
        FluidState fluidState = level.getFluidState(blockPos);
        if (info.getPosition().y > blockPos.getY() + fluidState.getHeight(level, blockPos)) return;

        Fluid fluid = fluidState.getType();

        if (CNFluids.URANIUM.get().isSame(fluid)) {
            event.setRed(56 / 255F);
            event.setGreen(255 / 255F);
            event.setBlue(8 / 255F);
        }
        if (CNFluids.THORIUM.get().isSame(fluid)){
            event.setRed(8 / 255F);
            event.setGreen(56 / 255F);
            event.setBlue(255 / 255F);
        }
    }

    private static void radiationOverlay(GuiGraphics graphics, float partialTicks) {
        ResourceLocation IRRADIATED_VISION = CreateNuclear.asResource("textures/misc/irradiated_vision.png");
        LocalPlayer localPlayer = Minecraft.getInstance().player;
        RenderSystem.enableBlend();
        if (localPlayer.hasEffect(CNEffects.RADIATION.get())) {
            irradiatedVisionAlpha = Math.min(1.0f, irradiatedVisionAlpha + 0.01f);
        } else {
            irradiatedVisionAlpha = Math.max(0.0f, irradiatedVisionAlpha - 0.01f);
        }
        if (irradiatedVisionAlpha > 0.0f) {
            renderTextureOverlay(graphics, IRRADIATED_VISION, irradiatedVisionAlpha);
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
