package net.ynov.createnuclear.content.reactor.controller;

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
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.ynov.createnuclear.CNMultiblock;
import net.ynov.createnuclear.blockentity.CNBlockEntities;
import net.ynov.createnuclear.gui.CNIconButton;
import net.ynov.createnuclear.item.CNItems;

import java.util.List;

public class ReactorControllerBlock extends HorizontalDirectionalBlock implements IBE<ReactorControllerBlockEntity> {
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

        if (CNItems.WELDING_KIT.is(item)) { //Si le weldingKit est dans la main
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

    @Override
    public Class<ReactorControllerBlockEntity> getBlockEntityClass() {
        return ReactorControllerBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends ReactorControllerBlockEntity> getBlockEntityType() {
        return CNBlockEntities.REACTOR_CONTROLLER.get();
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
}
