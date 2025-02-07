package net.nuclearteam.createnuclear.world.biome;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.biome.OverworldBiomes;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public abstract class CNBiomeData {
    public static void bootstrap(BootstapContext<Biome> context) {
        HolderGetter<PlacedFeature> holderGetter = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<ConfiguredWorldCarver<?>> holderGetter2 = context.lookup(Registries.CONFIGURED_CARVER);
        context.register(CNBiome.IRRADIATED_BIOME, CNBiome.irradiatedBiome(holderGetter, holderGetter2));

    }
}
