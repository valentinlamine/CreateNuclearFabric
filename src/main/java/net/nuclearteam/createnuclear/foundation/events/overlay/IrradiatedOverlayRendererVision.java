package net.nuclearteam.createnuclear.foundation.events.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.GameType;
import net.nuclearteam.createnuclear.CNEffects;
import net.nuclearteam.createnuclear.CreateNuclear;

public class IrradiatedOverlayRendererVision {
    private static float irradiatedVisionAlpha = 0.0f; // Variable to manage alpha <- Put it in the player to have it per character and not globally.

    public static final ResourceLocation IRRADIATED_VISION = CreateNuclear.asResource("textures/misc/irradiated_vision/irradiated_vision.png");

    public static void renderOverlay(GuiGraphics graphics, float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.gameMode == null || mc.player == null) {
            irradiatedVisionAlpha = 0.0f;
            return;
        }
        GameType playerMode = mc.gameMode.getPlayerMode();
        // Skip the irradiated vision overlay in creative/spectator: the effect icon still
        // shows in the HUD so the player stays aware, but the screen overlay is not applied.
        if (mc.options.hideGui || playerMode == GameType.SPECTATOR || playerMode == GameType.CREATIVE) {
            irradiatedVisionAlpha = 0.0f;
            return;
        }
        LocalPlayer localPlayer = mc.player;
        RenderSystem.enableBlend();
        if (localPlayer.hasEffect(CNEffects.RADIATION.get())) {
            irradiatedVisionAlpha = Math.min(1.0f, irradiatedVisionAlpha + 0.01f);
        } else {
            irradiatedVisionAlpha = Math.max(0.0f, irradiatedVisionAlpha - 0.01f);
        }
        if (irradiatedVisionAlpha > 0.0f) {
            renderTextureOverlay(graphics, IRRADIATED_VISION, irradiatedVisionAlpha, true);
        }
    }

    public static void renderTextureOverlay(GuiGraphics guiGraphics, ResourceLocation texture, float alpha, boolean onlyFirstPerson) {
        boolean isFirstPerson = Minecraft.getInstance().options.getCameraType().isFirstPerson();

        // If rendering is restricted to first-person and we're not in it, skip
        if (onlyFirstPerson && !isFirstPerson) return;

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        guiGraphics.setColor(1f, 1f, 1f, alpha);
        guiGraphics.blit(texture, 0, 0, -90, 0.0f, 0.0f, guiGraphics.guiWidth(), guiGraphics.guiHeight(), guiGraphics.guiWidth(), guiGraphics.guiHeight());
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        guiGraphics.setColor(1.0f, 1.0f, 1.0f, 1.0f);
    }
}
