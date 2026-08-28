package net.nuclearteam.createnuclear.infrastructure.worldgen.biome;

import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.synth.BlendedNoise;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.nuclearteam.createnuclear.CreateNuclear;

public class CNDensityFunctions {
    public static final class Irradiated {
        public static final ResourceKey<DensityFunction> EROSION = createKey("irradiated/erosion");
        public static final ResourceKey<DensityFunction> FINAL_DENSITY = createKey("irradiated/final_density");
    }

    private static ResourceKey<DensityFunction> createKey(String id) {
        return ResourceKey.create(Registries.DENSITY_FUNCTION, CreateNuclear.asResource(id));
    }

    public static void bootstrapRegistries(BootstapContext<DensityFunction> context) {
        context.register(Irradiated.EROSION, DensityFunctions.add(
                DensityFunctions.yClampedGradient(0, 90, 1, -1),
                BlendedNoise.createBase3dNoiseFunction(0.25, 0.375, 80.0, 160.0, 8.0)
        ));

        context.register(Irradiated.FINAL_DENSITY, DensityFunctions.add(
                DensityFunctions.yClampedGradient(0, 90, 1, -1),
                BlendedNoise.createBase3dNoiseFunction(0.25, 0.375, 80.0, 160.0, 8.0)
        ));
    }

    private static DensityFunction registerAndWrap(BootstapContext<DensityFunction> context, ResourceKey<DensityFunction> key, DensityFunction densityFunction) {
        return new DensityFunctions.RegistryEntryHolder(context.register(key, densityFunction));
    }

    public static DensityFunction getFunction(HolderLookup<DensityFunction> densityFunctions, ResourceKey<DensityFunction> key) {
        return new DensityFunctions.RegistryEntryHolder(densityFunctions.getOrThrow(key));
    }
}