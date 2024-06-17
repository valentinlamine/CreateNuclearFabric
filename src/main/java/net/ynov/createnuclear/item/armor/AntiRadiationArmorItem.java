package net.ynov.createnuclear.item.armor;

import com.simibubi.create.content.equipment.armor.BaseArmorItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;

public class AntiRadiationArmorItem {

    public static final ArmorItem.Type HELMET = ArmorItem.Type.HELMET;
    public static final ArmorItem.Type CHESTPLATE = ArmorItem.Type.CHESTPLATE;
    public static final ArmorItem.Type LEGGINGS = ArmorItem.Type.LEGGINGS;
    public static final ArmorItem.Type BOOTS = ArmorItem.Type.BOOTS;
    public static final ArmorMaterial ARMOR_MATERIAL = CNArmorMaterials.ANTI_RADIATION_SUIT;


    public static class Helmet extends ArmorItem {
        public Helmet(Properties properties) {
            super(ARMOR_MATERIAL, HELMET, properties);
        }
    }

    public static class Chestplate extends ArmorItem {
        public Chestplate(Properties properties) {
            super(ARMOR_MATERIAL, CHESTPLATE, properties);
        }
    }

    public static class Leggings extends ArmorItem {
        public Leggings(Properties properties) {
            super(ARMOR_MATERIAL, LEGGINGS, properties);
        }
    }

    public static class Boot extends ArmorItem {
        public Boot(Properties properties) {
            super(ARMOR_MATERIAL, BOOTS, properties);
        }
    }
}
