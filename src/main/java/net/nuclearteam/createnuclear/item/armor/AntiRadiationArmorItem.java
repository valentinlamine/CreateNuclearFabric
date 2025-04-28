package net.nuclearteam.createnuclear.item.armor;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.simibubi.create.foundation.data.recipe.CreateRecipeProvider;
import com.tterrag.registrate.util.entry.ItemEntry;
import io.github.fabricators_of_create.porting_lib.item.ArmorTextureItem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.*;
import net.nuclearteam.createnuclear.CreateNuclear;
import net.nuclearteam.createnuclear.attributes.CNAttributes;
import net.nuclearteam.createnuclear.item.CNItems;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public class AntiRadiationArmorItem {

    public static final ArmorItem.Type HELMET = ArmorItem.Type.HELMET;
    public static final ArmorItem.Type CHESTPLATE = ArmorItem.Type.CHESTPLATE;
    public static final ArmorItem.Type LEGGINGS = ArmorItem.Type.LEGGINGS;
    public static final ArmorItem.Type BOOTS = ArmorItem.Type.BOOTS;
    public static final ArmorMaterial ARMOR_MATERIAL = CNArmorMaterials.ANTI_RADIATION_SUIT;

    private static abstract class IrradiationImmuneArmor extends ArmorItem {
        private final Supplier<Multimap<Attribute, AttributeModifier>> extraModifiers;

        public IrradiationImmuneArmor(ArmorMaterial material, Type type, Properties properties) {
            super(material, type, properties);
            this.extraModifiers = Suppliers.memoize(() -> {
                ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
                builder.put(CNAttributes.IRRADIATED_RESISTANCE.get(), new AttributeModifier(UUID.fromString("2AD30246-FEE1-4E67-B886-69FD380BB150"), "Irradiation Immune", 1, AttributeModifier.Operation.ADDITION));

                return builder.build();
            });
        }

        public IrradiationImmuneArmor(Type type, Properties properties) {
            this(ARMOR_MATERIAL, type, properties);
        }

        @Override
        public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
            Multimap<Attribute, AttributeModifier> modifiers = super.getDefaultAttributeModifiers(slot);
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();

            for (Map.Entry<Attribute, AttributeModifier> entry : modifiers.entries()) {
                builder.put(entry.getKey(), entry.getValue());
            }

            for (Map.Entry<Attribute, AttributeModifier> entry : extraModifiers.get().entries()) {
                if (slot.getIndex() == EquipmentSlot.HEAD.getIndex()) {
                    builder.put(entry.getKey(), entry.getValue());
                }
            }

            return builder.build();
        }
    }

    private static abstract class ColoredIrradiationImmuneArmor extends IrradiationImmuneArmor implements ArmorTextureItem {
        protected final DyeColor color;

        public ColoredIrradiationImmuneArmor(ArmorMaterial material, Type type, Properties properties, DyeColor color) {
            super(material, type, properties);
            this.color = color;
        }

        public ColoredIrradiationImmuneArmor(Type type, Properties properties, DyeColor color) {
           this(ARMOR_MATERIAL, type, properties, color);
        }

        @Override
        public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
            int typeTexture = slot == EquipmentSlot.LEGS ? 2 : 1;
            return CreateNuclear.asResource("textures/models/armor/"+color+"_anti_radiation_suit_layer_"+typeTexture+".png").toString();
        }
    }

    public static class Helmets extends ColoredIrradiationImmuneArmor {
        public Helmets(Properties properties, DyeColor color) {
            super(HELMET, properties, color);
        }
    }

    public static class Chestplates extends ColoredIrradiationImmuneArmor {
        public Chestplates(Properties properties, DyeColor color) {
            super(CHESTPLATE, properties, color);
        }
    }

    public static class Leggings extends ColoredIrradiationImmuneArmor {

        public Leggings(Properties properties, DyeColor color) {
            super(LEGGINGS, properties, color);
        }
    }

    public static class Boots extends IrradiationImmuneArmor implements ArmorTextureItem {
        public Boots(Properties properties) {
            super(ARMOR_MATERIAL, BOOTS, properties);
        }

        @Override
        public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
            return CreateNuclear.asResource("textures/models/armor/white_anti_radiation_suit_layer_1.png").toString();

        }
    }


    public static class DyeItemList<T extends Item> implements Iterable<ItemEntry<T>> {
        private static final int COLOR_AMOUNT = DyeColor.values().length;
        private final ItemEntry<?>[] entries = new ItemEntry<?>[COLOR_AMOUNT];

        public DyeItemList(Function<DyeColor, ItemEntry<? extends T>> filler) {
            for (DyeColor color : DyeColor.values()) {
                entries[color.ordinal()] = filler.apply(color);
            }
        }

        @SuppressWarnings("unchecked")
        public ItemEntry<T> get(DyeColor color) {
            return (ItemEntry<T>) entries[color.ordinal()];
        }

        public boolean contains(Item item) {
            return Arrays.stream(entries).anyMatch(e -> e.is(item));
        }

        @SuppressWarnings("unchecked")
        public ItemEntry<T>[] toArray() {
            return (ItemEntry<T>[]) Arrays.copyOf(entries, entries.length);
        }

        @Override
        public Iterator<ItemEntry<T>> iterator() {
            return Arrays.stream(entries).map(e -> (ItemEntry<T>) e).iterator();
        }
    }

    public static class DyeRecipeArmorList implements Iterable<CreateRecipeProvider.GeneratedRecipe> {
        private static final int COLOR_AMOUNT = DyeColor.values().length;
        private final CreateRecipeProvider.GeneratedRecipe[] recipes = new CreateRecipeProvider.GeneratedRecipe[COLOR_AMOUNT];

        public DyeRecipeArmorList(Function<DyeColor, CreateRecipeProvider.GeneratedRecipe> filler) {
            for (DyeColor color : DyeColor.values()) {
                recipes[color.ordinal()] = filler.apply(color);
            }
        }

        public CreateRecipeProvider.GeneratedRecipe get(@Nullable DyeColor color) {
            return recipes[color.ordinal()];
        }

        public CreateRecipeProvider.GeneratedRecipe[] toArray() {
            return Arrays.copyOf(recipes, recipes.length);
        }

        @Override
        public Iterator<CreateRecipeProvider.GeneratedRecipe> iterator() {
            return Arrays.stream(recipes).iterator();
        }
    }

    public enum Armor {
        WHITE(DyeColor.WHITE),
        YELLOW(DyeColor.YELLOW),
        RED(DyeColor.RED),
        BLUE(DyeColor.BLUE),
        GREEN(DyeColor.GREEN),
        BLACK(DyeColor.BLACK),
        ORANGE(DyeColor.ORANGE),
        PURPLE(DyeColor.PURPLE),
        BROWN(DyeColor.BROWN),
        PINK(DyeColor.PINK),
        CYAN(DyeColor.CYAN),
        LIGHT_GRAY(DyeColor.LIGHT_GRAY),
        GRAY(DyeColor.GRAY),
        LIGHT_BLUE(DyeColor.LIGHT_BLUE),
        LIME(DyeColor.LIME),
        MAGENTA(DyeColor.MAGENTA);

        private static final Map<DyeColor, ItemEntry<Helmets>> helmets = new EnumMap<>(DyeColor.class);
        private static final Map<DyeColor, ItemEntry<Chestplates>> chestplates = new EnumMap<>(DyeColor.class);
        private static final Map<DyeColor, ItemEntry<Leggings>> leggings = new EnumMap<>(DyeColor.class);

        static {
            for (DyeColor color : DyeColor.values()) {
                helmets.put(color, CNItems.ANTI_RADIATION_HELMETS.get(color));
                chestplates.put(color, CNItems.ANTI_RADIATION_CHESTPLATES.get(color));
                leggings.put(color, CNItems.ANTI_RADIATION_LEGGINGS.get(color));
            }
        }

        private final DyeColor color;

        Armor(DyeColor color) {
            this.color = color;
        }

        public ItemEntry<Helmets> getHelmet() {
            return helmets.get(color);
        }
        public ItemEntry<Chestplates> getChestplate() {
            return chestplates.get(color);
        }
        public ItemEntry<Leggings> getLeggings() {
            return leggings.get(color);
        }

        public static boolean isArmored(ItemStack stack) {
            Item item = stack.getItem();
            return CNItems.ANTI_RADIATION_HELMETS.contains(item)
                    || CNItems.ANTI_RADIATION_CHESTPLATES.contains(item)
                    || CNItems.ANTI_RADIATION_LEGGINGS.contains(item)
                    || CNItems.ANTI_RADIATION_BOOTS.is(item);
        }
    }

}
