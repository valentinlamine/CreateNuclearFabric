package net.nuclearteam.createnuclear.content.multiblock.controller.manager;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.nuclearteam.createnuclear.content.multiblock.input.fluid.VirtualReactorInputFluid;

import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public interface ReactorInputFluidManagerI extends ReactorIOManager {
    List<BlockPos> getBlocksPosition(Level level);

    /** Preserves the original public spelling while returning native Fabric fluid storages. */
    List<Storage<FluidVariant>> getFuildHandlers(Level level);

    VirtualReactorInputFluid getInventory(Level level);
    boolean extractFluids(Level level, long fluidNeeded);
}
