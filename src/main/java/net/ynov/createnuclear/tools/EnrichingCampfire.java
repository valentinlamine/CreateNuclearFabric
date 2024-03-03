package net.ynov.createnuclear.tools;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Random;

public class EnrichingCampfire extends CampfireBlock {
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
}