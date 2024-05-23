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


    public static class Helmet extends BaseArmorItem {
        public Helmet(Properties properties, ResourceLocation textureLoc) {
            super(ARMOR_MATERIAL, HELMET, properties, textureLoc);
        }
    }

    public static class Chestplate extends BaseArmorItem {
        public Chestplate(Properties properties, ResourceLocation textureLoc) {
            super(ARMOR_MATERIAL, CHESTPLATE, properties, textureLoc);
        }
    }

    public static class Leggings extends BaseArmorItem {
        public Leggings(Properties properties, ResourceLocation textureLoc) {
            super(ARMOR_MATERIAL, LEGGINGS, properties, textureLoc);
        }
    }

    public static class Boot extends BaseArmorItem {
        public Boot(Properties properties, ResourceLocation textureLoc) {
            super(ARMOR_MATERIAL, BOOTS, properties, textureLoc);
        }
    }
}
