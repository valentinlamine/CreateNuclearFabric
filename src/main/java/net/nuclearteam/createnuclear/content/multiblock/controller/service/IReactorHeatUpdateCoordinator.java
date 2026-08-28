package net.nuclearteam.createnuclear.content.multiblock.controller.service;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.nuclearteam.createnuclear.content.logistics.BigFluidStack;
import net.nuclearteam.createnuclear.content.multiblock.controller.ReactorControllerInventory;
import net.nuclearteam.createnuclear.content.multiblock.controller.display.ReactorDisplayState;
import net.nuclearteam.createnuclear.content.multiblock.controller.manager.ReactorInputFluidManagerI;
import net.nuclearteam.createnuclear.content.multiblock.reactorLogic.HeatBalance;

/**
 * Centralizes the reactor's heat calculation logic and the decision of
 * whether it has enough fuel to run.
 * <p>
 * The {@link HeatBalance} consumed by {@link #updateHeatOnly} and
 * {@link #calculateAndWriteHeat} is produced upstream by
 * {@link #calculateHeatBalance}; callers resolve it once per tick and pass
 * the result into both methods below.
 */
public interface IReactorHeatUpdateCoordinator {

    /**
     * Computes the pattern's fuel/cooler heat balance (see {@link HeatBalance}),
     * used to determine whether the reactor is within the wiki-reference 6:1
     * fuel:cooler equilibrium.
     *
     * @param configuredPattern the blueprint's configured pattern (required items)
     * @param displayState      snapshot of items/fluids currently available to the reactor, may be {@code null}
     * @param level             the level the reactor is in, used to resolve rod types
     * @return the resolved heat balance for the pattern
     */
    HeatBalance calculateHeatBalance(ItemStack configuredPattern, ReactorDisplayState displayState, Level level);

    /**
     * Determines whether the reactor has enough fuel (FUEL-type rods) to start,
     * on top of being assembled and having a fluid input.
     *
     * @param configuredPattern the blueprint's configured pattern (required items)
     * @param displayState      snapshot of items/fluids currently available to the reactor, may be {@code null}
     * @param inputFluidManager the reactor's fluid input manager, used to check that at least one input exists
     * @param level             the level the reactor is in, used to resolve rod types
     * @param assembled         whether the multiblock is currently assembled
     * @return {@code true} if every fuel rod required by the pattern is
     * available and at least one fuel rod is required
     */
    boolean canRun(ItemStack configuredPattern, ReactorDisplayState displayState,
                                  ReactorInputFluidManagerI inputFluidManager, Level level, boolean assembled);

    /**
     * Updates the heat without running the reactor (case where it isn't ready).
     * Heat is only calculated if the full pattern (every required item, not
     * just fuel) is available; otherwise it drops back to zero.
     *
     * @param configuredPattern the blueprint's configured pattern (required items)
     * @param displayState      snapshot of items/fluids currently available to the reactor, may be {@code null}
     * @param fluidStack        the reactor's current coolant fluid stack, may be {@code null}
     * @param heatBalance       the pattern's fuel/cooler heat balance for this tick
     * @param previousHeat      the heat value calculated on the previous tick
     * @param inventory         the reactor controller's inventory, used to read the configured pattern items
     * @param level             the level the reactor is in
     * @param assembled         whether the multiblock is currently assembled
     * @return the calculated heat, also written to the configured pattern's tag
     */
    int updateHeatOnly(ItemStack configuredPattern, ReactorDisplayState displayState, BigFluidStack fluidStack,
                       HeatBalance heatBalance, int previousHeat, ReactorControllerInventory inventory, Level level, boolean assembled);

    /**
     * Calculates the reactor's heat during normal operation and writes it to
     * the configured pattern's tag.
     *
     * @param configuredPattern the blueprint's configured pattern (required items)
     * @param fluidStack        the reactor's current coolant fluid stack, may be {@code null}
     * @param heatBalance       the pattern's fuel/cooler heat balance for this tick
     * @param previousHeat      the heat value calculated on the previous tick
     * @param inventory         the reactor controller's inventory, used to read the configured pattern items
     * @param level             the level the reactor is in
     * @param displayState      snapshot of items/fluids currently available to the reactor, may be {@code null}
     * @return the calculated heat
     */
    int calculateAndWriteHeat(ItemStack configuredPattern, BigFluidStack fluidStack, HeatBalance heatBalance, int previousHeat,
                              ReactorControllerInventory inventory, Level level, ReactorDisplayState displayState);
}
