package net.nuclearteam.createnuclear.infrastructure.worldgen.biome.surfacerule;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.SurfaceRules.ConditionSource;
import net.minecraft.world.level.levelgen.SurfaceRules.RuleSource;
import net.nuclearteam.createnuclear.CNBlocks;
import net.nuclearteam.createnuclear.CreateNuclear;
import net.nuclearteam.createnuclear.infrastructure.worldgen.biome.CNBiomes;
import net.nuclearteam.createnuclear.infrastructure.worldgen.biome.CNNoiseData;
import org.jetbrains.annotations.NotNull;

public class IrradiatedSurfaceRules {
    private static final ConditionSource IS_IRRADIATED_PLAIN = biome(CNBiomes.Irradiated.PLAIN);

    private static final RuleSource BEDROCK = block(Blocks.BEDROCK);
    private static final RuleSource ENRICHED_SOUL_SOIL = block(CNBlocks.ENRICHED_SOUL_SOIL.get());
    private static final RuleSource LEAD_BLOCK = block(CNBlocks.LEAD_BLOCK.get());
    private static final RuleSource LEAD_ORE = block(CNBlocks.LEAD_ORE.get());
    private static final RuleSource RAW_LEAD_BLOCK = block(CNBlocks.RAW_LEAD_BLOCK.get());

    private static final RuleSource IRRADIATED_PLAIN_MATERIAL =
        SurfaceRules.ifTrue(IS_IRRADIATED_PLAIN, RAW_LEAD_BLOCK);
    private static final RuleSource SURFACE_GENERATION = SurfaceRules.sequence(
        SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, IRRADIATED_PLAIN_MATERIAL),
        SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, IRRADIATED_PLAIN_MATERIAL)
    );

    public static final RuleSource DEFAULT_RULE = createDefaultRule();

    public static @NotNull RuleSource createDefaultRule() {
        return SurfaceRules.sequence(
            SurfaceRules.ifTrue(
                SurfaceRules.isBiome(CNBiomes.Irradiated.PLAIN),
                SurfaceRules.sequence(
                    SurfaceRules.ifTrue(
                        SurfaceRules.noiseCondition(CNNoiseData.EROSION, 0.035, 0.0465),
                        LEAD_ORE
                    ),
                    SurfaceRules.ifTrue(
                        SurfaceRules.noiseCondition(CNNoiseData.EROSION, 0.039, 0.0545),
                        LEAD_BLOCK
                    ),
                    SurfaceRules.ifTrue(
                        SurfaceRules.noiseCondition(CNNoiseData.EROSION, 0.0545, 0.069),
                        RAW_LEAD_BLOCK
                    )
                )
            ),

            SurfaceRules.ifTrue(SurfaceRules.verticalGradient("bedrock_floor", VerticalAnchor.bottom(), VerticalAnchor.aboveBottom(5)), BEDROCK),
            SurfaceRules.ifTrue(SurfaceRules.abovePreliminarySurface(), SURFACE_GENERATION),
            SurfaceRules.ifTrue(SurfaceRules.verticalGradient("enriched", VerticalAnchor.absolute(-4), VerticalAnchor.absolute(4)), ENRICHED_SOUL_SOIL)
        );
    }

    private static @NotNull RuleSource block(@NotNull Block block) {
        return SurfaceRules.state(block.defaultBlockState());
    }

    public static SurfaceRules.@NotNull ConditionSource biome(@NotNull TagKey<Biome> biome) {
        return new BiomeTagRule(biome);
    }

    @SafeVarargs
    public static SurfaceRules.@NotNull ConditionSource biome(@NotNull ResourceKey<Biome> @NotNull ... keys) {
        return SurfaceRules.isBiome(keys);
    }

    public static void register() {
        Registry.register(BuiltInRegistries.MATERIAL_RULE, CreateNuclear.asResource("irradiated_surface"), Codec.unit(DEFAULT_RULE));
    }
}