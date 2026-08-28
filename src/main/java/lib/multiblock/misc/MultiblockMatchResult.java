package lib.multiblock.misc;

import java.util.List;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;

public record MultiblockMatchResult(List<BlockInWorld> blocks) {
}
