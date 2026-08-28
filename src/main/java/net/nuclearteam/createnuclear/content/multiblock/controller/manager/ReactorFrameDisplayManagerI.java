package net.nuclearteam.createnuclear.content.multiblock.controller.manager;

import io.github.fabricators_of_create.porting_lib.fluids.FluidStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;

public interface ReactorFrameDisplayManagerI {
    FluidStack getDisplayedFluid(Level level, ReactorInputFluidManagerI handlers);
    float getDisplayedFluidFillRatio(Level level, ReactorInputFluidManagerI handlers);
    void setFrameColumn(int minY, int maxY, Runnable onChange);
    int getFrameColumnMinY();
    int getFrameColumnMaxY();
    boolean hasFrameColumn();
    void read(CompoundTag compound);
    void write(CompoundTag compound);
}
