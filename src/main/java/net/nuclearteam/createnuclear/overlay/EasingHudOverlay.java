package net.nuclearteam.createnuclear.overlay;

import net.minecraft.client.gui.GuiGraphics;

/**
 * Abstract HUD overlay with a smooth fade-in/out (ease-in-out) effect.
 */
public abstract class EasingHudOverlay implements HudOverlay {
    private float progress = 0f;
    protected float fadeSpeed = 0.03f;

    @Override
    public void render(GuiGraphics graphics, float partialTicks) {
        // Update progress based on the active state
        progress = isActive()
                ? Math.min(1f, progress + fadeSpeed)
                : Math.max(0f, progress - fadeSpeed);
        if (progress > 0f) {
            renderWithAlpha(graphics, partialTicks, ease(progress));
        }
    }

    /**
     * Smoothstep interpolation (ease-in-out).
     * @param t linear progress [0,1]
     * @return eased value
     */
    private float ease(float t) {
        return t * t * (3f - 2f * t);
    }

    /**
     * Renders the overlay with a specific alpha.
     * Subclasses implement actual drawing here.
     * @param graphics the GUI graphics context
     * @param partialTicks frame interpolation value
     * @param alpha transparency level [0,1]
     */
    protected abstract void renderWithAlpha(GuiGraphics graphics, float partialTicks, float alpha);
}
