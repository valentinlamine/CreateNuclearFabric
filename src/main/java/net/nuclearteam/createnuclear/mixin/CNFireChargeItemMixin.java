package net.nuclearteam.createnuclear.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.FireChargeItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.CandleCakeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.nuclearteam.createnuclear.CreateNuclear;
import net.nuclearteam.createnuclear.block.CNBlocks;
import net.nuclearteam.createnuclear.tags.CNTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FireChargeItem.class)
public class CNFireChargeItemMixin {
    @Inject(at = @At("HEAD"), method = "useOn", cancellable = true)
    public void useOn(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        Level level = context.getLevel();
        BlockPos blockPos = context.getClickedPos();
        BlockState blockState = level.getBlockState(blockPos);
        boolean bl = false;
        if (CampfireBlock.canLight(blockState) || CandleBlock.canLight(blockState) || CandleCakeBlock.canLight(blockState)) {
            this.playSound(level, blockPos);
            level.setBlockAndUpdate(blockPos, (BlockState)blockState.setValue(BlockStateProperties.LIT, true));
            level.gameEvent((Entity)context.getPlayer(), GameEvent.BLOCK_CHANGE, blockPos);
            bl = true;
        } else if (BaseFireBlock.canBePlacedAt(level, blockPos = blockPos.relative(context.getClickedFace()), context.getHorizontalDirection())) {
            this.playSound(level, blockPos);

            BlockState blockState1 = context.getLevel().getBlockState(context.getClickedPos().below(0)).is(CNTag.BlockTags.ENRICHING_FIRE_BASE_BLOCKS.tag)
                    ? CNBlocks.ENRICHING_FIRE.get().defaultBlockState()
                    : BaseFireBlock.getState(level, blockPos);

            level.setBlockAndUpdate(blockPos, blockState1);
            level.gameEvent((Entity)context.getPlayer(), GameEvent.BLOCK_PLACE, blockPos);
            bl = true;
        }
        if (bl) {
            context.getItemInHand().shrink(1);
            cir.setReturnValue(InteractionResult.sidedSuccess(level.isClientSide));
        }
        cir.setReturnValue(InteractionResult.FAIL);
    }

    private void playSound(Level level, BlockPos pos) {
        RandomSource randomSource = level.getRandom();
        level.playSound(null, pos, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 1.0f, (randomSource.nextFloat() - randomSource.nextFloat()) * 0.2f + 1.0f);
    }
}
