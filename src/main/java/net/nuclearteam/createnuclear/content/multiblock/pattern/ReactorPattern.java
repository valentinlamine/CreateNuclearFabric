package net.nuclearteam.createnuclear.content.multiblock.pattern;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.Level;
import net.nuclearteam.createnuclear.CNBlocks;
import net.nuclearteam.createnuclear.content.multiblock.ReactorAssembler;
import net.nuclearteam.createnuclear.content.multiblock.controller.ReactorControllerBlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class ReactorPattern {

    private static final Predicate<BlockInWorld> blockInWorldAPredicate = state ->
        stateIs(CNBlocks.REACTOR_CASING.get()).test(state)
                || stateIs(CNBlocks.REACTOR_OUTPUT.get()).test(state)
                || stateIs(CNBlocks.REACTOR_ROD_INPUT.get()).test(state)
                || stateIs(CNBlocks.REACTOR_FLUID_INPUT.get()).test(state)
                || stateIs(CNBlocks.REACTOR_ALARM.get()).test(state)
    ;

    private static Predicate<BlockInWorld> stateIs(Block block) {
        return a -> {
            BlockState state = a.getState();
            return state != null && state.is(block);
        };
    }

    @FunctionalInterface
    private interface ControllerVisitor {
        boolean visit(BlockPos controllerPos, ReactorControllerBlockEntity entity);
    }

    private void scanControllerCandidates(BlockPos blockPos, Level level, ControllerVisitor visitor) {
        BlockPos newBlock;
        Vec3i pos = new Vec3i(blockPos.getX(), blockPos.getY(), blockPos.getZ());
        for (int y = pos.getY()-5; y != pos.getY()+6; y+=1) {
            for (int x = pos.getX()-9; x != pos.getX()+10; x+=1) {
                for (int z = pos.getZ()-9; z != pos.getZ()+10; z+=1) {
                    newBlock = new BlockPos(x, y, z);
                    if (level.getBlockState(newBlock).is(CNBlocks.REACTOR_CONTROLLER.get())
                            && level.getBlockEntity(newBlock) instanceof ReactorControllerBlockEntity entity) {
                        if (visitor.visit(newBlock, entity)) {
                            return;
                        }
                    }
                }
            }
        }
    }

    public void findController(BlockPos blockPos, Level level, boolean first){
        scanControllerCandidates(blockPos, level, ((controllerPos, entity) -> {
            boolean inRange = isInReactorRange(entity.getMultiblockPos(), blockPos);
            if (first) {
                if (inRange || !entity.isAssembled()) {
                    ReactorAssembler.assemble(controllerPos, level);
                }
            } else if (inRange) {
                ReactorAssembler.disassemble(controllerPos, level);
            }

            return false;
        }));
    }

    public BlockPos findControllerPos(BlockPos blockPos, Level level, boolean first){
        BlockPos[] found = {null};
        scanControllerCandidates(blockPos, level, ((controllerPos, entity) -> {
            boolean inRange = isInReactorRange(entity.getMultiblockPos(), blockPos);
            if (first) {
                if (inRange || !entity.isAssembled()) {
                    ReactorAssembler.assemble(controllerPos, level);
                }
            } else if (inRange) {
                ReactorAssembler.disassemble(controllerPos, level);
            }

            if (isInReactorRange(entity.getMultiblockPos(), blockPos)) {
                found[0] = controllerPos;
                return true;
            }

            return false;
        }));

        return found[0];
    }

    public BlockPos findControllerPos(BlockPos blockPos, Level level){
        BlockPos[] found = {null};
        scanControllerCandidates(blockPos, level, ((controllerPos, entity) -> {
            ReactorAssembler.assemble(controllerPos, level);
            if (isInReactorRange(entity.getMultiblockPos(), blockPos)) {
                found[0] = controllerPos;
                return true;
            }

            return false;
        }));

        return found[0];
    }

    public boolean isInReactorRange(@Nullable BoundingBox reactorPos, BlockPos blockPos) {
        return reactorPos != null && reactorPos.isInside(blockPos);
    }
}
