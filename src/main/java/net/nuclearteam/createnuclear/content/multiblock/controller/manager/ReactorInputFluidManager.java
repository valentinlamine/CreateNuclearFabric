package net.nuclearteam.createnuclear.content.multiblock.controller.manager;

import io.github.fabricators_of_create.porting_lib.fluids.FluidStack;
import io.github.fabricators_of_create.porting_lib.transfer.TransferUtil;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.ListTag;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.nuclearteam.createnuclear.content.multiblock.input.fluid.ReactorFluidInputEntity;
import net.nuclearteam.createnuclear.content.multiblock.input.fluid.VirtualReactorInputFluid;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public class ReactorInputFluidManager extends AbstractReactorIOManager implements ReactorInputFluidManagerI {
    private static final String NBT_KEY = "ReactorInputFluid";

    @Override
    public void read(CompoundTag compound) {
        positions.clear();
        if (!compound.contains(NBT_KEY)) return;
        ListTag list = compound.getList(NBT_KEY, Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            CompoundTag tag = list.getCompound(i);
            positions.add(new BlockPos(tag.getInt("x"), tag.getInt("y"), tag.getInt("z")));
        }
    }

    @Override
    public void write(CompoundTag compound) {
        ListTag list = new ListTag();
        for (BlockPos pos : positions) {
            CompoundTag tag = new CompoundTag();
            tag.putInt("x", pos.getX());
            tag.putInt("y", pos.getY());
            tag.putInt("z", pos.getZ());
            list.add(tag);
        }
        compound.put(NBT_KEY, list);
    }

    @Override
    public void clearInvalid(Level level) {
        if (level == null) {
            positions.clear();
            return;
        }
        positions.removeIf(pos -> TransferUtil.getFluidStorage(level, pos, null) == null);
    }

    @Override
    public List<BlockPos> getBlocksPosition(Level level) {
        if (level == null) return List.of();
        return positions.stream()
                .filter(pos -> level.getBlockEntity(pos) instanceof ReactorFluidInputEntity)
                .toList();
    }

    @Override
    public List<Storage<FluidVariant>> getFuildHandlers(Level level) {
        if (level == null) return List.of();
        List<Storage<FluidVariant>> handlers = new ArrayList<>();
        for (BlockPos pos : List.copyOf(positions)) {
            Storage<FluidVariant> storage = TransferUtil.getFluidStorage(level, pos, null);
            if (storage != null) handlers.add(storage);
        }
        return List.copyOf(handlers);
    }

    @Override
    public VirtualReactorInputFluid getInventory(Level level) {
        VirtualReactorInputFluid result = new VirtualReactorInputFluid();
        for (Storage<FluidVariant> storage : getFuildHandlers(level)) {
            for (StorageView<FluidVariant> view : storage.nonEmptyViews()) {
                result.addFluid(new FluidStack(view));
            }
        }
        return result;
    }

    /** Extracts at most fluidNeeded droplets in total and commits partial extraction. */
    @Override
    public boolean extractFluids(Level level, long fluidNeeded) {
        if (level == null || fluidNeeded <= 0) return false;
        long remaining = fluidNeeded;
        try (Transaction transaction = Transaction.openOuter()) {
            for (Storage<FluidVariant> storage : getFuildHandlers(level)) {
                for (StorageView<FluidVariant> view : storage.nonEmptyViews()) {
                    remaining -= view.extract(
                            view.getResource(),
                            Math.min(remaining, view.getAmount()),
                            transaction
                    );
                    if (remaining == 0) break;
                }
                if (remaining == 0) break;
            }
            if (remaining < fluidNeeded) transaction.commit();
        }
        return remaining < fluidNeeded;
    }
}
