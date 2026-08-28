package net.nuclearteam.createnuclear.content.multiblock.reactorLogic;

import net.nuclearteam.createnuclear.api.multiblock.fluid.ReactorFluidType;
import net.nuclearteam.createnuclear.content.logistics.BigFluidStack;
import net.nuclearteam.createnuclear.content.logistics.FluidUnits;

class DefaultOverheatController implements IOverheatController {
    private int overFlowHeatTimer = 0;
    private int overFlowLimiter = 30;
    private double overHeat = 0;

    private final int liquidTimer = 3600;

    @Override
    public void updateState(HeatBalance heatBalance, int currentHeat, BigFluidStack bigFluidStack, ReactorFluidType type) {
        long fluidAmount = bigFluidStack == null ? 0 : bigFluidStack.amount;
        int fluidEfficiency = type == null ? -1 : type.efficiency();
        long requiredFluid = fluidEfficiency < 0 ? -1 : FluidUnits.milliBucketsToDroplets(fluidEfficiency);
        boolean exceedsFluidMaxHeat = type != null && Math.abs(currentHeat) > type.maxHeat();

        boolean rodMalus = heatBalance.resolve() == EquilibriumState.OVERHEATING;
        boolean fluidMalus = fluidEfficiency == -1 || fluidAmount < requiredFluid || exceedsFluidMaxHeat;

        int malusPoints = (rodMalus ? 1 : 0) + (fluidMalus ? 1 : 0);

        if (malusPoints > 0) {
            overFlowHeatTimer++;
            if (overFlowHeatTimer >= overFlowLimiter) {
                overHeat += malusPoints;
                overFlowHeatTimer = 0;
                overFlowLimiter = Math.max(2, overFlowLimiter - malusPoints);
            }
        } else {
            overFlowHeatTimer = 0;
            overFlowLimiter = 30;
            if (overHeat > 0) overHeat -= 2;
            else overHeat = 0;
        }
    }

    @Override
    public double getOverHeat() { return overHeat; }


    @Override
    public int getLiquidTimer()  { return liquidTimer; }
}
