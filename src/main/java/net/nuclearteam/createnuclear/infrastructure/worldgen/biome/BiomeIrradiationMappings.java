package net.nuclearteam.createnuclear.infrastructure.worldgen.biome;

import java.util.List;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.Holder;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;

public class BiomeIrradiationMappings {
    private static final List<BiomeIrradiationMapping> MAPPINGS = List.of(
            new BiomeIrradiationMapping(BiomeTags.IS_OVERWORLD, CNBiomes.Irradiated.PLAIN),
            new BiomeIrradiationMapping(BiomeTags.IS_NETHER, CNBiomes.Irradiated.PLAIN),
            new BiomeIrradiationMapping(BiomeTags.IS_END, CNBiomes.Irradiated.PLAIN)
    );

    private BiomeIrradiationMappings() {}

    public static ResourceKey<Biome> resolveTarget(Holder<Biome> currentBiome, ResourceKey<Biome> fallback) {
        for (BiomeIrradiationMapping mapping : MAPPINGS) {
            if (currentBiome.is(mapping.sourceTag())) {
                return mapping.target();
            }
        }

        return fallback;
    }
}
