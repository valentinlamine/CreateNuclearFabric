package net.nuclearteam.createnuclear.tools;

import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.nuclearteam.createnuclear.tags.CNTag;

public class EnrichingFireBlock extends BaseFireBlock {
    public EnrichingFireBlock(Properties properties, float fireDamage) {
        super(properties, fireDamage);
    }

    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState();
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        return this.canSurvive(pState, pLevel, pCurrentPos)
                ? this.defaultBlockState()
                : Blocks.AIR.defaultBlockState();
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
        return EnrichingFireBlock.canSurviveOnBlock(worldIn.getBlockState(pos.below()));
    }

    @Override
    protected boolean canBurn(BlockState state) {
        return true;
    }

    public static boolean canSurviveOnBlock(BlockState state) {
        return state.is(CNTag.BlockTags.ENRICHING_FIRE_BASE_BLOCKS.tag);
    }

    public static NonNullUnaryOperator<Properties> getLight() {
        return p -> p.lightLevel(a -> 15);
    }
}
