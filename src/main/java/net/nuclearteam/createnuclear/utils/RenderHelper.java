package net.nuclearteam.createnuclear.utils;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

/**
 * Helper class for rendering full-screen overlays with optional scaling and caching.
 */
public class RenderHelper {
    private static float lastAlpha = -1f;
    private static float lastCoverage = -1f;

    /**
     * Renders a full-screen texture with given transparency and coverage scale.
     * Uses caching to skip rendering when parameters are unchanged.
     * @param graphics GUI graphics context
     * @param texture the texture to render
     * @param alpha transparency [0,1]
     * @param coverage scale factor (1.0 = normal size)
     */
    public static void renderTextureOverlay(GuiGraphics graphics, ResourceLocation texture, float alpha, float coverage) {
        // Skip rendering if parameters unchanged
        /* if (alpha == lastAlpha && coverage == lastCoverage) return;
        lastAlpha = alpha;
        lastCoverage = coverage;*/

        int width = graphics.guiWidth();
        int height = graphics.guiHeight();
        graphics.setColor(1f, 1f, 1f, alpha);

        if (coverage == 1f) {
            // Simple blit without scaling
            graphics.blit(texture, 0, 0, -90, 0, 0, width, height, width, height);
        } else {
            // Blit with scaling centered on screen
            graphics.pose().pushPose();
            graphics.pose().translate(width / 2f, height / 2f, 0);
            graphics.pose().scale(coverage, coverage, 1f);
            graphics.pose().translate(-width / 2f, -height / 2f, 0);
            graphics.blit(texture, 0, 0, -90, 0, 0, width, height, width, height);
            graphics.pose().popPose();
        }
    }

    /**
     * Renders a full-screen texture with given transparency.
     * This version does not apply scaling (no zoom effect).
     *
     * @param graphics GUI graphics context
     * @param texture the texture to render
     * @param alpha transparency [0,1]
     */
    public static void renderTextureOverlay(GuiGraphics graphics, ResourceLocation texture, float alpha) {
        // Skip rendering if parameters unchanged
        if (alpha == lastAlpha) return;
        lastAlpha = alpha;

        int width = graphics.guiWidth();
        int height = graphics.guiHeight();

        // Set the color with the provided alpha
        graphics.setColor(1f, 1f, 1f, alpha);

        // Simple blit without scaling (normal size)
        graphics.blit(texture, 0, 0, -90, 0, 0, width, height, width, height);
    }
}
