package net.ynov.createnuclear.tools;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.SoulFireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.ynov.createnuclear.Tags.ModTag;
import net.ynov.createnuclear.block.ModBlocks;

public class UraniumFireBlock extends BaseFireBlock {
    public UraniumFireBlock(Properties properties) {
        super(properties, 3.0f);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return UraniumFireBlock.getState(context.getLevel(), context.getClickedPos());
    }

    public static BlockState getState(BlockGetter reader, BlockPos pos) {
        BlockPos blockPos = pos.below();
        BlockState blockState = reader.getBlockState(blockPos);

        return ModBlocks.URANIUM_RAW_BLOCK.defaultBlockState();
        //return UraniumFireBlock.canSurviveBlock(blockState) ?  ModBlocks.ENRICHING_FLAME.defaultBlockState() : Blocks.SOUL_FIRE.defaultBlockState();

    }

    @Override
    protected boolean canBurn(BlockState state) {
        return true;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        return this.canSurvive(state, level, pos) ? this.defaultBlockState() : Blocks.AIR.defaultBlockState();
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return UraniumFireBlock.canSurviveBlock(level.getBlockState(pos.below()));

    }

    public static boolean canSurviveBlock(BlockState state) {
        return state.is(ModTag.URANIUM_FIRE_BASE_BLOCKS);
    }

}
