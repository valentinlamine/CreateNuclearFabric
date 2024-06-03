package net.ynov.createnuclear.multiblock.controller;

import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.ynov.createnuclear.CreateNuclear;
import net.ynov.createnuclear.multiblock.controller.ReactorControllerBlockEntity.State;

import java.util.List;

public class ConfigureReactorControllerPacket extends SimplePacketBase {

    public static enum CNOption {
        PLAY,
        STOP,
        COUNT_GRAPHITE_ROD,
        COUNT_URANIUM_ROD,
        REACTOR_POWER,
        HEAT,
        SCREEN_PATTERN
        ;//, INVALIDE_INPUT1, INVALIDE_INPUT2, LACK_URANIUM_ROD, LACK_GRAPHITE_ROD;
    }

    private CNOption option;
    private boolean set;
    private int value;
    private CompoundTag nbt = new CompoundTag();

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

    public ConfigureReactorControllerPacket(CNOption option, CompoundTag value) {
        this(option, true, 0);
        CreateNuclear.LOGGER.warn("d2 " + value);
        this.nbt = value;
        CreateNuclear.LOGGER.warn("d3 " + this.nbt);
    }

    public ConfigureReactorControllerPacket(FriendlyByteBuf buffer) {
        this(buffer.readEnum(CNOption.class), buffer.readBoolean(), buffer.readInt());
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeEnum(option);
        buffer.writeBoolean(set);
        buffer.writeInt(value);
        CreateNuclear.LOGGER.warn("d4 " + nbt);
        buffer.writeNbt(nbt);
    }

    private CompoundTag test() {
        return this.nbt;
    }

    @Override
    public boolean handle(Context context) {
        CreateNuclear.LOGGER.warn("dd " + nbt + " " + this.nbt + " " + test());
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null || !(player.containerMenu instanceof ReactorControllerMenu)) return;

            ReactorControllerBlockEntity be = ((ReactorControllerMenu) player.containerMenu).contentHolder;
            CreateNuclear.LOGGER.warn("d5 " + this.nbt);
            switch (option) {
                case PLAY:
                    be.powered = State.ON;
                    break;
                case STOP:
                    be.powered = State.OFF;
                    be.heat = 0;
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
                case SCREEN_PATTERN:
                    CreateNuclear.LOGGER.warn("d6 " + this.nbt);
                    be.screen_pattern = this.nbt;
                    CreateNuclear.LOGGER.warn("d7 " + this.nbt + " " + be.screen_pattern + " " + option);
                    break;
                default:
                    break;
            }
            be.sendUpdate = true;
        });
        return true;
    }
}
