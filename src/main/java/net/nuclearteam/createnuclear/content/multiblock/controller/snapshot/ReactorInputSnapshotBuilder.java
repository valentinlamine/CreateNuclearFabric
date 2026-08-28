package net.nuclearteam.createnuclear.content.multiblock.controller.snapshot;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.nuclearteam.createnuclear.content.logistics.BigFluidStack;
import net.nuclearteam.createnuclear.content.multiblock.controller.manager.ReactorInputFluidManagerI;
import net.nuclearteam.createnuclear.content.multiblock.controller.manager.ReactorInputManagerI;
import net.nuclearteam.createnuclear.content.multiblock.input.fluid.VirtualReactorInputFluid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ReactorInputSnapshotBuilder {
    private ReactorInputSnapshotBuilder() {
    }

    public static ReactorInputSnapshot build(
            Level level,
            ReactorInputManagerI inputManager,
            ReactorInputFluidManagerI inputFluidManager
    ) {
        Map<Item, Integer> items = new HashMap<>();
        for (Storage<ItemVariant> storage : inputManager.getItemHandlers(level)) {
            for (StorageView<ItemVariant> view : storage.nonEmptyViews()) {
                items.merge(
                        view.getResource().getItem(),
                        (int) Math.min(view.getAmount(), Integer.MAX_VALUE),
                        ReactorInputSnapshotBuilder::saturatedAdd
                );
            }
        }

        long maxFluidCapacity = 0;
        for (Storage<FluidVariant> storage : inputFluidManager.getFuildHandlers(level)) {
            for (StorageView<FluidVariant> view : storage) {
                maxFluidCapacity = saturatedAdd(maxFluidCapacity, view.getCapacity());
            }
        }

        VirtualReactorInputFluid virtualFluid = inputFluidManager.getInventory(level);
        List<BigFluidStack> fluids = VirtualReactorInputFluid.toBigList(virtualFluid.fluids());
        return new ReactorInputSnapshot(items, fluids, maxFluidCapacity);
    }

    private static int saturatedAdd(int left, int right) {
        return (int) Math.min(Integer.MAX_VALUE, (long) left + right);
    }

    private static long saturatedAdd(long left, long right) {
        if (right > 0 && left > Long.MAX_VALUE - right) return Long.MAX_VALUE;
        return left + right;
    }
}
