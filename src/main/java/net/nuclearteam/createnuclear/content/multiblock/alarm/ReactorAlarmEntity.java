package net.nuclearteam.createnuclear.content.multiblock.alarm;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.nuclearteam.createnuclear.CNSoundEvents;
import net.nuclearteam.createnuclear.CreateNuclear;
import net.nuclearteam.createnuclear.content.multiblock.controller.ReactorControllerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.BlockPos;
import net.nuclearteam.createnuclear.foundation.advancement.CNAdvancement;
import net.nuclearteam.createnuclear.foundation.advancement.CNAdvancementBehaviour;

import java.util.List;
import java.util.function.Consumer;

public class ReactorAlarmEntity extends SmartBlockEntity {

    public ReactorControllerBlockEntity controller = null;
    private CNAdvancementBehaviour advancement;
    private boolean advancementAwarded = false; // Prevents repeatedly awarding the advancement on every tick

    @Environment(EnvType.CLIENT)
    protected ReactorAlarmSoundInstance soundInstance;

    public ReactorAlarmEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void tick() {
        super.tick();
        if (getLevel() == null || !(getBlockState().getBlock() instanceof ReactorAlarm)) return;

        if (level.isClientSide) {
            tickAudio(); // DistExecutor is unnecessary here; the level-side check is enough and avoids thread overhead
        } else {
            tickServer();
        }
    }

    @Environment(EnvType.CLIENT)
    protected void tickAudio() {
        BlockState state = getBlockState();
        boolean powered = state.hasProperty(ReactorAlarm.POWERED) && state.getValue(ReactorAlarm.POWERED);

        if (!powered) {
            stopSound();
            return;
        }

        if (soundInstance == null || soundInstance.isStopped()) {
            Minecraft minecraft = Minecraft.getInstance();
            try {
                soundInstance = new ReactorAlarmSoundInstance(level, worldPosition, CNSoundEvents.REACTOR_ALARM_LOOP.getMainEvent());
                minecraft.getSoundManager().play(soundInstance);
            } catch (Exception e) {
                CreateNuclear.LOGGER.warn("Failed to start alarm sound: " + e.getMessage());
            }
        }
    }

    @Environment(EnvType.CLIENT)
    private void stopSound() {
        if (soundInstance != null) {
            try {
                soundInstance.fadeOut();
            } catch (Exception e) {
                CreateNuclear.LOGGER.warn("Error while stopping the sound: " + e.getMessage());
            }
            soundInstance = null;
        }
    }

    private void tickServer() {
        BlockState state = getBlockState();
        if (!(state.getBlock() instanceof ReactorAlarm)) return;

        boolean powered = state.getValue(ReactorAlarm.POWERED);

        if (powered) {
            if (!advancementAwarded) { // Award it only once, on the rising edge
                this.advancement.awardPlayer(CNAdvancement.SILENCE_THE_CORE);
                advancementAwarded = true;
            }
        } else {
            advancementAwarded = false; // Reset when the alarm turns off
        }
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        behaviours.add(advancement = new CNAdvancementBehaviour(this, CNAdvancement.SILENCE_THE_CORE));
    }

    public void setController(ReactorControllerBlockEntity controller) {
        this.controller = controller;
    }
}
