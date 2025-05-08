package net.nuclearteam.createnuclear.multiblock.casing;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SidedStorageBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.nuclearteam.createnuclear.multiblock.controller.ReactorControllerInventory;

import java.util.List;

public class ReactorCasingBlockEntity extends SmartBlockEntity implements SidedStorageBlockEntity {

    protected BlockPos controller;

    protected ReactorControllerInventory inventory;

    public ReactorCasingBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        setController(pos);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) { }


    public void setController(BlockPos pos) {
        controller = new BlockPos(pos.getX()+4, pos.getY(), pos.getZ()+4);
    }

    public BlockPos getController() {
        return controller;
    }



}
