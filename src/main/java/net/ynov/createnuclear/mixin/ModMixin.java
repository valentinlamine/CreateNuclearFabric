package net.ynov.createnuclear.mixin;


import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FlintAndSteelItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.ynov.createnuclear.CreateNuclear;
import net.ynov.createnuclear.block.ModBlocks;
import net.ynov.createnuclear.tools.UraniumFireBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(FlintAndSteelItem.class)
public class ModMixin {
    @Inject(at = @At("HEAD"), method = "useOn", cancellable=true)
    public void useOn(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        CreateNuclear.LOGGER.info("dd");
        BlockPos blockPos;
        Player player = context.getPlayer();

        Level level = context.getLevel();
        BlockState blockState = level.getBlockState(blockPos = context.getClickedPos());

        BlockPos blockPos2 = blockPos.relative(context.getClickedFace());
        if (UraniumFireBlock.canBePlacedAt(level, blockPos2, context.getHorizontalDirection())) {

            level.playSound(player, blockPos2, SoundEvents.FIRE_AMBIENT, SoundSource.BLOCKS, 1.0f, level.getRandom().nextFloat() * 0.4f + 0.8f);

            BlockState stateBelow = context.getLevel().getBlockState(context.getClickedPos().below(0));
            CreateNuclear.LOGGER.info(String.valueOf(stateBelow) + " " + context.getClickedPos().north(1));

            BlockState blockState2;
            if (stateBelow.is(ModBlocks.ENRICHED_SOUL_SOIL)) {
                blockState2 = ModBlocks.ENRICHING_FIRE.defaultBlockState();
            } else if (stateBelow.is(Blocks.SOUL_SOIL)) {
                blockState2 = Blocks.SOUL_FIRE.defaultBlockState();
            } else {
                blockState2 = Blocks.FIRE.defaultBlockState();
            }

            level.setBlock(blockPos2, blockState2, 11);
            level.gameEvent((Entity) player, GameEvent.BLOCK_PLACE, blockPos);
            ItemStack itemStack = context.getItemInHand();
            if (player instanceof ServerPlayer) {
                CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer) player, blockPos2, itemStack);
                itemStack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(context.getHand()));
            }
            cir.setReturnValue(InteractionResult.sidedSuccess(level.isClientSide()));
        }
    }
}


