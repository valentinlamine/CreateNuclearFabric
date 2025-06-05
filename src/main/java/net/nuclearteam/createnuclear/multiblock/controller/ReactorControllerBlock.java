package net.nuclearteam.createnuclear.multiblock.controller;

import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.item.ItemHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.nuclearteam.createnuclear.CNMultiblock;
import net.nuclearteam.createnuclear.block.CNBlocks;
import net.nuclearteam.createnuclear.blockentity.CNBlockEntities;
import net.nuclearteam.createnuclear.item.CNItems;
import net.nuclearteam.createnuclear.multiblock.output.ReactorOutput;
import net.nuclearteam.createnuclear.multiblock.output.ReactorOutputEntity;
import net.nuclearteam.createnuclear.tools.HorizontalDirectionalReactorBlock;

import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@SuppressWarnings({"deprecation", "UnstableApiUsage", "null"})
public class ReactorControllerBlock extends HorizontalDirectionalReactorBlock implements IWrenchable, IBE<ReactorControllerBlockEntity> {
    public static final BooleanProperty ASSEMBLED = BooleanProperty.create("assembled");

    public ReactorControllerBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, ASSEMBLED);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite())
                .setValue(ASSEMBLED, false);
    }

    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos,
                                boolean isMoving) {
        withBlockEntityDo(worldIn, pos, be -> be.created = false);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (world.isClientSide) return InteractionResult.SUCCESS;

        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (!(blockEntity instanceof ReactorControllerBlockEntity controller)) return InteractionResult.PASS;

        ItemStack heldItem = player.getItemInHand(hand);
        ItemStack slotItem = controller.inventory.getItem(0);

        if (!state.getValue(ASSEMBLED)) {
            player.sendSystemMessage(Component.translatable("reactor.info.assembled.none").withStyle(ChatFormatting.RED));
            return InteractionResult.SUCCESS;
        }

        if (heldItem.is(CNItems.REACTOR_BLUEPRINT.get()) && slotItem.isEmpty()) {
            withBlockEntityDo(world, pos, be -> {
                be.inventory.setStackInSlot(0, heldItem);
                be.configuredPattern = heldItem;
                player.setItemInHand(hand, ItemStack.EMPTY);
            });
            return InteractionResult.SUCCESS;
        }

        if (heldItem.isEmpty() && !slotItem.isEmpty()) {
            withBlockEntityDo(world, pos, be -> {
                player.setItemInHand(hand, slotItem);
                be.inventory.setStackInSlot(0, ItemStack.EMPTY);
                be.configuredPattern = ItemStack.EMPTY;
                be.total = 0.0;
                be.rotate(be.getBlockState(), be.getBlockPos().below(3), world, 0);
                be.notifyUpdate();
            });
            //world.setBlock(pos, state.setValue(ASSEMBLED, false), 3);
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.hasBlockEntity() || state.getBlock() == newState.getBlock()) return;

        withBlockEntityDo(world, pos, be -> ItemHelper.dropContents(world, pos, be.inventory));
        world.removeBlockEntity(pos);
        rotate(state, pos.below(3), world, 0);
        notifyPlayers(world, "reactor.info.assembled.destroyer", ChatFormatting.RED);
    }

    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, world, pos, oldState, movedByPiston);
        if (!state.getValue(ASSEMBLED)) {
            verify(state, pos, world, world.players(), true);
        }
    }

    @Override
    public void playerDestroy(Level world, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
        super.playerDestroy(world, player, pos, state, blockEntity, tool);
        ReactorControllerBlockEntity entity = getBlockEntity(world, pos);
        if (entity != null && entity.created) {
            rotate(state, pos.below(3), world, 0);
            notifyPlayers(world, "reactor.info.assembled.destroyer", ChatFormatting.RED);
        }
    }

    // this is the Function that verifies if the pattern is correct (as a test, we added the energy output)
    public void verify(BlockState state, BlockPos pos, Level level, List<? extends Player> players, boolean create){
        ReactorControllerBlockEntity entity = getBlockEntity(level, pos);
        boolean patternFound = CNMultiblock.REGISTRATE_MULTIBLOCK.findStructure(level, pos) != null;

        if (patternFound && create && !entity.created) {
            players.forEach(player -> player.sendSystemMessage(Component.translatable("reactor.info.assembled.creator").withStyle(ChatFormatting.GREEN)));
            level.setBlockAndUpdate(pos, state.setValue(ASSEMBLED, true));
            entity.created = true;
            entity.destroyed = false;
        } else if (!patternFound && !create && !entity.destroyed) {
            players.forEach(player -> player.sendSystemMessage(Component.translatable("reactor.info.assembled.destroyer").withStyle(ChatFormatting.RED)));
            level.setBlockAndUpdate(pos, state.setValue(ASSEMBLED, false));
            entity.created = false;
            entity.destroyed = true;
            rotate(state, pos.below(3), level, 0);
        }
    }
    public void rotate(BlockState state, BlockPos pos, Level level, int rotation) {
        if (!level.getBlockState(pos).is(CNBlocks.REACTOR_OUTPUT.get())) return;

        ReactorOutputEntity entity = ((ReactorOutput) level.getBlockState(pos).getBlock()).getBlockEntityType().getBlockEntity(level, pos);
        if (entity == null) return;

        if (state.getValue(ASSEMBLED) && rotation != 0) {
            entity.speed = Math.abs(rotation);
        } else {
            entity.speed = 0;
        }
        entity.setSpeed(entity.speed);
        entity.updateSpeed = true;
        entity.updateGeneratedRotation();
    }

    private void notifyPlayers(Level world, String key, ChatFormatting format) {
        world.players().forEach(p -> p.sendSystemMessage(Component.translatable(key).withStyle(format)));
    }

    @Override
    public Class<ReactorControllerBlockEntity> getBlockEntityClass() {
        return ReactorControllerBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends ReactorControllerBlockEntity> getBlockEntityType() {
        return CNBlockEntities.REACTOR_CONTROLLER.get();
    }
}