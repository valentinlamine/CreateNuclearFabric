package net.nuclearteam.createnuclear.content.multiblock.controller.service;

import net.minecraft.world.level.Level;
import net.nuclearteam.createnuclear.content.logistics.BigFluidStack;
import net.nuclearteam.createnuclear.content.multiblock.controller.manager.ReactorInputFluidManagerI;

/**
 * Computes how much fluid should be consumed per tick and triggers extraction
 * once the accumulated amount reaches a whole unit.
 */
public interface IFluidConsumptionRateCalculator {

    /**
     * Advances the consumption calculation by one tick.
     *
     * @param fluidStack       fluid currently loaded, or {@code null} if none
     * @param reactorSize      reactor size, used to determine the consumption rate
     * @param level            current level, used to resolve the fluid type
     * @param inputFluidManager input manager responsible for extracting the consumed fluid
     * @param fluidBuffer      fractional remainder accumulated since the previous tick
     * @return the new fractional remainder to carry over to the next tick
     */
    double tick(BigFluidStack fluidStack, int reactorSize, Level level,
                ReactorInputFluidManagerI inputFluidManager, double fluidBuffer);
}
