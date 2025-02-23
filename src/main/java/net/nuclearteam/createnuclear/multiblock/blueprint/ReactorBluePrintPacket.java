package net.nuclearteam.createnuclear.multiblock.blueprint;

import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public class ReactorBluePrintPacket extends SimplePacketBase {

    private final CompoundTag tag;
    private final float heat;
    private final int graphiteTime;
    private final int uraniumTime;
    private final int countGraphiteRod;
    private final int countUraniumRod;
    private double progress;

    private static double totalInit;

    public ReactorBluePrintPacket(CompoundTag tag, float heat, int graphiteTime, int uraniumTime, int countGraphiteRod, int countUraniumRod) {
        this.tag = tag;
        this.heat = heat;
        this.graphiteTime = graphiteTime;
        this.uraniumTime = uraniumTime;
        this.countGraphiteRod = countGraphiteRod;
        this.countUraniumRod = countUraniumRod;
        this.progress = calculatePostgres(graphiteTime, countGraphiteRod, uraniumTime, countUraniumRod);
    }

    public ReactorBluePrintPacket(CompoundTag tag) {
        this(tag, 0f, 3600, 5000, 0, 0);
    }

    public ReactorBluePrintPacket(float heat) {
        this(new CompoundTag(), heat, 3600,5000, 0, 0);
    }

    public ReactorBluePrintPacket(int graphiteTime, int uraniumTime, int countGraphiteRod, int countUraniumRod) {
        this(new CompoundTag(), 0f, graphiteTime, uraniumTime, countGraphiteRod, countUraniumRod);
    }


    public ReactorBluePrintPacket(FriendlyByteBuf buf) {
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

        totalInit = calculateTotalInit(countUraniumRod, countGraphiteRod);
    }

    @Override
    public boolean handle(Context context) {
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null || !(player.containerMenu instanceof ReactorBluePrintMenu c)) return;
            c.countUraniumRod = this.countUraniumRod;
            c.countGraphiteRod = this.countGraphiteRod;
            c.graphiteTime = this.graphiteTime;
            c.uraniumTime = this.uraniumTime;
            c.progress = this.progress;
            c.heat = this.heat;
            c.sendUpdate = true;
        });
        return true;
    }

    public static double calculatePostgres(int a, int a_exp, int b, int b_exp) {
        double a2 = Math.pow(a, a_exp);
        double b2 = Math.pow(b, b_exp);
        double total = a2 + b2;

        if (Double.isNaN(totalInit)) return total/calculateTotalInit(a, b);
        else return total/totalInit;
    }

    public static double calculateTotalInit(int a, int b) {
        return Math.pow(3600, a) + Math.pow(5000, b);
    }
}
