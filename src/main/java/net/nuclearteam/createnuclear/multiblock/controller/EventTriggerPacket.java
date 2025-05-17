package net.nuclearteam.createnuclear.multiblock.controller;

import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.network.FriendlyByteBuf;
import net.nuclearteam.createnuclear.overlay.EventTextOverlay;

/**
 * Packet sent from server to client to trigger a localized event overlay.
 */
public class EventTriggerPacket extends SimplePacketBase {
    // Duration in ticks for which the overlay should be displayed
    private final int duration;
    public static int timer = 0;

    public EventTriggerPacket(int duration) {
        this.duration = duration;
    }

    // Decoder constructor
    public EventTriggerPacket(FriendlyByteBuf buffer) {
        this(buffer.readInt());
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeInt(duration);
    }

    @Override
    public boolean handle(Context context) {
        context.enqueueWork(() -> {
            if (timer > 0) timer--;
            EventTextOverlay.triggerEvent(duration);
        });
        return true;
    }
}
