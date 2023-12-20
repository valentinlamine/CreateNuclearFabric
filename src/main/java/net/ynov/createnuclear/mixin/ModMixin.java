package net.ynov.createnuclear.mixin;


import com.simibubi.create.foundation.utility.BlockFace;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FlintAndSteelItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.ynov.createnuclear.CreateNuclear;
import net.ynov.createnuclear.block.ModBlocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.minecraft.world.level.block.CampfireBlock.LIT;
import static net.minecraft.world.level.block.CampfireBlock.WATERLOGGED;


@Mixin(FlintAndSteelItem.class)
public class ModMixin {
    @Inject(at = @At("HEAD"), method = "useOn", cancellable=true)
    public void useOn(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        BlockPos blockPos;
        Player player = context.getPlayer();
        Level level = context.getLevel();
        BlockState blockState = level.getBlockState(blockPos = context.getClickedPos());

        if (CampfireBlock.canLight(blockState) || CandleBlock.canLight(blockState) || CandleCakeBlock.canLight(blockState)) {
            level.playSound(player, blockPos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0f, level.getRandom().nextFloat() * 0.4f + 0.8f);
            level.setBlock(blockPos, (BlockState)blockState.setValue(BlockStateProperties.LIT, true), 11);
            level.gameEvent((Entity)player, GameEvent.BLOCK_CHANGE, blockPos);
            if (player != null) {
                context.getItemInHand().hurtAndBreak(1, player, p -> p.broadcastBreakEvent(context.getHand()));
            }
            cir.setReturnValue(InteractionResult.sidedSuccess(level.isClientSide()));
        }
        BlockPos blockPos2 = blockPos.relative(context.getClickedFace());
        if (context.getClickedFace() == Direction.UP) {
            if (BaseFireBlock.canBePlacedAt(level, blockPos2, context.getHorizontalDirection())) {
            level.playSound(player, blockPos2, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0f, level.getRandom().nextFloat() * 0.4f + 0.8f);
            BlockState stateBelow = context.getLevel().getBlockState(context.getClickedPos().below(0));

            BlockState blockState2 = stateBelow.is(ModBlocks.ENRICHED_SOUL_SOIL)
                    ? ModBlocks.ENRICHING_FIRE.defaultBlockState()
                    : stateBelow.is(Blocks.SOUL_SOIL)
                        ? Blocks.SOUL_FIRE.defaultBlockState()
                        : Blocks.FIRE.defaultBlockState();
            level.setBlock(blockPos2, blockState2, 11);
            level.gameEvent((Entity)player, GameEvent.BLOCK_PLACE, blockPos);
            ItemStack itemStack = context.getItemInHand();
            if (player instanceof ServerPlayer) {
                CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer)player, blockPos2, itemStack);
                itemStack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(context.getHand()));
            }
            cir.setReturnValue(InteractionResult.sidedSuccess(level.isClientSide()));
        }
        //cir.setReturnValue(InteractionResult.FAIL);

        }


    }
}


