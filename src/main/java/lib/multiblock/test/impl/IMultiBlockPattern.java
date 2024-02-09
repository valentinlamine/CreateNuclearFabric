package lib.multiblock.test.impl;

import lib.multiblock.test.misc.MultiblockMatchResult;
import lib.multiblock.test.misc.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.BiPredicate;

public interface IMultiBlockPattern {
    boolean matches(Level level, BlockPos pos, Rotation rotation);
    MultiblockMatchResult matchesWithResult(Level level, BlockPos pos, Rotation rotation);
    void construct(Level level, BlockPos pos, BiPredicate<Character, BlockState> blockStateBiPredicate);

    default boolean matches(Level level, BlockPos pos, Direction direction) {
        return matches(level, pos, Util.DirectionToRotation(direction));
    }

    default boolean matches(Level level, BlockPos pos) {
        return matches(level, pos, Rotation.NONE);
    }

    default MultiblockMatchResult matchesWithResult(Level level, BlockPos pos, Direction direction){
        return matchesWithResult(level, pos, Util.DirectionToRotation(direction));
    }

    default MultiblockMatchResult matchesWithResult(Level level, BlockPos pos) {
        return matchesWithResult(level, pos, Rotation.NONE);
    }

    default void contruct(Level level, BlockPos pos) {
        construct(level, pos, (a,b) -> true);
    }


    boolean matches(java.util.logging.Level level, BlockPos pos, Rotation rotation);
}
