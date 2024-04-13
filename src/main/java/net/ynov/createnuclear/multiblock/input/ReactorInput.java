package net.ynov.createnuclear.multiblock.input;


import com.simibubi.create.AllItems;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.ynov.createnuclear.CreateNuclear;
import net.ynov.createnuclear.block.CNBlocks;
import net.ynov.createnuclear.blockentity.CNBlockEntities;
import net.ynov.createnuclear.item.CNItems;
import net.ynov.createnuclear.multiblock.controller.ReactorControllerBlock;
import net.ynov.createnuclear.multiblock.controller.ReactorControllerBlockEntity;
import net.ynov.createnuclear.shape.CNShapes;
import net.ynov.createnuclear.tools.HorizontalDirectionalReactorBlock;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ReactorInput extends HorizontalDirectionalReactorBlock implements IWrenchable, IBE<ReactorInputEntity> {

    public ReactorInput(Properties properties) {
        super(properties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (worldIn.isClientSide()) return InteractionResult.SUCCESS;

        List<? extends Player> players = worldIn.players();
        ReactorControllerBlock controller = FindController(worldIn, pos);
        if (controller != null) {
            controller.Verify(controller.defaultBlockState(), new BlockPos(pos.getX(), pos.getY(), pos.getZ()+4), worldIn, players, false);
            BlockEntity blockEntity = worldIn.getBlockEntity(new BlockPos(pos.getX(), pos.getY(), pos.getZ()+4));
            if (blockEntity instanceof ReactorControllerBlockEntity controllerBlockEntity) {
                ItemStack item = new ItemStack(CNItems.GRAPHITE_ROD);
                item.setCount(3);
                controllerBlockEntity.inventory.setStackInSlot(1, item);
                CreateNuclear.LOGGER.warn(controllerBlockEntity.inventory.getSlots() + "  " +
                        controllerBlockEntity.inventory.getStackInSlot(0).getItem().getDescriptionId() + "  " +
                        controllerBlockEntity.inventory.serializeNBT());
            }
        }

        return InteractionResult.PASS;
    }

    @Override
    public void onPlace(BlockState state, Level worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, worldIn, pos, oldState, isMoving);
        List<? extends Player> players = worldIn.players();
        ReactorControllerBlock controller = FindController(worldIn, new BlockPos(pos.getX(), pos.getY(), pos.getZ()+4));
        if (controller != null)
            controller.Verify(controller.defaultBlockState(), new BlockPos(pos.getX(), pos.getY(), pos.getZ()+4), worldIn, players, true);
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
        super.playerDestroy(level, player, pos, state, blockEntity, tool);
        List<? extends Player> players = level.players();
        ReactorControllerBlock controller = FindController(level, new BlockPos(pos.getX(), pos.getY(), pos.getZ()+4));
        if (controller != null)
            controller.Verify(controller.defaultBlockState(), new BlockPos(pos.getX(), pos.getY(), pos.getZ()+4), level, players, false);
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
        List<? extends Player> players = pLevel.players();
        ReactorControllerBlock controller = FindController(pLevel, new BlockPos(pPos.getX(), pPos.getY(), pPos.getZ()+4));
        if (controller != null)
            controller.Verify(controller.defaultBlockState(), new BlockPos(pPos.getX(), pPos.getY(), pPos.getZ()+4), pLevel, players, false);
    }

    public ReactorControllerBlock FindController(Level level, BlockPos pos) {
        if (level.getBlockState(new BlockPos(pos.getX(), pos.getY(), pos.getZ()+4)).getBlock() instanceof ReactorControllerBlock) {
            return (ReactorControllerBlock) level.getBlockState(new BlockPos(pos.getX(), pos.getY(), pos.getZ()+4)).getBlock();
        }
        return null;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection());
    }
    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return CNShapes.REACTOR_INPUT.get(state.getValue(FACING));
    }

    @Override
    public Class<ReactorInputEntity> getBlockEntityClass() {
        return ReactorInputEntity.class;
    }

    @Override
    public BlockEntityType<? extends ReactorInputEntity> getBlockEntityType() {
        return CNBlockEntities.REACTOR_INPUT.get();
    }
}
