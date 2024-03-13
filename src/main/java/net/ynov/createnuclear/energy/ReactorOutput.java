package net.ynov.createnuclear.energy;

import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;

import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Vec3i;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.ynov.createnuclear.CreateNuclear;
import net.ynov.createnuclear.block.CNBlocks;
import net.ynov.createnuclear.block.ReactorBlock;
import net.ynov.createnuclear.block.ReactorController;
import net.ynov.createnuclear.blockentity.CNEntityTypes;
import net.ynov.createnuclear.shape.CNShapes;

public class ReactorOutput extends DirectionalKineticBlock implements IBE<ReactorOutputEntity> {
	public boolean structureBlock;

	public ReactorOutput(Properties properties) {
		super(properties);
	}

	@Override
	public void onPlace(BlockState state, Level worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
		super.onPlace(state, worldIn, pos, oldState, isMoving);
		FindController(pos, worldIn);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return CNShapes.REACTOR_OUTPUT.get(state.getValue(FACING));
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Direction preferred = getPreferredFacing(context);
		if ((context.getPlayer() != null && context.getPlayer()
			.isShiftKeyDown()) || preferred == null)
			return super.getStateForPlacement(context);
		return defaultBlockState().setValue(FACING, preferred);
	}

	// IRotate:

	@Override
	public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
		return face == state.getValue(FACING);
	}

	@Override
	public Axis getRotationAxis(BlockState state) {
		return state.getValue(FACING)
			.getAxis();
	}

	@Override
	public boolean hideStressImpact() {
		return true;
	}

	@Override
	public boolean isPathfindable(BlockState state, BlockGetter reader, BlockPos pos, PathComputationType type) {
		return false;
	}

	@Override
	public Class<ReactorOutputEntity> getBlockEntityClass() {
		return ReactorOutputEntity.class;
	}

	@Override
	public BlockEntityType<? extends ReactorOutputEntity> getBlockEntityType() {
		return CNEntityTypes.MOTOR2.get();
	}

	public void FindController(BlockPos blockPos, Level level){
        BlockPos newBlock;
        Vec3i pos = new Vec3i(blockPos.getX(), blockPos.getY(), blockPos.getZ());
        CreateNuclear.LOGGER.info("blockPos: " +pos.toString());
        for (int y = pos.getY()-3; y != pos.getY()+4; y+=1) {
            for (int x = pos.getX()-5; x != pos.getX()+5; x+=1) {
                for (int z = pos.getZ()-5; z != pos.getZ()+5; z+=1) {
                    newBlock = new BlockPos(x, y, z);
                    if (level.getBlockState(newBlock).is(CNBlocks.REACTOR_CONTROLLER.get())) {
                        CreateNuclear.LOGGER.info("ReactorController FOUND!!!!!!!!!!: ");
                        ReactorController controller = (ReactorController) level.getBlockState(newBlock).getBlock();
                        controller.Verify(newBlock, level);
                        return;
                    }
                    //else CreateNuclear.LOGGER.info("newBlock: " + level.getBlockState(newBlock).getBlock());
                }
            }
        }
    }

}

