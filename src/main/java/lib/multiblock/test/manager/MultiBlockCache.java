package lib.multiblock.test.manager;

import lib.multiblock.test.impl.IMultiBlockPattern;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Rotation;

import java.util.logging.Level;

public class MultiBlockCache<T extends IMultiBlockPattern> {
    private T cachedResult;
    public MultiBlockCache(){}

    public void updateStructure(T blockPattern) {
        this.cachedResult = blockPattern;
    }

    public boolean isValid(Level level, BlockPos pos, Rotation rotation) {
        return cachedResult != null && cachedResult.matches(level, pos, rotation);
    }

    public boolean isCached() {
        return cachedResult == null;
    }
}
