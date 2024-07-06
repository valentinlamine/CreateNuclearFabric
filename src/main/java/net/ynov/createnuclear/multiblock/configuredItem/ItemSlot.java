package net.ynov.createnuclear.multiblock.configuredItem;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import static net.ynov.createnuclear.tags.CNTag.ItemTags.COOLER;
import static net.ynov.createnuclear.tags.CNTag.ItemTags.FUEL;

public class ItemSlot extends Slot {
    public ItemSlot(Container container, int slot, int x, int y) {
        super(container, slot, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return COOLER.matches(stack) || FUEL.matches(stack);
    }
}
