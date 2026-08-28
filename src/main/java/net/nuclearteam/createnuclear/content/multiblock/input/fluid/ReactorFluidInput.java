package net.nuclearteam.createnuclear.content.multiblock.input.fluid;

import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.nuclearteam.createnuclear.CNBlockEntityTypes;
import net.nuclearteam.createnuclear.CNShapes;
import net.nuclearteam.createnuclear.content.multiblock.MultiblockHelpers;
import net.nuclearteam.createnuclear.content.multiblock.controller.ReactorControllerBlockEntity;
import net.nuclearteam.createnuclear.foundation.block.MultiDirectionalReactorBlock;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class ReactorFluidInput extends MultiDirectionalReactorBlock implements IWrenchable, IBE<ReactorFluidInputEntity> {

	public ReactorFluidInput(Properties properties) {
		super(properties);
	}

	@Override
	public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean moved) {
		super.onPlace(state, world, pos, oldState, moved);
		MultiblockHelpers.handleOnPlace(pos, world, ReactorControllerBlockEntity::addInputFluid);
	}

	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity pPlacer, ItemStack stack) {
		super.setPlacedBy(level, pos, state, pPlacer, stack);
		MultiblockHelpers.handleAdvancedPlacedBy(pos, level, pPlacer);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);
        super.createBlockStateDefinition(builder);
	}

	@Override
	public InteractionResult onWrenched(BlockState state, UseOnContext context) {
		return InteractionResult.SUCCESS;
	}

	@Override
	public VoxelShape getSidesShape(BlockState pState, BlockGetter pReader, BlockPos pPos) {
		return Shapes.fullCube();
	}

	@Override
	public BlockState getPlacementState(BlockPlaceContext context) {
		return this.defaultBlockState()
                .setValue(FACING, context.getPlayerLookDirection().getOpposite());
	}

	@Override
    public @NotNull VoxelShape getOutlineShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return CNShapes.REACTOR_FLUID_INPUT.get(state.getValue(FACING));
    }

	@Override
	public InteractionResult onUse(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand,
								 BlockHitResult ray) {
		ItemStack heldItem = player.getItemInHand(hand);
		boolean onClient = world.isClientSide;

		if (heldItem.isEmpty())
			return InteractionResult.PASS;
		if (!player.isCreative())
			return InteractionResult.PASS;

		return PlayerInteractReactorFluidInput.interact(world, pos, player, hand, heldItem, onClient, ray);
	}

	@Override
	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
		super.onRemove(state, world, pos, newState, isMoving);
		MultiblockHelpers.handleRemoval(pos, world, ReactorControllerBlockEntity::removeInputFluid);
	}

	@Override
	public Class<ReactorFluidInputEntity> getBlockEntityClass() {
		return ReactorFluidInputEntity.class;
	}

	@Override
	public BlockEntityType<? extends ReactorFluidInputEntity> getBlockEntityType() {
		return CNBlockEntityTypes.REACTOR_FLUID_INPUT.get();
	}

	@Override
	public boolean hasComparatorOutput(BlockState state) {
		return true;
	}
}