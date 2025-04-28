package net.nuclearteam.createnuclear.item.cloth;

import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.nuclearteam.createnuclear.item.CNItems;

import com.simibubi.create.foundation.data.recipe.CreateRecipeProvider.GeneratedRecipe;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;

/**
 * Represents a ClothItem (dyed cloth), which has a defined color.
 * This class allows managing different recipes and items based on the color.
 */
public class ClothItem extends Item {
    private final DyeColor color;

    /**
     * Constructor to create a ClothItem with a defined color.
     *
     * @param properties The properties of the item.
     * @param color The color of the cloth.
     */
    public ClothItem(Properties properties, DyeColor color) {
        super(properties);
        this.color = color;
    }

    /**
     * A class representing a list of recipes related to different cloth colors.
     * It allows retrieving generated recipes for each color.
     */
    public static class DyeRecipeList implements Iterable<GeneratedRecipe> {
        private static final int COLOR_AMOUNT = DyeColor.values().length;
        protected final GeneratedRecipe[] recipes = new GeneratedRecipe[COLOR_AMOUNT];

        /**
         * Constructor for the list of dyed cloth recipes.
         *
         * @param filler A function that generates a recipe for each color.
         */
        public DyeRecipeList(Function<@NotNull DyeColor, GeneratedRecipe> filler) {
            for (DyeColor color : DyeColor.values()) {
                recipes[color.ordinal()] = filler.apply(color);
            }
        }

        /**
         * Gets the total number of colors (cloth types) available.
         *
         * @return The number of colors (cloth types).
         */
        protected int getColorCount() {
            return COLOR_AMOUNT;
        }

        /**
         * Retrieves the recipe for a given color.
         *
         * @param color The color for which the recipe is requested.
         * @return The generated recipe for the color.
         */
        public GeneratedRecipe get(@Nullable DyeColor color) {
            return recipes[color.ordinal()];
        }

        /**
         * Converts the list of recipes to an array.
         *
         * @return An array containing all the generated recipes.
         */
        public GeneratedRecipe[] toArrays() {
            return Arrays.copyOf(recipes, recipes.length);
        }

        /**
         * Iterator to iterate over the generated recipes for each color.
         *
         * @return An iterator for the recipes.
         */
        @NotNull
        @Override
        public Iterator<GeneratedRecipe> iterator() {
            return new Iterator<>() {
                private int index = 0;

                @Override
                public boolean hasNext() {
                    return index < recipes.length;
                }

                @Override
                public GeneratedRecipe next() {
                    if (!hasNext()) throw new NoSuchElementException();
                    return recipes[index++];
                }
            };
        }

        /**
         * A subclass of DyeRecipeList which allows a nullable color, adding an extra recipe entry for null.
         */
        public static class NullableDyedRecipeList extends DyeRecipeList {

            /**
             * Constructor for creating a list with a nullable color (used for a default or null value).
             *
             * @param filler A function that generates a recipe for each color, including null.
             */
            public NullableDyedRecipeList(Function<@Nullable DyeColor, GeneratedRecipe> filler) {
                super(filler);
                recipes[recipes.length - 1] = filler.apply(null); // Add a recipe for null color.
            }

            /**
             * Returns the total number of colors (including the nullable entry).
             *
             * @return The number of colors (including nullable).
             */
            @Override
            protected int getColorCount() {
                return COLOR_AMOUNT + 1;
            }

            /**
             * Retrieves the recipe for a given color, including a special entry for null.
             *
             * @param color The color to retrieve the recipe for.
             * @return The generated recipe.
             */
            @Override
            public GeneratedRecipe get(@Nullable DyeColor color) {
                return color == null ? recipes[recipes.length - 1] : super.get(color);
            }
        }
    }

    /**
     * A class that holds a list of item entries for dyed cloth, where each color corresponds to an entry.
     * This allows for easy access and management of dyed cloth items.
     *
     * @param <T> The type of item entry.
     */
    public static class DyeItemListCloth<T extends Item> implements Iterable<ItemEntry<T>> {
        private static final int COLOR_AMOUNT = DyeColor.values().length;
        private final ItemEntry<?>[] entries = new ItemEntry<?>[COLOR_AMOUNT];

