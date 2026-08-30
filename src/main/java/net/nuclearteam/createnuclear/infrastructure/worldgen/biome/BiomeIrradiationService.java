package net.nuclearteam.createnuclear.infrastructure.worldgen.biome;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeResolver;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.nuclearteam.createnuclear.CreateNuclear;
import net.nuclearteam.createnuclear.infrastructure.config.CNConfigs;

import java.util.ArrayList;
import java.util.List;

public final class BiomeIrradiationService {
    public static void circularArea(ServerLevel serverLevel, BlockPos center, ResourceKey<Biome> defaultTarget, int radius) {
        Registry<Biome> biomeRegistry = serverLevel.registryAccess().registryOrThrow(Registries.BIOME);

        Holder<Biome> currentAtCenter = serverLevel.getBiome(center);
        ResourceKey<Biome> targetAtCenter = BiomeIrradiationMappings.resolveTarget(currentAtCenter, defaultTarget);
        if (currentAtCenter.is(targetAtCenter)) {
            return;
        }

        int minX = center.getX() - radius;
        int maxX = center.getX() + radius;
        int minZ = center.getZ() - radius;
        int maxZ = center.getZ() + radius;

        double radiusSq = radius * radius;
        BiomeResolver resolver = createCircularResolver(biomeRegistry, center, radiusSq, defaultTarget, serverLevel);

        List<ChunkAccess> chunks = new ArrayList<>();
        for (int cz = SectionPos.blockToSectionCoord(minZ); cz < SectionPos.blockToSectionCoord(maxZ); ++cz) {
            for (int cx = SectionPos.blockToSectionCoord(minX); cx < SectionPos.blockToSectionCoord(maxX); ++cx) {
                ChunkAccess chunkAccess = serverLevel.getChunk(cx, cz, ChunkStatus.FULL, false);
                if (chunkAccess != null) {
                    chunkAccess.fillBiomesFromNoise(resolver, serverLevel.getChunkSource().randomState().sampler());
                    chunkAccess.setUnsaved(true);
                    chunks.add(chunkAccess);
                }
            }
        }

        PersistentIrradiatedZones.get(serverLevel).addChunks(chunks.stream().map(ChunkAccess::getPos).toList());
        serverLevel.getChunkSource().chunkMap.resendBiomesForChunks(chunks);
    }

    public static boolean restoreArea(ServerLevel serverLevel, BlockPos pos) {
        ChunkPos centerChunk = new ChunkPos(pos);
        PersistentIrradiatedZones zones = PersistentIrradiatedZones.get(serverLevel);

        List<ChunkAccess> restored = new ArrayList<>();
        for (ChunkPos target : resolveRestoreTargets(centerChunk)) {
            if (!zones.containsChunk(target)) continue;

            ChunkAccess chunkAccess = serverLevel.getChunk(target.x, target.z, ChunkStatus.FULL, false);
            if (chunkAccess == null) continue;

            BiomeResolver resolver = (x, y, z, sample) -> serverLevel.getChunkSource().getGenerator().getBiomeSource().getNoiseBiome(x, y, z, sample);

            chunkAccess.fillBiomesFromNoise(resolver, serverLevel.getChunkSource().randomState().sampler());
            chunkAccess.setUnsaved(true);
            restored.add(chunkAccess);

            zones.removeChunk(target);
        }

        if (restored.isEmpty()) return false;

        serverLevel.getChunkSource().chunkMap.resendBiomesForChunks(restored);

        return true;
    }

    private static List<ChunkPos> resolveRestoreTargets(ChunkPos center) {
        if (!CNConfigs.server().biomeRestore.restoreInCircle.get()) {
            return List.of(center);
        }

        int radius = CNConfigs.server().biomeRestore.restoreRadiusChunks.get();
        List<ChunkPos> targets = new ArrayList<>();
        for (int dz = -radius; dz <= radius; dz++) {
            for (int dx = -radius; dx <= radius; dx++) {
                if (dx * dx + dz * dz <= radius * radius) {
                    targets.add(new ChunkPos(center.x + dx, center.z + dz));
                }
            }
        }

        return targets;
    }

    private static BiomeResolver createCircularResolver(Registry<Biome> biomeRegistry, BlockPos center, double radiusSq, ResourceKey<Biome> defaultTarget, ServerLevel level) {
        return (x, y, z, noise) -> {
            int blockX = x << 2;
            int blockZ = z << 2;

            double distX = blockX - center.getX();
            double distZ = blockZ - center.getZ();

            Holder<Biome> current = level.getBiome(new BlockPos(blockX, y << 2, blockZ));

            if ((distX * distX) + (distZ * distZ) <= radiusSq) {
                ResourceKey<Biome> target = BiomeIrradiationMappings.resolveTarget(current, defaultTarget);

                return biomeRegistry.getHolderOrThrow(target);
            }

            return current;
        };
    }
}
