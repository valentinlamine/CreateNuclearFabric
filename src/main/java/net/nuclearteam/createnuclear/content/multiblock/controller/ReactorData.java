package net.nuclearteam.createnuclear.content.multiblock.controller;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.nuclearteam.createnuclear.CNTags.CNItemTags;
import net.nuclearteam.createnuclear.infrastructure.config.CNConfigs;

public class ReactorData {
    public int countFuelRod;
    private int countCoolerRod;

    private int overFlowHeatTimer = 0;
    private int overFlowLimiter = 30;
    private double overHeat = 0;

    private final int baseUraniumHeat = CNConfigs.common().rods.baseValueUranium.get();
    private final int baseGraphiteHeat = CNConfigs.common().rods.baseValueGraphite.get();
    private final int proximityUraniumHeat = CNConfigs.common().rods.BoProxyUranium.get();
    private final int proximityGraphiteHeat = CNConfigs.common().rods.MaProxigraphite.get();
    private final int maxUraniumPerGraphite = CNConfigs.common().rods.uraMaxGraph.get();
    private final int coolerTimer = CNConfigs.common().rods.graphiteRodLifetime.get();
    private final int fuelTimer = CNConfigs.common().rods.uraniumRodLifetime.get();

    private int heat;
    private double total;

    private final int[][] formattedPattern = new int[][]{
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

    private final int[][] offsets = { {1, 0}, {-1, 0}, {0, 1}, {0, -1} };

    private ItemStack userPattern;

    public ReactorData() {
        userPattern = ItemStack.EMPTY;
    }


    public void write(CompoundTag nbt) {
        if (this.userPattern != null) {
            nbt.put("userPattern", userPattern.serializeNBT());
        }
    }

    public void read(CompoundTag nbt) {
        if (nbt.contains("userPattern")) {
            this.userPattern = ItemStack.of(nbt.getCompound("userPattern"));
        } else {
            this.userPattern = ItemStack.of(nbt.getCompound("items"));
        }
    }


    public boolean updateTimers() {
        total -= 1;
        return total <= 0;
    }

    public double calculateProgress(CompoundTag pattern) {
        countCoolerRod = pattern.getInt("countGraphiteRod");
        countFuelRod = pattern.getInt("countUraniumRod");

        double totalCoolerRodLife = (double) coolerTimer / countCoolerRod;
        double totalFuelRodLife = (double) fuelTimer / countFuelRod;

        return totalFuelRodLife - totalCoolerRodLife;
    }

    public double calculateHeat(CompoundTag pattern) {
        countCoolerRod = pattern.getInt("countGraphiteRod");
        countFuelRod = pattern.getInt("countUraniumRod");
        heat = 0;

        if (countFuelRod > countCoolerRod * maxUraniumPerGraphite) {
            overFlowHeatTimer++;
            if (overFlowHeatTimer >= overFlowLimiter) {
                overHeat += 1;
                overFlowHeatTimer = 0;
                if (overFlowLimiter > 1) {
                    overFlowLimiter--;
                }
            }
        } else {
            overFlowHeatTimer = 0;
            overFlowLimiter = 30;
            if (overHeat > 0) {
                overHeat -= 2;
            } else {
                overHeat = 0;
            }
        }

        char currentRod = '\n';
        ListTag list = this.getPattern();
        for (int i = 0; i < list.size(); i++) {
            if (ItemStack.of(list.getCompound(i)).is(CNItemTags.FUEL.tag)) {
                heat += baseUraniumHeat;
                currentRod = 'u';
            } else if (ItemStack.of(list.getCompound(i)).is(CNItemTags.COOLER.tag)) {
                heat += baseGraphiteHeat;
                currentRod = 'g';
            }
            for (int row = 0; row < formattedPattern.length; row++) {
                for (int patternSlot = 0; patternSlot < formattedPattern[row].length; patternSlot++) {
                    if (formattedPattern[row][patternSlot] == 99) continue;

                    if (list.getCompound(i).getInt("Slot") != formattedPattern[row][patternSlot]) continue;

                    for (int[] offset : offsets) {
                        int nRow = row + offset[0];
                        int nPatternSlot = patternSlot + offset[1];

                        // Check if the indices are within the array boundaries
                        if (nRow < 0 || nRow >= formattedPattern.length || nPatternSlot < 0 || nPatternSlot >= formattedPattern.length) continue;

                        int neighborSlot = formattedPattern[nRow][nPatternSlot];

                        // Loop through the list to find the neighbor slot
                        for (int l = 0; l < list.size(); l++) {
                            if (list.getCompound(l).getInt("Slot") == neighborSlot) {
                                // If currentRod equals "u", apply the corresponding heat
                                if (currentRod == 'u') {
                                    ItemStack stack = ItemStack.of(list.getCompound(l));
                                    if (stack.is(CNItemTags.FUEL.tag)) {
                                        heat += proximityUraniumHeat;
                                    } else if (stack.is(CNItemTags.COOLER.tag)) {
                                        heat += proximityGraphiteHeat;
                                    }
                                }
                            }
                        }
                    }

                }
            }

        }
        return heat + overHeat;
    }

    public void clear() {
    }

    private ListTag getPattern() {
        return userPattern.getOrCreateTag().getCompound("pattern").getList("Items", Tag.TAG_COMPOUND);
    }

}
