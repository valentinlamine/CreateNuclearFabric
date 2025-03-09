package net.nuclearteam.createnuclear.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;

import javax.annotation.Nullable;

public class IrradiatedParticles extends TextureSheetParticle {
    protected IrradiatedParticles(ClientLevel pLevel, double pX, double pY, double pZ,
                                  SpriteSet spriteSet, double pXSpeed, double pYSpeed, double pZSpeed) {
        super(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed);

        this.friction = 0.8f;
        this.lifetime = 40;

        this.setSpriteFromAge(spriteSet);

        this.rCol = 12f;
        this.gCol = 12f;
        this.bCol = 12f;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Provider implements ParticleProvider<IrradiatedParticlesData> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(IrradiatedParticlesData pType, ClientLevel pLevel, double pX, double pY, double pZ,
                                       double pXSpeed, double pYSpeed, double pZSpeed) {
            return new IrradiatedParticles(pLevel, pX, pY, pZ, this.spriteSet, pXSpeed, pYSpeed, pZSpeed);
        }
    }
}
