package net.ynov.createnuclear.multiblock.configuredItem;

import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.ynov.createnuclear.CreateNuclear;

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
    private double progress;

    private static final List<Map<String, Object>> items = new ArrayList<>();
    private static double totalInit;

    public ConfiguredReactorItemPacket(CompoundTag tag, float heat, int graphiteTime, int uraniumTime, int countGraphiteRod, int countUraniumRod) {
        this.tag = tag;
        this.heat = heat;
        this.graphiteTime = graphiteTime;
        this.uraniumTime = uraniumTime;
        this.countGraphiteRod = countGraphiteRod;
        this.countUraniumRod = countUraniumRod;
        this.progress = calculatePostgres(graphiteTime, countGraphiteRod, uraniumTime, countUraniumRod);
    }

    public ConfiguredReactorItemPacket(CompoundTag tag) {
        this(tag, 0f, 3600, 5000, 0, 0);
    }

    public ConfiguredReactorItemPacket(float heat) {
        this(new CompoundTag(), heat, 3600,5000, 0, 0);
    }

    public ConfiguredReactorItemPacket(int graphiteTime, int uraniumTime, int countGraphiteRod, int countUraniumRod) {
        this(new CompoundTag(), 0f, graphiteTime, uraniumTime, countGraphiteRod, countUraniumRod);
    }


    public ConfiguredReactorItemPacket(FriendlyByteBuf buf) {
        this(buf.readNbt(), buf.readFloat(), buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt());
        this.progress = buf.readDouble();
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
            if (player == null || !(player.containerMenu instanceof ConfiguredReactorItemMenu c)) return;
            c.countUraniumRod = this.countUraniumRod;
            c.countGraphiteRod = this.countGraphiteRod;
            c.graphiteTime = this.graphiteTime;
            c.uraniumTime = this.uraniumTime;
            c.progress = this.progress;
            c.heat = this.heat;
            CreateNuclear.LOGGER.warn("doomBidule " + c.progress + " heat: " + c.heat);
            c.sendUpdate = true;
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
