package net.ynov.createnuclear.world.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.DyeableLeatherItem;
import net.ynov.createnuclear.world.item.DyeableArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Iterator;
import java.util.List;

public interface DyeableAntiRadiationItem {
    String TAG_COLOR = "color";
    String TAG_DISPLAY = "display";
    int DEFAULT_LEATHER_COLOR = 10511680;

    default boolean hasCustomColor(ItemStack stack) {
        CompoundTag compoundTag = stack.getTagElement("display");
        return compoundTag != null && compoundTag.contains("color", 99);
    }

    default int getColor(ItemStack stack) {
        CompoundTag compoundTag = stack.getTagElement("display");
        return compoundTag != null && compoundTag.contains("color", 99) ? compoundTag.getInt("color") : 10511680;
    }

    default void clearColor(ItemStack stack) {
        CompoundTag compoundTag = stack.getTagElement("display");
        if (compoundTag != null && compoundTag.contains("color")) {
            compoundTag.remove("color");
        }

    }

    static void setColor(ItemStack stack, int color) {
        stack.getOrCreateTagElement("display").putInt("color", color);
    }

    static ItemStack dyeArmor(ItemStack stack, List<DyeItem> dyes) {
        ItemStack itemStack = ItemStack.EMPTY;
        int[] is = new int[3];
        int i = 0;
        int j = 0;
        Item item = stack.getItem();
        int k;
        float h;
        int n;
        DyeableAntiRadiationItem dyeableAntiRadiationItem = null;
        if (item instanceof DyeableAntiRadiationItem) {
            dyeableAntiRadiationItem = (DyeableAntiRadiationItem) ((Object) item);
            itemStack = stack.copyWithCount(1);
            if (dyeableAntiRadiationItem.hasCustomColor(stack)) {
                k = dyeableAntiRadiationItem.getColor(itemStack);
                float f = (float)(k >> 16 & 255) / 255.0F;
                float g = (float)(k >> 8 & 255) / 255.0F;
                h = (float)(k & 255) / 255.0F;
                i += (int)(Math.max(f, Math.max(g, h)) * 255.0F);
                is[0] += (int)(f * 255.0F);
                is[1] += (int)(g * 255.0F);
                is[2] += (int)(h * 255.0F);
                ++j;
            }

            for(Iterator var14 = dyes.iterator(); var14.hasNext(); ++j) {
                DyeItem dyeItem = (DyeItem)var14.next();
                float[] fs = dyeItem.getDyeColor().getTextureDiffuseColors();
                int l = (int)(fs[0] * 255.0F);
                int m = (int)(fs[1] * 255.0F);
                n = (int)(fs[2] * 255.0F);
                i += Math.max(l, Math.max(m, n));
                is[0] += l;
                is[1] += m;
                is[2] += n;
            }
        }

        if (dyeableAntiRadiationItem == null) {
            return ItemStack.EMPTY;
        } else {
            k = is[0] / j;
            int o = is[1] / j;
            int p = is[2] / j;
            h = (float)i / (float)j;
            float q = (float)Math.max(k, Math.max(o, p));
            k = (int)((float)k * h / q);
            o = (int)((float)o * h / q);
            p = (int)((float)p * h / q);
            n = (k << 8) + o;
            n = (n << 8) + p;
            DyeableAntiRadiationItem.setColor(itemStack, n);
            return itemStack;
        }
    }
}
