package net.nuclearteam.createnuclear.multiblock.frame;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import lib.multiblock.test.SimpleMultiBlockAislePatternBuilder;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SidedStorageBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.nuclearteam.createnuclear.CreateNuclear;
import net.nuclearteam.createnuclear.block.CNBlocks;
import net.nuclearteam.createnuclear.multiblock.IHeat;
import net.nuclearteam.createnuclear.multiblock.controller.ReactorControllerBlockEntity;
import net.nuclearteam.createnuclear.multiblock.controller.ReactorControllerInventory;

import java.util.List;

import static net.nuclearteam.createnuclear.CNMultiblock.*;

public class ReactorBlockEntity extends SmartBlockEntity implements SidedStorageBlockEntity {

    protected BlockPos controller;

    protected ReactorControllerInventory inventory;

    public ReactorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
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
