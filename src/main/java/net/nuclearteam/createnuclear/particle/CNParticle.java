package net.nuclearteam.createnuclear.particle;

import com.simibubi.create.foundation.particle.ICustomParticleData;
import com.simibubi.create.foundation.utility.Lang;
import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.nuclearteam.createnuclear.CreateNuclear;
import net.nuclearteam.createnuclear.particle.irradiated.IrradiationParticleData;

import java.util.function.Supplier;


public enum CNParticle {
    IRRADIATION_PARTICLE(IrradiationParticleData::new),
    ;

    private final ParticleEntry<?> entry;


    <D extends ParticleOptions> CNParticle(Supplier<? extends ICustomParticleData<D>> typeFactory) {
        String name = Lang.asId(name());
        entry = new ParticleEntry<>(name, typeFactory);
    }

    public static void register() {

    }

    @Environment(EnvType.CLIENT)
    public static void registerFactories() {
        ParticleEngine particles = Minecraft.getInstance().particleEngine;
        for (CNParticle particle : values()) {
            particle.entry.registerFactory(particles);
        }
    }

    public ParticleType<?> get() {
        return entry.object;
    }


    public String parameter() {
        CreateNuclear.LOGGER.warn("test: {}", entry.name);
        return entry.name;
    }

    private static class ParticleEntry<D extends ParticleOptions> {
        private static final LazyRegistrar<ParticleType<?>> REGISTER = LazyRegistrar.create(BuiltInRegistries.PARTICLE_TYPE, CreateNuclear.MOD_ID);

        private final String name;
        private final Supplier<? extends ICustomParticleData<D>> typeFactory;
        private final ParticleType<D> object;

        private ParticleEntry(String name, Supplier<? extends ICustomParticleData<D>> typeFactory) {
            this.name = name;
            this.typeFactory = typeFactory;

            object = this.typeFactory.get().createType();
            CreateNuclear.LOGGER.warn("test 21: {}", CreateNuclear.asResource(name));
            REGISTER.register(CreateNuclear.asResource("particles/" + name), () -> object);
        }

        @Environment(EnvType.CLIENT)
        public void registerFactory(ParticleEngine particle) {
            typeFactory.get()
                    .register(object, particle);
        }
    }
}
