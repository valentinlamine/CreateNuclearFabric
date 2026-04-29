package net.nuclearteam.createnuclear.content.particles;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.simibubi.create.foundation.particle.ICustomParticleDataWithSprite;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.nuclearteam.createnuclear.CNParticleTypes;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Locale;

@SuppressWarnings("deprecation")
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class IrradiatedParticlesData implements ParticleOptions, ICustomParticleDataWithSprite<IrradiatedParticlesData> {
    public static final Codec<IrradiatedParticlesData> CODEC = RecordCodecBuilder.create(i -> i
            .group(Codec.INT.fieldOf("t")
                    .forGetter(p -> p.tick))
            .apply(i, IrradiatedParticlesData::new)
    );

    public static final ParticleOptions.Deserializer<IrradiatedParticlesData> DESERIALIZER = new ParticleOptions.Deserializer<>() {
      public IrradiatedParticlesData fromCommand(ParticleType<IrradiatedParticlesData> particleTypeIn, StringReader reader) throws CommandSyntaxException {
//          reader.expect(' ');
//          int tick = reader.readInt();
//          return new IrradiatedParticlesData(tick);
          return new IrradiatedParticlesData();
      }

      public IrradiatedParticlesData fromNetwork(ParticleType<IrradiatedParticlesData> particleTypeIn,
                                                 FriendlyByteBuf buffer) {
          return new IrradiatedParticlesData(buffer.readInt());
      }
    };

    final int tick;

    public IrradiatedParticlesData(int tick) {
        this.tick = tick;
    }

    public IrradiatedParticlesData() {
        this(0);
    }

    @Override
    public ParticleType<?> getType() {
        return CNParticleTypes.IRRADIATED_PARTICLES.get();
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buffer) {
        buffer.writeInt(tick);
    }

    @Override
    public String writeToString() {
        return String.format(Locale.ROOT, "%s", CNParticleTypes.IRRADIATED_PARTICLES.parameter(), tick);
    }

    @Override
    public Deserializer<IrradiatedParticlesData> getDeserializer() {
        return DESERIALIZER;
    }

    @Override
    public Codec<IrradiatedParticlesData> getCodec(ParticleType<IrradiatedParticlesData> type) {
        return CODEC;
    }

    @Override
    public ParticleEngine.SpriteParticleRegistration<IrradiatedParticlesData> getMetaFactory() {
        return IrradiatedParticles.Factory::new;
    }
}
