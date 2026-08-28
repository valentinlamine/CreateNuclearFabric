package net.nuclearteam.createnuclear.content.multiblock.controller.consumable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.nuclearteam.createnuclear.content.multiblock.controller.manager.ReactorInputManagerI;

public class FluidConsumable implements IConsumable {
    private final String fluidName;
    private final int mbPerCycle;
    private final int baseTicks;

    public FluidConsumable(String fluidName, int mbPerCycle, int baseTicks) {
        this.fluidName = fluidName;
        this.mbPerCycle = mbPerCycle;
        this.baseTicks = baseTicks;
    }

    @Override
    public String getId() {
        return fluidName;
    }

    @Override
    public int computeTimer(int count) {
        return Math.max(1, baseTicks);
    }

    @Override
    public boolean consume(ReactorInputManagerI manager, Level level) {
        return false;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putString("type", "fluid");
        tag.putString("fluidName", fluidName);
        tag.putInt("mbPerCycle", mbPerCycle);
        tag.putInt("baseTicks", baseTicks);
        return tag;
    }

    public static FluidConsumable deserializeNBT(CompoundTag tag) {
        return new FluidConsumable(
                tag.getString("fluidName"),
                tag.getInt("mbPerCycle"),
                tag.getInt("baseTicks"));
    }
}
