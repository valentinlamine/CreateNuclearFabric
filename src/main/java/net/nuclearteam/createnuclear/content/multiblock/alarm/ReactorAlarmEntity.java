package net.nuclearteam.createnuclear.content.multiblock.alarm;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.nuclearteam.createnuclear.content.multiblock.controller.ReactorControllerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.BlockPos;
import net.nuclearteam.createnuclear.foundation.advancement.CNAdvancement;
import net.nuclearteam.createnuclear.foundation.advancement.CNAdvancementBehaviour;

import java.util.List;
import java.util.function.Consumer;

public class ReactorAlarmEntity extends SmartBlockEntity {

    private static Consumer<ReactorAlarmEntity> clientTick = ignored -> { };

    public ReactorControllerBlockEntity controller = null;
    private CNAdvancementBehaviour advancement;
    private boolean advancementAwarded = false; // Prevents repeatedly awarding the advancement on every tick

    public ReactorAlarmEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void tick() {
        super.tick();
        if (getLevel() == null || !(getBlockState().getBlock() instanceof ReactorAlarm)) return;

        if (getLevel().isClientSide) {
            clientTick.accept(this);
        } else {
            tickServer();
        }
    }

    public static void setClientTick(Consumer<ReactorAlarmEntity> hook) {
        clientTick = hook;
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
