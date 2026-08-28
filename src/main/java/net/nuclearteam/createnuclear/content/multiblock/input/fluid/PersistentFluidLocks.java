package net.nuclearteam.createnuclear.content.multiblock.input.fluid;

import net.minecraft.world.level.material.Fluid;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PersistentFluidLocks extends SavedData {
    private final Map<BlockPos, Fluid> locks = new ConcurrentHashMap<>();
    private static final String DATA_NAME = "createnuclear_fluid_locks";

    /**
     * Persistent storage for fluid locks associated with multiblock controllers.
     * Locks are persisted to world saved data so controllers keep their preferred
     * fluid across server restarts.
     */

    public static PersistentFluidLocks get(ServerLevel level) {
        return level.getPersistentStateManager().getOrCreate(nbt -> {
            PersistentFluidLocks d = new PersistentFluidLocks();
            d.readFrom(nbt);

            return d;
        }, PersistentFluidLocks::new, DATA_NAME);
    }

   /**
    * Attempt to acquire a persistent lock for the specified controller position.
    * @param pos controller block position
    * @param fluid fluid to lock to; if null the call is a no-op and returns true
    * @return true if the lock was acquired or already held for the same fluid
    */
   public boolean tryLock(BlockPos pos, Fluid fluid) {
        if (fluid == null) return true;
        boolean ok = locks.compute(pos, (k,v) -> v == null ? fluid : v) == fluid;
        if (ok) setDirty();
        return ok;
   }

    /**
     * Check whether the given fluid is acceptable for the controller at {@code pos}.
     * @param pos controller position
     * @param fluid fluid to check
     * @return true if no lock is present or the lock matches the provided fluid
     */
    public boolean canAccept(BlockPos pos, Fluid fluid) {
        if (fluid == null) return true;
        Fluid locked = locks.get(pos);
        return locked == null || locked == fluid;
    }

    /**
     * Clear any persistent lock for the specified controller position.
     * Marks the saved data as dirty when a lock was removed.
     */
    public void clearLock(BlockPos pos) {
        if (locks.remove(pos) != null) setDirty();
    }

    public void readFrom(CompoundTag nbt) {
        locks.clear();
        ListTag list = nbt.getList("locks", 10);
        list.forEach(tag -> {
            CompoundTag e = (CompoundTag) tag;
            BlockPos pos = new BlockPos(e.getInt("x"), e.getInt("y"), e.getInt("z"));
            Fluid f = BuiltInRegistries.FLUID.get(ResourceLocation.tryParse(e.getString("fluid")));
            if (f != null) locks.put(pos, f);
        });
    }

    @Override
    public CompoundTag writeNbt(CompoundTag nbt) {
        ListTag list = new ListTag();
        locks.forEach((pos, fluid) -> {
            CompoundTag e = new CompoundTag();
            e.putInt("x", pos.getX());
            e.putInt("y", pos.getY());
            e.putInt("z", pos.getZ());
            e.putString("fluid", BuiltInRegistries.FLUID.getKey(fluid).toString());
            list.add(e);
        });
        nbt.put("locks", list);
        return nbt;
    }
}
