package net.nuclearteam.createnuclear.content.multiblock.input.item;


import io.github.fabricators_of_create.porting_lib.transfer.item.ItemStackHandler;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.minecraft.world.level.Level;
import net.nuclearteam.createnuclear.api.multiblock.rods.RodType.TypeRodPredicate;
import org.jetbrains.annotations.NotNull;

public class ReactorRodInputInventory extends ItemStackHandler {
    private final ReactorRodInputEntity be;

    public ReactorRodInputInventory(ReactorRodInputEntity be) {
        super(1);
        this.be = be;
    }

    @Override
    protected void onContentsChanged(int slot) {
        super.onContentsChanged(slot);
        be.setChanged();
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemVariant resource, int count) {
        Level level = be.getLevel();
        return switch (slot) {
            case 0 -> level != null && (TypeRodPredicate.isFuel(resource.toStack(), level)
                    || TypeRodPredicate.isCooled(resource.toStack(), level));
            default -> super.isItemValid(slot, resource, count);
        };
    }
}
