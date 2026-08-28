package net.nuclearteam.createnuclear.foundation.utility;

import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.nuclearteam.createnuclear.CreateNuclear;
import net.nuclearteam.createnuclear.content.equipment.cloth.ClothItem;

public final class ClothTagHelper {
    public static final String COLOR = "ClothColor";
    public static final String ITEM = "ClothItem";
    private ClothTagHelper() {}

    public static void initClothTagsForResult(ItemStack resultItem, ItemStack baseItem, ItemStack additionItem) {
        CompoundTag baseTag = baseItem.getOrCreateTag();
        CompoundTag resultTag = resultItem.getOrCreateTag();

        if (!resultTag.contains(COLOR)) {
            if (baseTag.contains(COLOR))
                resultTag.putString(COLOR, baseTag.getString(COLOR));
        }
        if (!resultTag.contains(ITEM)) {
            if (baseTag.contains(ITEM))
                resultTag.put(ITEM, baseTag.getCompound(ITEM));
        }

        if (additionItem != null && additionItem.getItem() instanceof ClothItem) {
            resultTag.putString(COLOR, ((ClothItem) additionItem.getItem()).getColor().getSerializedName());
            resultTag.put(ITEM, additionItem.serializeNBT());
        }
    }

    public static String getClothColor(ItemStack stack, String defaultColor) {
        if (stack == null) return defaultColor;
        CompoundTag tag = stack.getTag();
        return (tag != null && tag.contains(COLOR)) ? tag.getString(COLOR) : defaultColor;
    }

    public static String getArmorTexturePath(ItemStack stack, String armorFileName) {
        String cloth = getClothColor(stack, "default");
        String textureFile = cloth + "_" + armorFileName;
        return CreateNuclear.asResource("textures/models/armor/" + textureFile).toString();
    }
}
