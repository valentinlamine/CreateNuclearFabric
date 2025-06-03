package net.nuclearteam.createnuclear.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.nuclearteam.createnuclear.block.CNBlocks;
import net.nuclearteam.createnuclear.tools.EnrichingFireBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BaseFireBlock.class)
public abstract class CNBaseFireBlockMixin {

    @Inject(at = @At("HEAD"), method = "getState", cancellable = true)
    private static void getState(BlockGetter reader, BlockPos pos, CallbackInfoReturnable<BlockState> cir) {
        BlockPos blockPos = pos.below();
        BlockState blockState = reader.getBlockState(blockPos);
        if (EnrichingFireBlock.canSurviveOnBlock(blockState)) {
            cir.cancel();
            cir.setReturnValue(CNBlocks.ENRICHING_FIRE.getDefaultState());
        }
    }
}
