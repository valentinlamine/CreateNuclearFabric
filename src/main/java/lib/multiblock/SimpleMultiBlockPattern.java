package lib.multiblock;

import lib.multiblock.impl.IMultiBlockPattern;
import lib.multiblock.misc.MultiBlockOffsetPos;
import lib.multiblock.misc.MultiblockMatchResult;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.server.TickTask;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.function.Supplier;


public record SimpleMultiBlockPattern(List<MultiBlockOffsetPos> multiBlockOffsetPosList,
                                      Map<Character, Predicate<BlockInWorld>> predicateHashMap,
                                      Map<Character, Supplier<BlockState>> blockProvider) implements IMultiBlockPattern {
    
  public SimpleMultiBlockPattern(List<MultiBlockOffsetPos> multiBlockOffsetPosList, Map<Character, Predicate<BlockInWorld>> predicateHashMap, Map<Character, Supplier<BlockState>> blockProvider) {
        this.multiBlockOffsetPosList = List.copyOf(multiBlockOffsetPosList);
        this.predicateHashMap = Map.copyOf(predicateHashMap);
        this.blockProvider = Map.copyOf(blockProvider);
    }

    public boolean matches(Level level, BlockPos blockPos, Rotation rotation) {
        for (MultiBlockOffsetPos multiBlockOffsetPos : multiBlockOffsetPosList) {
            char character = multiBlockOffsetPos.character();
            Predicate<BlockInWorld> predicate = predicateHashMap.get(character);

            BlockInWorld block = new BlockInWorld(level, blockPos.offset(multiBlockOffsetPos.pos().rotate(rotation)), false);
            if (predicate == null || !predicate.test(block)) return false;
        }
        return true;
    }

    public MultiblockMatchResult matchesWithResult(Level level, BlockPos blockPos, Rotation rotation) {
        List<BlockInWorld> result = new ArrayList<>();
        for (MultiBlockOffsetPos multiBlockOffsetPos : multiBlockOffsetPosList) {
            char character = multiBlockOffsetPos.character();
            Predicate<BlockInWorld> predicate = predicateHashMap.get(character);
            BlockInWorld block = new BlockInWorld(level, blockPos.offset(multiBlockOffsetPos.pos().rotate(rotation)), false);
            if (predicate == null || !predicate.test(block)) return null;
            result.add(block);
        }

        return new MultiblockMatchResult(List.copyOf(result));
    }

    @Override
    public void construct(Level level, BlockPos blockPos, BiPredicate<Character, BlockState> stateBiPredicate) {
        if (level.isClientSide) return;
        if (level.getServer() == null) return;
        for (MultiBlockOffsetPos multiBlockOffsetPos : multiBlockOffsetPosList) {
            char character = multiBlockOffsetPos.character();
            Supplier<BlockState> stateSupplier = blockProvider.get(character);
            if (stateSupplier != null) {
                var pos = blockPos.offset(multiBlockOffsetPos.pos().rotate(Rotation.NONE));
                var state = stateSupplier.get();
                if (stateBiPredicate.test(character, state))
                    level.getServer().tell(new TickTask(3, () -> level.setBlock(pos, state, Block.UPDATE_ALL)));
            }
        }
    }
}
