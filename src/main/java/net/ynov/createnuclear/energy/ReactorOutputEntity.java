package net.ynov.createnuclear.energy;

import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.kinetics.motor.KineticScrollValueBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueBehaviour;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import net.minecraft.world.phys.Vec3;
import net.ynov.createnuclear.block.CNBlocks;
import net.ynov.createnuclear.tools.EnrichingCampfireBlock;
import net.ynov.createnuclear.tools.VerifiedStructureItem;

import java.util.List;

public class ReactorOutputEntity extends GeneratingKineticBlockEntity {

	public static int DEFAULT_SPEED = 0;
	protected ScrollValueBehaviour generatedSpeed;

	public ReactorOutputEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}
	@Override
	public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
		super.addBehaviours(behaviours);
		generatedSpeed = new KineticScrollValueBehaviour(Lang.translateDirect("kinetics.reactor_output.rotation_speed"),
			this, new net.ynov.createnuclear.energy.ReactorOutputEntity.MotorValueBox());
		generatedSpeed.value = VerifiedStructureItem.SPEED;
		generatedSpeed.withCallback(i -> this.updateGeneratedRotation());
		behaviours.add(generatedSpeed);
	}

	@Override
	public void initialize() {
		super.initialize();
		if (!hasSource() || getGeneratedSpeed() > getTheoreticalSpeed())
			updateGeneratedRotation();
	}

	@Override
	public float getGeneratedSpeed() {
		if (!CNBlocks.REACTOR_OUTPUT.has(getBlockState()))
			return 0;
		return convertToDirection(generatedSpeed.getValue(), getBlockState().getValue(ReactorOutput.FACING));
	}

	@Override
	protected Block getStressConfigKey() {
		return super.getStressConfigKey();
	}

	class MotorValueBox extends ValueBoxTransform.Sided {

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
		public void rotate(BlockState state, PoseStack ms) {
			super.rotate(state, ms);
			Direction facing = state.getValue(ReactorOutput.FACING);
			if (facing.getAxis() == Direction.Axis.Y)
				return;
			if (getSide() != Direction.UP)
				return;
			TransformStack.cast(ms)
				.rotateZ(-AngleHelper.horizontalAngle(facing) + 180);
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


