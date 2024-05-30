package net.ynov.createnuclear.multiblock.controller;

import com.simibubi.create.foundation.gui.menu.MenuBase;
import io.github.fabricators_of_create.porting_lib.transfer.item.SlotItemHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.ynov.createnuclear.CreateNuclear;
import net.ynov.createnuclear.item.CNItems;
import net.ynov.createnuclear.menu.CNMenus;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ReactorControllerMenu extends MenuBase<ReactorControllerBlockEntity> {
    private Slot inputSlot;
    private Slot inputSlot2;
    private ReactorControllerBlockEntity test2;

    public ReactorControllerMenu(MenuType<?> type, int id, Inventory inv, FriendlyByteBuf extraData) {
        super(type, id, inv, extraData);

    }

    public ReactorControllerMenu(MenuType<?> type, int id, Inventory inv, ReactorControllerBlockEntity be) {
        super(type, id, inv, be);

    }

    @Override
    protected void init(Inventory inv, ReactorControllerBlockEntity contentHolderIn) {
        if (contentHolderIn != null) {
            contentHolder = test2 = contentHolderIn;
            CreateNuclear.LOGGER.warn("dfg 8 " + test2);

            this.addSlots();
        }

    }

    public static ReactorControllerMenu create(int id, Inventory inv, ReactorControllerBlockEntity be) {
        return new ReactorControllerMenu(CNMenus.REACTOR_CONTROLLER.get(), id, inv, be);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return null;
    }

    @Environment(EnvType.CLIENT)
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

        //CreateNuclear.LOGGER.warn("dd " + contentHolder.inventory + " " + String.valueOf(contentHolder instanceof ReactorControllerBlockEntity));
        //Permet de sélectionner les items accepté pour ce slot
        inputSlot = new SlotItemHandler((contentHolder == null ? test2 : contentHolder).inventory, 0, 8, 22) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return CNItems.URANIUM_ROD.isIn(stack);
            } //Permet de sélectionner les items accepté pour ce slot

            @Override
            public boolean mayPickup(@NotNull Player playerIn) {
                return false;
            }
        };

        inputSlot2 = new SlotItemHandler((contentHolder == null ? test2 : contentHolder).inventory, 1, 26, 22) {
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



    }

    @Override
    protected void saveData(ReactorControllerBlockEntity contentHolder) {

    }
}