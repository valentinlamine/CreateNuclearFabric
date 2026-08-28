package net.nuclearteam.createnuclear.content.multiblock.bluePrintItem;

import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

@SuppressWarnings("unused")
public class ReactorBluePrintItemPacket extends SimplePacketBase {

    private final CompoundTag tag;
    private final float heat;
    private final int coolerTime;
    private final int fuelTime;
    private final int countCoolerRod;
    private final int countFuelRod;
    private final double progress;

    public ReactorBluePrintItemPacket(CompoundTag tag, float heat, int coolerTime, int fuelTime, int countCoolerRod, int countFuelRod) {
        this.tag = tag;
        this.heat = heat;
        this.coolerTime = coolerTime;
        this.fuelTime = fuelTime;
        this.countCoolerRod = countCoolerRod;
        this.countFuelRod = countFuelRod;
        this.progress = calculatePostgres(coolerTime, countCoolerRod, fuelTime, countFuelRod);
    }

    public ReactorBluePrintItemPacket(CompoundTag tag) {
        this(tag, 0f, 3600, 5000, 0, 0);
    }

    public ReactorBluePrintItemPacket(float heat) {
        this(new CompoundTag(), heat, 3600, 5000, 0, 0);
    }

    public ReactorBluePrintItemPacket(int coolerTime, int fuelTime, int countCoolerRod, int countFuelRod) {
        this(new CompoundTag(), 0f, coolerTime, fuelTime, countCoolerRod, countFuelRod);
    }


    public ReactorBluePrintItemPacket(FriendlyByteBuf buf) {
        this(buf.readNbt(), buf.readFloat(), buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt());
    }


    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeNbt(tag);
        buffer.writeFloat(heat);
        buffer.writeInt(coolerTime);
        buffer.writeInt(fuelTime);
        buffer.writeInt(countCoolerRod);
        buffer.writeInt(countFuelRod);
    }

    @Override
    public boolean handle(Context context) {
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null || !(player.currentScreenHandler instanceof ReactorBluePrintMenu c)) return;
            c.countFuelRod = this.countFuelRod;
            c.countCooledRod = this.countCoolerRod;
            c.fuelTime = this.fuelTime;
            c.coolerTime = this.coolerTime;
            c.progress = this.progress;
            c.heat = this.heat;
            c.sendUpdate = true;
        });
        return true;
    }

    public static double calculatePostgres(int coolerTime, int countCoolerRod, int fuelTime, int countFuelRod) {
        double coolerTotal = Math.pow(coolerTime, countCoolerRod);
        double fuelTotal = Math.pow(fuelTime, countFuelRod);
        double total = coolerTotal + fuelTotal;

        double totalInit = calculateTotalInit(countCoolerRod, countFuelRod);
        if (totalInit == 0 || Double.isNaN(totalInit)) return 0;
        return total / totalInit;
    }

    public static double calculateTotalInit(int countCoolerRod, int countFuelRod) {
        // We assume 3600 and 5000 as base fallback times for initialization if needed.
        // It should ideally use the actual base times from config, but this works for now.
        return Math.pow(3600, countCoolerRod) + Math.pow(5000, countFuelRod);
    }
}
