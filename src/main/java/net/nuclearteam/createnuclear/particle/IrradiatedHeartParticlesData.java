package net.nuclearteam.createnuclear.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.simibubi.create.foundation.particle.ICustomParticleDataWithSprite;
import net.minecraft.client.particle.ParticleEngine.SpriteParticleRegistration;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;

import java.util.Locale;

public class IrradiatedHeartParticlesData implements ParticleOptions, ICustomParticleDataWithSprite<IrradiatedHeartParticlesData> {

    public static final Codec<IrradiatedHeartParticlesData> CODEC = RecordCodecBuilder.create(i ->
        i.group(
            Codec.INT.fieldOf("t").forGetter(p -> p.t)
        )
        .apply(i, IrradiatedHeartParticlesData::new)
    );

    public static final Deserializer<IrradiatedHeartParticlesData> DESERIALIZER =
        new Deserializer<>() {
            @Override
            public IrradiatedHeartParticlesData fromCommand(ParticleType<IrradiatedHeartParticlesData> particleType, StringReader reader) throws CommandSyntaxException {
                return new IrradiatedHeartParticlesData();
            }

            @Override
            public IrradiatedHeartParticlesData fromNetwork(ParticleType<IrradiatedHeartParticlesData> particleType, FriendlyByteBuf buffer) {
                return new IrradiatedHeartParticlesData();
            }
        };


    int t;

    public IrradiatedHeartParticlesData(int _t) {
        t = _t;
    }

    public IrradiatedHeartParticlesData() {
        this(0);
    }

    @Override
    public ParticleType<?> getType() {
        return CNParticleTypes.IRRADIATED_HEART.get();
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buffer) {
        buffer.writeInt(t);
    }

    @Override
    public String writeToString() {
        return String.format(Locale.ROOT, "%s %d", CNParticleTypes.IRRADIATED_HEART.parameter(), t);
    }

    @Override
    public Deserializer<IrradiatedHeartParticlesData> getDeserializer() {
        return DESERIALIZER;
    }

    @Override
    public Codec<IrradiatedHeartParticlesData> getCodec(ParticleType<IrradiatedHeartParticlesData> type) {
        return CODEC;
    }

    @Override
    public SpriteParticleRegistration<IrradiatedHeartParticlesData> getMetaFactory() {
        return IrradiatedHeartParticles.Provider::new;
    }






}
