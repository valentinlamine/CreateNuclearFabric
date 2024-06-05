package net.ynov.createnuclear.multiblock.energy;

import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;

import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.ynov.createnuclear.block.CNBlocks;
import net.ynov.createnuclear.blockentity.CNBlockEntities;
import net.ynov.createnuclear.multiblock.controller.ReactorControllerBlock;
import net.ynov.createnuclear.multiblock.controller.ReactorControllerBlockEntity;
import net.ynov.createnuclear.shape.CNShapes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class ReactorOutput extends DirectionalKineticBlock implements IWrenchable, IBE<ReactorOutputEntity> {

	//public static final IntegerProperty SPEED = IntegerProperty.create("speed", 0, 256);
	public static final IntegerProperty DIR = IntegerProperty.create("dir", 0, 2);

	public ReactorOutput(Properties properties) {
		super(properties);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		//builder.add(SPEED);
		builder.add(DIR);
		super.createBlockStateDefinition(builder);
	}

	@Override
	public @NotNull InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (level.isClientSide)
			return InteractionResult.SUCCESS;
		else {
			ReactorControllerBlock controller = FindController(pos, level);
			if (controller != null){
				ReactorControllerBlockEntity entity = controller.getBlockEntity(level, pos.above(3));
                assert entity != null;
                if (entity.getAssembled()){
					ReactorOutputEntity control = Objects.requireNonNull(getBlockEntity(level, pos));
					if (control.getDir() == 0)
						control.setDir(1, level, pos);
					else control.setDir(0, level, pos);
				}
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
			controller.Verify(controller.defaultBlockState(), pos.above(3), worldIn, players, true);
	}

	@Override
	public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
		super.playerDestroy(level, player, pos, state, blockEntity, tool);
		List<? extends Player> players = level.players();
		ReactorControllerBlock controller = FindController(pos, level);
		if (controller != null)
			controller.Verify(controller.defaultBlockState(), pos.above(3), level, players, false);
	}

	@Override
	public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
		super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
		List<? extends Player> players = pLevel.players();
		ReactorControllerBlock controller = FindController(pPos, pLevel);
		if (controller != null)
			controller.Verify(controller.defaultBlockState(), pPos.above(3), pLevel, players, false);
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
		return defaultBlockState().setValue(FACING, preferred)/*.setValue(SPEED, 0)*/.setValue(DIR, 0);
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
		return CNBlockEntities.REACTOR_OUTPUT.get();
	}

	public ReactorControllerBlock FindController(BlockPos pos, Level level){
		if (level.getBlockState(pos.above(3)).getBlock() == CNBlocks.REACTOR_CONTROLLER.get()){
            return (ReactorControllerBlock)level.getBlockState(pos.above(3)).getBlock();
		}
		return null;
	}
}

