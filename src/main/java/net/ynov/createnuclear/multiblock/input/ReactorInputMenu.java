package net.ynov.createnuclear.multiblock.input;

import com.simibubi.create.foundation.gui.menu.MenuBase;
import io.github.fabricators_of_create.porting_lib.transfer.item.SlotItemHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.ynov.createnuclear.CreateNuclear;
import net.ynov.createnuclear.menu.CNMenus;

public class ReactorInputMenu extends MenuBase<ReactorInputEntity> {

    public ReactorInputMenu(MenuType<?> type, int id, Inventory inv, FriendlyByteBuf extraData) {
        super(type, id, inv, extraData);
    }

    public ReactorInputMenu(MenuType<?> type, int id, Inventory inv, ReactorInputEntity contentHolder) {
        super(type, id, inv, contentHolder);
    }

    public static ReactorInputMenu create(int id, Inventory inv, ReactorInputEntity contentHolder) {
        return new ReactorInputMenu(CNMenus.SLOT_ITEM_STORAGE.get(), id, inv, contentHolder);
    }

    @Override
    protected ReactorInputEntity createOnClient(FriendlyByteBuf extraData) {
        ClientLevel world = Minecraft.getInstance().level;
        BlockEntity entity = world.getBlockEntity(extraData.readBlockPos());
        if (entity instanceof ReactorInputEntity reactorInputEntity) {
            reactorInputEntity.readClient(extraData.readNbt());
            return reactorInputEntity;
        }
        return null;
    }

    @Override
    protected void initAndReadInventory(ReactorInputEntity contentHolder) {

    }

    @Override
    protected void addSlots() {

        Slot slot1 = new SlotItemHandler(contentHolder.inventory, 0, 24, 29);
        Slot slot2 = new SlotItemHandler(contentHolder.inventory, 1, 57, 29);

        addSlot(slot1);
        addSlot(slot2);

    }

    @Override
    protected void saveData(ReactorInputEntity contentHolder) {
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return null;
    }
}
