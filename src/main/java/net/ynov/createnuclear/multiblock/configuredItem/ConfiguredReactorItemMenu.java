package net.ynov.createnuclear.multiblock.configuredItem;

import com.simibubi.create.foundation.gui.menu.GhostItemMenu;
import io.github.fabricators_of_create.porting_lib.transfer.item.ItemStackHandler;
import io.github.fabricators_of_create.porting_lib.transfer.item.SlotItemHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.ynov.createnuclear.CreateNuclear;
import net.ynov.createnuclear.gui.CNIconButton;
import net.ynov.createnuclear.gui.CNIcons;
import net.ynov.createnuclear.item.CNItems;
import net.ynov.createnuclear.menu.CNMenus;
import net.ynov.createnuclear.tags.CNTag;
import org.jetbrains.annotations.NotNull;

import static net.ynov.createnuclear.multiblock.configuredItem.ConfiguredReactorItem.getItemStorage;

public class ConfiguredReactorItemMenu extends GhostItemMenu<ItemStack> {

    private ItemStackHandler pattern;

    public ConfiguredReactorItemMenu(MenuType<?> type, int id, Inventory inv, FriendlyByteBuf extraData) {
        super(type, id, inv, extraData);
    }

    public ConfiguredReactorItemMenu(MenuType<?> type, int id, Inventory inv, ItemStack contentHolder) {
        super(type, id, inv, contentHolder);
    }

    public static ConfiguredReactorItemMenu create(int id, Inventory inv, ItemStack stack) {
        return new ConfiguredReactorItemMenu(CNMenus.TEST.get(), id, inv, stack);
    }

    @Override
    protected boolean allowRepeats() {
        return false;
    }

    @Override
    protected void initAndReadInventory(ItemStack contentHolder) {
        super.initAndReadInventory(contentHolder);
        CompoundTag tag = contentHolder.getOrCreateTag();
        CreateNuclear.LOGGER.warn("é: " + tag);
        //pattern = tag.get("pattern");

    }

    @Override
    protected ItemStackHandler createGhostInventory() {
        return getItemStorage(contentHolder);
    }

    @Override
    @Environment(EnvType.CLIENT)
    protected ItemStack createOnClient(FriendlyByteBuf extraData) {
        return extraData.readItem();
    }

    @Override
    protected void addSlots() {
        addPlayerSlots(getPlayerInventotryXOffset(), getplayerInventoryYOffset());
        addPatternSlots();
    }

    private void addPatternSlots() {
        int startWidth = 8;
        int startHeight = 40;
        int incr = 18;
        int i = 0;
        int[][] positions = {
                {3, 0}, {4, 0}, {5, 0},
                {2, 1}, {3, 1}, {4, 1}, {5, 1}, {6, 1},
                {1, 2}, {2, 2}, {3, 2}, {4, 2}, {5, 2}, {6, 2}, {7, 2},
                {0, 3}, {1, 3}, {2, 3}, {3, 3}, {4, 3}, {5, 3}, {6, 3}, {7, 3}, {8, 3},
                {0, 4}, {1, 4}, {2, 4}, {3, 4}, {4, 4}, {5, 4}, {6, 4}, {7, 4}, {8, 4},
                {0, 5}, {1, 5}, {2, 5}, {3, 5}, {4, 5}, {5, 5}, {6, 5}, {7, 5}, {8, 5},
                {1, 6}, {2, 6}, {3, 6}, {4, 6}, {5, 6}, {6, 6}, {7, 6},
                {2, 7}, {3, 7}, {4, 7}, {5, 7}, {6, 7},
                {3, 8}, {4, 8}, {5, 8}
        };

        for (int[] pos : positions) {// up and down not middle
            this.addSlot(new SlotItemHandler(ghostInventory,i, startWidth + incr * pos[0], startHeight + incr * pos[1]));
            i++;
        }
    }

    @Override
    protected void saveData(ItemStack contentHolder) {
        contentHolder.getOrCreateTag()
                .put("pattern", ghostInventory.serializeNBT());
        CompoundTag tag = contentHolder.getOrCreateTag();
        for (int i = 0; i < ghostInventory.getSlotCount(); i++) {
            if (!ghostInventory.getStackInSlot(i).isEmpty()) return;
            contentHolder.setTag(null);
        }
        CreateNuclear.LOGGER.warn("é: " + tag + " " + contentHolder);

    }

    protected int getPlayerInventotryXOffset() {
        return 31;
    }

    protected int getplayerInventoryYOffset() {
        return 229;
    }

    @Override
    public boolean stillValid(Player player) {
        return playerInventory.getSelected() == contentHolder;
    }
}
