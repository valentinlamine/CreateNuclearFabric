package net.nuclearteam.createnuclear.content.multiblock.controller.display;

import net.minecraft.world.item.Item;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.ListTag;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.nuclearteam.createnuclear.content.logistics.BigFluidStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Immutable snapshot of the data the reactor controller exposes for client-side
 * display (e.g. the Goggles tooltip rendered by {@link ReactorGoggleTooltipRenderer}).
 * <p>
 * Instances are produced server-side once per tick from the live input inventories
 * and tanks, then synced to the client through the block entity's {@code clientPacket}
 * NBT via {@link #serializeNBT()} / {@link #deserializeNBT(CompoundTag)}. This data is
 * transient sync data only: it is not part of the persisted save NBT, so its layout
 * can evolve freely without migration concerns.
 * <p>
 * Usage:
 * <pre>{@code
 * // Server side, e.g. in tick():
 * this.displayState = new ReactorDisplayState(items, fluids, maxFluidCapacity);
 *
 * // Writing the client packet:
 * compound.put("displayState", displayState.serializeNBT());
 *
 * // Reading the client packet:
 * displayState = compound.contains("displayState")
 *         ? ReactorDisplayState.deserializeNBT(compound.getCompound("displayState"))
 *         : ReactorDisplayState.EMPTY;
 * }</pre>
 *
 * @param items            item rods currently loaded, mapped to their stacked count
 * @param fluids           fluids currently loaded in the input tanks
 * @param maxFluidCapacity combined capacity (in millibuckets) of all input fluid tanks
 */
public record ReactorDisplayState(Map<Item, Integer> items, List<BigFluidStack> fluids, long maxFluidCapacity) {
    static final String COMPONENT_ITEM = "Item";
    static final String COMPONENT_COUNT = "Count";
    static final String COMPONENT_CLIENT_DISPLAY_ITEMS = "clientDisplayItems";
    static final String COMPONENT_CLIENT_DISPLAY_FLUIDS = "clientDisplayFluids";
    static final String COMPONENT_CLIENT_MAX_FLUIDS_CAPACITY = "clientMaxFluidCapacity";

    /**
     * Shared empty instance, used before the first tick has populated any display
     * data and as the fallback when the client packet doesn't carry a display state.
     */
    public static final ReactorDisplayState EMPTY = new ReactorDisplayState(Map.of(), List.of(), 0);

    /**
     * Defensively copies the given collections so the record stays immutable
     * regardless of how the caller's collections are mutated afterwards.
     */
    public ReactorDisplayState {
        items = Map.copyOf(items);
        fluids = List.copyOf(fluids);
    }

    /**
     * Serializes this state into NBT for inclusion in the block entity's
     * {@code clientPacket} data. Pair with {@link #deserializeNBT(CompoundTag)}
     * to restore an equivalent instance on the client.
     *
     * @return a new {@link CompoundTag} containing the serialized item map,
     *         fluid list and max fluid capacity
     */
    public CompoundTag serializeNBT() {
        CompoundTag compound = new CompoundTag();

        ListTag displayItemsTag = new ListTag();
        for (Entry<Item, Integer> entry : items().entrySet()) {
            CompoundTag tag = new CompoundTag();
            tag.putString(COMPONENT_ITEM, BuiltInRegistries.ITEM.getKey(entry.getKey()).toString());
            tag.putInt(COMPONENT_COUNT, entry.getValue());
            displayItemsTag.add(tag);
        }
        compound.put(COMPONENT_CLIENT_DISPLAY_ITEMS, displayItemsTag);

        ListTag displayFluidsTag = new ListTag();
        for (BigFluidStack stack : fluids()) {
            displayFluidsTag.add(stack.write());
        }
        compound.put(COMPONENT_CLIENT_DISPLAY_FLUIDS, displayFluidsTag);

        compound.putLong(COMPONENT_CLIENT_MAX_FLUIDS_CAPACITY, maxFluidCapacity());

        return compound;
    }

    /**
     * Reconstructs a {@link ReactorDisplayState} from NBT previously produced by
     * {@link #serializeNBT()}. Acts as a static factory since records cannot be
     * mutated in place.
     * <p>
     * Missing keys are treated as empty/zero, so a compound that contains none of
     * the expected entries yields an instance equivalent to {@link #EMPTY}.
     *
     * @param compound the {@code displayState} compound from the client packet
     * @return a new immutable {@link ReactorDisplayState} built from the given NBT
     */
    public static ReactorDisplayState deserializeNBT(CompoundTag compound) {
        Map<Item, Integer> items = new HashMap<>();
        if (compound.contains(COMPONENT_CLIENT_DISPLAY_ITEMS)) {
            ListTag list = compound.getList(COMPONENT_CLIENT_DISPLAY_ITEMS, Tag.TAG_COMPOUND);
            for (int i = 0; i < list.size(); i++) {
                CompoundTag tag = list.getCompound(i);
                Item item = BuiltInRegistries.ITEM.get(new ResourceLocation(tag.getString(COMPONENT_ITEM)));
                if (item != null) {
                    items.put(item, tag.getInt(COMPONENT_COUNT));
                }
            }
        }

        List<BigFluidStack> fluids = new ArrayList<>();
        if (compound.contains(COMPONENT_CLIENT_DISPLAY_FLUIDS)) {
            ListTag list = compound.getList(COMPONENT_CLIENT_DISPLAY_FLUIDS, Tag.TAG_COMPOUND);
            for (int i = 0; i < list.size(); i++) {
                fluids.add(BigFluidStack.read(list.getCompound(i)));
            }
        }

        long maxFluidCapacity = compound.getLong(COMPONENT_CLIENT_MAX_FLUIDS_CAPACITY);

        return new ReactorDisplayState(items, fluids, maxFluidCapacity);
    }
}
