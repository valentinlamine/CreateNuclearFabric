package net.nuclearteam.createnuclear.particle.irradiated;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.content.equipment.bell.BasicParticleData;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.phys.Vec3;
import net.nuclearteam.createnuclear.particle.CNParticle;
import org.jetbrains.annotations.Nullable;


public class IrradiationParticle extends TextureSheetParticle {
    protected Vec3 origin;
    protected boolean isVisible;

    private IrradiationParticle(ClientLevel world, double x, double y, double z, SpriteSet sprite) {
        super(world, x, y - 0.125, z);
        this.setSize(0.01f, 0.01f);
        this.pickSprite(sprite);
        this.quadSize *= this.random.nextFloat() * 0.6f + 0.2f;
        this.lifetime = (int)(16.0 / (Math.random() * 0.8 + 0.2));
        this.hasPhysics = false;
        this.friction = 1.0f;
        this.gravity = 0.0f;
        this.isVisible = true;
    }

    private IrradiationParticle(ClientLevel level, SpriteSet sprite, double x, double y, double z, double speed) {
        super(level, x, y - 0.125, z, speed, speed, speed);
        this.setSize(0.01f, 0.01f);
        this.pickSprite(sprite);
        this.quadSize *= this.random.nextFloat() * 0.6f + 0.6f;
        this.lifetime = (int)(16.0 / (Math.random() * 0.8 + 0.2));
        this.hasPhysics = false;
        this.friction = 1.0f;
        this.gravity = 0.0f;
    }

    public IrradiationParticle(ClientLevel clientLevel, double v, double v1, double v2, double v3, double v4, double v5, SpriteSet spriteSet) {
        super(clientLevel, v, v1 - 0.125, v2, v3, v4, v5);
        this.setSize(0.01f, 0.01f);
        this.pickSprite(spriteSet);
        this.quadSize *= this.random.nextFloat() * 0.6f + 0.6f;
        this.lifetime = (int)(16.0 / (Math.random() * 0.8 + 0.2));
        this.hasPhysics = false;
        this.friction = 1.0f;
        this.gravity = 0.0f;

    }


    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
        if (!isVisible) return;
        super.render(buffer, renderInfo, partialTicks);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }


    public static class Factory implements ParticleProvider<IrradiationParticleData> {
        private final SpriteSet sprite;

        public Factory(SpriteSet sprite) {
            this.sprite = sprite;
        }

        @Nullable
        @Override
        public Particle createParticle(IrradiationParticleData type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            double d = (double)level.random.nextFloat() * -1.9 * (double)level.random.nextFloat() * 0.1;
            IrradiationParticle suspendedParticle = new IrradiationParticle(level, this.sprite, x, y, z, d);
            suspendedParticle.setColor(0.1f, 0.1f, 0.3f);
            suspendedParticle.setSize(0.001f, 0.001f);
            return suspendedParticle;
        }
    }
}
