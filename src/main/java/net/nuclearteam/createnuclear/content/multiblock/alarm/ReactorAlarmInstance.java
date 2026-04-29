package net.nuclearteam.createnuclear.content.multiblock.alarm;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.nuclearteam.createnuclear.CreateNuclear;

public class ReactorAlarmInstance extends AbstractTickableSoundInstance {
    private final BlockPos pos;
    private final Level level;

    public ReactorAlarmInstance(Level level, BlockPos pos, SoundEvent sound) {
        super(sound, SoundSource.BLOCKS, level.random);
        this.level = level;
        this.pos = pos;
        this.x = pos.getX();
        this.y = pos.getY();
        this.z = pos.getZ();
        this.looping = true;
        this.delay = 0;
        this.volume = 1.5F; // Volume un peu plus raisonnable
    }

    @Override
    public void tick() {
        if (this.level == null) {
            this.stop();
            return;
        }

        BlockState state = this.level.getBlockState(this.pos);

        // On arrête le son si :
        // 1. Le bloc n'est plus une alarme
        // 2. Le bloc n'est plus alimenté
        if (!(state.getBlock() instanceof ReactorAlarm) || !state.getValue(ReactorAlarm.POWERED)) {
            CreateNuclear.LOGGER.info("Sound Stopped : " + state.getBlock());

            this.stop();
            ReactorAlarm.stopSound(this.pos);
        }
    }
}