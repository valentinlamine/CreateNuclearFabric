package net.nuclearteam.createnuclear.content.multiblock.controller.service;

import net.minecraft.world.level.Level;
import net.nuclearteam.createnuclear.content.multiblock.controller.manager.ReactorAlarmManagerI;

/**
 * Classifies the reactor's heat into a danger flag and drives the
 * {@link net.nuclearteam.createnuclear.content.multiblock.alarm.ReactorAlarm} blocks
 * registered in a {@link ReactorAlarmManagerI} accordingly.
 */
public interface IReactorAlarmCoordinator {

    /**
     * @param heat           the heat value to classify
     * @param multiblockSize the reactor's multiblock size, used to resolve the danger threshold
     * @return {@code true} if {@code heat} falls in the DANGER heat-level range
     */
    boolean computeDanger(int heat, int multiblockSize);

    /**
     * Powers on/off every alarm block known to {@code alarmManager} to match {@code isDanger},
     * and awards the SILENCE_THE_CORE advancement on the rising edge (alarm turning on).
     *
     * @param level        the reactor's level
     * @param alarmManager the manager holding the alarm block positions
     * @param isDanger     whether the alarms should be powered on
     */
    void update(Level level, ReactorAlarmManagerI alarmManager, boolean isDanger);
}
