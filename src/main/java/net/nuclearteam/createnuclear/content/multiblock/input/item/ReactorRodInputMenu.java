package net.nuclearteam.createnuclear.content.multiblock.input.item;

import com.simibubi.create.foundation.gui.menu.MenuBase;
import io.github.fabricators_of_create.porting_lib.transfer.item.SlotItemHandler;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.core.BlockPos;
import net.nuclearteam.createnuclear.CNMenus;

import java.util.Objects;

public class ReactorRodInputMenu extends MenuBase<ReactorRodInputEntity> {

    @FunctionalInterface
    public interface ClientContentResolver {
        ReactorRodInputEntity resolve(BlockPos pos, CompoundTag data);
    }

    private static ClientContentResolver clientContentResolver = (pos, data) -> {
        throw new IllegalStateException("Reactor rod input client resolver has not been initialized");
    };

    public static void setClientContentResolver(ClientContentResolver resolver) {
        clientContentResolver = Objects.requireNonNull(resolver);
    }

    public ReactorRodInputMenu(MenuType<?> type, int id, Inventory inv, FriendlyByteBuf extraData) {
        super(type, id, inv, extraData);
    }

    public ReactorRodInputMenu(MenuType<?> type, int id, Inventory inv, ReactorRodInputEntity contentHolder) {
        super(type, id, inv, contentHolder);
    }

    public static ReactorRodInputMenu create(int id, Inventory inv, ReactorRodInputEntity contentHolder) {
        return new ReactorRodInputMenu(CNMenus.SLOT_ITEM_STORAGE, id, inv, contentHolder);
    }

    @Override
    public ItemStack quickMove(Player playerIn, int index) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasStack()) {
            ItemStack stackInSlot = slot.getItem();
            result = stackInSlot.copy();

            int playerInventorySize = player.getInventory().main.size(); // normally 36
            int containerStart = playerInventorySize;
            int containerEnd = containerStart + 1; // 1 machine slot

            // Clicked a machine slot (after the player slots)
            if (index >= containerStart && index < containerEnd) {
                if (!this.insertItem(stackInSlot, 0, playerInventorySize, true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                // Clicked a player slot — try moving the stack into the machine container
                if (!this.insertItem(stackInSlot, containerStart, containerEnd, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (stackInSlot.isEmpty()) {
                slot.setStackNoCallbacks(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (stackInSlot.getCount() == result.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTakeItem(playerIn, stackInSlot);
        }
        return result;
    }


    @Override
    protected ReactorRodInputEntity createOnClient(FriendlyByteBuf extraData) {
        BlockPos pos = extraData.readBlockPos();
        CompoundTag data = extraData.readNbt();
        return clientContentResolver.resolve(pos, data);
    }

    @Override
    protected void initAndReadInventory(ReactorRodInputEntity contentHolder) {

    }

    @Override
    protected void addSlots() {

        // player Slots
        for (int hotbarSlot = 0; hotbarSlot < 9; ++hotbarSlot) {
            this.addSlot(new Slot(player.getInventory(), hotbarSlot, -31 + hotbarSlot * 18, 155));
        }

        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(player.getInventory(), col + row * 9 + 9, -31 + col * 18, 97 + row * 18));
            }
        }

        Slot slot1 = new SlotItemHandler(contentHolder.inventory, 0, 42, 29);

        addSlot(slot1);
    }

    @Override
    protected void saveData(ReactorRodInputEntity contentHolder) {
    }

    @Override
    public void clicked(int slotId, int button, ClickType clickType, Player player) {
        if (clickType == ClickType.THROW) {
            int[] targetSlotIds = {9, 18, 27, 0, 1, 28, 19, 10, 16, 17, 26, 25, 34, 35, 8, 7};
            for (int id : targetSlotIds) {
                if (slotId == id) {
                    clickType = ClickType.PICKUP;
                    super.clicked(slotId, button, clickType, player);
                }
            }
            return;
        }
        super.clicked(slotId, button, clickType, player);
    }
}
