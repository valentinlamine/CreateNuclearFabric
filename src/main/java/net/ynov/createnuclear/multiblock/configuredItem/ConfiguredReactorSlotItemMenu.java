package net.ynov.createnuclear.multiblock.configuredItem;

import com.simibubi.create.foundation.gui.menu.GhostItemMenu;
import com.simibubi.create.foundation.gui.menu.MenuBase;
import io.github.fabricators_of_create.porting_lib.transfer.item.ItemStackHandler;
import io.github.fabricators_of_create.porting_lib.transfer.item.ItemStackHandlerContainer;
import io.github.fabricators_of_create.porting_lib.transfer.item.SlotItemHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.ynov.createnuclear.item.CNItems;
import net.ynov.createnuclear.menu.CNMenus;

public class ConfiguredReactorSlotItemMenu extends GhostItemMenu<ItemStack> {

    public ConfiguredReactorSlotItemMenu(MenuType<?> type, int id, Inventory inv, FriendlyByteBuf extraData) {
        super(type, id, inv, extraData);
    }

    public ConfiguredReactorSlotItemMenu(MenuType<?> type, int id, Inventory inv, ItemStack contentHolder) {
        super(type, id, inv, contentHolder);
    }

    public static ConfiguredReactorSlotItemMenu create(int id, Inventory inv, ItemStack contentHolder) {
        return new ConfiguredReactorSlotItemMenu(CNMenus.SLOT_ITEM_STORAGE.get(), id, inv, contentHolder);
    }

    @Override
    protected boolean allowRepeats() {
        return false;
    }

    @Override
    protected ItemStackHandler createGhostInventory() {
        return new ItemStackHandler(2);
    }

    @Override
    @Environment(EnvType.CLIENT)
    protected ItemStack createOnClient(FriendlyByteBuf extraData) {
        return extraData.readItem();
    }

    @Override
    protected void addSlots() {
        addPlayerSlots(0, 95);
        addInventorySlot();

    }

    private void addInventorySlot() {
        Slot inputSlot1 = new SlotItemHandler(ghostInventory, 0, 24, 29);

        Slot inputSlot2 = new SlotItemHandler(ghostInventory, 1, 57, 29) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return CNItems.GRAPHENE.isIn(stack);
            }
        };

        addSlot(inputSlot1);
        addSlot(inputSlot2);

    }

    @Override
    protected void saveData(ItemStack contentHolder) {

    }
}
