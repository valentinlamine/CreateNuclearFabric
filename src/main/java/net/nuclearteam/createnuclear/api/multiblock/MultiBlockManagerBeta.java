package net.nuclearteam.createnuclear.api.multiblock;

import lib.multiblock.impl.IMultiBlockPattern;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import java.util.ArrayList;
import java.util.List;

public class MultiBlockManagerBeta <T> {
    private final ArrayList<BlockPattern<T>> structures = new ArrayList<>();

    public MultiBlockManagerBeta() {}

    public void register(String id, T data, IMultiBlockPattern blockPattern) {
        structures.add(new BlockPattern<>(id, data, blockPattern));
    }

    public BlockPattern<T> findStructure(Level level, BlockPos pos, IMultiblockController entity) {
        List<Direction> directions = new ArrayList<>();
        directions.add(Direction.NORTH);
        directions.add(Direction.WEST);
        directions.add(Direction.EAST);
        directions.add(Direction.SOUTH);

        for (Direction direction : directions) {
            for (BlockPattern<T> structure : structures) {
                boolean result = structure.structure().matches(level, pos, direction);
                if (result){
                    entity.setMultiblockFacing(direction.getClockWise());
                    return structure;
                }
            }
        }

        return null;
    }
}
