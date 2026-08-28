package net.nuclearteam.createnuclear.infrastructure.worldgen.biome;

import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public record BiomeIrradiationMapping(TagKey<Biome> sourceTag, ResourceKey<Biome> target) {
}
