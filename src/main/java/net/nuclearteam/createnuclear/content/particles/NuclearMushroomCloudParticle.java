package net.nuclearteam.createnuclear.content.particles;

import net.nuclearteam.createnuclear.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.nuclearteam.createnuclear.content.explosion.CNNuclearExplosionSound;
import net.nuclearteam.createnuclear.foundation.utility.Maths;
import net.nuclearteam.createnuclear.infrastructure.config.CNConfigs;

public class NuclearMushroomCloudParticle extends Particle {

    private static final ResourceLocation TEXTURE = CreateNuclear.asResource("textures/particle/nuclear_mushroom_cloud.png");
    private static final ResourceLocation TEXTURE_GLOW = CreateNuclear.asResource("textures/particle/nuclear_mushroom_cloud_glow.png");
    private static final ResourceLocation TEXTURE_PINK = CreateNuclear.asResource("textures/particle/nuclear_mushroom_cloud_pink.png");
    private static final ResourceLocation TEXTURE_PINK_GLOW = CreateNuclear.asResource("textures/particle/nuclear_mushroom_cloud_pink_glow.png");
    private static final NuclearMushroomCloudModel MODEL = new NuclearMushroomCloudModel();
    private static final int BALL_FOR = 10;
    private static final int GLOW_FOR = 20;
    private static final int FADE_SPEED = 10;
    private final float scale;

    private boolean playedRinging;
    private boolean playedExplosion;
    private boolean playedRumble;

    private final boolean pink;

    protected NuclearMushroomCloudParticle(ClientLevel level, double x, double y, double z, float scale, boolean pink) {
        super(level, x, y, z);
        this.gravity = 0.0F;
        this.lifetime = (int) Math.ceil(133.33F * scale);
        this.scale = scale + 0.2F;
        this.setBoundingBoxSpacing(3.0F, 3.0F);
        this.pink = pink;

    }

    public boolean shouldCull() {
        return false;
    }

    public void tick() {
        CNClientProxy.renderNukeSkyDarkFor = 70;
        CNClientProxy.muteNonNukeSoundsFor = 50;
        boolean large = this.scale > 2.0F;
        if(age > BALL_FOR / 2 + 5){
            if(!playedExplosion){
                playedExplosion = true;
                CreateNuclear.LOGGER.info("EXPLOSIOOOOOON");
                playSound(CNSoundEvents.NUCLEAR_EXPLOSION_MAIN.getMainEvent(), lifetime - 20, lifetime, 0.2F, false);
            }
        }
        if (age < BALL_FOR) {
            if (!playedRinging && CNConfigs.client().nuclearBombFlash.get()) {
                playedRinging = true;
                playSound(CNSoundEvents.NUCLEAR_EXPLOSION_RINGING.getMainEvent(), 178, 118, 0.05F, false);
            }
            CNClientProxy.renderNukeFlashFor = 16;
        } else if (age < lifetime - FADE_SPEED) {
            float life = (float) (Math.log(1 + (age - BALL_FOR) / (float) (lifetime - BALL_FOR))) * 2F;
            float explosionSpread = (12 * life + 4F) * scale;
            for (int i = 0; i < (1 + random.nextInt(2)) * scale; i++) {
                Vec3 from = new Vec3(level.random.nextFloat() - 0.5F, level.random.nextFloat() - 0.5F, level.random.nextFloat() - 0.5F).multiply(scale * 1.4F).add(this.x, this.y, this.z);
                Vec3 away = new Vec3(level.random.nextFloat() - 0.5F, level.random.nextFloat() - 0.5F, level.random.nextFloat() - 0.5F).multiply(2.34F);
                this.level.addParticle(CNParticleRegistry.NUCLEAR_MUSHROOM_CLOUD_SMOKE.get(), from.x, from.y, from.z, away.x, away.y, away.z);
            }
            for (int j = 0; j < scale * scale; j++) {
                Vec3 explosionBase = new Vec3((level.random.nextFloat() - 0.5F) * explosionSpread, (-0.6F + level.random.nextFloat() * 0.5F) * explosionSpread * 0.1F, (level.random.nextFloat() - 0.5F) * explosionSpread).add(this.x, this.y, this.z);
                this.level.addParticle(CNParticleRegistry.NUCLEAR_MUSHROOM_CLOUD_EXPLOSION.get(), explosionBase.x, explosionBase.y, explosionBase.z, 0, 0, 0);
            }
            if(age > BALL_FOR){
                if(!playedRumble){
                    playedRumble = true;

                    playSound(CNSoundEvents.NUCLEAR_EXPLOSION_RUMBLE_2.getMainEvent(), lifetime + 100, lifetime, 0.1F, true);
                }
            }
        }
        super.tick();
    }

    private void playSound(SoundEvent soundEvent, int duration, int fadesAt, float fadeInBy, boolean looping){
        Minecraft.getInstance().getSoundManager().playNextTick(new CNNuclearExplosionSound(soundEvent, this.x, this.y, this.z, duration, fadesAt, fadeInBy, looping));
    }

    public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float partialTick) {
        Vec3 vec3 = camera.getPosition();
        float f = (float) (Mth.lerp((double) partialTick, this.xo, this.x) - vec3.getX());
        float f1 = (float) (Mth.lerp((double) partialTick, this.yo, this.y) - vec3.getY());
        float f2 = (float) (Mth.lerp((double) partialTick, this.zo, this.z) - vec3.getZ());
        PoseStack posestack = new PoseStack();
        posestack.push();
        posestack.translate(f, f1 - 0.5F, f2);
        posestack.scale(-scale, -scale, scale);
        MultiBufferSource.Immediate multibuffersource$buffersource = Minecraft.getInstance().getBufferBuilders().getEntityVertexConsumers();
        MODEL.hideFireball(age >= BALL_FOR);
        float life = (float) (Math.log(1 + (age - BALL_FOR + partialTick) / (lifetime - BALL_FOR))) * 2F;
        float glowLife = life < 1F ? 1F - life : 0;
        int left = lifetime - age;
        float alpha = left <= FADE_SPEED ? left / (float) FADE_SPEED : 1.0F;
        MODEL.animateParticle(age, Maths.smin(life, 1.0F, 0.5F), partialTick);
        VertexConsumer baseConsumer = multibuffersource$buffersource.getBuffer(RenderLayer.getEntityTranslucent(pink ? TEXTURE_PINK : TEXTURE));
        MODEL.render(posestack, baseConsumer, getBrightness(partialTick), OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, alpha);
        multibuffersource$buffersource.draw();
        posestack.pop();
    }

    @Override
    public ParticleRenderType getType() {
        return ParticleRenderType.CUSTOM;
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            if (xSpeed == 0.0) {
                xSpeed = 1.0F;
            }
            return new NuclearMushroomCloudParticle(worldIn, x, y, z, (float) Math.max(0.5F, xSpeed), ySpeed >= 1.0F);
        }
    }
}
