package net.nuclearteam.createnuclear.content.multiblock.controller.consumable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.nuclearteam.createnuclear.api.multiblock.rods.RodType;
import net.nuclearteam.createnuclear.content.multiblock.controller.manager.ReactorInputManagerI;

public class ItemConsumable implements IConsumable {
    private final String itemName;
    private final RodType rodType;

    public ItemConsumable(String itemName, RodType rodType) {
        this.itemName = itemName;
        this.rodType = rodType;
    }

    @Override
    public String getId() {
        return itemName;
    }

    @Override
    public int computeTimer(int count) {
        int base = Math.max(1, rodType.rodTimer().get());
        return Math.max(1, (int) (base / Math.max(1, count)));
    }

    @Override
    public boolean consume(ReactorInputManagerI manager, Level level) {
        return manager.extractItemByName(level, itemName);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putString("type", "item");
        tag.putString("itemName", itemName);
        return tag;
    }

    public static ItemConsumable deserializeNBT(CompoundTag tag) {
        String itemName = tag.getString("itemName");
        return new ItemConsumable(itemName, null);
    }
}
