package net.nuclearteam.createnuclear.tools;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.nuclearteam.createnuclear.particle.irradiated.IrradiationParticleData;

import java.util.List;

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
                EnrichingCampfireBlockEntity.makeParticles2(level, pos);
            }
        }
        i = state.getValue(EnrichingCampfireBlock.FACING).get2DDataValue();
    }

    public static void makeParticles2(Level level, BlockPos pos) {
        RandomSource randomSource = level.getRandom();
        level.addAlwaysVisibleParticle(new IrradiationParticleData(0x123123), true, (double)pos.getX() + 0.5 + randomSource.nextDouble() / 3.0 * (double)(randomSource.nextBoolean() ? 1 : -1), (double)pos.getY() + randomSource.nextDouble() + randomSource.nextDouble(), (double)pos.getZ() + 0.5 + randomSource.nextDouble() / 3.0 * (double)(randomSource.nextBoolean() ? 1 : -1), 0.0, 0.07, 0.0);
    }

    private void markUpdated() {
        this.setChanged();
        this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
    }

    public void dowse() {
        if (this.level != null) {
            this.markUpdated();
        }
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {

    }
}
