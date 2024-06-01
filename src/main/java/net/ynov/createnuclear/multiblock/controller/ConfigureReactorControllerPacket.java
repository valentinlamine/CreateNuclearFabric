package net.ynov.createnuclear.multiblock.controller;

import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.ynov.createnuclear.multiblock.controller.ReactorControllerBlockEntity.State;

public class ConfigureReactorControllerPacket extends SimplePacketBase {

    public static enum CNOption {
        PLAY, STOP;//, INVALIDE_INPUT1, INVALIDE_INPUT2, LACK_URANIUM_ROD, LACK_GRAPHITE_ROD;
    }

    private CNOption option;
    private boolean set;

    public ConfigureReactorControllerPacket(CNOption option, boolean set) {
        this.option = option;
        this.set = set;
    }
    public ConfigureReactorControllerPacket(FriendlyByteBuf buffer) {
        this(buffer.readEnum(CNOption.class), buffer.readBoolean());
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeEnum(option);
        buffer.writeBoolean(set);
    }

    @Override
    public boolean handle(Context context) {
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null || !(player.containerMenu instanceof ReactorControllerMenu)) return;

            ReactorControllerBlockEntity be = ((ReactorControllerMenu) player.containerMenu).contentHolder;
            switch (option) {
                case PLAY:
                    be.powered = State.ON;
                    be.created = true;
                    be.destroyed = false;
                    break;
                case STOP:
                    be.powered = State.OFF;
                    be.created = false;
                    be.destroyed = true;
                    break;
                /*case INVALIDE_INPUT1:
                case INVALIDE_INPUT2:
                case LACK_URANIUM_ROD:
                case LACK_GRAPHITE_ROD:
                    break;*/
                default:
                    break;
            }
            be.sendUpdate = true;
        });
        return true;
    }
}
