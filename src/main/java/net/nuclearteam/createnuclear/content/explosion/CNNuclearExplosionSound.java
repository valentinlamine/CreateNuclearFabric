package net.nuclearteam.createnuclear.content.explosion;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;

public class CNNuclearExplosionSound extends AbstractTickableSoundInstance {

    /**
     * Inflated starting volume, set only so the sound carries across the whole blast radius.
     * {@code SoundEngine#play} derives the linear attenuation radius from
     * {@code max(volume, 1) * attenuationDistance} <em>once</em>, and never recomputes it, so
     * 20 here means ~320 blocks of reach instead of the default 16.
     */
    private static final float RANGE_BOOST = 20F;

    private int tickCount = 0;
    private final int duration;
    private final int fadesAt;
    private final float fadeInBy;

    public CNNuclearExplosionSound(SoundEvent soundEvent, double x, double y, double z, int duration, int fadesAt, float fadeInBy, boolean looping) {
        super(soundEvent, SoundSource.NEUTRAL, SoundInstance.createRandom());
        this.attenuationType = AttenuationType.LINEAR;
        this.repeat = looping;
        this.x = x;
        this.y = y;
        this.z = z;
        this.duration = duration;
        this.fadesAt = fadesAt;
        this.fadeInBy = fadeInBy;
        this.repeatDelay = 0;
        this.volume = RANGE_BOOST;
    }

    public void tick() {
        if (tickCount == 0) {
            // Queued ticking sounds are played before the tick loop runs (SoundEngine#tickNonPaused),
            // so by now the attenuation radius is locked in and RANGE_BOOST has done its job. It has
            // to come back down to the [0,1] gain range: SoundEngine#calculateVolume clamps gain to
            // 1.0, so any value above 1 makes the fades below inaudible until the very last tick.
            volume = 1F;
        }

        if(tickCount < this.duration){
            if(tickCount >= fadesAt){
                float shrinkVolumeFor = 1F / Math.max(this.duration - fadesAt, 1F);
                volume = Math.max(0, volume - shrinkVolumeFor);
            }else if(volume < 1F){
                volume = Math.min(1F, volume + fadeInBy);
            }
            tickCount++;
        }else{
            stop();
        }
    }

    public boolean shouldAlwaysPlay() {
        return true;
    }
}