package net.nuclearteam.createnuclear.content.multiblock.alarm;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.nuclearteam.createnuclear.CreateNuclear;

public class ReactorAlarmSoundInstance extends AbstractTickableSoundInstance {
    private final BlockPos pos;
    private final Level world;

    public ReactorAlarmSoundInstance(Level world, BlockPos pos, SoundEvent sound) {
        super(sound, SoundSource.BLOCKS, world != null ? world.random : null);
        this.world = world;
        this.pos = pos;
        if (pos != null) {
            this.x = pos.getX();
            this.y = pos.getY();
            this.z = pos.getZ();
        }
        this.repeat = true;
        this.repeatDelay = 0;
        this.volume = 10.0F;
    }

    @Override
    public void tick() {
        try {
            if (world == null || pos == null) {
                stop();
                return;
            }

            BlockState state = world.getBlockState(pos);
            if (!(state.getBlock() instanceof ReactorAlarm)) {
                stop();
            }
        } catch (Exception exception) {
            stop();
            CreateNuclear.LOGGER.warn("Failed to tick reactor alarm sound", exception);
        }
    }

    public void fadeOut() {
        stop();
    }
}
