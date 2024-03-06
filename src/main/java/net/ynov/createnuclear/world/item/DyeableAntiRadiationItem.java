package net.ynov.createnuclear.world.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public interface DyeableAntiRadiationItem {
    String TAG_COLOR = "color";
    String TAG_DISPLAY = "display";
    int DEFAULT_ANTI_RADIATION_COLOR = 10511680;

    default boolean hasCustomColor(ItemStack stack) {
        CompoundTag compoundTag = stack.getTagElement(TAG_DISPLAY);
        return compoundTag != null && compoundTag.contains(TAG_COLOR, 99);
    }

    default int getColor(ItemStack stack) {
        CompoundTag compoundTag = stack.getTagElement(TAG_DISPLAY);
        if (compoundTag != null && compoundTag.contains(TAG_COLOR, 99)) {
            return compoundTag.getInt(TAG_COLOR);
        }
        return 10511680;
    }

    default void clearColor(ItemStack stack) {
        CompoundTag compoundTag = stack.getTagElement(TAG_DISPLAY);
        if (compoundTag != null && compoundTag.contains(TAG_COLOR)) {
            compoundTag.remove(TAG_COLOR);
        }
    }

    default void setColor(ItemStack stack, int color) {
        stack.getOrCreateTagElement(TAG_DISPLAY).putInt(TAG_COLOR, color);
    }

    static ItemStack dyeArmor(ItemStack stack, List<DyeItem> dyes) {
        int n;
        float h;
        ItemStack itemStack = ItemStack.EMPTY;
        int[] is = new int[3];
        int i = 0;
        int j = 0;
        DyeableAntiRadiationItem dyeableAntiRadiationItem = null;
        Item item = stack.getItem();
        if (item instanceof DyeableAntiRadiationItem) {
            dyeableAntiRadiationItem = (DyeableAntiRadiationItem)((Object)item);
            itemStack = stack.copyWithCount(1);
            if (dyeableAntiRadiationItem.hasCustomColor(stack)) {
                int k = dyeableAntiRadiationItem.getColor(itemStack);
                float f = (float)(k >> 16 & 0xFF) / 255.0f;
                float g = (float)(k >> 8 & 0xFF) / 255.0f;
                h = (float)(k & 0xFF) / 255.0f;
                i += (int)(Math.max(f, Math.max(g, h)) * 255.0f);
                is[0] = is[0] + (int)(f * 255.0f);
                is[1] = is[1] + (int)(g * 255.0f);
                is[2] = is[2] + (int)(h * 255.0f);
                ++j;
            }
            for (DyeItem dyeItem : dyes) {
                float[] fs = dyeItem.getDyeColor().getTextureDiffuseColors();
                int l = (int)(fs[0] * 255.0f);
                int m = (int)(fs[1] * 255.0f);
                n = (int)(fs[2] * 255.0f);
                i += Math.max(l, Math.max(m, n));
                is[0] = is[0] + l;
                is[1] = is[1] + m;
                is[2] = is[2] + n;
                ++j;
            }
        }
        if (dyeableAntiRadiationItem == null) {
            return ItemStack.EMPTY;
        }
        int k = is[0] / j;
        int o = is[1] / j;
        int p = is[2] / j;
        h = (float)i / (float)j;
        float q = Math.max(k, Math.max(o, p));
        k = (int)((float)k * h / q);
        o = (int)((float)o * h / q);
        p = (int)((float)p * h / q);
        n = k;
        n = (n << 8) + o;
        n = (n << 8) + p;
        dyeableAntiRadiationItem.setColor(itemStack, n);
        return itemStack;
    }
}
