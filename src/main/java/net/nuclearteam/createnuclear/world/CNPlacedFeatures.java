package net.nuclearteam.createnuclear.world;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;
import net.nuclearteam.createnuclear.CreateNuclear;

import java.util.List;

public class CNPlacedFeatures {

    public static final ResourceKey<PlacedFeature> URANIUM_ORE = registerKey("uranium_ore");
    public static final ResourceKey<PlacedFeature> LEAD_ORE = registerKey("lead_ore");
    public static final ResourceKey<PlacedFeature> STRIATED_ORES_OVERWORLD = registerKey("striated_ores_overworld");

    public static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, CreateNuclear.asResource(name));
    }

    public static void boostrap(BootstapContext<PlacedFeature> context) {
        //var configuredFeatureRegistryEntryLookup = context.lookup(Registries.CONFIGURED_FEATURE);
        HolderGetter<ConfiguredFeature<?, ?>> featureLookup = context.lookup(Registries.CONFIGURED_FEATURE);
        Holder<ConfiguredFeature<?,?>> uraniumOre = featureLookup.getOrThrow(CNConfiguredFeatures.URANIUM_ORE_KEY);
        Holder<ConfiguredFeature<?,?>> leadOre = featureLookup.getOrThrow(CNConfiguredFeatures.LEAD_ORE);
        Holder<ConfiguredFeature<?,?>> striatedOresOverworld = featureLookup.getOrThrow(CNConfiguredFeatures.STRIATED_ORES_OVERWORLD);

        register(context, URANIUM_ORE, uraniumOre, placement(CountPlacement.of(6), -64,64));
        register(context, LEAD_ORE, leadOre, placement(CountPlacement.of(10), -64,64));
        register(context, STRIATED_ORES_OVERWORLD, striatedOresOverworld, placementUniform(RarityFilter.onAverageOnceEvery(18), -30, 70));


    }

    private static List<PlacementModifier> placement(PlacementModifier frequency, int minHeight, int maxHeight) {
        return List.of(
            frequency,
            InSquarePlacement.spread(),
            HeightRangePlacement.triangle(VerticalAnchor.absolute(minHeight), VerticalAnchor.absolute(maxHeight)),
            CNConfigPlacementFilter.INSTANCE
        );
    }

    private static List<PlacementModifier> placementUniform(PlacementModifier frequency, int minHeight, int maxHeight) {
        return List.of(
            frequency,
            InSquarePlacement.spread(),
            HeightRangePlacement.uniform(VerticalAnchor.absolute(minHeight), VerticalAnchor.absolute(maxHeight)),
            CNConfigPlacementFilter.INSTANCE
        );
    }

    public static void register(BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> configuredFeature, List<PlacementModifier> placements) {
        context.register(key, new PlacedFeature(configuredFeature, List.copyOf(placements)));
    }

}
