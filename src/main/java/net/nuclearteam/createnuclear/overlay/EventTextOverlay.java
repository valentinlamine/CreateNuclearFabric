package net.nuclearteam.createnuclear.overlay;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.nuclearteam.createnuclear.CreateNuclear;

/**
 * HUD overlay for displaying localized text when a specific event occurs.
 */
public class EventTextOverlay implements HudOverlay {
    private static int timer = 0;

    /**
     * Called via a network packet to activate the overlay for a specific duration.
     * @param displayDuration duration in ticks
     */
    public static void triggerEvent(int displayDuration) {
        timer = displayDuration;
    }

    @Override
    public boolean isActive() {
        return timer > 0;
    }

    @Override
    public void render(GuiGraphics graphics, float partialTicks) {
        if (timer-- <= 0) return;
        CreateNuclear.LOGGER.warn("hum EventTextOverlay: {}", timer);
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;
        Component text = Component.translatable("overlay.event_message", timer).withStyle(ChatFormatting.RED);
        int width = graphics.guiWidth();
        int x = (width - Minecraft.getInstance().font.width(text)) / 2;
        graphics.drawString(Minecraft.getInstance().font, text, x, 10, 0xFFFFFF);
    }

    @Override
    public int getPriority() {
        return 300; // render on top of other overlays
    }
}
