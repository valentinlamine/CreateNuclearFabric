package net.nuclearteam.createnuclear.infrastructure.worldgen;

import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.Holder;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.nuclearteam.createnuclear.CreateNuclear;

import java.util.List;

import static net.minecraft.data.worldgen.placement.PlacementUtils.register;

public class CNPlacedFeatures {
    public static final ResourceKey<PlacedFeature>
        URANIUM_ORE = key("uranium_ore"),
        LEAD_ORE = key("lead_ore"),
        THORIUM_ORE = key("thorium_ore"),
        NITRATE_ORE = key("nitrate_ore"),
        STRIATED_ORES_OVERWORLD = key("striated_ores_overworld")
    ;

    private static ResourceKey<PlacedFeature> key(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, CreateNuclear.asResource(name));
    }

    public static void bootstrap(BootstapContext<PlacedFeature> ctx) {
        HolderLookup<ConfiguredFeature<?, ?>> featureLookup = ctx.getRegistryLookup(Registries.CONFIGURED_FEATURE);
        Holder<ConfiguredFeature<?, ?>> uraniumOre = featureLookup.getOrThrow(CNConfiguredFeatures.URANIUM_ORE);
        Holder<ConfiguredFeature<?, ?>> leadOre = featureLookup.getOrThrow(CNConfiguredFeatures.LEAD_ORE);
        Holder<ConfiguredFeature<?, ?>> thoriumOre = featureLookup.getOrThrow(CNConfiguredFeatures.THORIUM_ORE);
        Holder<ConfiguredFeature<?, ?>> nitrateOre = featureLookup.getOrThrow(CNConfiguredFeatures.NITRATE_ORE);
        Holder<ConfiguredFeature<?, ?>> striatedOresOverworld = featureLookup.getOrThrow(CNConfiguredFeatures.STRIATED_ORES_OVERWORLD);

        register(ctx, URANIUM_ORE, uraniumOre, placementOres(CountPlacement.of(4), -63, 16));
        register(ctx, LEAD_ORE, leadOre, placementOres(CountPlacement.of(10), -16, 60));
        register(ctx, THORIUM_ORE, thoriumOre, placementOres(CountPlacement.of(6), -16, 55));
        register(ctx, NITRATE_ORE, nitrateOre, placementOres(CountPlacement.of(6), -63, 64));
        register(ctx, STRIATED_ORES_OVERWORLD, striatedOresOverworld, placement(RarityFilter.of(64), -63, 16));

    }

    private static List<PlacementModifier> placementOres(PlacementModifier frequency, int minHeight, int maxHeight) {
        return List.of(
                frequency,
                InSquarePlacement.of(),
                HeightRangePlacement.trapezoid(VerticalAnchor.absolute(minHeight), VerticalAnchor.absolute(maxHeight)),
                ConfigPlacementFilter.INSTANCE
        );
    }

    private static List<PlacementModifier> placement(PlacementModifier frequency, int minHeight, int maxHeight) {
        return List.of(
                frequency,
                InSquarePlacement.of(),
                HeightRangePlacement.uniform(VerticalAnchor.absolute(minHeight), VerticalAnchor.absolute(maxHeight)),
                ConfigPlacementFilter.INSTANCE
        );
    }
}
