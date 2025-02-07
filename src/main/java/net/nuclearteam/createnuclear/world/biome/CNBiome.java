package net.nuclearteam.createnuclear.world.biome;


import com.mojang.blaze3d.shaders.Effect;
import com.simibubi.create.AllParticleTypes;
import com.simibubi.create.content.kinetics.base.RotationIndicatorParticleData;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.biome.OverworldBiomes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.Musics;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.nuclearteam.createnuclear.CreateNuclear;
import net.nuclearteam.createnuclear.particles.CNParticleTypes;
import org.jetbrains.annotations.Nullable;


public class CNBiome {
    public static final ResourceKey<Biome> IRRADIATED_BIOME = ResourceKey.create(Registries.BIOME,
            CreateNuclear.asResource("irradiated_biome"));

    public static Biome biome(boolean hasPercipitation, float temperature, float downfall, MobSpawnSettings.Builder mobSpawnSettings, BiomeGenerationSettings.Builder generationSettings, @Nullable Music backgroundMusic) {
        return CNBiome.biome(hasPercipitation, temperature, downfall, 4159204, 329011, null, null, mobSpawnSettings, generationSettings, backgroundMusic);
    }

    public static Biome biome(boolean hasPrecipitation, float temperature, float downfall, int waterColor, int waterFogColor, @Nullable Integer grassColorOverride, @Nullable Integer foliageColorOverride, MobSpawnSettings.Builder mobSpawnSettings, BiomeGenerationSettings.Builder generationSettings, @Nullable Music backgroundMusic) {
        BiomeSpecialEffects.Builder builder = new BiomeSpecialEffects.Builder()
                .waterColor(waterColor)
                .waterFogColor(waterFogColor)
                .fogColor(12638463)
                .skyColor(OverworldBiomes.calculateSkyColor(temperature))
                .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                .ambientParticle(new AmbientParticleSettings(CNParticleTypes.RADIATION, 0.025F))
                .backgroundMusic(backgroundMusic);
        if (grassColorOverride != null) {
            builder.grassColorOverride(grassColorOverride);
        }
        if (foliageColorOverride != null) {
            builder.foliageColorOverride(foliageColorOverride);
        }
        return new Biome.BiomeBuilder().hasPrecipitation(hasPrecipitation).temperature(temperature).downfall(downfall).specialEffects(builder.build()).mobSpawnSettings(mobSpawnSettings.build()).generationSettings(generationSettings.build()).build();
    }

    public static void globalOverworldGeneration(BiomeGenerationSettings.Builder generationSettings) {
        BiomeDefaultFeatures.addDefaultCarversAndLakes(generationSettings);
        BiomeDefaultFeatures.addDefaultCrystalFormations(generationSettings);
        BiomeDefaultFeatures.addDefaultMonsterRoom(generationSettings);
        BiomeDefaultFeatures.addDefaultUndergroundVariety(generationSettings);
        BiomeDefaultFeatures.addDefaultSprings(generationSettings);
        BiomeDefaultFeatures.addSurfaceFreezing(generationSettings);
    }

    public static Biome irradiatedBiome(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> worldCarvers) {
        MobSpawnSettings.Builder builder = new MobSpawnSettings.Builder();
        BiomeDefaultFeatures.desertSpawns(builder);
        BiomeGenerationSettings.Builder builder2 = new BiomeGenerationSettings.Builder(placedFeatures, worldCarvers);
        BiomeDefaultFeatures.addFossilDecoration(builder2);
        CNBiome.globalOverworldGeneration(builder2);
        BiomeDefaultFeatures.addDefaultOres(builder2);
        BiomeDefaultFeatures.addDefaultSoftDisks(builder2);
        BiomeDefaultFeatures.addDefaultFlowers(builder2);
        BiomeDefaultFeatures.addDefaultGrass(builder2);
        BiomeDefaultFeatures.addDesertVegetation(builder2);
        BiomeDefaultFeatures.addDefaultMushrooms(builder2);
        BiomeDefaultFeatures.addDesertExtraVegetation(builder2);
        BiomeDefaultFeatures.addDesertExtraDecoration(builder2);


        return CNBiome.biome(false, 2.0f, 0.0f, builder, builder2, Musics.createGameMusic(SoundEvents.MUSIC_BIOME_DESERT));

    }
}
