package net.ynov.createnuclear.content.reactor.gauge;

import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.ynov.createnuclear.CreateNuclear;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ReactorGaugeBlock extends HorizontalDirectionalBlock {

    public static final Property<Part> PART = EnumProperty.create("part", Part.class);
    // public static final BooleanProperty

    public ReactorGaugeBlock(Properties properties) {
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
        return this.defaultBlockState().setValue(PART, Part.NONE);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        Part part = state.getValue(PART);

        if (part != Part.NONE) {
            return state;
        }

        BlockState pastBlock = level.getBlockState(currentPos.below(1));
        BlockState postBlock = level.getBlockState(currentPos.above(1));

        if (pastBlock.getBlock() instanceof ReactorGaugeBlock && !(postBlock.getBlock() instanceof ReactorGaugeBlock)) {
            return state.setValue(PART, Part.START);
        }
        else if (!(pastBlock.getBlock() instanceof ReactorGaugeBlock) && postBlock.getBlock() instanceof ReactorGaugeBlock) {
            return state.setValue(PART, Part.END);
        }
        else if (pastBlock.getBlock() instanceof ReactorGaugeBlock && postBlock.getBlock() instanceof ReactorGaugeBlock) {
            return state.setValue(PART, Part.MIDDLE);
        }
        else {
            return state.setValue(PART, Part.NONE);
        }
    }



    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return super.rotate(state, rotation);
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return super.mirror(state, mirror);
    }
}
