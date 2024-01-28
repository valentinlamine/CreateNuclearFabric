package net.ynov.createnuclear.blockentity;

import com.simibubi.create.content.kinetics.base.DirectionalShaftHalvesBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

public class ReinforcedGlassEntity extends DirectionalShaftHalvesBlockEntity {
    public ReinforcedGlassEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    @Override
    protected boolean isNoisy() {
        return false;
    }
}
