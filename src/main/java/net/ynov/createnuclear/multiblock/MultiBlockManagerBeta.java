package net.ynov.createnuclear.multiblock;

import lib.multiblock.test.impl.IMultiBlockPattern;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Rotation;
import net.ynov.createnuclear.multiblock.CNBlockPattern;

import java.util.ArrayList;

public class MultiBlockManagerBeta<T> {
    private final ArrayList<CNBlockPattern<T>> structures = new ArrayList<>();

    public MultiBlockManagerBeta() {}

    public void register(String id, T data, IMultiBlockPattern blockPattern) {
        structures.add(new CNBlockPattern<>(id, data, blockPattern));
    }

    public CNBlockPattern<T> findStructure(Level level, BlockPos pos, Rotation rotation) {
        for (CNBlockPattern<T> structure : structures) {
            var result = structure.structure().matches(level, pos, rotation);
            if (result) return structure;
        }
        return null;
    }


}
