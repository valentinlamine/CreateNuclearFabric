package net.ynov.createnuclear.multiblock;

import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.ynov.createnuclear.block.CNBlocks;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
public class ReactorShape {
    private final ReactorType reactorType;

    private final BoundingBox fullBoundingBox;
    private final BoundingBox core;
    private final BoundingBox innerFrontPlane;
    private final BoundingBox innerRearPlane;
    private final BoundingBox innerTopPlane;
    private final BoundingBox innerBottomPlane;
    private final BoundingBox innerLeftPlane;
    private final BoundingBox innerRightPlane;

    private final BoundingBox frontTopBorder;
    private final BoundingBox frontBottomBorder;
    private final BoundingBox leftTopBorder;
    private final BoundingBox leftBottomBorder;
    private final BoundingBox rightTopBorder;
    private final BoundingBox rightBottomBorder;
    private final BoundingBox rearTopBorder;
    private final BoundingBox rearBottomBorder;
    private final BoundingBox frontLeftCornerBorder;
    private final BoundingBox frontRightCornerBorder;
    private final BoundingBox rearLeftCornerBorder;
    private final BoundingBox rearRightCornerBorder;

    public ReactorShape(BlockPos pos, ReactorType reactorType, Level level) {
        this.reactorType = reactorType;

        Direction facing = level.getBlockState(pos).getValue(BlockStateProperties.HORIZONTAL_FACING);
        Direction oppositeFacing = facing.getOpposite();
        Direction rightFacing = facing.getCounterClockWise();
        Direction leftFacing = facing.getClockWise();

        BlockPos coreBottom = BlockPos.of(pos.offset(2, oppositeFacing));
        BlockPos coreTop = BlockPos.of(coreBottom.offset(2, Direction.UP));
        core = fromCorners(coreBottom, coreTop);

        BlockPos frontTopRight = BlockPos.of(BlockPos.of(pos.offset(3, Direction.UP)).offset(2, rightFacing));
        BlockPos frontTopLeft = BlockPos.of(BlockPos.of(pos.offset(3, Direction.UP)).offset(2, leftFacing));
        BlockPos frontBottomRight = BlockPos.of(BlockPos.of(pos.offset(1, Direction.DOWN)).offset(2, rightFacing));
        BlockPos frontBottomLeft = BlockPos.of(BlockPos.of(pos.offset(1, Direction.DOWN)).offset(2, leftFacing));

        BlockPos rearTopRight = BlockPos.of(BlockPos.of(BlockPos.of(pos.offset(3, Direction.UP)).offset(4, rightFacing)).offset(2, rightFacing));
        BlockPos rearTopLeft = BlockPos.of(BlockPos.of(BlockPos.of(pos.offset(3, Direction.UP)).offset(4, leftFacing)).offset(2, leftFacing));
        BlockPos rearBottomRight = BlockPos.of(BlockPos.of(BlockPos.of(pos.offset(1, Direction.UP)).offset(4, rightFacing)).offset(2, rightFacing));
        BlockPos rearBottomLeft = BlockPos.of(BlockPos.of(BlockPos.of(pos.offset(1, Direction.UP)).offset(4, leftFacing)).offset(2, leftFacing));

        this.fullBoundingBox = fromCorners(frontTopLeft, rearBottomRight);
        this.frontTopBorder = fromCorners(frontTopRight, frontTopLeft);
        this.frontBottomBorder = fromCorners(frontBottomRight, frontBottomLeft);
        this.leftTopBorder = fromCorners(frontTopLeft, rearTopLeft);
        this.leftBottomBorder = fromCorners(frontBottomLeft, rearBottomLeft);
        this.rightTopBorder = fromCorners(frontTopRight, rearTopRight);
        this.rightBottomBorder = fromCorners(frontBottomRight, rearBottomRight);
        this.rearTopBorder = fromCorners(rearTopRight, rearTopLeft);
        this.rearBottomBorder = fromCorners(rearBottomRight, rearBottomLeft);
        this.frontLeftCornerBorder = fromCorners(frontTopLeft, frontBottomLeft);
        this.frontRightCornerBorder = fromCorners(frontTopRight, frontBottomRight);
        this.rearLeftCornerBorder = fromCorners(rearTopLeft, rearBottomLeft);
        this.rearRightCornerBorder = fromCorners(rearTopRight, rearBottomRight);

        BlockPos innerFrontTopClockwise = BlockPos.of(BlockPos.of(BlockPos.of(pos.offset(3, Direction.UP)).offset(1, oppositeFacing)).offset(1, rightFacing));
        BlockPos innerRearTopCounterClockwise = BlockPos.of(BlockPos.of(BlockPos.of(pos.offset(3, Direction.UP)).offset(3, oppositeFacing)).offset(2, leftFacing));
        innerTopPlane = fromCorners(innerFrontTopClockwise, innerRearTopCounterClockwise);

        BlockPos innerFrontBottomRight = BlockPos.of(BlockPos.of(BlockPos.of(pos.offset(1, Direction.DOWN)).offset(1, oppositeFacing)).offset(1, rightFacing));
        BlockPos innerRearBottomLeft = BlockPos.of(BlockPos.of(BlockPos.of(pos.offset(1, Direction.DOWN)).offset(3, oppositeFacing)).offset(1, leftFacing));
        innerBottomPlane = fromCorners(innerFrontBottomRight, innerRearBottomLeft);

        BlockPos innerLeftFrontTop = BlockPos.of(BlockPos.of(BlockPos.of(pos.offset(2, Direction.UP)).offset(1, oppositeFacing)).offset(2, leftFacing));
        BlockPos innerLeftRearBottom = BlockPos.of(BlockPos.of(pos.offset(3, oppositeFacing)).offset(2, leftFacing));
        innerLeftPlane = fromCorners(innerLeftFrontTop, innerLeftRearBottom);

        BlockPos innerRightFrontTop = BlockPos.of(BlockPos.of(BlockPos.of(pos.offset(2, Direction.UP)).offset(1, oppositeFacing)).offset(2, rightFacing));
        BlockPos innerRightRearBottom = BlockPos.of(BlockPos.of(pos.offset(3, oppositeFacing)).offset(2, rightFacing));
        innerRightPlane = fromCorners(innerRightFrontTop, innerRightRearBottom);

        BlockPos innerFrontLeftTop = BlockPos.of(BlockPos.of(pos.offset(2, Direction.UP)).offset(1, leftFacing));
        BlockPos innerFrontRightBottom = BlockPos.of(pos.offset(1, rightFacing));
        innerFrontPlane = fromCorners(innerFrontLeftTop, innerFrontRightBottom);

        BlockPos innerRearLeftTop = BlockPos.of(BlockPos.of(BlockPos.of(pos.offset(2, Direction.UP)).offset(4, oppositeFacing)).offset(1, leftFacing));
        BlockPos innerRearRightBottom = BlockPos.of(BlockPos.of(pos.offset(4, oppositeFacing)).offset(1, rightFacing));
        innerRearPlane = fromCorners(innerRearLeftTop, innerRearRightBottom);
    }

    public Map<BoundingBox, List<BlockEntry<Block>>> createShapeMap() {
        Map<BoundingBox, List<BlockEntry<Block>>> reactorShapeMap = new HashMap<>();

        BlockEntry<Block> coreComponent = switch (this.reactorType) {
            case FUSION -> CNBlocks.REINFORCED_GLASS;
            case FISSION -> CNBlocks.ENRICHED_SOUL_SOIL;
        };
        BlockEntry<Block> conrollerComponent = switch (reactorType) {
            case FUSION, FISSION -> CNBlocks.RAW_URANIUM_BLOCK;
        };

        reactorShapeMap.put(core, List.of(coreComponent));




        return reactorShapeMap;
    }

    private static BoundingBox fromCorners(BlockPos start, BlockPos end) {
        return BoundingBox.fromCorners(blockPosToVec3i(start), blockPosToVec3i(end));
    }
    private static Vec3i blockPosToVec3i(BlockPos pos) {
        return new Vec3i(pos.getX(), pos.getY(), pos.getZ());
    }
}*/
