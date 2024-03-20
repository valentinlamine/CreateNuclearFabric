package net.ynov.createnuclear.multiblock.controller;

import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.item.ItemHelper;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
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
import net.ynov.createnuclear.multiblock.energy.ReactorOutput;
import net.ynov.createnuclear.multiblock.energy.ReactorOutputEntity;
import net.ynov.createnuclear.gui.CNIconButton;
import net.ynov.createnuclear.item.CNItems;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class ReactorControllerBlock extends HorizontalDirectionalBlock implements IBE<ReactorControllerBlockEntity> {
    public boolean destroyed = false;
    public boolean created = false;
    public int speed = 16; // This is the result speed of the reactor, change this to change the total capacity
    //public ReactorController controller;
    public static final BooleanProperty ASSEMBLED = BooleanProperty.create("assembled");
    private boolean powered;
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
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn,
                                 BlockHitResult hit) {
        if (worldIn.isClientSide)
            return InteractionResult.SUCCESS;

        Item item = player.getItemInHand(handIn).getItem();

        if (item.getDescriptionId().equals(CNItems.WELDING_KIT.getDescriptionId())) { //Si le weldingKit est dans la main
            if (Boolean.TRUE.equals(state.getValue(ASSEMBLED))) {
                player.sendSystemMessage(Component.literal("Multiblock déjà assemblé").withStyle(ChatFormatting.YELLOW));
                return InteractionResult.SUCCESS;
            }
            player.sendSystemMessage(Component.literal("Analyse multiBlock"));

            var result = CNMultiblock.REGISTRATE_MULTIBLOCK.findStructure(worldIn, pos);
            if (result != null) {
                player.sendSystemMessage(Component.literal("MultiBlock assemblé.").withStyle(ChatFormatting.BLUE));
                worldIn.setBlockAndUpdate(pos, state.setValue(ASSEMBLED, true));
            } else {
                player.sendSystemMessage(Component.literal("Erreur dans l'assemblage du multiBlock").withStyle(ChatFormatting.RED));
            }
            return InteractionResult.SUCCESS;
        }


        if (Boolean.FALSE.equals(state.getValue(ASSEMBLED))) {
            player.sendSystemMessage(Component.literal("Multiblock not assembled").withStyle(ChatFormatting.RED));
        }else {
            withBlockEntityDo(worldIn, pos, be -> NetworkHooks.openScreen((ServerPlayer) player, be, be::sendToMenu)); // Ouvre le menu de reactor controller
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.hasBlockEntity() || state.getBlock() == newState.getBlock())
            return;

        withBlockEntityDo(worldIn, pos, be -> ItemHelper.dropContents(worldIn, pos, be.inventory));
        worldIn.removeBlockEntity(pos);
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
        List<? extends Player> players = level.players();
        this.Verify(pos, level, players, true);
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
        if (!created)
            return;
        ReactorOutputEntity.structure = false;
        Rotate(pos.below(3), level, 0);
        List<? extends Player> players = level.players();
        for (Player p : players) {
            p.sendSystemMessage(Component.literal("CRITICAL : Reactor Destroyed"));
        }
        super.playerDestroy(level, player, pos, state, blockEntity, tool);
    }

    /*@Override
    protected void read(CompoundTag compound, boolean clientPacket) { //Permet de stocker les item 1/2

        super.read(compound, clientPacket);
    }

    @Override
    protected void write(CompoundTag compound, boolean clientPacket) { //Permet de stocker les item 2/2
        compound.put("Inventory", inventory.serializeNBT());
        super.write(compound, clientPacket);
    }*/

    // this is the Function that verifies if the pattern is correct (as a test, we added the energy output)
    public void Verify(BlockPos pos, Level level, List<? extends Player> players, boolean create){
        var result = CNMultiblock.REGISTRATE_MULTIBLOCK.findStructure(level, pos); // control the pattern
        if (result != null) { // the pattern is correct
            CreateNuclear.LOGGER.info("structure verified, SUCCESS to create multiblock");

            for (Player player : players) {
                if (create && !created)
                {
                    player.sendSystemMessage(Component.literal("WARNING : Reactor Assembled"));
                    created = true;
                    destroyed = false;
                }
            }
            ReactorOutputEntity.structure = true;
            return;
        }

        // the pattern is incorrect
        CreateNuclear.LOGGER.info("structure not verified, FAILED to create multiblock");
        for (Player player : players) {
            if (!create && !destroyed)
            {
                player.sendSystemMessage(Component.literal("CRITICAL : Reactor Destroyed"));
                destroyed = true;
                created = false;
            }
        }
        ReactorOutputEntity.structure = false;
        Rotate(pos.below(3), level, 0);
    }
    public void Rotate(BlockPos pos, Level level, int rotation) {
        if (level.getBlockState(pos).is(CNBlocks.REACTOR_OUTPUT.get())) {
            if (ReactorOutputEntity.structure) { // Starting the energy
                ReactorOutput block = (ReactorOutput) level.getBlockState(pos).getBlock();
                Objects.requireNonNull(block.getBlockEntityType().getBlockEntity(level, pos)).speed = rotation;
                Objects.requireNonNull(block.getBlockEntityType().getBlockEntity(level, pos)).updateSpeed = true;
                Objects.requireNonNull(block.getBlockEntityType().getBlockEntity(level, pos)).updateGeneratedRotation();
            } else { // stopping the energy
                ReactorOutput block = (ReactorOutput) level.getBlockState(pos).getBlock();
                Objects.requireNonNull(block.getBlockEntityType().getBlockEntity(level, pos)).speed = 0;
                Objects.requireNonNull(block.getBlockEntityType().getBlockEntity(level, pos)).updateSpeed = true;
                Objects.requireNonNull(block.getBlockEntityType().getBlockEntity(level, pos)).updateGeneratedRotation();
            }

            ReactorOutput block = (ReactorOutput) level.getBlockState(pos).getBlock();

            //CompoundTag compoundtag =
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
