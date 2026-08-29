package net.nuclearteam.createnuclear.content.particles;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.simibubi.create.foundation.particle.ICustomParticleDataWithSprite;
import net.minecraft.client.particle.ParticleEngine.SpriteParticleRegistration;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.nuclearteam.createnuclear.CNParticleTypes;

import java.util.Locale;

public class IrradiatedParticlesData implements ParticleOptions, ICustomParticleDataWithSprite<IrradiatedParticlesData> {

    public static final Codec<IrradiatedParticlesData> CODEC = RecordCodecBuilder.create(i ->
        i.group(
            Codec.INT.fieldOf("t").forGetter(p -> p.t)
        )
        .apply(i, IrradiatedParticlesData::new)
    );

    public static final ParticleOptions.Deserializer<IrradiatedParticlesData> DESERIALIZER =
        new ParticleOptions.Deserializer<>() {
            @Override
            public IrradiatedParticlesData fromCommand(ParticleType<IrradiatedParticlesData> particleType, StringReader reader) throws CommandSyntaxException {
                return new IrradiatedParticlesData();
            }

            @Override
            public IrradiatedParticlesData fromNetwork(ParticleType<IrradiatedParticlesData> particleType, FriendlyByteBuf buffer) {
                return new IrradiatedParticlesData(buffer.readInt());
            }
        };


    int t;

    public IrradiatedParticlesData(int _t) {
        t = _t;
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
        buffer.writeInt(t);
    }

    @Override
    public String writeToString() {
        return String.format(Locale.ROOT, "%s %d", CNParticleTypes.IRRADIATED_PARTICLES.parameter(), t);
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
    public SpriteParticleRegistration<IrradiatedParticlesData> getMetaFactory() {
        return IrradiatedParticles.Provider::new;
    }
}
