package net.nuclearteam.createnuclear.multiblock.input;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import lib.multiblock.test.SimpleMultiBlockAislePatternBuilder;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SidedStorageBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.nuclearteam.createnuclear.CreateNuclear;
import net.nuclearteam.createnuclear.block.CNBlocks;
import net.nuclearteam.createnuclear.multiblock.controller.ReactorControllerBlock;
import net.nuclearteam.createnuclear.multiblock.controller.ReactorControllerBlockEntity;
import net.nuclearteam.createnuclear.multiblock.controller.ReactorControllerInventory;

import org.jetbrains.annotations.Nullable;

import static net.nuclearteam.createnuclear.CNMultiblock.*;

import java.util.List;

public class ReactorInputEntity extends SmartBlockEntity implements SidedStorageBlockEntity {
    protected BlockPos block;
    protected ReactorControllerBlockEntity controller;

    protected ReactorControllerInventory inventory;

    public ReactorInputEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        setBlock(pos);
    }

     /*public class ReactorInputInventory extends ItemStackHandler {
        public ReactorInputInventory(int slot) {
            super(slot);
        }

        public ReactorInputInventory() {
            super(2);
        }

        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
        }

    }*/

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) { }

    @Override
    protected void read(CompoundTag tag, boolean clientPacket) {

        BlockPos findController = FindController('I');
        if (getLevel().isClientSide()){
            //CreateNuclear.LOGGER.warn("  " + stateInput.getBlock() + "  " + block + "  " + String.valueOf(stateInput.getBlock() == CNBlocks.REACTOR_CASING.get()) + "  " + level.getBlockEntity(block) + " " + worldPosition.relative(Direction.NORTH) + " " + level.getBlockState(worldPosition.relative(Direction.NORTH)));
            if (this.getLevel().getBlockState(new BlockPos(block.getX(), block.getY(), block.getZ() + findController.getX())).is(CNBlocks.REACTOR_CONTROLLER.get())) { // NORTH
                BlockPos newBlock = new BlockPos(block.getX(), block.getY(), block.getZ() + findController.getX());
                ReactorControllerBlock controllerBlock = (ReactorControllerBlock) level.getBlockState(newBlock).getBlock();
                controllerBlock.Verify(controllerBlock.defaultBlockState(), newBlock, level, level.players(), false);
                ReactorControllerBlockEntity entity = controllerBlock.getBlockEntity(level, newBlock);
                CreateNuclear.LOGGER.warn("read NORTH");

                if (entity.created || entity.getAssembled()) {
                    entity.inventory.deserializeNBT(tag.merge(entity.inventory.serializeNBT()));

                }
            } else if (level.getBlockState(new BlockPos(block.getX(), block.getY(), block.getZ() - findController.getX())).is(CNBlocks.REACTOR_CONTROLLER.get())) { // SOUTH
                BlockPos newBlock = new BlockPos(block.getX(), block.getY(), block.getZ() - findController.getX());
                ReactorControllerBlock controllerBlock = (ReactorControllerBlock) level.getBlockState(newBlock).getBlock();
                controllerBlock.Verify(controllerBlock.defaultBlockState(), newBlock, level, level.players(), false);
                ReactorControllerBlockEntity entity = controllerBlock.getBlockEntity(level, newBlock);
                CreateNuclear.LOGGER.warn("read SOUTH");

                if (entity.created || entity.getAssembled()) {
                    entity.inventory.deserializeNBT(tag.merge(entity.inventory.serializeNBT()));

                }
            } else if (level.getBlockState(new BlockPos(block.getX() - findController.getX(), block.getY(), block.getZ())).is(CNBlocks.REACTOR_CONTROLLER.get())) { // EAST
                BlockPos newBlock = new BlockPos(block.getX() - findController.getX(), block.getY(), block.getZ());
                ReactorControllerBlock controllerBlock = (ReactorControllerBlock) level.getBlockState(newBlock).getBlock();
                controllerBlock.Verify(controllerBlock.defaultBlockState(), newBlock, level, level.players(), false);
                ReactorControllerBlockEntity entity = controllerBlock.getBlockEntity(level, newBlock);
                CreateNuclear.LOGGER.warn("read EAST");

                if (entity.created || entity.getAssembled()) {
                    entity.inventory.deserializeNBT(tag.merge(entity.inventory.serializeNBT()));
                }
            } else if (level.getBlockState(new BlockPos(block.getX() + findController.getX(), block.getY(), block.getZ())).is(CNBlocks.REACTOR_CONTROLLER.get())) { // WEST
                BlockPos newBlock = new BlockPos(block.getX() + findController.getX(), block.getY(), block.getZ());
                ReactorControllerBlock controllerBlock = (ReactorControllerBlock) level.getBlockState(newBlock).getBlock();
                controllerBlock.Verify(controllerBlock.defaultBlockState(), newBlock, level, level.players(), false);
                ReactorControllerBlockEntity entity = controllerBlock.getBlockEntity(level, newBlock);
                CreateNuclear.LOGGER.warn("read WEST");

                if (entity.created || entity.getAssembled()) {
                    entity.inventory.deserializeNBT(tag.merge(entity.inventory.serializeNBT()));
                }
            }
            CreateNuclear.LOGGER.warn("read q23q5");

        }
        super.read(tag, clientPacket);
    }

    @Override
    protected void write(CompoundTag tag, boolean clientPacket) {
        BlockPos findController = FindController('I');
        if (this.getLevel().isClientSide()){
            if (this.getLevel().getBlockState(new BlockPos(block.getX(), block.getY(), block.getZ() + findController.getX())).is(CNBlocks.REACTOR_CONTROLLER.get())) { // NORTH
                BlockPos newBlock = new BlockPos(block.getX(), block.getY(), block.getZ() + findController.getX());
                ReactorControllerBlock controllerBlock = (ReactorControllerBlock) level.getBlockState(newBlock).getBlock();
                controllerBlock.Verify(controllerBlock.defaultBlockState(), newBlock, level, level.players(), false);
                ReactorControllerBlockEntity entity = controllerBlock.getBlockEntity(level, newBlock);
                CreateNuclear.LOGGER.warn("write NORTH");

                if (entity.created || entity.getAssembled()) {
                    tag.merge(entity.inventory.serializeNBT());
                }
            } else if (level.getBlockState(new BlockPos(block.getX(), block.getY(), block.getZ() - findController.getX())).is(CNBlocks.REACTOR_CONTROLLER.get())) { // SOUTH
                BlockPos newBlock = new BlockPos(block.getX(), block.getY(), block.getZ() - findController.getX());
                ReactorControllerBlock controllerBlock = (ReactorControllerBlock) level.getBlockState(newBlock).getBlock();
                controllerBlock.Verify(controllerBlock.defaultBlockState(), newBlock, level, level.players(), false);
                ReactorControllerBlockEntity entity = controllerBlock.getBlockEntity(level, newBlock);
                CreateNuclear.LOGGER.warn("write SOUTH");

                if (entity.created || entity.getAssembled()) {
                    tag.merge(entity.inventory.serializeNBT());
                }
            } else if (level.getBlockState(new BlockPos(block.getX() - findController.getX(), block.getY(), block.getZ())).is(CNBlocks.REACTOR_CONTROLLER.get())) { // EAST
                BlockPos newBlock = new BlockPos(block.getX() - findController.getX(), block.getY(), block.getZ());
                ReactorControllerBlock controllerBlock = (ReactorControllerBlock) level.getBlockState(newBlock).getBlock();
                controllerBlock.Verify(controllerBlock.defaultBlockState(), newBlock, level, level.players(), false);
                ReactorControllerBlockEntity entity = controllerBlock.getBlockEntity(level, newBlock);
                CreateNuclear.LOGGER.warn("write EAST");

                if (entity.created || entity.getAssembled()) {
                    tag.merge(entity.inventory.serializeNBT());
                }
            } else if (level.getBlockState(new BlockPos(block.getX() + findController.getX(), block.getY(), block.getZ())).is(CNBlocks.REACTOR_CONTROLLER.get())) { // WEST
                BlockPos newBlock = new BlockPos(block.getX() + findController.getX(), block.getY(), block.getZ());
                ReactorControllerBlock controllerBlock = (ReactorControllerBlock) level.getBlockState(newBlock).getBlock();
                controllerBlock.Verify(controllerBlock.defaultBlockState(), newBlock, level, level.players(), false);
                ReactorControllerBlockEntity entity = controllerBlock.getBlockEntity(level, newBlock);
                CreateNuclear.LOGGER.warn("write WEST");

                if (entity.created || entity.getAssembled()) {
                    tag.merge(entity.inventory.serializeNBT());
                }
            }
            CreateNuclear.LOGGER.warn("write q23q5");
        }
        super.write(tag, clientPacket);
    }

    @Override
    public Storage<ItemVariant> getItemStorage(@Nullable Direction face) {
        BlockPos findController = FindController('I');
        if (!this.getLevel().isClientSide()){
            if (this.getLevel().getBlockState(new BlockPos(block.getX(), block.getY(), block.getZ() + findController.getX())).is(CNBlocks.REACTOR_CONTROLLER.get())) { // NORTH
                BlockPos newBlock = new BlockPos(block.getX(), block.getY(), block.getZ() + findController.getX());
                ReactorControllerBlock controllerBlock = (ReactorControllerBlock) level.getBlockState(newBlock).getBlock();
                controllerBlock.Verify(controllerBlock.defaultBlockState(), newBlock, level, level.players(), false);
                ReactorControllerBlockEntity entity = controllerBlock.getBlockEntity(level, newBlock);
                CreateNuclear.LOGGER.warn("getItemStorage NORTH");

                if (entity.created || entity.getAssembled()) {
                    return entity.inventory;
                }

            } else if (level.getBlockState(new BlockPos(block.getX(), block.getY(), block.getZ() - findController.getX())).is(CNBlocks.REACTOR_CONTROLLER.get())) { // SOUTH
                BlockPos newBlock = new BlockPos(block.getX(), block.getY(), block.getZ() - findController.getX());
                ReactorControllerBlock controllerBlock = (ReactorControllerBlock) level.getBlockState(newBlock).getBlock();
                controllerBlock.Verify(controllerBlock.defaultBlockState(), newBlock, level, level.players(), false);
                ReactorControllerBlockEntity entity = controllerBlock.getBlockEntity(level, newBlock);
                CreateNuclear.LOGGER.warn("getItemStorage SOUTH");

                if (entity.created || entity.getAssembled()) {
                    this.controller = entity;
                    return entity.inventory;
                }

            } else if (level.getBlockState(new BlockPos(block.getX() - findController.getX(), block.getY(), block.getZ())).is(CNBlocks.REACTOR_CONTROLLER.get())) { // EAST
                BlockPos newBlock = new BlockPos(block.getX() - findController.getX(), block.getY(), block.getZ());
                ReactorControllerBlock controllerBlock = (ReactorControllerBlock) level.getBlockState(newBlock).getBlock();
                controllerBlock.Verify(controllerBlock.defaultBlockState(), newBlock, level, level.players(), false);
                ReactorControllerBlockEntity entity = controllerBlock.getBlockEntity(level, newBlock);
                CreateNuclear.LOGGER.warn("getItemStorage EAST");

                if (entity.created || entity.getAssembled()) {
                    this.controller = entity;
                    return entity.inventory;
                }

            } else if (level.getBlockState(new BlockPos(block.getX() + findController.getX(), block.getY(), block.getZ())).is(CNBlocks.REACTOR_CONTROLLER.get())) { // WEST
                BlockPos newBlock = new BlockPos(block.getX() + findController.getX(), block.getY(), block.getZ());
                ReactorControllerBlock controllerBlock = (ReactorControllerBlock) level.getBlockState(newBlock).getBlock();
                controllerBlock.Verify(controllerBlock.defaultBlockState(), newBlock, level, level.players(), false);
                ReactorControllerBlockEntity entity = controllerBlock.getBlockEntity(level, newBlock);
                CreateNuclear.LOGGER.warn("getItemStorage WEST");

                if (entity.created || entity.getAssembled()) {
                    this.controller = entity;
                    return entity.inventory;
                }
            }
        }
        return inventory;
    }

    @Nullable
    @Override
    public Level getLevel() {
        return super.getLevel();
    }

    public void setBlock(BlockPos pos) {
        block = pos;
    }


    private static BlockPos FindController(char character) {
        return SimpleMultiBlockAislePatternBuilder.start()
                .aisle(AAAAA, AAAAA, AAAAA, AAAAA, AAAAA)
                .aisle(AABAA, ADADA, BACAB, ADADA, AABAA)
                .aisle(AABAA, ADADA, BACAB, ADADA, AABAA)
                .aisle(AAIAA, ADADA, BACAB, ADADA, AAAA)
                .aisle(AABAA, ADADA, BACAB, ADADA, AABAA)
                .aisle(AABAA, ADADA, BACAB, ADADA, AABAA)
                .aisle(AAAAA, AAAAA, AAAAA, AAAAA, AAOAA)
                .where('A', a -> a.getState().is(CNBlocks.REACTOR_CASING.get()))
                .where('B', a -> a.getState().is(CNBlocks.REACTOR_MAIN_FRAME.get()))
                .where('C', a -> a.getState().is(CNBlocks.REACTOR_CORE.get()))
                .where('D', a -> a.getState().is(CNBlocks.REACTOR_COOLING_FRAME.get()))
                .where('*', a -> a.getState().is(CNBlocks.REACTOR_CONTROLLER.get()))
                .where('O', a -> a.getState().is(CNBlocks.REACTOR_OUTPUT.get()))
                .where('I', a -> a.getState().is(CNBlocks.REACTOR_INPUT.get()))
                .getDistanceController(character);
    }

}
