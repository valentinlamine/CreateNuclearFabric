package net.nuclearteam.createnuclear.content.equipment.armor;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.DyeColor;
import net.minecraft.Util;
import net.minecraft.world.level.Level;
import io.github.fabricators_of_create.porting_lib.item.ArmorTextureItem;
import net.nuclearteam.createnuclear.foundation.advancement.CNAdvancement;

import net.nuclearteam.createnuclear.CNAttributes;
import net.nuclearteam.createnuclear.CNItems;
import net.nuclearteam.createnuclear.foundation.utility.ClothTagHelper;

import java.util.*;

public abstract class AntiRadiationArmorItem extends ArmorItem implements ArmorTextureItem {
    public static final double RADIATION_VALUE = 0.25;
    protected final DyeColor color;

    public AntiRadiationArmorItem(ArmorMaterials materials, Type type, Properties properties, DyeColor color) {
        super(materials, type, properties);
        this.color = color;
    }

    private static final EnumMap<ArmorItem.Type, UUID> ARMOR_MODIFIER_UUID_PER_TYPE = Util.make(new EnumMap<>(ArmorItem.Type.class), (p_266744_) -> {
        p_266744_.put(ArmorItem.Type.BOOTS, UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"));
        p_266744_.put(ArmorItem.Type.LEGGINGS, UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"));
        p_266744_.put(ArmorItem.Type.CHESTPLATE, UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"));
        p_266744_.put(ArmorItem.Type.HELMET, UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150"));
    });

    private static Multimap<Attribute, AttributeModifier> irradiatedArmorAttribute(Multimap<Attribute, AttributeModifier> map, ArmorItem.Type type) {
        UUID uuid = ARMOR_MODIFIER_UUID_PER_TYPE.get(type);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.putAll(map);
        // Use ADDITION so pieces add up to a resistance fraction (e.g. 0.25 per piece -> full set = 1.0)
        builder.put(CNAttributes.IRRADIATED_RESISTANCE.get(), new AttributeModifier(uuid, "Armor Resistance Irradiation", RADIATION_VALUE, AttributeModifier.Operation.ADDITION));

        return builder.build();
    }

    // Tells the game where to find the armor's PNG texture
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return ClothTagHelper.getArmorTexturePath(stack, "anti_radiation_suit.png");
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);
        if (level.isClientSide || !(entity instanceof Player player)) return;
        if (!stack.hasTag() || !stack.getTag().contains(ClothTagHelper.COLOR)) return;
        if (CNAdvancement.DYE_ANTI_RADIATION_ARMOR.isAlreadyAwardedTo(player)) return;
        CNAdvancement.DYE_ANTI_RADIATION_ARMOR.awardTo(player);
    }
  
    public static class Helmet extends AntiRadiationArmorItem implements IGoggleHelmet {
        public Helmet(Properties p, DyeColor color) {
            super(ArmorMaterials.ANTI_RADIATION_SUIT, Type.HELMET, p, color);
        }

        @Override
        public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
            if (slot != this.getType().getEquipmentSlot()) return super.getAttributeModifiers(slot);
            Multimap<Attribute, AttributeModifier> map = super.getAttributeModifiers(slot);
            return AntiRadiationArmorItem.irradiatedArmorAttribute(map, Type.HELMET);
        }
    }

    public static class Chestplate extends AntiRadiationArmorItem {
        public Chestplate(Properties p, DyeColor color) {
            super(ArmorMaterials.ANTI_RADIATION_SUIT, Type.CHESTPLATE, p, color);
        }

        @Override
        public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
            if (slot != this.getType().getEquipmentSlot()) return super.getAttributeModifiers(slot);
            Multimap<Attribute, AttributeModifier> map = super.getAttributeModifiers(slot);
            return AntiRadiationArmorItem.irradiatedArmorAttribute(map, Type.CHESTPLATE);
        }
    }

    public static class Leggings extends AntiRadiationArmorItem {
        public Leggings(Properties p, DyeColor color) {
            super(ArmorMaterials.ANTI_RADIATION_SUIT, Type.LEGGINGS, p, color);
        }

        @Override
        public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
            if (slot != this.getType().getEquipmentSlot()) return super.getAttributeModifiers(slot);
            Multimap<Attribute, AttributeModifier> map = super.getAttributeModifiers(slot);
            return AntiRadiationArmorItem.irradiatedArmorAttribute(map, Type.LEGGINGS);
        }
    }

    public static class Boot extends AntiRadiationArmorItem {
        public Boot(Properties p, DyeColor color) {
            super(ArmorMaterials.ANTI_RADIATION_SUIT, Type.BOOTS, p, color);
        }

        @Override
        public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
            if (slot != this.getType().getEquipmentSlot()) return super.getAttributeModifiers(slot);
            Multimap<Attribute, AttributeModifier> map = super.getAttributeModifiers(slot);
            return AntiRadiationArmorItem.irradiatedArmorAttribute(map, Type.BOOTS);
        }
    }

    public interface IGoggleHelmet {
        static boolean isGoggleHelmet(LivingEntity entity) {
            ItemStack headSlot = entity.getEquippedStack(EquipmentSlot.HEAD);
            return CNItems.ANTI_RADIATION_HELMETS.is(headSlot);
        }
    }
}
