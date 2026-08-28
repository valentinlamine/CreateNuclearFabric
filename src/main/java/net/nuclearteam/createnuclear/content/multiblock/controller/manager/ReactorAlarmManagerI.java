package net.nuclearteam.createnuclear.content.multiblock.controller.manager;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public interface ReactorAlarmManagerI extends ReactorIOManager {
    /**
     * Returns an immutable copy of the currently valid alarm positions in the given level.
     */
    List<BlockPos> getBlocksPosition(Level level);
}