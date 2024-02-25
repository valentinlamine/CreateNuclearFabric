package net.ynov.createnuclear.tools.reactor;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.ynov.createnuclear.CNMultiblock;
import net.ynov.createnuclear.item.CNItems;


public class ReactorController extends Block {

    public static final BooleanProperty POWER = BooleanProperty.create("power");
    public static final BooleanProperty ASSEMBLED = BooleanProperty.create("assembled");
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;


    public ReactorController(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(POWER, false).setValue(ASSEMBLED, false).setValue(FACING, Direction.NORTH));
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder){
        builder.add(POWER, FACING, ASSEMBLED);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        // Ignore the interaction if it was run on the client.
        if (world.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        Item item = player.getItemInHand(hand).getItem();

        if (item.getDescriptionId().equals(CNItems.WELDING_KIT.getDescriptionId())) {
            if (Boolean.TRUE.equals(state.getValue(ASSEMBLED))) {
                player.sendSystemMessage(Component.literal("Multiblock déjà assemblé").withStyle(ChatFormatting.YELLOW));
                return InteractionResult.SUCCESS;
            }
            player.sendSystemMessage(Component.literal("Analyse multiBlock"));

            var result = CNMultiblock.REGISTRATE_MULTIBLOCK.findStructure(world, pos);
            if (result != null) {
                player.sendSystemMessage(Component.literal("MultiBlock assemblé.").withStyle(ChatFormatting.BLUE));
                world.setBlockAndUpdate(pos, state.setValue(ASSEMBLED, true));
            } else {
                player.sendSystemMessage(Component.literal("Erreur dans l'assemblage du multiBlock").withStyle(ChatFormatting.RED));
            }
            return InteractionResult.SUCCESS;
        }

        if (state.hasProperty(FACING) && state.hasProperty(POWER) && state.hasProperty(ASSEMBLED)) {

            if (Boolean.FALSE.equals(state.getValue(ASSEMBLED))) {
                player.sendSystemMessage(Component.literal("Multiblock not assembled").withStyle(ChatFormatting.RED));
            }else {
                player.sendSystemMessage(Component.literal("Multiblock ASSEMBLED!!").withStyle(ChatFormatting.BLUE));
            }
            return InteractionResult.SUCCESS;

//
//
//            // Get the current value of the "activated" property
//            boolean activated = state.getValue(POWER);
//
//            // Flip the value of activated and save the new blockstate.
//            world.setBlockAndUpdate(pos, state.setValue(POWER, !activated));

        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
    }

}
