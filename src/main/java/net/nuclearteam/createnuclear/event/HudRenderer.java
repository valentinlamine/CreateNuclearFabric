package net.nuclearteam.createnuclear.event;

import net.minecraft.client.gui.GuiGraphics;
import net.nuclearteam.createnuclear.overlay.EventTextOverlay;
import net.nuclearteam.createnuclear.overlay.HelmetOverlay;
import net.nuclearteam.createnuclear.overlay.HudOverlay;
import net.nuclearteam.createnuclear.overlay.RadiationOverlay;

import java.util.Comparator;
import java.util.List;

/**
 * Manages and renders all registered HUD overlays in priority order.
 */
public class HudRenderer {
    private final List<HudOverlay> overlays = List.of(
            new RadiationOverlay(),
            new HelmetOverlay(),
            new EventTextOverlay()
    );

    /**
     * Called each render tick to draw active overlays.
     * @param graphics GUI graphics context
     * @param partialTicks frame interpolation value
     */
    public void onHudRender(GuiGraphics graphics, float partialTicks) {
        overlays.stream()
                .filter(HudOverlay::isActive)
                .sorted(Comparator.comparingInt(HudOverlay::getPriority))
                .forEach(overlay -> overlay.render(graphics, partialTicks));
    }
}
