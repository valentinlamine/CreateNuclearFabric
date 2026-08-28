package net.nuclearteam.createnuclear.content.multiblock.reactorLogic;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import io.github.fabricators_of_create.porting_lib.transfer.item.ItemStackHandler;
import net.nuclearteam.createnuclear.api.multiblock.fluid.ReactorFluidType;
import net.nuclearteam.createnuclear.api.multiblock.rods.RodType;
import net.nuclearteam.createnuclear.api.multiblock.rods.RodType.TypeRod;
import net.nuclearteam.createnuclear.api.multiblock.rods.RodType.TypeRodPredicate;
import net.nuclearteam.createnuclear.content.logistics.BigFluidStack;
import net.nuclearteam.createnuclear.content.multiblock.bluePrintItem.ReactorBluePrintItem;
import net.nuclearteam.createnuclear.content.multiblock.controller.ReactorControllerInventory;
import net.nuclearteam.createnuclear.content.multiblock.controller.display.ReactorDisplayState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultHeatCalculator implements IHeatCalculator {
    private static final int[][] FORMATTED_PATTERN = new int[][]{
            {99,99,99,0,1,2,99,99,99},
            {99,99,3,4,5,6,7,99,99},
            {99,8,9,10,11,12,13,14,99},
            {15,16,17,18,19,20,21,22,23},
            {24,25,26,27,28,29,30,31,32},
            {33,34,35,36,37,38,39,40,41},
            {99,42,43,44,45,46,47,48,99},
            {99,99,49,50,51,52,53,99,99},
            {99,99,99,54,55,56,99,99,99}
    };
    private static final int[][] OFFSETS = { {1, 0}, {-1, 0}, {0, 1}, {0, -1} };

    private static final Map<Integer, List<Integer>> NEIGHBORS_BY_SLOT = buildNeighborsBySlot();

    private static Map<Integer, List<Integer>> buildNeighborsBySlot() {
        Map<Integer, List<Integer>> result = new HashMap<>();
        for (int row = 0; row < FORMATTED_PATTERN.length; row++) {
            for (int col = 0; col < FORMATTED_PATTERN[row].length; col++) {
                int slot = FORMATTED_PATTERN[row][col];
                if (slot == 99) continue;

                List<Integer> neighbors = new ArrayList<>();
                for (int[] offset : OFFSETS) {
                    int nj = row + offset[0];
                    int nk = col + offset[1];

                    if (nj < 0 || nj >= FORMATTED_PATTERN.length || nk < 0 || nk >= FORMATTED_PATTERN[nj].length) continue;

                    int neighborSlot = FORMATTED_PATTERN[nj][nk];
                    if (neighborSlot != 99) neighbors.add(neighborSlot);
                }

                result.put(slot, neighbors);
            }
        }

        return result;
    }

    @Override
    public double computeHeat(BigFluidStack bigFluidStack, ReactorFluidType type, ReactorControllerInventory inventory, double overHeat, ReactorDisplayState displayState, Level level) {
        double heat = 0;

        ItemStackHandler pattern = ReactorBluePrintItem.getItemStorage(inventory.getStackInSlot(0));

        Map<Item, Integer> availableItems = displayState != null && displayState.items() != null 
                ? new HashMap<>(displayState.items()) 
                : new HashMap<>();

        Map<Integer, ItemStack> actualRods = new HashMap<>();

        for (int slot = 0; slot < pattern.getSlotCount(); slot++) {
            ItemStack currentStack = pattern.getStackInSlot(slot);
            if (currentStack.isEmpty()) continue;

            Item rodItem = currentStack.getItem();
            if (availableItems.getOrDefault(rodItem, 0) > 0) {
                availableItems.put(rodItem, availableItems.get(rodItem) - 1);
                actualRods.put(slot, currentStack);
            }
        }

        for (Map.Entry<Integer, ItemStack> entry : actualRods.entrySet()) {
            int slot = entry.getKey();
            ItemStack currentStack = entry.getValue();
            RodType rod = RodType.resolveRodType(currentStack.getItem(), level);

            if (!rod.isNotEmptyItem() || rod.type() == TypeRod.NONE) continue;

            heat += rod.baseRodHeat().get();

            // find position in formattedPattern and check neighbors
            for (int neighborSlot : NEIGHBORS_BY_SLOT.getOrDefault(slot, List.of())) {
                ItemStack neighborStack = actualRods.get(neighborSlot);
                if (neighborStack == null) continue;

                RodType neighborRod = RodType.resolveRodType(neighborStack.getItem(), level);
                if (!neighborRod.isNotEmptyItem()) continue;

                if (TypeRodPredicate.isFuel(rod) && TypeRodPredicate.isFuel(neighborRod)) {
                    heat += rod.proximityRodHeat().get();
                } else if (TypeRodPredicate.isCooled(rod) && TypeRodPredicate.isFuel(neighborRod)) {
                    heat += neighborRod.baseRodHeat().get() * rod.proximityRodHeat().get();
                }
            }
        }
        return Math.max(0, heat + overHeat);
    }
}
