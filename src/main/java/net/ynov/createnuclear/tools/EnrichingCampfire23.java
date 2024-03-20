package net.ynov.createnuclear.tools;
/*
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
//import net.ynov.createnuclear.block.CNBlockEntityType;
import net.ynov.createnuclear.blockentity.CNBlockEntity;
import net.ynov.createnuclear.blockentity.EnrichingCampFireEntity;
import org.jetbrains.annotations.Nullable;*/

/*
public class EnrichingCampfire extends CampfireBlock implements IBE<EnrichingCampFireEntity> {
    public EnrichingCampfire(boolean lit, int fireDamage, BlockBehaviour.Properties properties) {
        super(lit, fireDamage, properties);
    }

    public void randomTick(BlockState state, Level world, BlockPos pos, Random random) {
        if (world.isRainingAt(pos)) {
            return;
        }

        if (world.dimensionType().ultraWarm() && random.nextInt(10) == 0) {
            world.setBlock(pos, Blocks.COBBLESTONE.defaultBlockState(), 3);
            return;
        }

        super.randomTick(state, (ServerLevel) world, pos, (RandomSource) random);

        // Spawn smoke signal particles when the campfire is lit
        if (state.getValue(LIT) && world.getBlockState(pos.above()).isAir()) {
            spawnSmokeSignalParticles(world, pos);
        }
    }

    // Custom method to spawn smoke signal particles
    private static void spawnSmokeSignalParticles(Level world, BlockPos pos) {
        double posX = pos.getX() + 0.5;
        double posY = pos.getY() + 1.1;
        double posZ = pos.getZ() + 0.5;

        world.addParticle(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, posX, posY, posZ, 0.0, 1.0, 0.0);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (!state.getValue(LIT).booleanValue()) {
            return;
        }
        if (random.nextInt(10) == 0) {
            level.playLocalSound((double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, SoundEvents.ALLAY_ITEM_GIVEN, SoundSource.BLOCKS, 0.5f + random.nextFloat(), random.nextFloat() * 0.7f + 0.6f, false);
        }
        if (random.nextInt(5) == 0) {
            for (int i = 0; i < random.nextInt(1) + 1; ++i) {
                level.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, (double)pos.getX(), (double)pos.getY() + 1.5, (double)pos.getZ(), random.nextFloat() / 2.0f, 5.0E-5, random.nextFloat() / 2.0f);
            }
        }
    }

    public static void dowse(@Nullable Entity entity, LevelAccessor level, BlockPos pos, BlockState state) {
        BlockEntity blockEntity;
        if (level.isClientSide()) {
            for (int i = 0; i < 20; ++i) {
                EnrichingCampfire.makeParticles((Level)level, pos, state.getValue(SIGNAL_FIRE), true);
            }
        }
        if ((blockEntity = level.getBlockEntity(pos)) instanceof CampfireBlockEntity) {
            ((CampfireBlockEntity)blockEntity).dowse();
        }
        level.gameEvent(entity, GameEvent.BLOCK_CHANGE, pos);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new EnrichingCampFireEntity(null, pos, state);
    }

    @Override
    public boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluidState) {
        if (!state.getValue(BlockStateProperties.WATERLOGGED).booleanValue() && fluidState.getType() == Fluids.WATER) {
            boolean bl = state.getValue(LIT);
            if (bl) {
                if (!level.isClientSide()) {
                    level.playSound(null, pos, SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.BLOCKS, 1.0f, 1.0f);
                }
                EnrichingCampfire.dowse(null, level, pos, state);
            }
            level.setBlock(pos, (BlockState)((BlockState)state.setValue(WATERLOGGED, true)).setValue(LIT, false), 3);
            level.scheduleTick(pos, fluidState.getType(), fluidState.getType().getTickDelay(level));
            return true;
        }
        return false;
    }

    @Override
    public Class<EnrichingCampFireEntity> getBlockEntityClass() {
        return EnrichingCampFireEntity.class;
    }

    @Override
    public BlockEntityType<? extends EnrichingCampFireEntity> getBlockEntityType() {
        return CNBlockEntity.ENRICHED_CAMPFIRE_BLOCK.get();
    }
}*/
