package net.nuclearteam.createnuclear.multiblock.controller;

import com.simibubi.create.foundation.item.SmartInventory;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.nuclearteam.createnuclear.item.CNItems;

public class ReactorControllerInventory extends SmartInventory {
    private final ReactorControllerBlockEntity be;

    public ReactorControllerInventory(ReactorControllerBlockEntity be) {
        super(1, be, 1, false);
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
            case 0 -> CNItems.REACTOR_BLUEPRINT.get() == resource.getItem();
            default -> !super.isItemValid(slot, resource, count);
        };
    }
}