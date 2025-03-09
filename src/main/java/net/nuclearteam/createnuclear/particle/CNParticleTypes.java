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

import java.util.function.Supplier;

public enum CNParticleTypes {
    IRRADIATED_PARTICLES(IrradiatedParticlesData::new),
    ;

    private final ParticleEntry<?> entry;

    <D extends ParticleOptions> CNParticleTypes(Supplier<? extends ICustomParticleData<D>> typeFactory) {
        String name = Lang.asId(name());
        entry = new ParticleEntry<>(name, typeFactory);
    }

    public static void register() {
        ParticleEntry.REGISTER.register();
    }

    @Environment(EnvType.CLIENT)
    public static void registerFactories() {
        ParticleEngine particles = Minecraft.getInstance().particleEngine;
        for (CNParticleTypes particle : values()) {
            particle.entry.registerFactory(particles);
        }
    }

    public ParticleType<?> get() {
        return entry.object;
    }

    public String parameter() {
        return entry.name;
    }

    private static class ParticleEntry<D extends ParticleOptions> {
        private static final LazyRegistrar<ParticleType<?>> REGISTER = LazyRegistrar.create(BuiltInRegistries.PARTICLE_TYPE, CreateNuclear.MOD_ID);

        private final String name;
        private final Supplier<? extends ICustomParticleData<D>> typeFactory;
        private final ParticleType<D> object;

        public ParticleEntry(String name, Supplier<? extends ICustomParticleData<D>> typeFactory) {
            this.name = name;
            this.typeFactory = typeFactory;

            object = this.typeFactory.get().createType();
            REGISTER.register(name, () -> object);
        }

        @Environment(EnvType.CLIENT)
        public void registerFactory(ParticleEngine particles) {
            typeFactory.get().register(object, particles);
        }
    }
}
