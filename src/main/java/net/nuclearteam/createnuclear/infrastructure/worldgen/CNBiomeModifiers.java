package net.nuclearteam.createnuclear.infrastructure.worldgen;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.nuclearteam.createnuclear.CreateNuclear;

import java.util.function.Predicate;

public final class CNBiomeModifiers {
    public static final ResourceLocation URANIUM_ORE = id("uranium_ore");
    public static final ResourceLocation LEAD_ORE = id("lead_ore");
    public static final ResourceLocation THORIUM_ORE = id("thorium_ore");
    public static final ResourceLocation NITRATE_ORE = id("nitrate_ore");
    public static final ResourceLocation STRIATED_ORES_OVERWORLD = id("striated_ores_overworld");

    private CNBiomeModifiers() {
    }

    private static ResourceLocation id(String name) {
        return CreateNuclear.asResource(name);
    }

    public static void bootstrap() {
        Predicate<BiomeSelectionContext> isOverworld = BiomeSelectors.tag(BiomeTags.IS_OVERWORLD);
        addOre(isOverworld, CNPlacedFeatures.URANIUM_ORE);
        addOre(isOverworld, CNPlacedFeatures.LEAD_ORE);
        addOre(isOverworld, CNPlacedFeatures.THORIUM_ORE);
        addOre(isOverworld, CNPlacedFeatures.NITRATE_ORE);
        addOre(isOverworld, CNPlacedFeatures.STRIATED_ORES_OVERWORLD);
    }

    private static void addOre(Predicate<BiomeSelectionContext> selector, ResourceKey<PlacedFeature> feature) {
        BiomeModifications.addFeature(selector, GenerationStep.Feature.UNDERGROUND_ORES, feature);
    }
}
