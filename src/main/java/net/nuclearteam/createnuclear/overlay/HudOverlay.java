package net.nuclearteam.createnuclear.overlay;

import net.minecraft.client.gui.GuiGraphics;

/**
 * Base interface for all HUD overlays.
 */
public interface HudOverlay {
    /**
     * Determines whether this overlay should be rendered.
     * @return true if the overlay is active, false otherwise.
     */
    boolean isActive();

    /**
     * Renders the overlay.
     * @param graphics the GUI graphics context
     * @param partialTicks frame interpolation value
     */
    void render(GuiGraphics graphics, float partialTicks);

    /**
     * Computes the dynamic rendering priority of the overlay.
     * Lower values render behind higher values.
     * @return integer priority value
     */
    int getPriority();
}
