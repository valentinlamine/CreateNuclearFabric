package net.ynov.createnuclear.multiblock.configuredItem;

import com.simibubi.create.foundation.utility.Couple;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public interface ItemBag {
    void add(ItemStack item, int count);
    void subtract(ItemStack item, int count);
    void set(ItemStack item, int count, int spurRemainder);
    Couple<Integer> get(ItemStack item);
    ItemStack asStack(ItemStack item);
    int getValue();
    default boolean isEmpty() {
        return getValue() == 0;
    }
    CompoundTag save(CompoundTag tag);
    void load(CompoundTag tag);
    void clear();
}
