package net.ynov.createnuclear.energy;

import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;

import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.ynov.createnuclear.CreateNuclear;
import net.ynov.createnuclear.block.CNBlocks;
import net.ynov.createnuclear.block.ReactorController;
import net.ynov.createnuclear.blockentity.CNEntityTypes;
import net.ynov.createnuclear.content.reactor.controller.ReactorControllerBlock;
import net.ynov.createnuclear.shape.CNShapes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class ReactorOutput extends DirectionalKineticBlock implements IBE<ReactorOutputEntity> {

	public ReactorOutput(Properties properties) {
		super(properties);
	}

	@Override
	public @NotNull InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (level.isClientSide)
			return InteractionResult.SUCCESS;
		else {
			ReactorControllerBlock controller = FindController(pos, level);
			if (controller != null){
				player.sendSystemMessage(Component.literal("controller is " + controller.created + " " + controller.speed));
				if (controller.created){
					controller.speed = -controller.speed;
					controller.Rotate(pos, level, controller.speed);
				}
				else controller.Rotate(pos, level, 0);
			}
			return InteractionResult.CONSUME;
		}
	}

	@Override
	public void onPlace(BlockState state, Level worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
		super.onPlace(state, worldIn, pos, oldState, isMoving);
		List<? extends Player> players = worldIn.players();
		ReactorControllerBlock controller = FindController(pos, worldIn);
		if (controller != null)
			controller.Verify(pos, worldIn, players, true);
	}

	@Override
	public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
		super.playerDestroy(level, player, pos, state, blockEntity, tool);
		List<? extends Player> players = level.players();
		ReactorControllerBlock controller = FindController(pos, level);
		if (controller != null)
			controller.Verify(pos, level, players, false);
	}

	@Override
	public @NotNull VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return CNShapes.REACTOR_OUTPUT.get(state.getValue(FACING));
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Direction preferred = getPreferredFacing(context);
		if ((context.getPlayer() != null && context.getPlayer()
			.isShiftKeyDown()) || preferred == null)
			return super.getStateForPlacement(context);
		return defaultBlockState().setValue(FACING, preferred);
	}

	// IRotate:

	@Override
	public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
		return face == state.getValue(FACING);
	}

	@Override
	public Axis getRotationAxis(BlockState state) {
		return state.getValue(FACING)
			.getAxis();
	}

	@Override
	public boolean hideStressImpact() {
		return true;
	}

	@Override
	public boolean isPathfindable(BlockState state, BlockGetter reader, BlockPos pos, PathComputationType type) {
		return false;
	}

	@Override
	public Class<ReactorOutputEntity> getBlockEntityClass() {
		return ReactorOutputEntity.class;
	}

	@Override
	public BlockEntityType<? extends ReactorOutputEntity> getBlockEntityType() {
		return CNEntityTypes.MOTOR2.get();
	}

	public ReactorControllerBlock FindController(BlockPos pos, Level level){
		if (level.getBlockState(pos.above(3)).getBlock() == CNBlocks.REACTOR_CONTROLLER.get()){
			ReactorControllerBlock controller = (ReactorControllerBlock)level.getBlockState(pos.above(3)).getBlock();
			return controller;
		}
		return null;
	}

}

