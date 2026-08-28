package net.nuclearteam.createnuclear.content.multiblock.controller.snapshot;

import net.minecraft.world.item.Item;
import net.nuclearteam.createnuclear.content.logistics.BigFluidStack;

import java.util.List;
import java.util.Map;

/**
 * Snapshot of the reactor's inputs, collected once per tick.
 * <p>
 * {@code items} reflects the items actually present (for tooltip display).
 * {@code bigFuelItem}/{@code bigCoolerItem} are derived from
 * {@link net.nuclearteam.createnuclear.content.multiblock.input.item.VirtualReactorInputsItem} and always represent, respectively,
 * Uranium Rod / Graphite Rod (generic tag-based count, fixed identity) —
 * do not use these fields to infer the actual item type loaded.
 * <p>
 * The elements of {@code fluids} ({@link BigFluidStack}) remain mutable
 * (public fields); this snapshot only guarantees immutability of the
 * list/map structure, not of their contents.
 */

public record ReactorInputSnapshot(Map<Item, Integer> items, List<BigFluidStack> fluids, long maxFluidCapacity) {
    public static final ReactorInputSnapshot EMPTY = new ReactorInputSnapshot(
            Map.of(), List.of(), 0
    );

    public ReactorInputSnapshot {
        items = Map.copyOf(items);
        fluids = List.copyOf(fluids);
    }
}
