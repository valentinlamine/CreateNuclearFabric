package net.nuclearteam.createnuclear.content.enriching.fire;

import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.nuclearteam.createnuclear.CNTags;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
@SuppressWarnings("deprecation")
public class EnrichingFireBlock extends BaseFireBlock {
    public EnrichingFireBlock(Properties properties, float fireDamage) {
        super(properties, fireDamage);
    }

    public BlockState getPlacementState(BlockPlaceContext pContext) {
        return this.defaultBlockState();
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        return this.canPlaceAt(pState, pLevel, pCurrentPos)
                ? this.defaultBlockState()
                : Blocks.AIR.defaultBlockState();
    }

    @Override
    public boolean canPlaceAt(BlockState state, LevelReader worldIn, BlockPos pos) {
        return EnrichingFireBlock.canSurviveOnBlock(worldIn.getBlockState(pos.down()));
    }

    @Override
    protected boolean isFlammable(BlockState state) {
        return true;
    }

    public static boolean canSurviveOnBlock(BlockState state) {
        return state.is(CNTags.CNBlockTags.ENRICHING_FIRE_BASE_BLOCKS.tag);
    }

    public static NonNullUnaryOperator<Properties> getLight() {
        return p -> p.luminance(a -> 15);
    }
}
