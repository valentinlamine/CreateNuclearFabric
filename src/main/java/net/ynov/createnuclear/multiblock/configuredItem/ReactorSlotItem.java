package net.ynov.createnuclear.multiblock.configuredItem;

import com.tterrag.registrate.util.entry.ItemEntry;
import io.github.fabricators_of_create.porting_lib.transfer.item.ItemStackHandler;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ReactorSlotItem extends Slot {
    private static final Container emptyInventory = new SimpleContainer(0);
    private final ItemStackHandler inventory;
    private final ItemEntry<?> item;
    private final boolean canInsert;
    private final boolean canExtract;

    public ReactorSlotItem(ItemStackHandler container, ItemEntry<?> item, int slot, int x, int y, boolean canInsert, boolean canExtract) {
        super(emptyInventory, slot, x, y);
        this.canInsert = canInsert;
        this.canExtract = canExtract;
        this.inventory = container;
        this.item = item;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        if (!canInsert) return false;
        if (stack.isEmpty()) return false;

        return stack.getItem() instanceof Item;
    }

    @Override
    public ItemStack getItem() {
        return inventory.as;
    }
}
