package net.ynov.createnuclear.multiblock.controller;

import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.ynov.createnuclear.CreateNuclear;
import net.ynov.createnuclear.multiblock.controller.ReactorControllerBlockEntity.State;

import net.ynov.createnuclear.multiblock.controller.CNOption;

import java.util.List;

import static net.ynov.createnuclear.packets.CNPackets.getChannel;

public class ConfigureReactorControllerPacket extends SimplePacketBase {

    private CNOption option;
    private boolean set;
    private int value;
    private CompoundTag nbt = new CompoundTag();

    public ConfigureReactorControllerPacket(CNOption option, boolean set, int value, CompoundTag nbt) {
        this.option = option;
        this.set = set;
        this.value = value;
        this.nbt = nbt;
    }

    public ConfigureReactorControllerPacket(CNOption option) {
        this(option, true, 0, null);
    }

    public ConfigureReactorControllerPacket(CNOption option, int value) {
        this(option, true, value, null);
    }

    public ConfigureReactorControllerPacket(CNOption option, CompoundTag value) {
        this(option, true, 0, value);
    }

    public ConfigureReactorControllerPacket(FriendlyByteBuf buffer) {
        this(buffer.readEnum(CNOption.class), buffer.readBoolean(), buffer.readInt(), buffer.readNbt());
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeEnum(option);
        buffer.writeBoolean(set);
        buffer.writeInt(value);
        CreateNuclear.LOGGER.warn("d4 " + nbt);
        buffer.writeNbt(nbt);
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
                    break;
                case STOP:
                    be.powered = State.OFF;
                    be.heat = 0;
                    break;
                case HEAT:
                    be.heat = this.value;
                    break;
                case SCREEN_PATTERN:
                    be.screen_pattern = this.nbt;
                    //getChannel().sendToClientsInCurrentServer(new ConfigureReactorScreenPacket(option, this.nbt));
                    break;
                default:
                    break;
            }
            be.sendUpdate = true;
        });
        return true;
    }
}
