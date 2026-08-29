package net.nuclearteam.createnuclear.content.multiblock.input.item;

import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.item.ItemHelper;
import io.github.fabricators_of_create.porting_lib.util.NetworkHooks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.nuclearteam.createnuclear.CNBlockEntityTypes;
import net.nuclearteam.createnuclear.CNShapes;
import net.nuclearteam.createnuclear.content.multiblock.controller.ReactorControllerBlockEntity;
import net.nuclearteam.createnuclear.content.multiblock.MultiblockHelpers;
import net.nuclearteam.createnuclear.foundation.advancement.CNAdvancementBehaviour;
import net.nuclearteam.createnuclear.foundation.block.MultiDirectionalReactorBlock;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;


@ParametersAreNonnullByDefault
public class ReactorRodInput extends MultiDirectionalReactorBlock implements IWrenchable, IBE<ReactorRodInputEntity> {
    
    public ReactorRodInput(Properties properties) {
        super(properties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        ItemStack itemInHand = player.getItemInHand(handIn);

        if (itemInHand.getItem() instanceof BlockItem) {
            return InteractionResult.PASS;
        }

        if (worldIn.isClientSide()) {return InteractionResult.SUCCESS;}

        if (player instanceof ServerPlayer serverPlayer) {
            withBlockEntityDo(worldIn, pos,
                    be -> NetworkHooks.openScreen(serverPlayer, be, be::sendToMenu));
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, level, pos, oldState, isMoving);
        MultiblockHelpers.handleOnPlace(pos, level, ReactorControllerBlockEntity::addInput);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity pPlacer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, pPlacer, stack);
        MultiblockHelpers.handleAdvancedPlacedBy(pos, level, pPlacer);
    }


    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        // Only act when the block is actually removed/replaced, not on a mere state change
        // (e.g. a FACING update) — otherwise rotating the block would spill its rods.
        if (!pState.is(pNewState.getBlock())) {
            // Drop the contents BEFORE super removes the block entity: super.onRemove()
            // calls level.removeBlockEntity(pos), after which the inventory is gone.
            withBlockEntityDo(pLevel, pPos, be -> ItemHelper.dropContents(pLevel, pPos, be.inventory));
            MultiblockHelpers.handleRemoval(pPos, pLevel, ReactorControllerBlockEntity::removeInput);
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(FACING, context.getNearestLookingDirection().getOpposite());
    }
    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return CNShapes.REACTOR_ROD_INPUT.get(state.getValue(FACING));
    }

    @Override
    public Class<ReactorRodInputEntity> getBlockEntityClass() {
        return ReactorRodInputEntity.class;
    }

    @Override
    public BlockEntityType<? extends ReactorRodInputEntity> getBlockEntityType() {
        return CNBlockEntityTypes.REACTOR_ROD_INPUT.get();
    }
}
