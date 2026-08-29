package net.nuclearteam.createnuclear.content.enriching.fire;

import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.world.level.BlockGetter;
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

    public BlockState getStateForPlacement() {
        return this.defaultBlockState();
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        return this.canSurvive(pState, pLevel, pCurrentPos)
                ? this.getStateForPlacement()
                : Blocks.AIR.defaultBlockState();
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
        BlockPos blockpos = pos.below();
        return worldIn.getBlockState(blockpos).isFaceSturdy(worldIn, blockpos, Direction.UP) || this.isValidFireLocation(worldIn, pos);
    }

    private boolean isValidFireLocation(BlockGetter pLevel, BlockPos pPos) {
        for(Direction direction : Direction.values()) {
            if (this.canCatchFire(pLevel, pPos.relative(direction), direction.getOpposite())) {
                return true;
            }
        }

        return false;
    }

    public boolean canCatchFire(BlockGetter world, BlockPos pos, Direction face) {
        FlammableBlockRegistry.Entry entry = FlammableBlockRegistry.getDefaultInstance().get(world.getBlockState(pos).getBlock());
        return entry != null && entry.getBurnChance() > 0;
    }

    @Override
    protected boolean canBurn(BlockState p_49284_) {
        return true;
    }

    public static boolean canSurviveOnBlock(BlockState pState) {
        return pState.is(CNTags.CNBlockTags.ENRICHING_FIRE_BASE_BLOCKS.tag);
    }

    public static NonNullUnaryOperator<Properties> getLight() {
        return p -> p.lightLevel(a -> 15);
    }
}
