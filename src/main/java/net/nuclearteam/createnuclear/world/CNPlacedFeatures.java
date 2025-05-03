package net.nuclearteam.createnuclear.world;

import com.simibubi.create.infrastructure.worldgen.ConfigPlacementFilter;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;
import net.nuclearteam.createnuclear.CreateNuclear;

import java.util.List;

public class CNPlacedFeatures {

    public static final ResourceKey<PlacedFeature> URANIUM_ORE = registerKey("uranium_ore");
    public static final ResourceKey<PlacedFeature> LEAD_ORE = registerKey("lead_ore");

    public static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, CreateNuclear.asResource(name));
    }

    public static void boostrap(BootstapContext<PlacedFeature> context) {
        //var configuredFeatureRegistryEntryLookup = context.lookup(Registries.CONFIGURED_FEATURE);
        HolderGetter<ConfiguredFeature<?, ?>> featureLookup = context.lookup(Registries.CONFIGURED_FEATURE);
        Holder<ConfiguredFeature<?,?>> uraniumOre = featureLookup.getOrThrow(CNConfiguredFeatures.URANIUM_ORE_KEY);
        Holder<ConfiguredFeature<?,?>> leadOre = featureLookup.getOrThrow(CNConfiguredFeatures.LEAD_ORE);

        register(context, URANIUM_ORE, uraniumOre, placement(CountPlacement.of(6), -64,20));
        register(context, LEAD_ORE, leadOre, placement(CountPlacement.of(10), -64,320));



        /*register(context, URANIUM_ORE_PLACED_KEY, configuredFeatureRegistryEntryLookup.getOrThrow(CNConfiguredFeatures.URANIUM_ORE_KEY),
                CNOrePlacement.modifiersWithCount(6, // Veins per Chunk
                        HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(-64), VerticalAnchor.belowTop(64))));*/ // Height Range


    }

    private static List<PlacementModifier> placement(PlacementModifier frequency, int minHeight, int maxHeight) {
        return List.of(
            frequency,
            InSquarePlacement.spread(),
            HeightRangePlacement.triangle(VerticalAnchor.absolute(minHeight), VerticalAnchor.absolute(maxHeight)),
            ConfigPlacementFilter.INSTANCE
        );
    }

    public static void register(BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> configuredFeature, List<PlacementModifier> placements) {
        context.register(key, new PlacedFeature(configuredFeature, List.copyOf(placements)));
    }

}
