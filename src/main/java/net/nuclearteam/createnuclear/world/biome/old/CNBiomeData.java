package net.nuclearteam.createnuclear.world.biome.old;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.nuclearteam.createnuclear.world.biome.IrradiatedBiomesRegion;
import net.nuclearteam.createnuclear.world.biome.d;
import net.nuclearteam.createnuclear.world.biome.surface.IrradiatedSurfaceRuleData;

public abstract class CNBiomeData {
    public static void bootstrap(BootstapContext<Biome> context) {
        HolderGetter<PlacedFeature> holderGetter = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<ConfiguredWorldCarver<?>> holderGetter2 = context.lookup(Registries.CONFIGURED_CARVER);
        HolderGetter<Biome> holderGetter3 = context.lookup(Registries.BIOME);

    }
}
