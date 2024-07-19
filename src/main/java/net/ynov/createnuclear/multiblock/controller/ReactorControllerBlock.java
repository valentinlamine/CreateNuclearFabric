package net.ynov.createnuclear.multiblock.controller;

import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.item.ItemHelper;
import io.github.fabricators_of_create.porting_lib.transfer.TransferUtil;
import io.github.fabricators_of_create.porting_lib.transfer.item.ItemHandlerHelper;
import io.github.fabricators_of_create.porting_lib.util.NetworkHooks;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.ynov.createnuclear.CNMultiblock;
import net.ynov.createnuclear.CreateNuclear;
import net.ynov.createnuclear.block.CNBlocks;
import net.ynov.createnuclear.blockentity.CNBlockEntities;
import net.ynov.createnuclear.item.CNItems;
import net.ynov.createnuclear.multiblock.energy.ReactorOutput;
import net.ynov.createnuclear.multiblock.energy.ReactorOutputEntity;
import net.ynov.createnuclear.gui.CNIconButton;
import net.ynov.createnuclear.tools.HorizontalDirectionalReactorBlock;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ReactorControllerBlock extends HorizontalDirectionalReactorBlock implements IWrenchable, IBE<ReactorControllerBlockEntity> {
    public static final BooleanProperty ASSEMBLED = BooleanProperty.create("assembled");
    private List<CNIconButton> switchButtons;


    public ReactorControllerBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING).add(ASSEMBLED);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection())
                .setValue(ASSEMBLED, false);
    }

    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos,
                                boolean isMoving) {
        withBlockEntityDo(worldIn, pos, be -> be.created = false);
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (worldIn.isClientSide)
            return InteractionResult.SUCCESS;

        BlockEntity blockEntity = worldIn.getBlockEntity(pos);
        if (!(blockEntity instanceof ReactorControllerBlockEntity controllerBlockEntity)) return InteractionResult.PASS;

        ItemStack heldItem = player.getItemInHand(handIn);

        if (Boolean.FALSE.equals(state.getValue(ASSEMBLED))) {
            player.sendSystemMessage(Component.translatable("reactor.info.assembled.none").withStyle(ChatFormatting.RED));
        }
        else {
            /*worldIn.getBlockEntity(pos, CNBlockEntities.REACTOR_CONTROLLER.get())
                    .map(be -> be.onClick(player, handIn))
                    .orElse(InteractionResult.PASS);*/

            if (heldItem.is(CNItems.CONFIGURED_REACTOR_ITEM.get())){
                withBlockEntityDo(worldIn, pos, be -> {
                    be.inventory.setStackInSlot(0, heldItem);
                    be.configuredPattern = heldItem;
                    CreateNuclear.LOGGER.warn(""+be.inventory.getStackInSlot(0).getOrCreateTag());

                    player.setItemInHand(handIn, ItemStack.EMPTY);
                });
                return InteractionResult.SUCCESS;

            }
            else if (heldItem.isEmpty() && !controllerBlockEntity.inventory.getItem(0).isEmpty()) {
                withBlockEntityDo(worldIn, pos, be -> {
                    player.setItemInHand(handIn, be.inventory.getItem(0));
                    be.inventory.setStackInSlot(0, ItemStack.EMPTY);
                    be.configuredPattern = ItemStack.EMPTY;
                    be.notifyUpdate();
                });
                state.setValue(ASSEMBLED, false);
                return InteractionResult.SUCCESS;

            }
            else if (!heldItem.isEmpty() && !controllerBlockEntity.inventory.getItem(0).isEmpty()) {
                return InteractionResult.PASS;
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.hasBlockEntity() || state.getBlock() == newState.getBlock())
            return;

        withBlockEntityDo(worldIn, pos, be -> ItemHelper.dropContents(worldIn, pos, be.inventory));
        worldIn.removeBlockEntity(pos);

        ReactorControllerBlock controller = (ReactorControllerBlock) state.getBlock();

        controller.Rotate(state, pos.below(3), worldIn, 0);
        List<? extends Player> players = worldIn.players();
        for (Player p : players) {
            p.sendSystemMessage(Component.translatable("reactor.info.assembled.destroyer"));
        }
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        if (Boolean.TRUE.equals(state.getValue(ASSEMBLED)))
            return;
        List<? extends Player> players = level.players();
        ReactorControllerBlock controller = (ReactorControllerBlock) state.getBlock();
        controller.Verify(state, pos, level, players, true);
        for (Player p : players) {
            p.sendSystemMessage(Component.translatable("reactor.info.is"));
        }
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
        super.playerDestroy(level, player, pos, state, blockEntity, tool);
        ReactorControllerBlock controller = (ReactorControllerBlock) state.getBlock();
        ReactorControllerBlockEntity entity = controller.getBlockEntity(level, pos);
        if (!entity.created)
            return;
        controller.Rotate(state, pos.below(3), level, 0);
        List<? extends Player> players = level.players();
        for (Player p : players) {
            p.sendSystemMessage(Component.translatable("reactor.info.assembled.destroyer"));
        }
    }

    // this is the Function that verifies if the pattern is correct (as a test, we added the energy output)
    public void Verify(BlockState state, BlockPos pos, Level level, List<? extends Player> players, boolean create){
        ReactorControllerBlock controller = (ReactorControllerBlock) level.getBlockState(pos).getBlock();
        ReactorControllerBlockEntity entity = controller.getBlockEntity(level, pos);
        var result = CNMultiblock.REGISTRATE_MULTIBLOCK.findStructure(level, pos); // control the pattern
        if (result != null) { // the pattern is correct
            CreateNuclear.LOGGER.info("structure verified, SUCCESS to create multiblock");

            for (Player player : players) {
                if (create && !entity.created)                 {
                    player.sendSystemMessage(Component.literal("WARNING : Reactor Assembled"));
                    level.setBlockAndUpdate(pos, state.setValue(ASSEMBLED, true));
                    entity.created = true;
                    entity.destroyed = false;
                }
            }
            return;
        }

        // the pattern is incorrect
        CreateNuclear.LOGGER.info("structure not verified, FAILED to create multiblock");
        for (Player player : players) {
            if (!create && !entity.destroyed)
            {
                //p.sendSystemMessage(Component.literal("CRITICAL : Reactor Destroyed"));
                player.sendSystemMessage(Component.translatable("reactor.info.assembled.destroyer"));
                level.setBlockAndUpdate(pos, state.setValue(ASSEMBLED, false));
                entity.created = false;
                entity.destroyed = true;
                Rotate(state, pos.below(3), level, 0);
            }
        }
    }
    public void Rotate(BlockState state, BlockPos pos, Level level, int rotation) {
        if (level.getBlockState(pos).is(CNBlocks.REACTOR_OUTPUT.get())) {
            ReactorOutput block = (ReactorOutput) level.getBlockState(pos).getBlock();
            ReactorOutputEntity entity = block.getBlockEntityType().getBlockEntity(level, pos);

            if (Boolean.TRUE.equals(state.getValue(ASSEMBLED)) && rotation != 0) { // Starting the energy
                //CreateNuclear.LOGGER.info("Change " + pos);
                if (entity.getDir() == 1)
                    rotation = -rotation;
                entity.speed = rotation;
                entity.setSpeed(Math.abs(entity.speed));
                entity.updateSpeed = true;
                entity.updateGeneratedRotation();
            } else { // stopping the energy

                entity.setSpeed(0);
                entity.speed = 0;
                entity.updateSpeed = true;
                entity.updateGeneratedRotation();
                //CreateNuclear.LOGGER.info("Unchanged " + pos);
            }
            if (rotation < 0)
                rotation = -rotation;
            entity.setSpeed(rotation);

            //CreateNuclear.LOGGER.info("SPEED : " + entity.getSpeed2() + " - DIR : " + entity.getDir() + "  pos : " + pos);
        }
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