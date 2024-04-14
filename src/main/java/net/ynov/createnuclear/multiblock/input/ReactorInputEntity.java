package net.ynov.createnuclear.multiblock.input;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import io.github.fabricators_of_create.porting_lib.transfer.item.ItemStackHandler;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SidedStorageBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.ynov.createnuclear.multiblock.controller.ReactorControllerBlockEntity;
import org.jetbrains.annotations.Nullable;
import net.ynov.createnuclear.multiblock.controller.ReactorControllerBlockEntity.ReactorControllerInventory;

import java.util.List;

public class ReactorInputEntity extends SmartBlockEntity implements SidedStorageBlockEntity {
    protected BlockPos controller;

    protected ReactorControllerInventory inventory;

    public ReactorInputEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        setController(pos);
    }

    public class ReactorInputInventory extends ItemStackHandler {
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

    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) { }

    @Override
    protected void read(CompoundTag tag, boolean clientPacket) {
        BlockPos controllerBlock = getController();
        BlockEntity blockEntity = level.getBlockEntity(controllerBlock);
        if (blockEntity instanceof ReactorControllerBlockEntity controllerBlockEntity) {
            controllerBlockEntity.inventory.deserializeNBT(tag.merge(controllerBlockEntity.inventory.serializeNBT()));

        }
        super.read(tag, clientPacket);
    }

    @Override
    protected void write(CompoundTag tag, boolean clientPacket) {
        BlockPos controllerBlock = getController();
        BlockEntity blockEntity = level.getBlockEntity(controllerBlock);
        if (blockEntity instanceof ReactorControllerBlockEntity controllerBlockEntity) {
            tag.merge(controllerBlockEntity.inventory.serializeNBT());
        }
        super.write(tag, clientPacket);
    }

    @Override
    public Storage<ItemVariant> getItemStorage(@Nullable Direction face) {
        BlockPos controllerBlock = getController();
        BlockEntity blockEntity = level.getBlockEntity(controllerBlock);
        if (blockEntity instanceof ReactorControllerBlockEntity controllerBlockEntity) {
            inventory = controllerBlockEntity.inventory;
        }
        return inventory;
    }

    public void setController(BlockPos pos) {
        controller = new BlockPos(pos.getX(), pos.getY(), pos.getZ()+4);
    }

    public BlockPos getController() {
        return controller;
    }

}
