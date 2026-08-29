package net.nuclearteam.createnuclear.content.multiblock.controller;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.nuclearteam.createnuclear.CreateNuclear;

/**
 * Client-side looping sound played while a reactor is actively producing energy.
 * <p>
 * Mirrors {@code ReactorAlarmSoundInstance}: the instance stops itself once the block at
 * {@code pos} is no longer a running reactor controller (the {@code ACTIVE} blockstate property
 * is synced from the server), so it survives the controller block being broken or the reactor
 * shutting down even if the owning block entity's tick stops driving it.
 */
public class ReactorRunningSoundInstance extends AbstractTickableSoundInstance {
    private final BlockPos pos;
    private final Level level;

    public ReactorRunningSoundInstance(Level level, BlockPos pos, SoundEvent sound) {
        super(sound, SoundSource.BLOCKS, level != null ? level.random : null);
        this.level = level;
        this.pos = pos;
        if (pos != null) {
            this.x = pos.getX();
            this.y = pos.getY();
            this.z = pos.getZ();
        }
        this.looping = true;
        this.delay = 0;
        this.volume = 1.0F;
    }

    @Override
    public void tick() {
        try {
            if (level == null || pos == null) {
                stop();
                return;
            }

            BlockState state = level.getBlockState(pos);
            if (!(state.getBlock() instanceof ReactorControllerBlock)
                    || !state.hasProperty(ReactorControllerBlock.ACTIVE)
                    || !state.getValue(ReactorControllerBlock.ACTIVE)) {
                stop();
            }
        } catch (Exception e) {
            stop();
            CreateNuclear.LOGGER.warn(e.getMessage());
        }
    }

    /** Public entry point so the owning block entity can stop the loop ({@link #stop()} is protected). */
    public void stopSound() {
        stop();
    }
}
