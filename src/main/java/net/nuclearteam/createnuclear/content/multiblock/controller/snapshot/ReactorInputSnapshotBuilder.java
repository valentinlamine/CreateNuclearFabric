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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@SuppressWarnings("UnstableApiUsage")
public final class ReactorInputSnapshotBuilder {
    private ReactorInputSnapshotBuilder() {}

    public static ReactorInputSnapshot build(Level level, ReactorInputManagerI inputManager, ReactorInputFluidManagerI inputFluidManager) {
        Map<Item, Integer> items = new HashMap<>();
        for (Storage<ItemVariant> storage : inputManager.getItemHandlers(level)) {
            for (StorageView<ItemVariant> view : storage.nonEmptyViews()) {
                items.merge(view.getResource().getItem(), (int) Math.min(view.getAmount(), Integer.MAX_VALUE), Integer::sum);
            }
        }

        long maxFluidCapacity = 0;
        for (Storage<FluidVariant> storage : inputFluidManager.getFuildHandlers(level)) {
            Iterator<StorageView<FluidVariant>> views = storage.iterator();
            if (views.hasNext()) {
                maxFluidCapacity = saturatedAdd(maxFluidCapacity, views.next().getCapacity());
            }
        }

        VirtualReactorInputFluid virtualFluid = inputFluidManager.getInventory(level);
        List<BigFluidStack> fluids = VirtualReactorInputFluid.toBigList(virtualFluid.fluids());
        return new ReactorInputSnapshot(items, fluids, maxFluidCapacity);
    }

    private static long saturatedAdd(long left, long right) {
        if (right > 0 && left > Long.MAX_VALUE - right) return Long.MAX_VALUE;
        return left + right;
    }
}
