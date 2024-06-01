package net.ynov.createnuclear.multiblock.controller;

import io.github.fabricators_of_create.porting_lib.transfer.item.ItemStackHandler;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.ynov.createnuclear.item.CNItems;

public class ReactorControllerInventory extends ItemStackHandler {
    private ReactorControllerBlockEntity be;

    public ReactorControllerInventory(ReactorControllerBlockEntity be) {
        super(2);
        this.be = be;
    }

    @Override
    protected void onContentsChanged(int slot) {
        super.onContentsChanged(slot);
        be.setChanged();
    }

    @Override
    public boolean isItemValid(int slot, ItemVariant resource, int count) {
        return switch (slot) {
            case 0 -> CNItems.URANIUM_ROD.get() == resource.getItem();
            case 1 -> CNItems.GRAPHITE_ROD.get() == resource.getItem();
            default -> super.isItemValid(slot, resource, count);
        };
    }
}