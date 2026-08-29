package net.nuclearteam.createnuclear.content.multiblock.controller.manager;

import io.github.fabricators_of_create.porting_lib.fluids.FluidStack;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("UnstableApiUsage")
public class ReactorFrameDisplayManager implements ReactorFrameDisplayManagerI {
    public static final String COMPONENT_FRAME_COLUMN_MIN_Y = "frameColumnMinY";
    public static final String COMPONENT_FRAME_COLUMN_MAX_Y = "frameColumnMaxY";

    private int frameColumnMinY = Integer.MAX_VALUE;
    private int frameColumnMaxY = Integer.MIN_VALUE;
    private long frameFluidCacheTick = -1;
    private float frameFluidFillRatioCache;
    private FluidStack frameFluidCache = FluidStack.EMPTY;

    private void refreshFrameFluidCache(@Nullable Level level, ReactorInputFluidManagerI handlers) {
        if (level == null || level.getGameTime() == frameFluidCacheTick) return;
        frameFluidCacheTick = level.getGameTime();

        long amount = 0;
        long capacity = 0;
        FluidStack first = FluidStack.EMPTY;
        for (Storage<FluidVariant> storage : handlers.getFuildHandlers(level)) {
            for (StorageView<FluidVariant> view : storage) {
                capacity += view.getCapacity();
                amount += view.getAmount();
                if (first.isEmpty() && !view.isResourceBlank() && view.getAmount() > 0) {
                    first = new FluidStack(view);
                }
            }
        }

        frameFluidCache = first;
        frameFluidFillRatioCache = capacity == 0 ? 0
                : Math.min(1, (float) ((double) amount / (double) capacity));
    }

    @Override
    public FluidStack getDisplayedFluid(Level level, ReactorInputFluidManagerI handlers) {
        refreshFrameFluidCache(level, handlers);
        return frameFluidCache;
    }

    @Override
    public float getDisplayedFluidFillRatio(Level level, ReactorInputFluidManagerI handlers) {
        refreshFrameFluidCache(level, handlers);
        return frameFluidFillRatioCache;
    }

    @Override
    public void setFrameColumn(int minY, int maxY, Runnable onChange) {
        if (frameColumnMinY == minY && frameColumnMaxY == maxY) return;
        frameColumnMinY = minY;
        frameColumnMaxY = maxY;
        onChange.run();
    }

    @Override
    public int getFrameColumnMinY() {
        return frameColumnMinY;
    }

    @Override
    public int getFrameColumnMaxY() {
        return frameColumnMaxY;
    }

    @Override
    public boolean hasFrameColumn() {
        return frameColumnMinY != Integer.MAX_VALUE
                && frameColumnMaxY != Integer.MIN_VALUE
                && frameColumnMaxY >= frameColumnMinY;
    }

    @Override
    public void read(CompoundTag compound) {
        if (compound.contains(COMPONENT_FRAME_COLUMN_MIN_Y)) {
            frameColumnMinY = compound.getInt(COMPONENT_FRAME_COLUMN_MIN_Y);
        }
        if (compound.contains(COMPONENT_FRAME_COLUMN_MAX_Y)) {
            frameColumnMaxY = compound.getInt(COMPONENT_FRAME_COLUMN_MAX_Y);
        }
    }

    @Override
    public void write(CompoundTag compound) {
        if (!hasFrameColumn()) return;
        compound.putInt(COMPONENT_FRAME_COLUMN_MIN_Y, frameColumnMinY);
        compound.putInt(COMPONENT_FRAME_COLUMN_MAX_Y, frameColumnMaxY);
    }
}
