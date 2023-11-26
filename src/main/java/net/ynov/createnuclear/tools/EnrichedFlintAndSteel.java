package net.ynov.createnuclear.tools;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
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
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.ynov.createnuclear.Tags.ModTag;
import net.ynov.createnuclear.block.ModBlocks;

public class EnrichedFlintAndSteel extends FlintAndSteelItem {
    public EnrichedFlintAndSteel(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        BlockPos blockPos;
        Player player = context.getPlayer();

        Level level = context.getLevel();
        BlockState blockState = level.getBlockState(blockPos = context.getClickedPos());

        BlockPos blockPos2 = blockPos.relative(context.getClickedFace());
        if (UraniumFireBlock.canBePlacedAt(level, blockPos2, context.getHorizontalDirection())) {
            level.playSound(player, blockPos2, SoundEvents.AMETHYST_BLOCK_BREAK, SoundSource.BLOCKS, 1.0f, level.getRandom().nextFloat() * 0.4f + 0.8f);
            BlockState blockState2 = blockState.is(ModBlocks.ENRICHED_SOUL_SOIL)
                    ? ModBlocks.ENRICHING_FLAME.defaultBlockState()
                    : Blocks.AIR.defaultBlockState();
            level.setBlock(blockPos2, blockState2, 11);
            level.gameEvent((Entity)player, GameEvent.BLOCK_PLACE, blockPos);
            ItemStack itemStack = context.getItemInHand();
            if (player instanceof ServerPlayer) {
                CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer)player, blockPos2, itemStack);
                itemStack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(context.getHand()));
            }
            return InteractionResult.sidedSuccess(level.isClientSide());
        }
        return InteractionResult.FAIL;
    }
}
