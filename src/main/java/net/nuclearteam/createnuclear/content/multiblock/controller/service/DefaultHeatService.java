package net.nuclearteam.createnuclear.content.multiblock.controller.service;

import net.minecraft.world.level.Level;
import net.nuclearteam.createnuclear.content.logistics.BigFluidStack;
import net.nuclearteam.createnuclear.content.multiblock.controller.ReactorControllerInventory;
import net.nuclearteam.createnuclear.content.multiblock.controller.display.ReactorDisplayState;
import net.nuclearteam.createnuclear.content.multiblock.reactorLogic.HeatBalance;
import net.nuclearteam.createnuclear.content.multiblock.reactorLogic.HeatManager;

public class DefaultHeatService implements IHeatService {
    private final HeatManager impl;

    public DefaultHeatService(HeatManager impl) {
        this.impl = impl;
    }
    
    @Override
    public  int getLiquidTimer() {
        return impl.getLiquidTimer();
    }

    @Override
    public double calculateHeat(BigFluidStack bigFluidStack, HeatBalance heatBalance, int previousHeat, ReactorControllerInventory inventory, Level level, ReactorDisplayState displayState) {
        return impl.calculateHeat(bigFluidStack, heatBalance, previousHeat, inventory, level, displayState);
    }
}
