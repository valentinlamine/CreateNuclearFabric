package net.nuclearteam.createnuclear.content.multiblock.alarm;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.nuclearteam.createnuclear.CNSoundEvents;
import net.nuclearteam.createnuclear.CreateNuclear;

import java.util.Map;
import java.util.WeakHashMap;

public final class ReactorAlarmClient {
    private static final Map<ReactorAlarmEntity, ReactorAlarmSoundInstance> ACTIVE_SOUNDS = new WeakHashMap<>();

    private ReactorAlarmClient() {
    }

    public static void tick(ReactorAlarmEntity alarm) {
        BlockState state = alarm.getBlockState();
        boolean powered = state.contains(ReactorAlarm.POWERED) && state.getValue(ReactorAlarm.POWERED);
        ReactorAlarmSoundInstance sound = ACTIVE_SOUNDS.get(alarm);

        if (!powered) {
            stop(alarm, sound);
            return;
        }

        if (sound == null || sound.isDone()) {
            try {
                sound = new ReactorAlarmSoundInstance(
                    alarm.getLevel(), alarm.getBlockPos(), CNSoundEvents.REACTOR_ALARM_LOOP.getMainEvent());
                ACTIVE_SOUNDS.put(alarm, sound);
                Minecraft.getInstance().getSoundManager().play(sound);
            } catch (Exception exception) {
                ACTIVE_SOUNDS.remove(alarm);
                CreateNuclear.LOGGER.warn("Failed to start reactor alarm sound", exception);
            }
        }
    }

    private static void stop(ReactorAlarmEntity alarm, ReactorAlarmSoundInstance sound) {
        if (sound != null) {
            sound.fadeOut();
        }
        ACTIVE_SOUNDS.remove(alarm);
    }
}
