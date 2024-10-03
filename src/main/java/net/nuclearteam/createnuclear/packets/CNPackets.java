package net.nuclearteam.createnuclear.packets;

import com.simibubi.create.AllPackets;
import com.simibubi.create.foundation.networking.SimplePacketBase;
import me.pepperbell.simplenetworking.S2CPacket;
import me.pepperbell.simplenetworking.SimpleChannel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.nuclearteam.createnuclear.CreateNuclear;
import net.nuclearteam.createnuclear.multiblock.configuredItem.ConfiguredReactorItemPacket;

import static com.simibubi.create.foundation.networking.SimplePacketBase.NetworkDirection;
import static com.simibubi.create.foundation.networking.SimplePacketBase.NetworkDirection.PLAY_TO_CLIENT;
import static com.simibubi.create.foundation.networking.SimplePacketBase.NetworkDirection.PLAY_TO_SERVER;

import java.util.function.Function;

public enum CNPackets {
    // To server
    //CONFIGURE_REACTOR_CONTROLLER(ConfigureReactorControllerPacket.class, ConfigureReactorControllerPacket::new, PLAY_TO_SERVER),
    CONFIGURE_REACTOR_PATTERN(ConfiguredReactorItemPacket.class, ConfiguredReactorItemPacket::new, PLAY_TO_SERVER),
    ;

    public static final ResourceLocation CHANNEL_NAME = CreateNuclear.asResource("main");
    public static final int NETWORK_VERSION = 0;
    public static final String NETWORK_VERSION_STR = String.valueOf(NETWORK_VERSION);
    private static SimpleChannel channel;

    private PacketType<?> packetType;

    <T extends SimplePacketBase> CNPackets(Class<T> type, Function<FriendlyByteBuf, T> factory, NetworkDirection direction) {
        packetType = new PacketType<>(type, factory, direction);
    }

    public static void registerPackets() {
        channel = new SimpleChannel(CHANNEL_NAME);
        for (CNPackets packets : values()) packets.packetType.register();
    }

    public static SimpleChannel getChannel() {
        return channel;
    }

    public static void sendToNear(Level world, BlockPos pos, int range, Object message) {
        AllPackets.getChannel().sendToClientsAround((S2CPacket) message, (ServerLevel) world, pos, range);
    }

    private static class PacketType<T extends SimplePacketBase> {
        private static int index = 0;

        private Function<FriendlyByteBuf, T> decoder;
        private Class<T> type;
        private NetworkDirection direction;

        private PacketType(Class<T> type, Function<FriendlyByteBuf, T> factory, NetworkDirection direction) {
            decoder = factory;
            this.type = type;
            this.direction = direction;
        }

        private void register() {
            switch (direction) {
                case PLAY_TO_CLIENT -> getChannel().registerS2CPacket(type, index++, decoder);
                case PLAY_TO_SERVER -> getChannel().registerC2SPacket(type, index++, decoder);
            }
        }
    }
}
