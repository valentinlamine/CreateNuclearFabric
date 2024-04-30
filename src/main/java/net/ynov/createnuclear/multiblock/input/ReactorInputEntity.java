package net.ynov.createnuclear.multiblock.input;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.Iterate;
import io.github.fabricators_of_create.porting_lib.transfer.item.ItemStackHandler;
import io.github.fabricators_of_create.porting_lib.util.StorageProvider;
import lib.multiblock.test.SimpleMultiBlockAislePatternBuilder;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SidedStorageBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.ynov.createnuclear.CreateNuclear;
import net.ynov.createnuclear.block.CNBlocks;
import net.ynov.createnuclear.multiblock.controller.ReactorControllerBlock;
import net.ynov.createnuclear.multiblock.controller.ReactorControllerBlockEntity;
import org.jetbrains.annotations.Nullable;
import net.ynov.createnuclear.multiblock.controller.ReactorControllerBlockEntity.ReactorControllerInventory;

import java.util.Arrays;
import java.util.List;

import static net.ynov.createnuclear.CNMultiblock.*;

public class ReactorInputEntity extends SmartBlockEntity implements SidedStorageBlockEntity {
    protected BlockPos block;
    ReactorControllerBlockEntity controller;

    protected ReactorControllerInventory inventory;
    public CompoundTag entityTag;

    public ReactorInputEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        setBlock(pos);
        entityTag = new CompoundTag();
    }

   @Override
    public void setLevel(Level level) {
        super.setLevel(level);

        BlockPos findController = FindController('I');
        if (level.isClientSide()){
            //CreateNuclear.LOGGER.warn("  " + stateInput.getBlock() + "  " + block + "  " + String.valueOf(stateInput.getBlock() == CNBlocks.REACTOR_CASING.get()) + "  " + level.getBlockEntity(block) + " " + worldPosition.relative(Direction.NORTH) + " " + level.getBlockState(worldPosition.relative(Direction.NORTH)));
            if (level.getBlockState(new BlockPos(block.getX(), block.getY(), block.getZ() + findController.getX())).is(CNBlocks.REACTOR_CONTROLLER.get())) { // NORTH
                BlockPos newBlock = new BlockPos(block.getX(), block.getY(), block.getZ() + findController.getX());
                ReactorControllerBlock controllerBlock = (ReactorControllerBlock) level.getBlockState(newBlock).getBlock();
                controllerBlock.Verify(controllerBlock.defaultBlockState(), newBlock, level, level.players(), false);
                ReactorControllerBlockEntity entity = controllerBlock.getBlockEntity(level, newBlock);
                CreateNuclear.LOGGER.warn("q23q NORTH " + entity + " " + entity.toString());

                if (entity.created || entity.getAssembled()) {
                    this.controller = entity;
                    this.inventory = entity.inventory;
                    entityTag.putString("Entity", entity.toString());

                    CreateNuclear.LOGGER.warn("q23q4 NORTH " + entity.inventory.serializeNBT());
                    //return;
                }
            } /*else if (level.getBlockState(new BlockPos(block.getX(), block.getY(), block.getZ() - findController.getX())).is(CNBlocks.REACTOR_CONTROLLER.get())) { // SOUTH
                BlockPos newBlock = new BlockPos(block.getX(), block.getY(), block.getZ() - findController.getX());
                ReactorControllerBlock controllerBlock = (ReactorControllerBlock) level.getBlockState(newBlock).getBlock();
                controllerBlock.Verify(controllerBlock.defaultBlockState(), newBlock, level, level.players(), false);
                ReactorControllerBlockEntity entity = controllerBlock.getBlockEntity(level, newBlock);
                CreateNuclear.LOGGER.warn("q23q SOUTH " + entity);

                if (entity.created || entity.getAssembled()) {
                    this.controller = entity;
                    return;
                }
            } else if (level.getBlockState(new BlockPos(block.getX() - findController.getX(), block.getY(), block.getZ())).is(CNBlocks.REACTOR_CONTROLLER.get())) { // EAST
                BlockPos newBlock = new BlockPos(block.getX() - findController.getX(), block.getY(), block.getZ());
                ReactorControllerBlock controllerBlock = (ReactorControllerBlock) level.getBlockState(newBlock).getBlock();
                controllerBlock.Verify(controllerBlock.defaultBlockState(), newBlock, level, level.players(), false);
                ReactorControllerBlockEntity entity = controllerBlock.getBlockEntity(level, newBlock);
                CreateNuclear.LOGGER.warn("q23q EAST " + entity);

                if (entity.created || entity.getAssembled()) {
                    this.controller = entity;
                    return;
                }
            } else if (level.getBlockState(new BlockPos(block.getX() + findController.getX(), block.getY(), block.getZ())).is(CNBlocks.REACTOR_CONTROLLER.get())) { // WEST
                BlockPos newBlock = new BlockPos(block.getX() + findController.getX(), block.getY(), block.getZ());
                ReactorControllerBlock controllerBlock = (ReactorControllerBlock) level.getBlockState(newBlock).getBlock();
                controllerBlock.Verify(controllerBlock.defaultBlockState(), newBlock, level, level.players(), false);
                ReactorControllerBlockEntity entity = controllerBlock.getBlockEntity(level, newBlock);
                CreateNuclear.LOGGER.warn("q23q WEST " + entity);

                if (entity.created || entity.getAssembled()) {
                    this.controller = entity;
                    return;
                }
            }*/
            CreateNuclear.LOGGER.warn("q23q5 " + controller + " " + String.valueOf(controller == null));

        }
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
        /*BlockEntity blockEntity = getController();
        if (blockEntity instanceof ReactorControllerBlockEntity controllerBlockEntity) {
            controllerBlockEntity.inventory.deserializeNBT(tag.merge(controllerBlockEntity.inventory.serializeNBT()));
        }*/
        super.read(tag, clientPacket);
    }

    @Override
    protected void write(CompoundTag tag, boolean clientPacket) {
       /* BlockEntity blockEntity = getController();
        if (blockEntity instanceof ReactorControllerBlockEntity controllerBlockEntity) {
            tag.merge(controllerBlockEntity.inventory.serializeNBT());
        }*/
        super.write(tag, clientPacket);
    }

    @Override
    public Storage<ItemVariant> getItemStorage(@Nullable Direction face) {
        CreateNuclear.LOGGER.warn("var " + this.controller + " inv " + inventory);
        /*if (blockEntity instanceof ReactorControllerBlockEntity controllerBlockEntity) {
        }*/
        //inventory = controller.inventory;
        return inventory;
        //StorageProvider.createForItems()
    }

    public void setBlock(BlockPos pos) {
        block = pos;
    }



    public ReactorControllerBlockEntity getController() {
        return controller;
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
