package net.ynov.createnuclear.tools.reactor;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
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


public class ReactorController extends Block {

    public static final BooleanProperty POWER = BooleanProperty.create("power");
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;


    public ReactorController(Properties properties) {
        super(properties);
        this.registerDefaultState((BlockState) ((BlockState)this.defaultBlockState().setValue(POWER, false)).setValue(FACING, Direction.NORTH));
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder){
        builder.add(POWER, FACING);
    }

    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        // Ignore the interaction if it was run on the client.
        if (world.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        if (state.hasProperty(FACING) && state.hasProperty(POWER)) {
            // Get the current value of the "activated" property
            boolean activated = state.getValue(POWER);

            // Flip the value of activated and save the new blockstate.
            world.setBlockAndUpdate(pos, state.setValue(POWER, !activated));

        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return (BlockState)this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
    }


}
