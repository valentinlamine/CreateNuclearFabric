package net.nuclearteam.createnuclear.content.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class IrradiatedParticles extends TextureSheetParticle {
    protected IrradiatedParticles(ClientLevel pLevel, double pX, double pY, double pZ,
                                  SpriteSet spriteSet, double pXSpeed, double pYSpeed, double pZSpeed) {
        super(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed);

        RandomSource randomSource = level.random;

        this.xo = randomSource.nextGaussian() * (double)1.0E-6f;
        this.yo = randomSource.nextGaussian() * (double)1.0E-4f;
        this.zo = randomSource.nextGaussian() * (double)1.0E-6f;

        this.quadSize *= this.random.nextFloat() * 0.6f + 0.6f;
        this.hasPhysics = false;
        this.gravity = 0.0f;

        this.friction = 0.8f;
        this.lifetime = 40;

        this.setSpriteFromAge(spriteSet);
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Factory implements ParticleProvider<IrradiatedParticlesData> {
        private final SpriteSet spriteSet;

        public Factory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public @Nullable Particle createParticle(IrradiatedParticlesData type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new IrradiatedParticles(level, x, y, z, this.spriteSet, xSpeed, ySpeed, zSpeed);
        }
    }
}
