package net.nuclearteam.createnuclear.content.multiblock.controller.manager;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.nuclearteam.createnuclear.content.multiblock.output.ReactorOutput;
import net.nuclearteam.createnuclear.content.multiblock.output.ReactorOutputEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Manager for a reactor's outputs (`ReactorOutput`).
 * Handles serialization of output positions.
 */
public class ReactorOutputManager extends AbstractReactorIOManager implements ReactorOutputManagerI {
    private static final String NBT_KEY = "ReactorOutputs";
    public static final int RPM_DIVIDER = 32;

    @Override
    public void write(CompoundTag compound) {
        ListTag list = new ListTag();
        for (BlockPos p : positions) {
            CompoundTag t = new CompoundTag();
            t.putLong("p", p.asLong());
            list.add(t);
        }
        compound.put(NBT_KEY, list);
    }

    @Override
    public void read(CompoundTag compound) {
        positions.clear();
        if (!compound.contains(NBT_KEY)) return;
        ListTag list = compound.getList(NBT_KEY, 10);
        for (int i = 0; i < list.size(); i++) {
            BlockPos p = BlockPos.fromLong(list.getCompound(i).getLong("p"));
            positions.add(p);
        }
    }

    /**
     * {@inheritDoc}
     * A tracked position is dropped if its chunk isn't loaded or the block
     * entity at that position is no longer a {@link ReactorOutputEntity}.
     */
    @Override
    public void clearInvalid(Level level) {
        List<BlockPos> toRemove = new ArrayList<>();
        for (BlockPos p : positions) {
            if (level == null || !level.canSetBlock(p)) {
                toRemove.add(p);
                continue;
            }
            BlockEntity be = level.getBlockEntity(p);
            if (!(be instanceof ReactorOutputEntity)) toRemove.add(p);
        }
        positions.removeAll(toRemove);
    }

    /**
     * {@inheritDoc}
     * Filters the tracked positions down to those currently backed by a
     * loaded {@link ReactorOutputEntity}.
     */
    @Override
    public List<BlockPos> getBlocksPosition(Level level) {
        List<BlockPos> positions = new ArrayList<>();

        for (BlockPos p : this.getBlocksPosition()) {
            if (level.getBlockEntity(p) instanceof ReactorOutputEntity) positions.add(p);
        }
        return List.copyOf(positions);
    }

    /**
     * {@inheritDoc}
     * The total rotation (in RPM, divided down by {@link #RPM_DIVIDER}) is
     * split as evenly as possible across the tracked outputs, with the
     * remainder distributed to the first outputs. Each output is set to its
     * share of the rotation speed if the reactor is assembled, otherwise
     * stopped.
     */
    @Override
    public void rotateOutputs(Level level, boolean assembled, int rotation) {
        if (positions.isEmpty()) return;

        int totalRpm = rotation / RPM_DIVIDER;
        int size = positions.size();
        int remainingRotation = totalRpm % size;

        for (int i = 0; i < size; i++) {
            int dividedRotation = (totalRpm / size) + (i < remainingRotation ? 1 : 0);
            BlockPos pos = positions.get(i);

            if (!(level.getBlockState(pos).getBlock() instanceof ReactorOutput block)) continue;
            ReactorOutputEntity entity = block.getBlockEntityType().get(level, pos);
            if (entity == null) continue;

            if (dividedRotation > 0) {
                entity.speed = assembled ? dividedRotation : 0;
                entity.updateSpeed = true;
                entity.setSpeedAndUpdate(dividedRotation);
                entity.updateGeneratedRotation();
            } else {
                entity.setSpeedAndUpdate(0);
                entity.updateSpeed = true;
                entity.updateGeneratedRotation();
            }
        }
    }
}
