package net.nuclearteam.createnuclear.infrastructure.worldgen.biome;

import net.minecraft.core.HolderGetter;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.biome.AmbientMoodSettings;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.AmbientParticleSettings;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.nuclearteam.createnuclear.CNSoundEvents;
import net.nuclearteam.createnuclear.content.particles.IrradiatedParticlesData;

public class IrradiatedBiomes {
    public static Biome createPlain(HolderGetter<PlacedFeature> featureLookup, HolderGetter<ConfiguredWorldCarver<?>> carverLookup, HolderGetter<SoundEvent> soundLookup) {
        return IrradiatedBiomes.irradiated(featureLookup, carverLookup, soundLookup, new BiomeGenerationSettings.Builder(featureLookup, carverLookup));
    }

    public static Biome irradiated(HolderGetter<PlacedFeature> featureGetter, HolderGetter<ConfiguredWorldCarver<?>> carverGetter, HolderGetter<SoundEvent> soundLookup, BiomeGenerationSettings.Builder generation) {
        MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();
        BiomeSpecialEffects.Builder effectBuilder = new BiomeSpecialEffects.Builder();

        effectBuilder
            // Water: murky green
            .waterColor(0x3B5133)
            .waterFogColor(0x0A0E0A)

            // Fog: sickly radioactive green (kept bright so it stays visible)
            .fogColor(0x485E3E)

            // Sky: washed-out grey-green (still lets the sun/moon and day-night cycle show through)
            .skyColor(0x324132)

            // Grass: sulfur yellow / scorched
            .grassColorOverride(0x8C8F5B)

            // Foliage: withered olive
            .foliageColorOverride(0x565E3E)

            .ambientParticle(new AmbientParticleSettings(new IrradiatedParticlesData(), 0.025F))
            .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
            // Plays continuously for as long as the player stands in the biome, under the
            // Ambient/Environment volume slider. Deliberately NOT also set as .backgroundMusic():
            // that would start a second, unsynchronised copy of the same file in the MUSIC
            // category, and the two would phase against each other.
            .ambientLoopSound(soundLookup.getOrThrow(ResourceKey.create(Registries.SOUND_EVENT, CNSoundEvents.BIOME_WASTELAND.getId())))
        ;

        return new Biome.BiomeBuilder()
                .mobSpawnSettings(spawnBuilder.build())
                .hasPrecipitation(false)
                .temperature(2.0f)
                .downfall(0f)
                .specialEffects(effectBuilder.build())
                .generationSettings(generation.build())
                .build();
    }
}
