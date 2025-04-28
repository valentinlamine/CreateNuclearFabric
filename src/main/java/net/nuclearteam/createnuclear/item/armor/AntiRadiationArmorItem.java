package net.nuclearteam.createnuclear.item.armor;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.simibubi.create.foundation.data.recipe.CreateRecipeProvider;
import com.tterrag.registrate.util.entry.ItemEntry;
import io.github.fabricators_of_create.porting_lib.item.ArmorTextureItem;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.*;
import net.nuclearteam.createnuclear.CreateNuclear;
import net.nuclearteam.createnuclear.attributes.CNAttributes;
import net.nuclearteam.createnuclear.item.CNItems;


import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Main class responsible for creating and registering anti-radiation armor.
 *
 * <p>This class defines attribute modifier definitions,
 * caches texture paths, and provides helpers to initialize
 * colored armor items and their crafting recipes.</p>
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AntiRadiationArmorItem {

    /**
     * Unique armor material used for all anti-radiation pieces.
     */
    public static final ArmorMaterial ARMOR_MATERIAL = CNArmorMaterials.ANTI_RADIATION_SUIT;

    /**
     * Definition of an attribute modifier associated with an equipment slot.
     *
     * @param slot          Target equipment slot (HEAD, CHEST, etc.)
     * @param name          Display the name of the modifier.
     * @param attribute  Key of the attribute (e.g., "irradiated_resistance").
     * @param value         Numeric value of the modifier.
     * @param op            Operation type (ADDITION, MULTIPLY, etc.).
     */
    private record AttributeDef(EquipmentSlot slot, String name, Attribute attribute, double value, AttributeModifier.Operation op, UUID uuid) {
        public AttributeDef(EquipmentSlot slot, String name, Attribute attribute, double value, AttributeModifier.Operation op) {
            this(slot, name, attribute, value, op, generateUUID(attribute, slot));
        }

        private static UUID generateUUID(Attribute attribute, EquipmentSlot slot) {
            String raw = attribute.getDescriptionId() + "_" + slot.name();
            return UUID.nameUUIDFromBytes(raw.getBytes(StandardCharsets.UTF_8));
        }
    }

    /**
     * Static list of modifier definitions.
     */
    private static final List<AttributeDef> ATTRIBUTE_DEFS = List.of(
            new AttributeDef(EquipmentSlot.HEAD, "Irradiation Resistance", CNAttributes.IRRADIATED_RESISTANCE.get(), 1.0, AttributeModifier.Operation.ADDITION),
            new AttributeDef(EquipmentSlot.CHEST, "Irradiation Resistance", CNAttributes.IRRADIATED_RESISTANCE.get(), 3.0, AttributeModifier.Operation.ADDITION),
            new AttributeDef(EquipmentSlot.LEGS, "Irradiation Resistance", CNAttributes.IRRADIATED_RESISTANCE.get(), 2.0, AttributeModifier.Operation.ADDITION),
            new AttributeDef(EquipmentSlot.FEET, "Irradiation Resistance", CNAttributes.IRRADIATED_RESISTANCE.get(), 1.0, AttributeModifier.Operation.ADDITION)
    );

    /**
     * Maps each equipment slot to its set of attribute modifiers,
     * initialized from MOD_DEFINITIONS.
     */
    private static final Map<EquipmentSlot, Multimap<Attribute, AttributeModifier>> SLOT_EXTRA_MODIFIERS = initSlotModifiers();


    /**
     * Initializes the slot-to-modifier map based on definitions.
     *
     * @return EnumMap mapping slots to their attribute modifier multimaps.
     */
    private static Map<EquipmentSlot, Multimap<Attribute, AttributeModifier>> initSlotModifiers() {
        Map<EquipmentSlot, Multimap<Attribute, AttributeModifier>> map = new EnumMap<>(EquipmentSlot.class);
        for (AttributeDef def : ATTRIBUTE_DEFS) {
            var builder = ImmutableMultimap.<Attribute, AttributeModifier>builder();
            Attribute attr = def.attribute;
            builder.put(attr, new AttributeModifier(def.uuid, def.name, def.value, def.op));
            map.put(def.slot, builder.build());
        }
        return map;
    }

    /**
     * Caches texture URIs for each color and layer combination using lazy suppliers.
     */
    private static final Map<String, Supplier<String>> TEXTURE_LOCATIONS = initTextureLocations();

    /**
     * Initializes the texture path cache mapping "<color>_<layer>" to a lazy-loaded URI.
     *
     * @return HashMap containing precomputed texture suppliers.
     */
    private static Map<String, Supplier<String>> initTextureLocations() {
        Map<String, Supplier<String>> map = new HashMap<>();

        for (DyeColor color : DyeColor.values()) {
            for (int layer = 1; layer <= 2; layer++) {
                String key = color.getName() + "_" + layer;
                Supplier<String> supplier;
                if (color == DyeColor.WHITE && layer == 1) {
                    supplier = () -> CreateNuclear.asResource("textures/models/armor/white_anti_radiation_layer_1.png").toString();
                } else {
                    String finalPath = String.format("textures/models/armor/%s_anti_radiation_layer_%d.png", color.getName(), layer);
                    supplier = () -> CreateNuclear.asResource(finalPath).toString();
                }
                map.put(key, supplier);
            }
        }

        return map;
    }

    /**
     * Base class for armor pieces that grant irradiation immunity.
     *
     * <p>This class injects additional attribute modifiers (such as Irradiated Resistance)
     * when equipped, on top of the default armor attributes.</p>
     */
    private static abstract class IrradiationImmuneArmor extends ArmorItem {

        /**
         * Supplier that caches extra attribute modifiers applied by this armor.
         */
        private final Supplier<Multimap<Attribute, AttributeModifier>> extraModifiers;

        /**
         * Constructs an irradiation-immune armor piece.
         *
         * @param material  The armor material to use.
         * @param type      The type of armor piece (helmet, chestplate, etc.).
         * @param properties Item properties (group, durability, etc.).
         */
        public IrradiationImmuneArmor(ArmorMaterial material, Type type, Properties properties) {
            super(material, type, properties);
            this.extraModifiers = Suppliers.memoize(() -> {
                ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
                Multimap<Attribute, AttributeModifier> slotModifiers = SLOT_EXTRA_MODIFIERS.get(this.getType().getSlot());
                if (slotModifiers != null) {
                    builder.putAll(slotModifiers);
                }
                return builder.build();
            });
        }

        /**
         * Constructs an irradiation-immune armor piece.
         *
         * @param type      The type of armor piece (helmet, chestplate, etc.).
         * @param properties Item properties (group, durability, etc.).
         */
        public IrradiationImmuneArmor(Type type, Properties properties) {
            this(ARMOR_MATERIAL, type, properties);
        }


        /**
         * Overrides default attributes to include irradiation-specific attributes.
         *
         * @param slot Equipment slot to retrieve attributes for.
         * @return Multimap of attributes and modifiers.
         */
        @Override
        public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
            Multimap<Attribute, AttributeModifier> baseModifiers = super.getDefaultAttributeModifiers(slot);

            if (slot != this.getType().getSlot()) {
                // Only apply extra modifiers when the correct piece is equipped
                return baseModifiers;
            }

            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.putAll(baseModifiers);
            builder.putAll(extraModifiers.get());

            return builder.build();
        }
    }

    /**
     * Extension of {@link IrradiationImmuneArmor} that supports color variations.
     *
     * <p>This class allows dynamically selecting different armor textures
     * based on the dye color associated with the armor piece.</p>
     */
    private static abstract class ColoredIrradiationImmuneArmor extends IrradiationImmuneArmor implements ArmorTextureItem {
        /**
         * Color of the armor, determining its texture.
         */
        protected final DyeColor color;

        /**
         * Constructs a colored irradiation-immune armor piece.
         *
         * @param material   The armor material to use.
         * @param type       The type of armor (helmet, chestplate, etc.).
         * @param properties Item settings (durability, group, etc.).
         * @param color      The dye color applied to the armor.
         */
        public ColoredIrradiationImmuneArmor(ArmorMaterial material, Type type, Properties properties, DyeColor color) {
            super(material, type, properties);
            this.color = color;
        }

        /**
         * Shortcut constructor using the default anti-radiation material.
         *
         * @param type       The armor type.
         * @param properties Item settings.
         * @param color      The dye color applied to the armor.
         */
        public ColoredIrradiationImmuneArmor(Type type, Properties properties, DyeColor color) {
            this(ARMOR_MATERIAL, type, properties, color);
        }

        @Override
        public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
            int layer = slot == EquipmentSlot.LEGS ? 2 : 1;
            String key = color.getName() + "_" + layer;
            Supplier<String> textureSupplier = TEXTURE_LOCATIONS.get(key);
            if (textureSupplier == null) {
                // Fallback: In case the color-layer combination was not found
                return CreateNuclear.asResource("textures/models/armor/white_anti_radiation_layer_1.png").toString();
            }
            return textureSupplier.get();
        }
    }

    public static class Helmets extends ColoredIrradiationImmuneArmor {
        public Helmets(Properties properties, DyeColor color) {
            super(Type.HELMET, properties, color);
        }
    }

    public static class Chestplates extends ColoredIrradiationImmuneArmor {
        public Chestplates(Properties properties, DyeColor color) {
            super(Type.CHESTPLATE, properties, color);
        }
    }

    public static class Leggings extends ColoredIrradiationImmuneArmor {

        public Leggings(Properties properties, DyeColor color) {
            super(Type.LEGGINGS, properties, color);
        }
    }

    public static class Boots extends IrradiationImmuneArmor implements ArmorTextureItem {
        public Boots(Properties properties) {
            super(ARMOR_MATERIAL, Type.BOOTS, properties);
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
