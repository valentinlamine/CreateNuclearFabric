package net.nuclearteam.createnuclear.content.redstone.displayLink.source;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.util.Mth;

public final class ReactorGaugeRenderer {
    private ReactorGaugeRenderer() {}

    static MutableComponent drawGauge(int current, int max, ChatFormatting color, int width) {
        int filled = (int) (Mth.clamp((float) current / max, 0, 1) * width);
        return Component.literal("█".repeat(filled) + "▒".repeat(Math.max(0, width - filled))).withStyle(color);
    }
}
