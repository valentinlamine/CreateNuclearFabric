package net.ynov.createnuclear.multiblock.configuredItem;

import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConfiguredReactorItemPacket extends SimplePacketBase {

    private final CompoundTag tag;
    private final float heat;
    private final int graphiteTime;
    private final int uraniumTime;
    private final int countGraphiteRod;
    private final int countUraniumRod;
    private final double progress;

    private static final List<Map<String, Object>> items = new ArrayList<>();
    private static double totalInit;

    public ConfiguredReactorItemPacket(CompoundTag tag, float heat, int graphiteTime, int uraniumTime, int countGraphiteRod, int countUraniumRod, double progress) {
        this.tag = tag;
        this.heat = heat;
        this.graphiteTime = graphiteTime;
        this.uraniumTime = uraniumTime;
        this.countGraphiteRod = countGraphiteRod;
        this.countUraniumRod = countUraniumRod;
        this.progress = progress;
    }

    public ConfiguredReactorItemPacket(CompoundTag tag) {
        this(tag, 0f, 3600, 5000, 0, 0, 0);
    }

    public ConfiguredReactorItemPacket(float heat) {
        this(new CompoundTag(), heat, 3600,5000, 0, 0, 0);
    }

    public ConfiguredReactorItemPacket(int graphiteTime, int uraniumTime, int countGraphiteRod, int countUraniumRod, int progress) {
        this(new CompoundTag(), 0f, graphiteTime, uraniumTime, countGraphiteRod, countUraniumRod, progress);
    }

    public ConfiguredReactorItemPacket(int graphiteTime, int countGraphiteRod, int uraniumTime, int countUraniumRod) {
        this(new CompoundTag(), 0f, graphiteTime, uraniumTime, countGraphiteRod, countUraniumRod, calculatePostgres(graphiteTime, countGraphiteRod, uraniumTime, countUraniumRod));
    }

    public ConfiguredReactorItemPacket(int progress) {
        this(new CompoundTag(), 0f, 3600, 5000, 0, 0, progress);
    }

    public ConfiguredReactorItemPacket(FriendlyByteBuf buf) {
        this(buf.readNbt(), buf.readFloat(), buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt());
    }


    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeNbt(tag);
        buffer.writeFloat(heat);
        buffer.writeInt(graphiteTime);
        buffer.writeInt(uraniumTime);
        buffer.writeInt(countGraphiteRod);
        buffer.writeInt(countUraniumRod);
        buffer.writeDouble(progress);

        totalInit = Math.pow(3600, countUraniumRod) + Math.pow(5000, countGraphiteRod);
    }

    @Override
    public boolean handle(Context context) {
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null || !(player.containerMenu instanceof ConfiguredReactorItemMenu)) return;
            ConfiguredReactorItemMenu c = (ConfiguredReactorItemMenu) player.containerMenu;

        });
        return true;
    }

    private static double calculatePostgres(int a, int a_exp, int b, int b_exp) {
        double a2 = Math.pow(a, a_exp);
        double b2 = Math.pow(b, b_exp);
        double total = a2 + b2;

        return total/totalInit;
    }
}
