package net.ynov.createnuclear.blockentity;

import com.jozufozu.flywheel.api.MaterialManager;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
/*
public class EnrichingCampFireEntity extends SmartBlockEntity {
    private final NonNullList<ItemStack> items = NonNullList.withSize(0, ItemStack.EMPTY);



    public EnrichingCampFireEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }


    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {

    }

    public static void particleTick(Level level, BlockPos pos, BlockState state, EnrichingCampFireEntity blockEntity) {
        int i;
        RandomSource randomSource = level.random;
        if (randomSource.nextFloat() < 0.11f) {
            for (i = 0; i < randomSource.nextInt(2) + 2; ++i) {
                CampfireBlock.makeParticles(level, pos, state.getValue(CampfireBlock.SIGNAL_FIRE), false);
            }
        }
        i = state.getValue(CampfireBlock.FACING).get2DDataValue();
        for (int j = 0; j < blockEntity.items.size(); ++j) {
            if (!(randomSource.nextFloat() < 0.2f)) continue;
            Direction direction = Direction.from2DDataValue(Math.floorMod(j + i, 4));
            float f = 0.3125f;
            double d = (double)pos.getX() + 0.5 - (double)((float)direction.getStepX() * 0.3125f) + (double)((float)direction.getClockWise().getStepX() * 0.3125f);
            double e = (double)pos.getY() + 0.5;
            double g = (double)pos.getZ() + 0.5 - (double)((float)direction.getStepZ() * 0.3125f) + (double)((float)direction.getClockWise().getStepZ() * 0.3125f);
            for (int k = 0; k < 4; ++k) {
                level.addParticle(ParticleTypes.SMOKE, d, e, g, 0.0, 5.0E-4, 0.0);
            }
        }
    }

    public void dowse() {
        if (this.level != null) {
            this.markUpdated();
        }
    }
    private void markUpdated() {
        this.setChanged();
        this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
    }
}*/
