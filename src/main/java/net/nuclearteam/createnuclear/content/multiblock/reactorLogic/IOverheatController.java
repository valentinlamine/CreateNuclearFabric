package net.nuclearteam.createnuclear.content.multiblock.reactorLogic;

import net.nuclearteam.createnuclear.api.multiblock.fluid.ReactorFluidType;
import net.nuclearteam.createnuclear.content.logistics.BigFluidStack;

public interface IOverheatController {
    void updateState(HeatBalance heatBalance, int currentHeat, BigFluidStack bigFluidStack, ReactorFluidType type);
    double getOverHeat();
    int getLiquidTimer();
}
