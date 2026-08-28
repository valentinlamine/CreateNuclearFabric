package net.nuclearteam.createnuclear.content.multiblock.output;


import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import net.createmod.catnip.placement.IPlacementHelper;
import net.createmod.catnip.placement.PlacementHelpers;
import net.createmod.catnip.placement.PlacementOffset;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.nuclearteam.createnuclear.*;
import net.nuclearteam.createnuclear.content.multiblock.MultiblockHelpers;
import net.nuclearteam.createnuclear.content.multiblock.controller.ReactorControllerBlockEntity;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Predicate;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ReactorOutput extends DirectionalKineticBlock implements IWrenchable, IBE<ReactorOutputEntity> {
    public static final IntegerProperty DIR = IntegerProperty.of("dir", 0, 2);

    private static final int placementHelperId = PlacementHelpers.register(new PlacementHelper());

    public ReactorOutput(Properties properties) {
        super(properties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(DIR);
        super.createBlockStateDefinition(builder);
    }


    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, level, pos, oldState, isMoving);
        MultiblockHelpers.handleOnPlace(pos, level, ReactorControllerBlockEntity::addOutput);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @javax.annotation.Nullable LivingEntity pPlacer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, pPlacer, stack);
        MultiblockHelpers.handleAdvancedPlacedBy(pos, level, pPlacer);
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
        MultiblockHelpers.handleRemoval(pPos, pLevel, ReactorControllerBlockEntity::removeOutput);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return CNShapes.REACTOR_OUTPUT.get(state.getValue(FACING));
    }

    @Override
    public BlockState getPlacementState(BlockPlaceContext context) {
        Direction preferred = getPreferredFacing(context);
        if ((context.getPlayer() != null && context.getPlayer()
                .isShiftKeyDown()) || preferred == null)
            return super.getPlacementState(context);
        return getDefaultState().setValue(FACING, preferred).setValue(DIR, 0);
    }

    @Override
    public InteractionResult onUse(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        ItemStack heldItem = player.getItemInHand(hand);
        IPlacementHelper placementHelper = PlacementHelpers.get(placementHelperId);
        if (!player.isShiftKeyDown() && player.canModifyBlocks()) {
            if (placementHelper.matchesItem(heldItem) && placementHelper.getOffset(player, level, state, pos, hitResult)
                .placeInWorld(level, (BlockItem) heldItem.getItem(), player, hand, hitResult)
                .isAccepted())
                return InteractionResult.SUCCESS;
        }

        return super.onUse(state, level, pos, player, hand, hitResult);
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
    public boolean canPathfindThrough(BlockState state, BlockGetter reader, BlockPos pos, PathComputationType type) {
        return false;
    }

    @Override
    public Class<ReactorOutputEntity> getBlockEntityClass() {
        return ReactorOutputEntity.class;
    }

    @Override
    public BlockEntityType<? extends ReactorOutputEntity> getBlockEntityType() {
        return CNBlockEntityTypes.REACTOR_OUTPUT.get();
    }

    private static class PlacementHelper implements IPlacementHelper {

        @Override
        public Predicate<ItemStack> getItemPredicate() {
            return AllBlocks.SHAFT::isIn;
        }

        @Override
        public Predicate<BlockState> getStatePredicate() {
            return s -> s.getBlock() instanceof ReactorOutput;
        }

        @Override
        public PlacementOffset getOffset(Player player, Level world, BlockState state, BlockPos pos, BlockHitResult ray) {
            Direction facing = state.getValue(FACING);
            BlockPos target = pos.offset(facing);

            if (!world.getBlockState(target).isReplaceable())
                return PlacementOffset.fail();

            return PlacementOffset.success(target, s -> s.setValue(BlockStateProperties.AXIS, facing.getAxis()));
        }
    }
}