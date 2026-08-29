package net.nuclearteam.createnuclear.content.multiblock.controller;

import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.item.ItemHelper;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.ChatFormatting;
import net.minecraft.world.InteractionHand;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.nuclearteam.createnuclear.*;
import net.nuclearteam.createnuclear.content.multiblock.IHeat;
import net.nuclearteam.createnuclear.content.multiblock.MultiblockHelpers;
import net.nuclearteam.createnuclear.content.multiblock.ReactorAssembler;
import net.nuclearteam.createnuclear.content.multiblock.input.fluid.PersistentFluidLocks;
import net.nuclearteam.createnuclear.foundation.advancement.CNAdvancement;
import net.nuclearteam.createnuclear.foundation.block.HorizontalDirectionalReactorBlock;
import net.nuclearteam.createnuclear.foundation.utility.CreateNuclearLang;
import net.nuclearteam.createnuclear.foundation.utility.NotifyUtil;
import net.nuclearteam.createnuclear.infrastructure.config.CNConfigs;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
@SuppressWarnings("deprecation")
public class ReactorControllerBlock extends HorizontalDirectionalReactorBlock implements IWrenchable, IBE<ReactorControllerBlockEntity> {
    public static final BooleanProperty ASSEMBLED = BooleanProperty.create("assembled");
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    public ReactorControllerBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(ASSEMBLED, false)
                .setValue(ACTIVE, false) // Inactive by default
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING).add(ASSEMBLED).add(ACTIVE);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite())
                .setValue(ASSEMBLED, false)
                .setValue(ACTIVE, false);
    }
    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos,
                                boolean isMoving) {
        if (worldIn.isClientSide)
            return;
        // A direct neighbor of the controller has changed. Do NOT blindly disassemble:
        // only do so if the structure is actually broken. Otherwise, placing/replacing a
        // block adjacent to the controller (frame above/below, input/alarm on the sides,
        // cooler behind) would invalidate the assembly that was just validated.
        // disassemble() checks findStructure and does nothing if the structure is complete.
        if (state.getValue(ASSEMBLED))
            ReactorAssembler.disassemble(pos, worldIn);
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (worldIn.isClientSide)
            return InteractionResult.SUCCESS;

        BlockEntity blockEntity = worldIn.getBlockEntity(pos);
        if (!(blockEntity instanceof ReactorControllerBlockEntity controllerBlockEntity)) return InteractionResult.PASS;

        ItemStack heldItem = player.getItemInHand(handIn);
        if (heldItem.is(Items.DEBUG_STICK)) {
            withBlockEntityDo(worldIn, pos, be -> be.logReactorConnections(player));
        }

        if (!state.getValue(ASSEMBLED)) {
        }
        else {
            if (heldItem.is(CNItems.REACTOR_BLUEPRINT.get()) && controllerBlockEntity.getInventoryObject().getItem(0).isEmpty() && heldItem.getTag() != null){
                withBlockEntityDo(worldIn, pos, be -> {
                    be.getInventoryObject().setStackInSlot(0, heldItem);
                    be.setConfiguredPattern(heldItem);

                    player.setItemInHand(handIn, ItemStack.EMPTY);
                });
                // Inserting the blueprint is what starts energy production: this is the activation
                // cue, not the multiblock assembly one (that lives in ReactorAssembler).
                // One-shot played server-side (null player) so it broadcasts to nearby clients.
                worldIn.playSound(null, pos, CNSoundEvents.REACTOR_ACTIVATION.getMainEvent(), SoundSource.BLOCKS, 1.0f, 1.0f);
                return InteractionResult.SUCCESS;

            }
            else if (heldItem.isEmpty() && !controllerBlockEntity.getInventoryObject().getItem(0).isEmpty()) {
                withBlockEntityDo(worldIn, pos, be -> {
                    if (IHeat.HeatLevel.of(be.getInventoryObject().getItem(0).getOrCreateTag().getInt("heat"), be.getMultiblockSize()) == IHeat.HeatLevel.DANGER) {
                        be.getAdvancement().setPlayer(player.getUUID());
                        be.getAdvancement().awardPlayer(CNAdvancement.NO_TIME_TO_DIE);
                    }
                    player.setItemInHand(handIn, be.getInventoryObject().getItem(0));
                    be.getInventoryObject().setStackInSlot(0, ItemStack.EMPTY);
                    be.setConfiguredPattern(ItemStack.EMPTY);
                    //be.clearTimers(); // uncomment if the timer should reset when the reactor stops
                    be.getOutputManager().rotateOutputs(be.getLevel(), be.getAssembled(), 0);
                    be.notifyUpdate();
                });
                // Blueprint removed: the multiblock stays assembled, it just stops producing.
                worldIn.playSound(null, pos, CNSoundEvents.REACTOR_SHUT_OFF.getMainEvent(), SoundSource.BLOCKS, 1.0f, 1.0f);
                state.setValue(ASSEMBLED, false);
                return InteractionResult.SUCCESS;

            }
            else if (!heldItem.isEmpty() && !controllerBlockEntity.getInventoryObject().getItem(0).isEmpty()) {
                return InteractionResult.PASS;
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.hasBlockEntity() || state.getBlock() == newState.getBlock())
            return;

        withBlockEntityDo(worldIn, pos, be -> ItemHelper.dropContents(worldIn, pos, be.getInventoryObject()));
        worldIn.removeBlockEntity(pos);

        if (worldIn instanceof ServerLevel serverLevel) {
            PersistentFluidLocks.get(serverLevel).clearLock(pos);
        }

          if (!state.getValue(ASSEMBLED))
            return;

        int configRadius = CNConfigs.server().notify.warningDistance.get();
        boolean configWarnAll = CNConfigs.server().notify.warnAllPlayers.get();
        NotifyUtil.sendActionBar(worldIn, pos,
                CreateNuclearLang.translate("notification.reactor.disassembled"),
                ChatFormatting.GOLD, configRadius, configWarnAll
        );
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity pPlacer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, pPlacer, stack);
        MultiblockHelpers.handleAdvancedPlacedBy(pos, level, pPlacer);
        if (state.getValue(ASSEMBLED))
            return;
        ReactorAssembler.assemble(pos, level);
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
        super.playerDestroy(level, player, pos, state, blockEntity, tool);
        if (!(blockEntity instanceof ReactorControllerBlockEntity entity)) return;
        if (!entity.isAssembled()) return;
        for (Player p : level.players()) {
            p.displayClientMessage(Component.translatable("reactor.info.assembled.creator"), false);
        }
//        entity.removeIOAll();
    }

    @Override
    public Class<ReactorControllerBlockEntity> getBlockEntityClass() {
        return ReactorControllerBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends ReactorControllerBlockEntity> getBlockEntityType() {
        return CNBlockEntityTypes.REACTOR_CONTROLLER.get();
    }
}
