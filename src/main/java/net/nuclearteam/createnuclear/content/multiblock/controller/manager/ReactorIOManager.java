package net.nuclearteam.createnuclear.content.multiblock.controller.manager;

import java.util.List;
import java.util.function.Function;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

/**
 * Generic manager interface for reactor IO positions (inputs or outputs).
 * Defines expected operations to manage a collection of `BlockPos` instances:
 * serialization, validation, and resolution into concrete objects via a `resolver`.
 */
public interface ReactorIOManager {
    /** Returns true when the position is known. */
    boolean contains(BlockPos pos);

    /** Current number of tracked positions. */
    int size();

    /** Reads state from an NBT tag (deserialization). */
    void read(CompoundTag compound);

    /** Writes state to an NBT tag (serialization). */
    void write(CompoundTag compound);

    /**
     * Adds a position; returns true if added.
     */
    void addBlock(BlockPos pos);

    /**
     * Removes a position; returns true if it was present and removed.
     */
    void removeBlock(BlockPos pos);

    /** Returns an immutable copy of tracked positions. */
    List<BlockPos> getBlocksPosition();

    /** Removes invalid positions for the provided `level` (e.g. missing block entity). */
    void clearInvalid(Level level);

    /**
     * Resolves each `BlockPos` into an instance using `resolver` and returns
     * the list of non-null results.
     */
    <T> List<T> resolveBlock(Level level, Function<BlockPos, T> resolver);
}
