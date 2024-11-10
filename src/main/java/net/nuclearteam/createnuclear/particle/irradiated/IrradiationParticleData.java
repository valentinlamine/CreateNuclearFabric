package net.nuclearteam.createnuclear.particle.irradiated;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.PrimitiveCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.simibubi.create.foundation.particle.ICustomParticleDataWithSprite;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.nuclearteam.createnuclear.particle.CNParticle;

import java.util.Locale;

public class IrradiationParticleData implements ParticleOptions, ICustomParticleDataWithSprite<IrradiationParticleData> {

    public static final PrimitiveCodec<Character> CHAR = new PrimitiveCodec<Character>() {

        @Override
        public <T> DataResult<Character> read(DynamicOps<T> ops, T input) {
            return ops.getNumberValue(input)
                    .map(n -> (char) n.shortValue());
        }

        @Override
        public <T> T write(DynamicOps<T> ops, Character value) {
            return ops.createShort((short) value.charValue());
        }

        @Override
        public String toString() {
            return "Char";
        }
    };

    public static final Codec<IrradiationParticleData> CODEC = RecordCodecBuilder.create(i -> i
            .group(Codec.INT.fieldOf("color").forGetter(p -> p.color))
            .apply(i, IrradiationParticleData::new));

    public  static final ParticleOptions.Deserializer<IrradiationParticleData> DESERIALIZER =
            new ParticleOptions.Deserializer<IrradiationParticleData>() {

                @Override
                public IrradiationParticleData fromCommand(ParticleType<IrradiationParticleData> particleType, StringReader reader) throws CommandSyntaxException {
                    reader.expect(' ');
                    int color = reader.readInt();
                    return new IrradiationParticleData(color);
                }

                @Override
                public IrradiationParticleData fromNetwork(ParticleType<IrradiationParticleData> particleType, FriendlyByteBuf buffer) {
                    return new IrradiationParticleData(buffer.readInt());
                }
            };

    final int color;


    public IrradiationParticleData(int color) {
        this.color = color;
    }

    public IrradiationParticleData() {
        this(0);
    }

    @Override
    public ParticleType<?> getType() {
        return CNParticle.IRRADIATION_PARTICLE.get();
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buffer) {
        buffer.writeInt(color);
    }

    @Override
    public String writeToString() {
        return String.format(Locale.ROOT, "%s", CNParticle.IRRADIATION_PARTICLE.parameter());
    }

    @Override
    public Deserializer<IrradiationParticleData> getDeserializer() {
        return DESERIALIZER;
    }

    @Override
    public Codec<IrradiationParticleData> getCodec(ParticleType<IrradiationParticleData> type) {
        return CODEC;
    }

    @Override
    public ParticleEngine.SpriteParticleRegistration<IrradiationParticleData> getMetaFactory() {
        return IrradiationParticle.Factory::new;
    }
}
