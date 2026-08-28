package net.nuclearteam.createnuclear.foundation.events;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.nuclearteam.createnuclear.foundation.events.overlay.IrradiatedOverlayRendererVision;

public class CNClientEvent {
    private static final HudRenderer HUD_RENDERER = new HudRenderer();

    public static void register() {
        HudRenderCallback.EVENT.register(HUD_RENDERER::onHudRender);
        HudRenderCallback.EVENT.register(IrradiatedOverlayRendererVision::renderOverlay);
    }
}
