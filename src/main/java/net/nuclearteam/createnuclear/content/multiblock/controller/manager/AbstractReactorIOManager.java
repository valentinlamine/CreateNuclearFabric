package net.nuclearteam.createnuclear.content.multiblock.controller.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

/**
 * Abstract base providing common implementation for managing
 * positions used by IO managers (inputs / outputs).
 *
 * This class holds a collection of `BlockPos` instances and exposes
 * basic operations (add, remove, contains, iteration via `resolveBlock`).
 * Serialization (`read`/`write`) and validation (`clearInvalid`) are
 * left abstract for concrete implementations to provide.
 */
public abstract class AbstractReactorIOManager implements ReactorIOManager {
    /** List of `BlockPos` instances tracked by this manager. */
    protected final List<BlockPos> positions = new ArrayList<>();

    /**
     * Adds `pos` if non-null and not already present; returns true when added.
     */
    @Override
    public void addBlock(BlockPos pos) {
        if (pos == null) return;
        if (!contains(pos)) {
            positions.add(pos);
        }
    }

    /**
     * Removes `pos` if present.
     */
    @Override
    public void removeBlock(BlockPos pos) {
        positions.remove(pos);
    }

    /** Tests whether `pos` is present. */
    @Override
    public boolean contains(BlockPos pos) {
        return positions.contains(pos);
    }

    /** Returns the current number of tracked positions. */
    @Override
    public int size() {
        return positions.size();
    }

    /** Returns an immutable copy of the tracked positions. */
    @Override
    public List<BlockPos> getBlocksPosition() {
        return List.copyOf(positions);
    }

    /**
     * Resolves each position with `resolver` and returns a list of non-null results.
     * Useful to convert positions to block entities or handlers.
     */
    @Override
    public <T> List<T> resolveBlock(Level level, Function<BlockPos, T> resolver) {
        List<T> result = new ArrayList<>();
        for (BlockPos p: new ArrayList<>(positions)) {
            T r = resolver.apply(p);
            if (r != null) result.add(r);
        }
        return result;
    }

    /** Serialization: implementation provided by subclasses. */
    @Override
    public abstract void read(CompoundTag compound);

    /** Deserialization: implementation provided by subclasses. */
    @Override
    public abstract void write(CompoundTag compound);

    /**
     * Removes invalid positions (e.g. unloaded chunk, missing block entity).
     * Subclasses must implement manager-specific validation logic.
     */
    @Override
    public abstract void clearInvalid(Level level);
}
