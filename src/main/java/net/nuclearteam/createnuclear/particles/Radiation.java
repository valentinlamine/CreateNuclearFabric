package net.nuclearteam.createnuclear.particles;

import com.mojang.math.Axis;
import com.simibubi.create.AllParticleTypes;
import com.simibubi.create.content.equipment.bell.BasicParticleData;
import com.simibubi.create.content.equipment.bell.CustomRotationParticle;
import com.simibubi.create.content.equipment.bell.SoulPulseEffect;
import io.github.fabricators_of_create.porting_lib.util.ParticleHelper;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleType;
import org.joml.Quaternionf;

public class Radiation extends CustomRotationParticle {

    private final SpriteSet animatedSprite;

    public Radiation(ClientLevel worldIn, double x, double y, double z, double vx, double vy, double vz,
                            SpriteSet spriteSet) {
        super(worldIn, x, y, z, spriteSet, 0);
        this.animatedSprite = spriteSet;
        this.quadSize = 0.5f;
        this.setSize(this.quadSize, this.quadSize);
        this.loopLength = 16 + (int) (this.random.nextFloat() * 2f - 1f);
        this.lifetime = (int) (90.0F / (this.random.nextFloat() * 0.36F + 0.64F));
        this.selectSpriteLoopingWithAge(animatedSprite);
        ParticleHelper.setStoppedByCollision(this, true); // disable movement
    }

    @Override
    public void tick() {
        selectSpriteLoopingWithAge(animatedSprite);

        BlockPos pos = BlockPos.containing(x, y, z);
        if (age++ >= lifetime || !SoulPulseEffect.isDark(level, pos))
            remove();
    }

    @Override
    public Quaternionf getCustomRotation(Camera camera, float partialTicks) {
        return Axis.XP.rotationDegrees(90);
    }

    public static class Data extends BasicParticleData<Radiation> {
        @Override
        public IBasicParticleFactory<Radiation> getBasicFactory() {
            return Radiation::new;
        }

        @Override
        public ParticleType<?> getType() {
            return CNParticleTypes.RADIATION.get();
        }
    }
}

