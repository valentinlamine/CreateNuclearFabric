package lib.multiblock.test.impl;

import lib.multiblock.test.misc.MultiblockMatchResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.BiPredicate;

public class IBetterPattern implements IMultiBlockPattern {
    @Override
    public boolean matches(Level level, BlockPos pos, Rotation rotation) {
        return false;
    }

    @Override
    public MultiblockMatchResult matchesWithResult(Level level, BlockPos pos, Rotation rotation) {
        return null;
    }

    @Override
    public void construct(Level level, BlockPos pos, BiPredicate<Character, BlockState> blockStateBiPredicate) {

    }

    @Override
    public boolean matches(java.util.logging.Level level, BlockPos pos, Rotation rotation) {
        return false;
    }
}
