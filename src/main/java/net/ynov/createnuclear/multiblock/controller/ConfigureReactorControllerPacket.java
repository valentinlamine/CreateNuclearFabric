package net.ynov.createnuclear.multiblock.controller;

import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.ynov.createnuclear.CreateNuclear;
import net.ynov.createnuclear.multiblock.controller.ReactorControllerBlockEntity.State;

public class ConfigureReactorControllerPacket extends SimplePacketBase {

    public static enum CNOption {
        PLAY,
        STOP,
        COUNT_GRAPHITE_ROD,
        COUNT_URANIUM_ROD,
        REACTOR_POWER,
        HEAT,
        ;//, INVALIDE_INPUT1, INVALIDE_INPUT2, LACK_URANIUM_ROD, LACK_GRAPHITE_ROD;
    }

    private CNOption option;
    private boolean set;
    private int value;

    public ConfigureReactorControllerPacket(CNOption option, boolean set, int value) {
        this.option = option;
        this.set = set;
        this.value = value;
    }

    public ConfigureReactorControllerPacket(CNOption option) {
        this(option, true, 0);
    }

    public ConfigureReactorControllerPacket(CNOption option, int value) {
        this(option, true, value);
    }

    public ConfigureReactorControllerPacket(FriendlyByteBuf buffer) {
        this(buffer.readEnum(CNOption.class), buffer.readBoolean(), buffer.readInt());
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeEnum(option);
        buffer.writeBoolean(set);
        buffer.writeInt(value);
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
                /*case COUNT_URANIUM_ROD:
                    be.countUraniumRod = this.value;
                    break;
                case COUNT_GRAPHITE_ROD:
                    be.countGraphiteRod = this.value;
                    break;
                case REACTOR_POWER:
                    be.reactorPower = this.value;*/
                case HEAT:
                    be.heat = this.value;
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
