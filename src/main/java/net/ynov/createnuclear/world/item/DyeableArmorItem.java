package net.ynov.createnuclear.world.item;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;

public class DyeableArmorItem {
    public class DyeableArmorItem extends ArmorItem implements DyeableAntiRadiationItem {
        public DyeableArmorItem(ArmorMaterial armorMaterial, ArmorItem.Type type, Item.Properties properties) {
            super(armorMaterial, type, properties);
        }
    }
}
