package net.ynov.createnuclear.multiblock;

import java.util.List;

import com.simibubi.create.content.kinetics.motor.CreativeMotorBlock;
import com.simibubi.create.content.kinetics.motor.KineticScrollValueBehaviour;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueBehaviour;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.VecHelper;
import net.ynov.createnuclear.block.CNBlocks;

public class ReactorOutputEntity extends GeneratingKineticBlockEntity {

	public static final int DEFAULT_SPEED = 64;

	public ReactorOutputEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	/*@Override
	public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
		super.addBehaviours(behaviours);
		int max = MAX_SPEED;
		generatedSpeed = new KineticScrollValueBehaviour(Lang.translateDirect("kinetics.reactor_output.rotation_speed"),
			this, new net.ynov.createnuclear.multiblock.ReactorOutputEntity.MotorValueBox());
		generatedSpeed.between(-max, max);
		generatedSpeed.value = DEFAULT_SPEED;
		generatedSpeed.withCallback(i -> this.updateGeneratedRotation());
		behaviours.add(generatedSpeed);
	}*/

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
		return convertToDirection(DEFAULT_SPEED, getBlockState().getValue(ReactorOutput.FACING));
	}
}


