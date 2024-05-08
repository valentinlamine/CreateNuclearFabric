package net.ynov.createnuclear.multiblock.controller;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.IInteractionChecker;
import io.github.fabricators_of_create.porting_lib.transfer.item.ItemStackHandler;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SidedStorageBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.ynov.createnuclear.CreateNuclear;
import net.ynov.createnuclear.gui.CNIconButton;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static net.ynov.createnuclear.multiblock.controller.ReactorControllerBlock.ASSEMBLED;

public class ReactorControllerBlockEntity extends SmartBlockEntity implements MenuProvider, IInteractionChecker, SidedStorageBlockEntity {
    public boolean destroyed = false;
    public boolean created = false;
    public int speed = 16; // This is the result speed of the reactor, change this to change the total capacity
    public ReactorControllerBlock controller;

    public ReactorControllerInventory inventory;
    public ReactorControllerScreen screen;


    public class ReactorControllerInventory extends ItemStackHandler {
        public ReactorControllerInventory() {
            super(2);
        }
        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            setChanged();
        }
    }
    public ReactorControllerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        inventory = new ReactorControllerInventory();
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {

    }

    public boolean getAssembled() { // permet de savoir si le réacteur est formé ou pas.
        BlockState state = getBlockState();
        return Boolean.TRUE.equals(state.getValue(ASSEMBLED));
    }

    @Override
    public Component getDisplayName() {
        return Components.translatable("gui.createnuclear.reactor_controller.title");
    }
    //(Si les methode read et write ne sont pas implémenté alors lorsque l'on relance le monde minecraft les items dans le composant auront disparu !)
    @Override
    protected void read(CompoundTag compound, boolean clientPacket) { //Permet de stocker les item 1/2
        inventory.deserializeNBT(compound.getCompound("Inventory"));
        super.read(compound, clientPacket);
    }

    @Override
    protected void write(CompoundTag compound, boolean clientPacket) { //Permet de stocker les item 2/2
        compound.put("Inventory", inventory.serializeNBT());
        super.write(compound, clientPacket);
    }

    @Nullable
    @Override
    public Storage<ItemVariant> getItemStorage(@Nullable Direction face) {
        return inventory;
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        return ReactorControllerMenu.create(id, inv, this);
    }

    public Boolean isPowered() {
        BlockState blockState = getBlockState();
        if (blockState.getBlock() instanceof ReactorControllerBlock)
            return ((ReactorControllerBlock) blockState.getBlock()).isPowered();
        return null;
    }
    public void setPowered(boolean power) {
        BlockState blockState = getBlockState();
        if (blockState.getBlock() instanceof ReactorControllerBlock)
            ((ReactorControllerBlock) blockState.getBlock()).setPowered(power);
    }

    public void setSwitchButtons(List<CNIconButton> switchButtons) {
        BlockState blockState = getBlockState();
        if (blockState.getBlock() instanceof ReactorControllerBlock)
            ((ReactorControllerBlock) blockState.getBlock()).setSwitchButtons(switchButtons);
    }
    public List<CNIconButton> getSwitchButtons() {
        BlockState blockState = getBlockState();
        if (blockState.getBlock() instanceof ReactorControllerBlock)
            return ((ReactorControllerBlock) blockState.getBlock()).getSwitchButtons();
        return null;
    }

//    @Override
//    public boolean canPlayerUse(Player player) {
//        if (level == null || level.getBlockEntity(worldPosition) != this) {
//            return false;
//        }
//        return player.distanceToSqr(worldPosition.getX() + 0.5D, worldPosition.getY() + 0.5D,
//                worldPosition.getZ() + 0.5D) <= 64.0D;
//    }
}
