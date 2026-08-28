package net.nuclearteam.createnuclear.content.multiblock.reactorLogic;

import net.minecraft.world.level.Level;
import net.nuclearteam.createnuclear.api.multiblock.fluid.ReactorFluidType;
import net.nuclearteam.createnuclear.content.logistics.BigFluidStack;
import net.nuclearteam.createnuclear.content.multiblock.controller.ReactorControllerInventory;
import net.nuclearteam.createnuclear.content.multiblock.controller.display.ReactorDisplayState;

/**
 * HeatManager facade delegating to extracted components (in separate files).
 */
public class HeatManager {
    private final IHeatCalculator calculator;
    private final IOverheatController overheatController;

    public HeatManager(IHeatCalculator calculator, IOverheatController overheatController) {
        this.calculator = calculator;
        this.overheatController = overheatController;
    }

    public HeatManager() {
        this(new DefaultHeatCalculator(), new DefaultOverheatController());
    }

    public double calculateHeat(BigFluidStack bigFluidStack, HeatBalance heatBalance, int previousHeat, ReactorControllerInventory inventory, Level level, ReactorDisplayState displayState) {
        ReactorFluidType type = bigFluidStack == null ? null : bigFluidStack.getFluidtype(level);
        overheatController.updateState(heatBalance, previousHeat, bigFluidStack, type);

        return calculator.computeHeat(bigFluidStack, type, inventory, overheatController.getOverHeat(), displayState, level);
    }

    public int getLiquidTimer() { return  overheatController.getLiquidTimer();}
}
