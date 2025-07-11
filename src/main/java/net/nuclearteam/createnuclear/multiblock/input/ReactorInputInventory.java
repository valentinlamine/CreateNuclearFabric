package net.nuclearteam.createnuclear.multiblock.input;

import io.github.fabricators_of_create.porting_lib.transfer.item.ItemStackHandler;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.nuclearteam.createnuclear.item.CNItems;
import net.nuclearteam.createnuclear.tags.CNTag;

public class ReactorInputInventory extends ItemStackHandler {
    private final ReactorInputEntity be;

    public ReactorInputInventory(ReactorInputEntity be) {
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
            case 0 -> CNTag.ItemTags.FUEL.matches(resource.getItem());
            case 1 -> CNTag.ItemTags.COOLER.matches(resource.getItem());
            default -> !super.isItemValid(slot, resource, count);
        };
    }
}