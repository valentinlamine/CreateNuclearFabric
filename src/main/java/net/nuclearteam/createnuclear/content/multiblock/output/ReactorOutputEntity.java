package net.nuclearteam.createnuclear.content.multiblock.output;

import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.CreateLang;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.nuclearteam.createnuclear.content.multiblock.pattern.ReactorPattern;

import java.util.List;

public class ReactorOutputEntity extends GeneratingKineticBlockEntity {
    public int speed = 0;
    public float heat = 0;

    protected ReactorPattern pattern =  new ReactorPattern();

    protected float generatedSpeed;

    public ReactorOutputEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);

    }

    @Override
    public void lazyTick() {
        super.lazyTick();

        determineSpeed();
    }

    public void determineSpeed() {
        int deterSpeed = this.speed;
        setSpeedAndUpdate(deterSpeed);
    }

    public void setSpeedAndUpdate(int speed) {
        if (generatedSpeed == speed) return;

        generatedSpeed = (float) speed;

        updateGeneratedRotation();
		setChanged();
    }

    // Tracks the output's linked block position for persistence across reloads.
    private BlockPos outputPos;

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);

        // Restore the generated rotation speed
        generatedSpeed = compound.getFloat("generatedSpeed");

        // Restore the output position, if present in the tag
        if (compound.contains("outputPos")) {
            this.outputPos = BlockPos.of(compound.getLong("outputPos"));
        }
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);

        // Persist the generated rotation speed
        compound.putFloat("generatedSpeed", generatedSpeed);

        // Persist the output position, if set
        if (this.outputPos != null) {
            compound.putLong("outputPos", this.outputPos.asLong());
        }
    }

     @Override
     public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {

         float stressBase = calculateAddedStressCapacity();

         CreateLang.translate("gui.goggles.generator_stats")
                 .forGoggles(tooltip);
         CreateLang.translate("tooltip.capacityProvided")
                 .style(ChatFormatting.GRAY)
                 .forGoggles(tooltip);

         float speed = getTheoreticalSpeed();
         speed = Math.abs(speed);

         float stressTotal = stressBase * speed;

         CreateLang.number(stressTotal)
                 .translate("generic.unit.stress")
                 .style(ChatFormatting.AQUA)
                 .space()
                 .add(CreateLang.translate("gui.goggles.at_current_speed")
                         .style(ChatFormatting.DARK_GRAY))
                 .forGoggles(tooltip, 1);
         return true;
     }

    @Override
    public void initialize() {
        super.initialize();

        if (!hasSource() || getGeneratedSpeed() > getTheoreticalSpeed())
        {
            assert getLevel() != null;
            pattern.findController(getBlockPos(), getLevel(), true);
        }
    }

    @Override
    public float getGeneratedSpeed() {
        return Mth.clamp(generatedSpeed, 0, 1500000);
    }

}
