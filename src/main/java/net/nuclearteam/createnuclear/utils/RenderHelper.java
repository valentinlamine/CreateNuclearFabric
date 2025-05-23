package net.nuclearteam.createnuclear.utils;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

/**
 * Helper class for rendering full-screen overlays with optional scaling and caching.
 */
@SuppressWarnings("unused")
public class RenderHelper {
    private static float lastAlpha = Float.NaN;
    private static float lastCoverage = Float.NaN;
    private static boolean lastFirstPerson = false;

    /**
     * Renders a full-screen texture with given transparency and coverage scale.
     * Caches the last parameters to skip redundant rendering.
     *
     * @param graphics      GUI graphics context
     * @param texture       the texture to render
     * @param alpha         transparency [0,1]
     * @param coverage      scale factor (1.0 = normal size)
     * @param onlyFirstPerson  if true, renders only in first-person camera mode
     */
    public static void renderOverlay(GuiGraphics graphics, ResourceLocation texture,
                                     float alpha, float coverage, boolean onlyFirstPerson) {
        boolean isFirstPerson = Minecraft.getInstance().options.getCameraType().isFirstPerson();

        // If rendering is restricted to first-person, and we're not in it, skip
        if (onlyFirstPerson && !isFirstPerson) return;

        // Skip rendering if parameters unchanged

        lastAlpha = alpha;
        lastCoverage = coverage;
        lastFirstPerson = isFirstPerson;

        int width = graphics.guiWidth();
        int height = graphics.guiHeight();

        graphics.setColor(1f, 1f, 1f, alpha);
        RenderSystem.enableBlend();

        if (coverage != 1f) {
            // Center + scale
            graphics.pose().pushPose();
            graphics.pose().translate(width / 2f, height / 2f, 0);
            //graphics.pose().scale(coverage, coverage, 1f);

            graphics.pose().translate(-width / 2f, -height / 2f, 0);
            graphics.blit(texture, 0, 0, -90, 0, 0, width, height, width, height);
            graphics.pose().popPose();
        }

        if (coverage == 1f) {
            graphics.blit(texture, 0, 0, -90, 0, 0, width, height, width, height);
        }

        RenderSystem.disableBlend();
    }

    /**
     * Convenience overload: always renders in any camera mode, no scaling.
     */
    public static void renderOverlay(GuiGraphics graphics, ResourceLocation texture, float alpha, float coverage) {
        renderOverlay(graphics, texture, alpha, coverage, false);
    }

    /**
     * Convenience overload: only in first-person, no scaling.
     */
    public static void renderFirstPersonOverlay(GuiGraphics graphics,
                                                ResourceLocation texture, float alpha, float coverage) {
        renderOverlay(graphics, texture, alpha, coverage, true);
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
        int width = graphics.guiWidth();
        int height = graphics.guiHeight();

        // Set the color with the provided alpha
        graphics.setColor(1f, 1f, 1f, alpha);

        // Simple blit without scaling (normal size)
        RenderSystem.enableBlend();
        graphics.blit(texture, 0, 0, -90, 0, 0, width, height, width, height);
        RenderSystem.disableBlend();
    }
}
