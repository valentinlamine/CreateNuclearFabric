package net.nuclearteam.createnuclear.multiblock.energy;

import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.kinetics.KineticNetwork;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.motor.CreativeMotorBlock;
import com.simibubi.create.content.kinetics.motor.CreativeMotorBlockEntity;
import com.simibubi.create.content.kinetics.motor.KineticScrollValueBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueBehaviour;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import net.minecraft.world.phys.Vec3;
import net.nuclearteam.createnuclear.CreateNuclear;
import net.nuclearteam.createnuclear.block.CNBlocks;
import net.nuclearteam.createnuclear.multiblock.controller.ReactorControllerBlock;
import net.nuclearteam.createnuclear.multiblock.controller.ReactorControllerBlockEntity;

import static net.nuclearteam.createnuclear.multiblock.energy.ReactorOutput.DIR;

import java.util.List;
import java.util.Objects;

public class ReactorOutputEntity extends GeneratingKineticBlockEntity {
	public int speed = 1;
	public float heat = 0;

	ReactorControllerBlock controller = null;
	ReactorControllerBlockEntity controllerEntity = null;


	protected ScrollValueBehaviour generatedSpeed;

	public ReactorOutputEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}
	//KineticBlockEntity
	@Override
	public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
		super.addBehaviours(behaviours);
		generatedSpeed = new KineticScrollValueBehaviour(Lang.translateDirect("kinetics.reactor_output.rotation_speed"), this, new ReactorOutputValue());
		generatedSpeed.between(-1500000, 1500000);
		generatedSpeed.setValue(speed);
		generatedSpeed.withCallback(i -> this.updateGeneratedRotation());
		behaviours.add(generatedSpeed);

	}

	@Override
	public void tick() {
		super.tick();

		BlockGetter level = getLevel();

		if (level.getBlockState(getBlockPos().above(3)).getBlock() == CNBlocks.REACTOR_CONTROLLER.get()) {
			controller = (ReactorControllerBlock) level.getBlockState(getBlockPos().above(3)).getBlock();
			controllerEntity = (ReactorControllerBlockEntity) level.getBlockEntity(getBlockPos().above(3));
			if (controllerEntity != null) {
				if (!controllerEntity.getAssembled() && getSpeed() != 0) {
					setSpeed(0);
				}
			}
		} else setSpeed(0);
	}

	@Override
	public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {

		float stressBase = calculateAddedStressCapacity();

		Lang.translate("gui.goggles.generator_stats")
				.forGoggles(tooltip);
		Lang.translate("tooltip.capacityProvided")
				.style(ChatFormatting.GRAY)
				.forGoggles(tooltip);

		float speed = getTheoreticalSpeed();
		speed = Math.abs(speed);

		float stressTotal = stressBase * speed;

		Lang.number(stressTotal)
				.translate("generic.unit.stress")
				.style(ChatFormatting.AQUA)
				.space()
				.add(Lang.translate("gui.goggles.at_current_speed")
						.style(ChatFormatting.DARK_GRAY))
				.forGoggles(tooltip, 1);
		return true;
	}

	@Override
	public void initialize() {
		super.initialize();

		if (!hasSource() || getGeneratedSpeed() > getTheoreticalSpeed())
		{
			//CreateNuclear.LOGGER.info("Init SPEED : " + getSpeed2() + "  pos : " + getBlockPos());
			FindController(getBlockPos(), Objects.requireNonNull(getLevel()));
		}
	}

	public void FindController(BlockPos pos, Level level){
		if (level.getBlockState(pos.above(3)).getBlock() == CNBlocks.REACTOR_CONTROLLER.get()){
            ReactorControllerBlock controller = (ReactorControllerBlock)level.getBlockState(pos.above(3)).getBlock();
			controller.Verify(controller.defaultBlockState(), pos.above(3), level, level.players(), false);
		}
	}

	public void setSpeed(int speed) {
		this.speed = speed;
		CreateNuclear.LOGGER.warn(speed + " ReactorOutputEntity " + this.speed);
	}


	public int getDir() {
        BlockState state = getBlockState();
        return state.getValue(DIR);
    }

	public void setDir(int dir, Level level, BlockPos pos) {
        BlockState state = getBlockState();
		level.setBlockAndUpdate(pos, state.setValue(DIR, dir));
    }

	@Override
	public float getGeneratedSpeed() {
		if (!CNBlocks.REACTOR_OUTPUT.has(getBlockState()))
			return 0;
		return speed; //convertToDirection(speed, getBlockState().getValue(ReactorOutput.FACING));
	}

	@Override
	protected Block getStressConfigKey() {
		return super.getStressConfigKey();
	}

	class ReactorOutputValue extends ValueBoxTransform.Sided {

		@Override
		protected Vec3 getSouthLocation() {
			return VecHelper.voxelSpace(8, 8, 12.5);
		}

		@Override
		public Vec3 getLocalOffset(BlockState state) {
			Direction facing = state.getValue(ReactorOutput.FACING);
			return super.getLocalOffset(state).add(Vec3.atLowerCornerOf(facing.getNormal())
				.scale(-1 / 16f));
		}



		@Override
		protected boolean isSideActive(BlockState state, Direction direction) {
			Direction facing = state.getValue(ReactorOutput.FACING);
			if (facing.getAxis() != Direction.Axis.Y && direction == Direction.DOWN)
				return false;
			return direction.getAxis() != facing.getAxis();
		}

	}
}


