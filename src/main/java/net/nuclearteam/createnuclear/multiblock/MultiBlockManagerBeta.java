package net.nuclearteam.createnuclear.multiblock;

import lib.multiblock.test.impl.IMultiBlockPattern;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Rotation;
import net.nuclearteam.createnuclear.multiblock.CNBlockPattern;

import java.util.ArrayList;
import java.util.List;

public class MultiBlockManagerBeta<T> {
    private final ArrayList<CNBlockPattern<T>> structures = new ArrayList<>();

    public MultiBlockManagerBeta() {}

    public void register(String id, T data, IMultiBlockPattern blockPattern) {
        structures.add(new CNBlockPattern<>(id, data, blockPattern));
    }

    public CNBlockPattern<T> findStructure(Level level, BlockPos pos) {
        List<Direction> directions = new ArrayList<>();
        directions.add(Direction.NORTH);
        directions.add(Direction.WEST);
        directions.add(Direction.EAST);
        directions.add(Direction.SOUTH);
        for (Direction direction : directions) {
            for (CNBlockPattern<T> structure : structures) {
                var result = structure.structure().matches(level, pos, direction);
                if (result) return structure;
            }
        }
        return null;
    }


}
