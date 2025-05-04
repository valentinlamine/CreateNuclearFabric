package net.nuclearteam.createnuclear.multiblock;

import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.nuclearteam.createnuclear.block.CNBlocks;
import net.nuclearteam.createnuclear.multiblock.controller.ReactorControllerBlock;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

/**
 * Utility for locating the ReactorControllerBlock in the vicinity of a given origin.
 * Uses a per-chunk cache to minimize repeated scans.
 */
public final class ReactorStructureHelper {

    /** Map from chunk to last known controller position */
    private static final ConcurrentMap<ChunkPos, BlockPos> controllerCache = Maps.newConcurrentMap();

    /**
     * Finds and verifies the controller for a multiblock operation.
     *
     * @param level   the world instance
     * @param origin  the block position initiating the search
     * @param players list of players to notify of events
     * @param create  true if assembling, false if disassembling
     * @return the ReactorControllerBlock if found and active, otherwise null
     */
    public static ReactorControllerBlock findController(Level level, BlockPos origin, List<? extends Player> players, boolean create) {
        if (level.isClientSide()) return null;

        ChunkPos chunk = new ChunkPos(origin);
        BlockPos cached = controllerCache.get(chunk);

        // Attempt cached lookup first
        if (cached != null && level.isLoaded(cached)) {
            if (level.getBlockState(cached).is(CNBlocks.REACTOR_CONTROLLER.get())) {
                ReactorControllerBlock ctrl = (ReactorControllerBlock) level.getBlockState(cached).getBlock();
                ctrl.Verify(level.getBlockState(cached), cached, level, players, create);
                if (ctrl.getBlockEntity(level, cached).created) {
                    return ctrl;
                } else {
                    controllerCache.remove(chunk);
                }
            } else {
                controllerCache.remove(chunk);
            }
        }

        // Full scan in an 11×7×11 area around origin
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
        int minX = origin.getX() - 5, maxX = origin.getX() + 5;
        int minY = origin.getY() - 3, maxY = origin.getY() + 3;
        int minZ = origin.getZ() - 5, maxZ = origin.getZ() + 5;

        for (int y = minY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {
                for (int z = minZ; z <= maxZ; z++) {
                    cursor.set(x, y, z);
                    if (!level.getBlockState(cursor).is(CNBlocks.REACTOR_CONTROLLER.get())) continue;

                    ReactorControllerBlock ctrl = (ReactorControllerBlock) level.getBlockState(cursor).getBlock();
                    ctrl.Verify(level.getBlockState(cursor), cursor, level, players, create);
                    if (ctrl.getBlockEntity(level, cursor).created) {
                        controllerCache.put(chunk, cursor.immutable());
                        return ctrl;
                    }
                }
            }
        }

        return null;
    }

    /**
     * Invalidates the cached controller for the chunk containing the given position.
     *
     * @param pos any position within the chunk to invalidate
     */
    public static void invalidateCache(BlockPos pos) {
        controllerCache.remove(new ChunkPos(pos));
    }
}
