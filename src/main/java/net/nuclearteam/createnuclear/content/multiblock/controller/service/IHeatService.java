package net.nuclearteam.createnuclear.content.multiblock.controller.service;

import net.minecraft.world.level.Level;
import net.nuclearteam.createnuclear.content.logistics.BigFluidStack;
import net.nuclearteam.createnuclear.content.multiblock.controller.ReactorControllerInventory;
import net.nuclearteam.createnuclear.content.multiblock.controller.display.ReactorDisplayState;
import net.nuclearteam.createnuclear.content.multiblock.reactorLogic.HeatBalance;

public interface IHeatService {
    int getLiquidTimer();

    double calculateHeat(BigFluidStack bigFluidStack, HeatBalance heatBalance, int previousHeat, ReactorControllerInventory inventory, Level level, ReactorDisplayState displayState);
}
