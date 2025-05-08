package net.nuclearteam.createnuclear.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.nuclearteam.createnuclear.fluid.FluidInteractionManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(LiquidBlock.class)
public abstract class CNLiquidBlockMixin {
    @Shadow @Final protected FlowingFluid fluid;

    @Inject(at = @At("HEAD"), method = "shouldSpreadLiquid", cancellable = true)
    private void shouldSpreadLiquid(Level level, BlockPos pos, BlockState state, CallbackInfoReturnable<Boolean> cir) {
        Optional<Boolean> result = FluidInteractionManager.applyRules(fluid, level, pos);

        if (result.isPresent()) {
            cir.setReturnValue(result.get());
            cir.cancel();
        }
    }
}
