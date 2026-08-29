package net.nuclearteam.createnuclear.content.multiblock.controller.service;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.nuclearteam.createnuclear.content.multiblock.IHeat;
import net.nuclearteam.createnuclear.content.multiblock.alarm.ReactorAlarm;
import net.nuclearteam.createnuclear.content.multiblock.controller.manager.ReactorAlarmManagerI;
import net.nuclearteam.createnuclear.foundation.advancement.CNAdvancement;
import net.nuclearteam.createnuclear.foundation.advancement.CNAdvancementBehaviour;

/**
 * Default {@link IReactorAlarmCoordinator}. Extracted from
 * {@code ReactorControllerBlockEntity.activateAlarms(boolean)}: the alarm wiring and
 * advancement award are unchanged from the original implementation.
 */
public class ReactorAlarmCoordinator implements IReactorAlarmCoordinator {
    private final CNAdvancementBehaviour advancement;

    public ReactorAlarmCoordinator(CNAdvancementBehaviour advancement) {
        this.advancement = advancement;
    }

    @Override
    public boolean computeDanger(int heat, int multiblockSize) {
        return IHeat.HeatLevel.of(heat, multiblockSize) == IHeat.HeatLevel.DANGER;
    }

    @Override
    public void update(Level level, ReactorAlarmManagerI alarmManager, boolean isDanger) {
        if (alarmManager == null) return;

        for (BlockPos pos : alarmManager.getBlocksPosition(level)) {
            if (!level.isLoaded(pos)) continue;

            BlockState state = level.getBlockState(pos);

            if (state.getBlock() instanceof ReactorAlarm && state.getValue(ReactorAlarm.POWERED) != isDanger) {
                level.setBlock(pos, state.setValue(ReactorAlarm.POWERED, isDanger), 3);
                if (isDanger) advancement.awardPlayer(CNAdvancement.SILENCE_THE_CORE);
            }
        }
    }
}
