package net.nuclearteam.createnuclear.content.multiblock.controller.service;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

/**
 * Tracks the reactor's meltdown countdown and the player notifications tied to it.
 * Consumes the danger flag computed by {@link IReactorAlarmCoordinator#computeDanger}
 * instead of recomputing it, so the heat-level classification stays in a single place.
 */
public interface IReactorMeltdownMonitor {

    /**
     * Outcome of a single countdown tick.
     * {@code NONE} - not in danger, countdown reset to zero.
     * {@code CRITICAL} - in danger, countdown running, explosion threshold not reached yet.
     * {@code EXPLODE} - countdown threshold reached, the caller must trigger the explosion now.
     */
    enum MeltdownState { NONE, CRITICAL, EXPLODE }

    /**
     * Advances the countdown by one tick and sends the matching player notifications
     * (stabilized / overheating / meltdown flash / critical failure).
     *
     * @param level    the reactor's level, used for notifications and game time
     * @param pos      the controller's position, used as the notification origin
     * @param isDanger whether the reactor is currently at heat level DANGER
     * @return the resulting {@link MeltdownState} for this tick
     */
    MeltdownState tick (Level level, BlockPos pos, boolean isDanger);
}
