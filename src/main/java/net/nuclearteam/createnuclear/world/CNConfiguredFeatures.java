package net.nuclearteam.createnuclear.world;


import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.nuclearteam.createnuclear.CreateNuclear;
import net.nuclearteam.createnuclear.block.CNBlocks;

import org.intellij.lang.annotations.Identifier;

import java.util.List;

public class CNConfiguredFeatures {

    public static final ResourceKey<ConfiguredFeature<?, ?>> URANIUM_ORE_KEY = registerKey("uranium_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> LEAD_ORE = registerKey("lead_ore");

    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, CreateNuclear.asResource(name));
    }


    public static void boostrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
        RuleTest stoneReplaceable = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepslateReplaceable = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);

        List<OreConfiguration.TargetBlockState> uraniumTargetStates = List.of(
                OreConfiguration.target(stoneReplaceable, CNBlocks.URANIUM_ORE.get().defaultBlockState()),
                OreConfiguration.target(deepslateReplaceable, CNBlocks.DEEPSLATE_URANIUM_ORE.get().defaultBlockState())
        );

        register(context, URANIUM_ORE_KEY, Feature.ORE, new OreConfiguration(uraniumTargetStates, 7));

        List<OreConfiguration.TargetBlockState> leadTargetStates = List.of(
                OreConfiguration.target(stoneReplaceable, CNBlocks.LEAD_ORE.get().defaultBlockState()),
                OreConfiguration.target(deepslateReplaceable, CNBlocks.DEEPSLATE_LEAD_ORE.get().defaultBlockState())
        );

        register(context, LEAD_ORE, Feature.ORE, new OreConfiguration(leadTargetStates, 7));


        /*List<OreConfiguration> overworldOres =
                List.of(new OreConfiguration(stoneReplaceable, CNBlocks.URANIUM_ORE.getDefaultState(), 12),
                        new OreConfiguration(templateReplaceable, CNBlocks.DEEPSLATE_URANIUM_ORE.getDefaultState(), 12));



    register(context, URANIUM_ORE_KEY, Feature.ORE, new OreConfiguration(stoneReplaceable, CNBlocks.URANIUM_ORE.getDefaultState(), 7));*/


    }



    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstapContext<ConfiguredFeature<?, ?>> context,
                                                                                          ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC config) {
        context.register(key, new ConfiguredFeature<>(feature, config));
    }
}
