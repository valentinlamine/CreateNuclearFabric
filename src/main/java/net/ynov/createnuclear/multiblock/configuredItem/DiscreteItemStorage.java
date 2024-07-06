package net.ynov.createnuclear.multiblock.configuredItem;

import com.simibubi.create.foundation.utility.Couple;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.ynov.createnuclear.CreateNuclear;

import java.util.HashMap;
import java.util.Map;

public class DiscreteItemStorage implements ItemBag {
    private final Map<ItemStack, Integer> item = new HashMap<>();
    private int value = 0;

    protected DiscreteItemStorage(Map<ItemStack, Integer> item) {
        this.item.putAll(item);
    }

    @Override
    public void add(ItemStack item, int count) {
        this.item.put(item, get(item).getFirst() + count);
    }

    @Override
    public void subtract(ItemStack item, int count) {
        this.item.put(item, Math.max(0, get(item).getFirst() - count));
    }

    @Override
    public void set(ItemStack item, int count, int spurRemainder) {
        if (spurRemainder != 0) {
            CreateNuclear.LOGGER.warn("called with spurRemainder != 0");
        }
        count = Math.max(0, count);
        this.item.put(item, count);
    }

    @Override
    public Couple<Integer> get(ItemStack item) {
        return Couple.create(this.item.getOrDefault(item, 0), 0);
    }

    @Override
    public ItemStack asStack(ItemStack item) {
        int amt = get(item).getFirst();
        if (amt == 0) return ItemStack.EMPTY;
        return null;
    }

    @Override
    public int getValue() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return ItemBag.super.isEmpty();
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        return null;
    }

    @Override
    public void load(CompoundTag tag) {

    }

    @Override
    public void clear() {

    }
}
