package lib.multiblock.test.impl;

import lib.multiblock.test.misc.MultiBlockOffsetPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;

public interface IPatternBuilder<T extends IMultiBlockPattern> {
    T make(List<MultiBlockOffsetPos> multiBlockOffsetPos, Map<Character, Predicate<BlockInWorld>> predicateMap, Map<Character, Supplier<BlockState>> blockSuppliers);
}
