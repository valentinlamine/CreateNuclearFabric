package net.ynov.createnuclear.multiblock.controller;

import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.item.ItemHelper;
import io.github.fabricators_of_create.porting_lib.util.BlockSnapshot;
import io.github.fabricators_of_create.porting_lib.util.NetworkHooks;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.ynov.createnuclear.CNMultiblock;
import net.ynov.createnuclear.CreateNuclear;
import net.ynov.createnuclear.block.CNBlocks;
import net.ynov.createnuclear.blockentity.CNBlockEntities;
import net.ynov.createnuclear.multiblock.energy.ReactorOutput;
import net.ynov.createnuclear.multiblock.energy.ReactorOutputEntity;
import net.ynov.createnuclear.gui.CNIconButton;
import net.ynov.createnuclear.item.CNItems;
import org.jetbrains.annotations.Nullable;

import java.awt.desktop.AboutHandler;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import static net.ynov.createnuclear.multiblock.energy.ReactorOutput.ACTIVATED;
import static net.ynov.createnuclear.multiblock.energy.ReactorOutput.SPEED;

public class ReactorControllerBlock extends HorizontalDirectionalBlock implements IBE<ReactorControllerBlockEntity> {
    public static final BooleanProperty ASSEMBLED = BooleanProperty.create("assembled");
    public static final BooleanProperty PLACED = BooleanProperty.create("placed");
    private boolean powered;
    private List<CNIconButton> switchButtons;

    public ReactorControllerBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING).add(ASSEMBLED);
        builder.add(PLACED);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection())
                .setValue(ASSEMBLED, false)
                .setValue(PLACED, false);
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn,
                                 BlockHitResult hit) {
        if (worldIn.isClientSide)
            return InteractionResult.SUCCESS;

        if (Boolean.FALSE.equals(state.getValue(ASSEMBLED))) {
            player.sendSystemMessage(Component.literal("Multiblock not assembled").withStyle(ChatFormatting.RED));
        }else {
            ReactorOutput b = (ReactorOutput) worldIn.getBlockState(pos.below(3)).getBlock();
            ReactorControllerBlock controller = (ReactorControllerBlock) state.getBlock();
            controller.Rotate(b.getBlockEntity(worldIn, pos.below(3)), 0);
            withBlockEntityDo(worldIn, pos, be -> NetworkHooks.openScreen((ServerPlayer) player, be, be::sendToMenu)); // Ouvre le menu de reactor controller
        }

        return InteractionResult.SUCCESS;
    }

    @Override // called when the player destroys the block, with or without a tool
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
        List<? extends Player> players = level.players();
        Verify(state, pos, level, players, false);
        super.playerDestroy(level, player, pos, state, blockEntity, tool);
    }

    public boolean isPowered() {
       return powered; // les variables ne sont pas sauvegarder lors d'un déchargement/rechargement de monde (donc passer par le blockState/ou trouver une autre methode)
    }
    public void setPowered(boolean power) {
        powered = power;
//        worldIn.setBlockAndUpdate(pos, state.setValue(POWERED, power));
    }

    public List<CNIconButton> getSwitchButtons() {
        return switchButtons;
    }

    public void setSwitchButtons(List<CNIconButton> switchButtons) {
        this.switchButtons = switchButtons;
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        state.setValue(PLACED, true);
        if (level.getBlockState(pos.below(3)).getBlock() != CNBlocks.REACTOR_OUTPUT.get())
            return;
        List<? extends Player> players = level.players();
        ReactorControllerBlock controller = (ReactorControllerBlock) state.getBlock();
        controller.Verify(state, pos, level, players, true);
    }

    // this is the Function that verifies if the pattern is correct (as a test, we added the energy output)
    public void Verify(BlockState state, BlockPos pos, Level level, List<? extends Player> players, boolean create){

        CreateNuclear.LOGGER.info("Reactors : " + ReactorControllerScreen.exist);
        ReactorControllerBlock controller = (ReactorControllerBlock) level.getBlockState(pos).getBlock();
        ReactorControllerBlockEntity entity = controller.getBlockEntity(level, pos);

        var result = CNMultiblock.REGISTRATE_MULTIBLOCK.findStructure(level, pos); // control the pattern
        if (result != null) { // the pattern is correct
            for (Player player : players) {
                if (Boolean.FALSE.equals(state.getValue(ASSEMBLED)))
                {
                    player.sendSystemMessage(Component.literal("WARNING : Reactor Assembled"));
                }
                level.setBlockAndUpdate(pos, state.setValue(ASSEMBLED, true));
            }
            return;
        }

        // the pattern is incorrect
        for (Player player : players) {
            if (Boolean.TRUE.equals(state.getValue(ASSEMBLED))) {
                player.sendSystemMessage(Component.literal("CRITICAL : Reactor Destroyed"));
            }
            level.setBlockAndUpdate(pos, state.setValue(ASSEMBLED, false));
            ReactorOutput b = (ReactorOutput) level.getBlockState(pos.below(3)).getBlock();
            Rotate(b.getBlockEntity(level, pos.below(3)), 0);
        }
    }
    public void Rotate(ReactorOutputEntity entity, int rotation) {
        CreateNuclear.LOGGER.info("No entity");
        if (entity != null) {
            ReactorControllerBlock b = (ReactorControllerBlock) entity.getLevel().getBlockState(entity.getBlockPos().above(3)).getBlock();
            if (Boolean.TRUE.equals(b.getBlockEntity(entity.getLevel(), entity.getBlockPos().above(3)).getBlockState().getValue(ASSEMBLED)) && rotation != 0) { // Starting the energy
                if (entity.getDir() == 1)
                    rotation = -rotation;
                CreateNuclear.LOGGER.info(entity.toString());
                entity.speed = rotation;
                // entity.setSpeed2(Math.abs(entity.speed), level, pos.below(3));
                entity.updateSpeed = true;
                entity.updateGeneratedRotation();
                CreateNuclear.LOGGER.info("Rotation - updated in theory");
            } else { // stopping the energy

                // entity.setSpeed2(0, level, pos.below(3));
                entity.speed = 0;
                entity.updateSpeed = true;
                entity.updateGeneratedRotation();
                CreateNuclear.LOGGER.info("Rotation - reseted in theory");
            }
            if (rotation < 0)
                rotation = -rotation;
            entity.setSpeed2(rotation, entity.getLevel(), entity.getBlockPos());

            CreateNuclear.LOGGER.info("SPEED : " + entity.getSpeed2() + " - DIR : " + entity.getDir());
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
