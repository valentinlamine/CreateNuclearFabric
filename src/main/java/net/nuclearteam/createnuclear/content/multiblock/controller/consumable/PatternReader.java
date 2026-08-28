package net.nuclearteam.createnuclear.content.multiblock.controller.consumable;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.nbt.CompoundTag;
import io.github.fabricators_of_create.porting_lib.transfer.item.ItemStackHandler;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class PatternReader {

    private PatternReader() {
    }

    public static Map<Item, Integer> readItemCounts(ItemStack configuredPattern) {
        CompoundTag tag = configuredPattern.getTag();
        if (tag == null || tag.isEmpty())
            return Collections.emptyMap();

        ItemStackHandler handler = new ItemStackHandler(57);
        if (tag.contains("patternAll"))
            handler.deserializeNBT(tag.getCompound("patternAll"));
        else if (tag.contains("pattern"))
            handler.deserializeNBT(tag.getCompound("pattern"));
        else
            return Collections.emptyMap();

        Map<Item, Integer> counts = new HashMap<>();
        for (int i = 0; i < handler.getSlotCount(); i++) {
            ItemStack stack = handler.getStackInSlot(i);
            if (!stack.isEmpty() && !stack.is(Items.GLASS_PANE))
                counts.merge(stack.getItem(), 1, Integer::sum);
        }
        return counts;
    }
}
