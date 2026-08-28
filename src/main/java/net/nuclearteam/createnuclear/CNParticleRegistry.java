package net.nuclearteam.createnuclear;

import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;

public class CNParticleRegistry {
    public static final LazyRegistrar<ParticleType<?>> DEF_REG;

    public static final RegistryObject<SimpleParticleType> NUCLEAR_MUSHROOM_CLOUD;
    public static final RegistryObject<SimpleParticleType> NUCLEAR_MUSHROOM_CLOUD_SMOKE;
    public static final RegistryObject<SimpleParticleType> NUCLEAR_MUSHROOM_CLOUD_EXPLOSION;

    static {
        DEF_REG = LazyRegistrar.create(Registries.PARTICLE_TYPE, CreateNuclear.MOD_ID);
        NUCLEAR_MUSHROOM_CLOUD = DEF_REG.register("nuclear_mushroom_cloud", () -> FabricParticleTypes.simple(false));
        NUCLEAR_MUSHROOM_CLOUD_SMOKE = DEF_REG.register("nuclear_mushroom_cloud_smoke", () -> FabricParticleTypes.simple(false));
        NUCLEAR_MUSHROOM_CLOUD_EXPLOSION = DEF_REG.register("nuclear_mushroom_cloud_explosion", () -> FabricParticleTypes.simple(false));
    }

    public static void register() {
        DEF_REG.register();
    }

}
