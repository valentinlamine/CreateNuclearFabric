package net.nuclearteam.createnuclear.particles;

import com.mojang.serialization.Codec;
import com.simibubi.create.content.equipment.bell.CustomRotationParticle;
import net.minecraft.network.FriendlyByteBuf;
import net.nuclearteam.createnuclear.CreateNuclear;
import net.nuclearteam.createnuclear.particles.CNParticleTypes;
import com.simibubi.create.Create;
import com.simibubi.create.content.equipment.bell.SoulBaseParticle;
import com.simibubi.create.content.equipment.bell.SoulParticle;
import com.simibubi.create.content.fluids.particle.FluidParticleData;
import com.simibubi.create.content.kinetics.base.RotationIndicatorParticleData;
import com.simibubi.create.content.kinetics.fan.AirFlowParticleData;
import com.simibubi.create.content.kinetics.steamEngine.SteamJetParticleData;
import com.simibubi.create.content.trains.CubeParticleData;
import com.simibubi.create.foundation.particle.AirParticleData;
import com.simibubi.create.foundation.particle.ICustomParticleData;
import com.simibubi.create.foundation.utility.Lang;
import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.function.Function;
import java.util.function.Supplier;

public enum CNParticleTypes implements ParticleOptions {
    RADIATION(Radiation.Data::new);


    private final CNParticleTypes.ParticleEntry<?> entry;

    <D extends ParticleOptions> CNParticleTypes(Supplier<? extends ICustomParticleData<D>> typeFactory) {
        String name = Lang.asId(name());
        entry = new ParticleEntry(name, typeFactory);
    }

    public static void register() {
        CNParticleTypes.ParticleEntry.REGISTER.register();
    }

    @Environment(EnvType.CLIENT)
    public static void registerFactories() {
        ParticleEngine particles = Minecraft.getInstance().particleEngine;
        for (CNParticleTypes particle : values())
            particle.entry.registerFactory(particles);
    }

    public ParticleType<?> get() {
        return entry.object;
    }

    public String parameter() {
        return entry.name;
    }

    @Override
    public ParticleType<?> getType() {
        return CNParticleTypes.RADIATION.get();
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buffer) {

    }

    @Override
    public String writeToString() {
        return null;
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
            typeFactory.get()
                    .register(object, particles);
        }

    }
}