        /**
         * Constructor to create the item list for dyed cloth.
         *
         * @param filler A function that fills the list with item entries for each color.
         */
        public DyeItemListCloth(Function<DyeColor, ItemEntry<? extends T>> filler) {
            for (DyeColor color : DyeColor.values()) {
                entries[color.ordinal()] = filler.apply(color);
            }
        }

        /**
         * Retrieves the item entry for a given color.
         *
         * @param color The color of the item entry.
         * @return The item entry for the color.
         */
        @SuppressWarnings("unchecked")
        public ItemEntry<T> get(DyeColor color) {
            return (ItemEntry<T>) entries[color.ordinal()];
        }

        /**
         * Checks if an item is contained within the list of item entries.
         *
         * @param block The item to check.
         * @return True if the item is found in the list, false otherwise.
         */
        public boolean contains(Item block) {
            for (ItemEntry<?> entry : entries) {
                if (entry.is(block)) return true;
            }
            return false;
        }

        /**
         * Converts the list of item entries into an array.
         *
         * @return An array of item entries.
         */
        @SuppressWarnings("unchecked")
        public ItemEntry<T>[] toArray() {
            return (ItemEntry<T>[]) Arrays.copyOf(entries, entries.length);
        }

        /**
         * Iterator to iterate over the item entries for each color.
         *
         * @return An iterator for the item entries.
         */
        @Override
        public Iterator<ItemEntry<T>> iterator() {
            return new Iterator<>() {
                private int index = 0;

                @Override
                public boolean hasNext() {
                    return index < entries.length;
                }

                @SuppressWarnings("unchecked")
                @Override
                public ItemEntry<T> next() {
                    if (!hasNext()) throw new NoSuchElementException();
                    return (ItemEntry<T>) entries[index++];
                }
            };
        }
    }

    /**
     * Enum representing the different cloths available for crafting.
     * Each cloth corresponds to a specific dye color and allows for easy access to item entries.
     */
    public enum Cloths {
        WHITE_CLOTH(DyeColor.WHITE),
        YELLOW_CLOTH(DyeColor.YELLOW),
        RED_CLOTH(DyeColor.RED),
        BLUE_CLOTH(DyeColor.BLUE),
        GREEN_CLOTH(DyeColor.GREEN),
        BLACK_CLOTH(DyeColor.BLACK),
        ORANGE_CLOTH(DyeColor.ORANGE),
        PURPLE_CLOTH(DyeColor.PURPLE),
        BROWN_CLOTH(DyeColor.BROWN),
        PINK_CLOTH(DyeColor.PINK),
        CYAN_CLOTH(DyeColor.CYAN),
        LIGHT_GRAY_CLOTH(DyeColor.LIGHT_GRAY),
        GRAY_CLOTH(DyeColor.GRAY),
        LIGHT_BLUE_CLOTH(DyeColor.LIGHT_BLUE),
        LIME_CLOTH(DyeColor.LIME),
        MAGENTA_CLOTH(DyeColor.MAGENTA);

        private static final Map<DyeColor, ItemEntry<ClothItem>> clothMap = new EnumMap<>(DyeColor.class);

        static {
            for (DyeColor color : DyeColor.values()) {
                clothMap.put(color, CNItems.CLOTHS.get(color));
            }
        }

        private final DyeColor color;

        Cloths(DyeColor color) {
            this.color = color;
        }

        /**
         * Retrieves the item entry associated with this cloth's color.
         *
         * @return The item entry for the cloth color.
         */
        public ItemEntry<ClothItem> getItem() {
            return clothMap.get(this.color);
        }

        /**
         * Retrieves the item entry for a specific color.
         *
         * @param color The color of the cloth item.
         * @return The item entry for the given color.
         */
        public static ItemEntry<ClothItem> getByColor(DyeColor color) {
            return clothMap.get(color);
        }
    }
}
