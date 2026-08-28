package net.nuclearteam.createnuclear.content.multiblock.controller.manager;

import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.nuclearteam.createnuclear.content.multiblock.input.item.VirtualReactorInputsItem;

import java.util.List;

public interface ReactorInputManagerI extends ReactorIOManager {
    List<Storage<ItemVariant>> getItemHandlers(Level level);
    List<BlockPos> getBlocksPosition(Level level);
    VirtualReactorInputsItem getInventory(Level level);
    boolean extractItems(Level level, int fuelNeeded, int coolerNeeded);
    boolean extractItemByName(Level level, String itemName);
}
