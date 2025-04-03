package net.nuclearteam.createnuclear.multiblock.frame;

import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.nuclearteam.createnuclear.block.CNBlocks;
import net.nuclearteam.createnuclear.multiblock.controller.ReactorControllerBlock;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ReactorFrameBlock extends Block implements IWrenchable {

    public static final Property<Part> PART = EnumProperty.create("part", Part.class);


    public ReactorFrameBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(PART);
        super.createBlockStateDefinition(builder);
    }

    public enum Part implements StringRepresentable {
        START,
        MIDDLE,
        END,
        NONE;

        @Override
        public @NotNull String getSerializedName() {
            return Lang.asId(name());
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Direction.Axis axis = context.getClickedFace().getAxis();

        if (axis == Direction.Axis.X || axis == Direction.Axis.Z) axis = Direction.Axis.Y;

        return this.defaultBlockState().setValue(PART, getType(this.defaultBlockState(), getRelativeTop(level, pos, axis), getRelativeBottom(level, pos, axis)));
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        if (level.isClientSide) return;

        Direction.Axis axis = Direction.Axis.Y;
        Part part = getType(state, getRelativeTop(level, pos, axis), getRelativeBottom(level, pos, axis));

        if (state.getValue(PART) == part) return;

        state = state.setValue(PART, part);
        level.setBlock(pos, state, 3);
    }

    public BlockState getRelativeTop(Level level, BlockPos pos, Direction.Axis axis) {
        return level.getBlockState(pos.relative(Direction.fromAxisAndDirection(axis, Direction.AxisDirection.POSITIVE)));
    }

    public  BlockState getRelativeBottom(Level level, BlockPos pos, Direction.Axis axis) {
        return level.getBlockState(pos.relative(Direction.fromAxisAndDirection(axis, Direction.AxisDirection.NEGATIVE)));
    }

    public Part getType(BlockState state, BlockState above, BlockState below) {
        boolean shapeAboveSame = above.is(state.getBlock());
        boolean shapeBelowSame = below.is(state.getBlock());

        if (shapeAboveSame && ! shapeBelowSame) return  Part.END;
        else if (!shapeAboveSame && shapeBelowSame) return  Part.START;
        else if (shapeAboveSame) return Part.MIDDLE;
        return Part.NONE;
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return super.rotate(state, rotation);
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return super.mirror(state, mirror);
    }

    @Override // Called when the block is placed on the world
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        List<? extends Player> players = level.players();
        FindController(pos, level, players, true);
    }

    @Override // called when the player destroys the block, with or without a tool
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
        super.playerDestroy(level, player, pos, state, blockEntity, tool);
        List<? extends Player> players = level.players();
        FindController(pos, level, players, false);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        super.onRemove(state, level, pos, newState, movedByPiston);
        List<? extends Player> players = level.players();
        FindController(pos, level, players, false);
    }

    public ReactorControllerBlock FindController(BlockPos blockPos, Level level, List<? extends Player> players, boolean first){ // Function that checks the surrounding blocks in order
        BlockPos newBlock;                                                   // to find the controller and verify the pattern
        Vec3i pos = new Vec3i(blockPos.getX(), blockPos.getY(), blockPos.getZ());
        for (int y = pos.getY()-3; y != pos.getY()+4; y+=1) {
            for (int x = pos.getX()-5; x != pos.getX()+5; x+=1) {
                for (int z = pos.getZ()-5; z != pos.getZ()+5; z+=1) {
                    newBlock = new BlockPos(x, y, z);
                    if (level.getBlockState(newBlock).is(CNBlocks.REACTOR_CONTROLLER.get())) { // verifying the pattern
                        ReactorControllerBlock controller = (ReactorControllerBlock) level.getBlockState(newBlock).getBlock();
                        controller.Verify(level.getBlockState(newBlock), newBlock, level, players, first);
                        return controller;
                    }
                }
            }
        }
        return null;
    }
}
