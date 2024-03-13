package net.ynov.createnuclear.block;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.ynov.createnuclear.CNMultiblock;
import net.ynov.createnuclear.CreateNuclear;
import net.ynov.createnuclear.energy.ReactorOutput;
import net.ynov.createnuclear.energy.ReactorOutputEntity;

import java.util.Objects;

public class ReactorController extends Block {
    Level level;

    public ReactorController(Properties properties) {
        super(properties);
    }

    public void Verify(BlockPos pos, Level level){
            boolean structureFound = false;

            var result = CNMultiblock.REGISTRATE_MULTIBLOCK.findStructure(level, pos);
            if (result != null) {
                CreateNuclear.LOGGER.info("structure Verified, multiblock created");
                structureFound = true;
                ReactorOutputEntity.structure = true;
                if (level.getBlockState(pos.below(3)).is(CNBlocks.REACTOR_OUTPUT.get())){
                    ReactorOutput block = (ReactorOutput) level.getBlockState(pos.below(3)).getBlock();
                    Objects.requireNonNull(block.getBlockEntityType().getBlockEntity(level, pos.below(3))).speed = 16;
                    Objects.requireNonNull(block.getBlockEntityType().getBlockEntity(level, pos.below(3))).updateSpeed = true;
                    Objects.requireNonNull(block.getBlockEntityType().getBlockEntity(level, pos.below(3))).updateGeneratedRotation();
                }
            }

            if (!structureFound) {
                CreateNuclear.LOGGER.info("structure not verified, FAILED to create multiblock");
                ReactorOutputEntity.structure = false;
                if (level.getBlockState(pos.below(3)).is(CNBlocks.REACTOR_OUTPUT.get())){
                    ReactorOutput block = (ReactorOutput) level.getBlockState(pos.below(3)).getBlock();
                    Objects.requireNonNull(block.getBlockEntityType().getBlockEntity(level, pos.below(3))).speed = 0;
                    Objects.requireNonNull(block.getBlockEntityType().getBlockEntity(level, pos.below(3))).updateSpeed = true;
                    Objects.requireNonNull(block.getBlockEntityType().getBlockEntity(level, pos.below(3))).updateGeneratedRotation();
                }
            }
    }

}
