package net.nuclearteam.createnuclear.foundation.events.overlay;

public interface HudOverlay {
    boolean isActive();
    void render(net.minecraft.client.gui.GuiGraphics graphics, float partialTicks);
    int getPriority();
}
