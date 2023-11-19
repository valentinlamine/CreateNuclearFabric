package net.ynov.createnuclear.tools;

import io.github.fabricators_of_create.porting_lib.tags.Tags;
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
        return BaseFireBlock.getState(context.getLevel(), context.getClickedPos());
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
    @SuppressWarnings("deprecation")
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos below = pos.below();
        return level.getBlockState(below).isFaceSturdy(level, pos, Direction.UP);
    }

    public static boolean canSurviveBlock(BlockState state) {
        return state.is(ModTag.URANIUM_FIRE_BASE_BLOCKS);
    }


    public static BlockState getState(BlockGetter reader, BlockPos pos) {
        BlockPos blockPos = pos.below();
        BlockState blockState = reader.getBlockState(blockPos);
        if (UraniumFireBlock.canSurviveBlock(blockState)){
            return ModBlocks.ENRICHING_FLAME.defaultBlockState();
        }

        if (SoulFireBlock.canSurviveOnBlock(blockState)) {
            return Blocks.SOUL_FIRE.defaultBlockState();
        }
        return Blocks.FIRE.defaultBlockState();
    }
}
