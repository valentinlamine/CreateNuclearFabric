package net.nuclearteam.createnuclear.content.multiblock.core;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.BlockPos;
import net.nuclearteam.createnuclear.content.multiblock.casing.ReactorCasingEntity;

@SuppressWarnings({"unused"})
public class ReactorCoreEntity extends ReactorCasingEntity {

    private int countdownTicks = 0;
    private boolean hasExploded = false;

    public ReactorCoreEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void tick() {
        super.tick();

        if (getLevel().isClientSide() || hasExploded) return;
    }
}