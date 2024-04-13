package net.ynov.createnuclear.multiblock.frame;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import dev.architectury.event.events.common.TickEvent;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SidedStorageBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.ynov.createnuclear.CreateNuclear;
import net.ynov.createnuclear.block.CNBlocks;
import net.ynov.createnuclear.multiblock.controller.ReactorControllerBlock;
import net.ynov.createnuclear.multiblock.controller.ReactorControllerBlockEntity;
import net.ynov.createnuclear.multiblock.controller.ReactorControllerBlockEntity.ReactorControllerInventory;
import org.jetbrains.annotations.Nullable;


import java.util.List;

public class ReactorBlockEntity extends SmartBlockEntity implements SidedStorageBlockEntity {
    protected BlockPos controller;
    protected ReactorControllerInventory inventory;

    public ReactorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);

        setController(FindController(new BlockPos(0,0,0), level));
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {

    }

    public void setController(BlockPos pos) {
        controller = FindController(pos, level);
    }
    public BlockPos getController() {
        return controller;
    }

    public BlockPos FindController(BlockPos pos, Level level) {
        BlockPos newBlock;
        CompoundTag t = new CompoundTag();
        CreateNuclear.LOGGER.warn("gf " + pos + " " + level + " " + t.get("controller"));
        return null;
/*
        if (level == null || pos == null) return null;
        else {*/

            /*if (pos != null) {
                Vec3i Vpos = new Vec3i(pos.getX(), pos.getY(), pos.getZ());
                for (int y = Vpos.getY()-3; y != Vpos.getY()+4 ; y+=1) {
                    for (int x = Vpos.getX()-5; x != Vpos.getX()+5 ; x+=1) {
                        for (int z = Vpos.getZ(); z != pos.getZ()+5 ; z+=1) {
                            newBlock = new BlockPos(x, y, z);
                            if (level.getBlockState(newBlock).is(CNBlocks.REACTOR_CONTROLLER.get())) {
                                ReactorControllerBlock controller = (ReactorControllerBlock) level.getBlockState(newBlock).getBlock();
                                controller.Verify(controller.defaultBlockState(), newBlock, level, level.players(), true);
                                ReactorControllerBlockEntity entity = controller.getBlockEntity(level, newBlock);
                                if (entity.created) {
                                    return newBlock;
                                }
                            }

                        }
                    }
                }
            }
            */
            /*return null;
        }*/
    }

    @Override
    protected void read(CompoundTag tag, boolean clientPacket) {
       /* BlockPos controllerBlock = getController();
        assert level != null;
        BlockEntity blockEntity = level.getBlockEntity(controllerBlock);
        if (blockEntity instanceof ReactorControllerBlockEntity controllerBlockEntity) {
            controllerBlockEntity.inventory.deserializeNBT(tag.merge(controllerBlockEntity.inventory.serializeNBT()));

        }*/
        super.read(tag, clientPacket);
    }

    @Override
    protected void write(CompoundTag tag, boolean clientPacket) {
        /*BlockPos controllerBlock = getController();
        assert level != null;
        BlockEntity blockEntity = level.getBlockEntity(controllerBlock);
        if (blockEntity instanceof ReactorControllerBlockEntity controllerBlockEntity) {
            tag.merge(controllerBlockEntity.inventory.serializeNBT());
        }*/
        super.write(tag, clientPacket);
    }

    @Override
    public Storage<ItemVariant> getItemStorage(@Nullable Direction face) {
        /*BlockPos controllerBlock = getController();
        assert level != null;
        BlockEntity blockEntity = level.getBlockEntity(controllerBlock);
        if (blockEntity instanceof ReactorControllerBlockEntity controllerBlockEntity) {
            inventory = controllerBlockEntity.inventory;
        }*/
        return inventory;
    }
}
