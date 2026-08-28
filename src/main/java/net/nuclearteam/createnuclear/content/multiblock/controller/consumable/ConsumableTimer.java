package net.nuclearteam.createnuclear.content.multiblock.controller.consumable;

import net.minecraft.nbt.CompoundTag;

public class ConsumableTimer {
    private final IConsumable consumable;
    private int remainingTicks;
    private int maxTicks;
    private int countInPattern;

    public ConsumableTimer(IConsumable consumable, int maxTicks, int count) {
        this.consumable = consumable;
        this.maxTicks = maxTicks;
        this.remainingTicks = maxTicks;
        this.countInPattern = count;
    }

    public boolean tick() {
        if (remainingTicks > 0)
            remainingTicks--;
        return remainingTicks <= 0;
    }

    public void reset(int newMaxTicks, int newCount) {
        this.maxTicks = newMaxTicks;
        this.remainingTicks = newMaxTicks;
        this.countInPattern = newCount;
    }

    public IConsumable getConsumable() {
        return consumable;
    }

    public String getId() {
        return consumable.getId();
    }

    public int getCountInPattern() {
        return countInPattern;
    }

    public int getMaxTicks() {
        return maxTicks;
    }

    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.put("consumable", consumable.serializeNBT());
        tag.putInt("remainingTicks", remainingTicks);
        tag.putInt("maxTicks", maxTicks);
        tag.putInt("countInPattern", countInPattern);
        return tag;
    }

    public static ConsumableTimer deserializeNBT(CompoundTag tag) {
        IConsumable consumable = IConsumable.deserializeNBT(tag.getCompound("consumable"));
        ConsumableTimer t = new ConsumableTimer(
                consumable, tag.getInt("maxTicks"), tag.getInt("countInPattern"));
        t.remainingTicks = tag.getInt("remainingTicks");
        return t;
    }
}
