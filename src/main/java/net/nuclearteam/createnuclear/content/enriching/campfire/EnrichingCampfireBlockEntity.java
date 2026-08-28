package net.nuclearteam.createnuclear.content.enriching.campfire;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import java.util.List;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;

public class EnrichingCampfireBlockEntity extends SmartBlockEntity {
    public EnrichingCampfireBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    public static void particleTick(Level level, BlockPos pos, BlockState state, EnrichingCampfireBlockEntity blockEntity) {
        int i;
        RandomSource randomSource = level.random;
        if (randomSource.nextFloat() < 0.11f) {
            for (i = 0; i < randomSource.nextInt(2) + 2; ++i) {
                EnrichingCampfireBlock.makeParticles(level, pos);
            }
        }
        i = state.getValue(EnrichingCampfireBlock.FACING).getHorizontal();
    }

    private void markUpdated() {
        this.setChanged();
        this.getLevel().updateListeners(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
    }

    public void dowse() {
        if (this.getLevel() != null) {
            this.markUpdated();
        }
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {

    }
}
