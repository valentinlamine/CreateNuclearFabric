package net.ynov.createnuclear.multiblock.controller;

import com.simibubi.create.foundation.gui.menu.MenuBase;
import io.github.fabricators_of_create.porting_lib.transfer.item.SlotItemHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.ynov.createnuclear.item.CNItems;
import net.ynov.createnuclear.menu.CNMenus;
import org.jetbrains.annotations.NotNull;

public class ReactorControllerMenu extends MenuBase<ReactorControllerBlockEntity> {
    private Slot inputSlot;
    private Slot inputSlot2;
    public ReactorControllerMenu(MenuType<?> type, int id, Inventory inv, FriendlyByteBuf extraData) {
        super(type, id, inv, extraData);
    }

    public ReactorControllerMenu(MenuType<?> type, int id, Inventory inv, ReactorControllerBlockEntity be) {
        super(type, id, inv, be);
    }

    public static AbstractContainerMenu create(int id, Inventory inv, ReactorControllerBlockEntity be) {
        return new ReactorControllerMenu(CNMenus.REACTOR_CONTROLLER.get(), id, inv, be);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return null;
    }

    @Override
    protected ReactorControllerBlockEntity createOnClient(FriendlyByteBuf extraData) {
        ClientLevel world = Minecraft.getInstance().level;
        BlockEntity blockEntity = world.getBlockEntity(extraData.readBlockPos());
        if (blockEntity instanceof ReactorControllerBlockEntity reactorControllerBlockEntity) {
            reactorControllerBlockEntity.readClient(extraData.readNbt());
            return reactorControllerBlockEntity;
        }
        return null;
    }

    @Override
    protected void initAndReadInventory(ReactorControllerBlockEntity contentHolder) {

    }

    @Override
    protected void addSlots() {

        inputSlot = new SlotItemHandler(contentHolder.inventory, 0, 8, 22) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return CNItems.URANIUM_ROD.isIn(stack);
            } //Permet de sélectionner les items accepté pour ce slot
            @Override
            public boolean mayPickup(@NotNull Player playerIn){
                return false;
            }
        };

        inputSlot2 = new SlotItemHandler(contentHolder.inventory, 1, 26, 22) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return CNItems.GRAPHITE_ROD.isIn(stack);
            }
            @Override
            public boolean mayPickup(@NotNull Player playerIn){
                return false;
            }
        };

        addSlot(inputSlot);
        addSlot(inputSlot2);

//        // player Slots
//        for (int row = 0; row < 3; ++row) {
//            for (int col = 0; col < 9; ++col) {
//                this.addSlot(new Slot(player.getInventory(), col + row * 9 + 9, 38 + col * 18, 105 + row * 18));
//            }
//        }
//
//        for (int hotbarSlot = 0; hotbarSlot < 9; ++hotbarSlot) {
//            this.addSlot(new Slot(player.getInventory(), hotbarSlot, 38 + hotbarSlot * 18, 163));
//        }
    }

    @Override
    protected void saveData(ReactorControllerBlockEntity contentHolder) {

    }
}