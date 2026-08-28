package net.nuclearteam.createnuclear.content.multiblock.controller.service;

import net.minecraft.world.level.Level;
import net.nuclearteam.createnuclear.content.logistics.BigFluidStack;
import net.nuclearteam.createnuclear.content.logistics.FluidUnits;
import net.nuclearteam.createnuclear.content.multiblock.controller.manager.ReactorInputFluidManagerI;

/**
 * Default implementation of {@link IFluidConsumptionRateCalculator}.
 * The rate per cycle depends on the fluid's efficiency and the liquid timer
 * exposed by {@link IHeatService}, which itself depends on the reactor size.
 */
public class FluidConsumptionRateCalculator implements IFluidConsumptionRateCalculator {
    private final IHeatService heatService;

    public FluidConsumptionRateCalculator(IHeatService heatService) {
        this.heatService = heatService;
    }

    /**
     * {@inheritDoc}
     * Does nothing if the fluid stack is {@code null} or holds a negligible
     * amount (&le; 1 mB).
     */
    @Override
    public double tick(BigFluidStack fluidStack, int reactorSize, Level level,
                       ReactorInputFluidManagerI inputFluidManager, double fluidBuffer) {
        if (fluidStack == null || fluidStack.amount <= FluidUnits.DROPLETS_PER_MILLIBUCKET) {
            return fluidBuffer;
        }

        double amountPerCycle = fluidStack.getFluidtype(level).efficiency();
        switch (reactorSize) {
            case 5 -> amountPerCycle /= (double) heatService.getLiquidTimer() / 40;
            case 7 -> amountPerCycle /= (double) heatService.getLiquidTimer() / 147;
            case 9 -> amountPerCycle /= (double) heatService.getLiquidTimer() / 360;
        }

        fluidBuffer += amountPerCycle;

        if (fluidBuffer >= 1.0) {
            int toExtractMilliBuckets = (int) Math.floor(fluidBuffer);
            long toExtractDroplets = FluidUnits.milliBucketsToDroplets(toExtractMilliBuckets);
            if (inputFluidManager.extractFluids(level, toExtractDroplets)) {
                fluidBuffer -= toExtractMilliBuckets;
            }
        }
        return fluidBuffer;
    }
}
