package net.ynov.createnuclear.multiblock.frame;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import io.github.fabricators_of_create.porting_lib.transfer.item.ItemStackHandler;
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
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ReactorBlockEntity extends SmartBlockEntity implements SidedStorageBlockEntity {

    protected BlockPos controller;

    protected ReactorControllerBlockEntity.ReactorControllerInventory inventory;

    public ReactorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        setController(pos);
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
        controller = FindController(pos, level, level.players(), true);
                //new BlockPos(pos.getX(), pos.getY(), pos.getZ()+4);
    }

    public BlockPos getController() {
        return controller;
    }

    public BlockPos FindController(BlockPos blockPos, Level level, List<? extends Player> players, boolean first){ // Function that checks the surrounding blocks in order
        BlockPos newBlock;                                                   // to find the controller and verify the pattern
        Vec3i pos = new Vec3i(blockPos.getX(), blockPos.getY(), blockPos.getZ());
        for (int y = pos.getY()-3; y != pos.getY()+4; y+=1) {
            for (int x = pos.getX()-5; x != pos.getX()+5; x+=1) {
                for (int z = pos.getZ()-5; z != pos.getZ()+5; z+=1) {
                    newBlock = new BlockPos(x, y, z);
                    if (level.getBlockState(newBlock).is(CNBlocks.REACTOR_CONTROLLER.get())) { // verifying the pattern
                        CreateNuclear.LOGGER.info("ReactorController FOUND!!!!!!!!!!: ");      // from the controller
                        return newBlock;
                        /*ReactorControllerBlock controller = (ReactorControllerBlock) level.getBlockState(newBlock).getBlock();
                        controller.Verify(controller.defaultBlockState(), newBlock, level, players, first);
                        ReactorControllerBlockEntity entity = controller.getBlockEntity(level, newBlock);
                        if (entity.created) {
                            return controller;
                        }*/
                    }
                    //else CreateNuclear.LOGGER.info("newBlock: " + level.getBlockState(newBlock).getBlock());
                }
            }
        }
        return null;
    }
}
