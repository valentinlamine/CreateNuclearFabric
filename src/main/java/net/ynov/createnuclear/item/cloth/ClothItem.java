package net.ynov.createnuclear.item.cloth;

import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import com.simibubi.create.foundation.data.recipe.CreateRecipeProvider.GeneratedRecipe;
import net.ynov.createnuclear.item.CNItems;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Function;

public class ClothItem extends Item {
    private final DyeColor color;

    public ClothItem(Properties properties, DyeColor color) {
        super(properties);
        this.color = color;
    }


    public static class DyeRecipeList implements Iterable<GeneratedRecipe> {
        private static final int COLOR_AMOUNT = DyeColor.values().length;

        protected final GeneratedRecipe[] recipes = new GeneratedRecipe[getColorCount()];

        public DyeRecipeList(Function<@NotNull DyeColor, GeneratedRecipe> filler) {
            for (DyeColor color : DyeColor.values()) {
                recipes[color.ordinal()] = filler.apply(color);
            }
        }

        protected int getColorCount() {
            return COLOR_AMOUNT;
        }

        public GeneratedRecipe get(@Nullable DyeColor color) {
            return recipes[color.ordinal()];
        }

        public GeneratedRecipe[] toArrays() {
            return Arrays.copyOf(recipes, recipes.length);
        }

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

        public static class NullableDyedRecipeList extends DyeRecipeList {
            public NullableDyedRecipeList(Function<@Nullable DyeColor, GeneratedRecipe> fillter) {
                super(fillter);
                recipes[recipes.length - 1] = fillter.apply(null);
            }

            @Override
            protected int getColorCount() {
                return COLOR_AMOUNT + 1;
            }

            @Override
            public GeneratedRecipe get(@Nullable DyeColor color) {
                return color == null ? recipes[recipes.length - 1] : super.get(color);
            }
        }
    }

    public static class DyeItemList<T extends Item> implements Iterable<ItemEntry<T>> {
        private static final int COLOR_AMOUNT = DyeColor.values().length;

        private final ItemEntry<?>[] entrys = new ItemEntry<?>[COLOR_AMOUNT];

        public DyeItemList(Function<DyeColor, ItemEntry<? extends T>> filler) {
            for (DyeColor color : DyeColor.values()) {
                entrys[color.ordinal()] = filler.apply(color);
            }
        }

        @SuppressWarnings("unchecked")
        public ItemEntry<T> get(DyeColor color) {
            return (ItemEntry<T>) entrys[color.ordinal()];
        }

        public boolean contains(Item block) {
            for (ItemEntry<?> entry : entrys) {
                if (entry.is(block)) return true;
            }
            return false;
        }

        @SuppressWarnings("unchecked")
        public ItemEntry<T>[] toArray() {
            return (ItemEntry<T>[]) Arrays.copyOf(entrys, entrys.length);
        }

        @Override
        public Iterator<ItemEntry<T>> iterator() {
            return new Iterator<>() {
                private int index = 0;
                @Override
                public boolean hasNext() {
                    return index < entrys.length;
                }

                @SuppressWarnings("unchecked")
                @Override
                public ItemEntry<T> next() {
                    if (!hasNext()) throw new NoSuchElementException();
                    return (ItemEntry<T>) entrys[index++];
                }
            };
        }
    }

    public enum Cloths {
        WHITE_CLOTH(CNItems.CLOTHS.get(DyeColor.WHITE)),
        YELLOW_CLOTH(CNItems.CLOTHS.get(DyeColor.YELLOW)),
        RED_CLOTH(CNItems.CLOTHS.get(DyeColor.RED)),
        BLUE_CLOTH(CNItems.CLOTHS.get(DyeColor.BLUE)),
        GREEN_CLOTH(CNItems.CLOTHS.get(DyeColor.GREEN)),
        BLACK_CLOTH(CNItems.CLOTHS.get(DyeColor.BLACK)),
        ORANGE_CLOTH(CNItems.CLOTHS.get(DyeColor.ORANGE)),
        PURPLE_CLOTH(CNItems.CLOTHS.get(DyeColor.PURPLE)),
        BROWN_CLOTH(CNItems.CLOTHS.get(DyeColor.BROWN)),
        PINK_CLOTH(CNItems.CLOTHS.get(DyeColor.PINK)),
        CYAN_CLOTH(CNItems.CLOTHS.get(DyeColor.CYAN)),
        LIGHT_GRAY_CLOTH(CNItems.CLOTHS.get(DyeColor.LIGHT_GRAY)),
        GRAY_CLOTH(CNItems.CLOTHS.get(DyeColor.GRAY)),
        LIGHT_BLUE_CLOTH(CNItems.CLOTHS.get(DyeColor.LIGHT_BLUE)),
        LIME_CLOTH(CNItems.CLOTHS.get(DyeColor.LIME)),
        MAGENTA_CLOTH(CNItems.CLOTHS.get(DyeColor.MAGENTA))
        ;

        private final ItemEntry<ClothItem> item;

        Cloths(ItemEntry<ClothItem> clothItemItemEntry) {
            this.item = clothItemItemEntry;
        }

        public ItemEntry<ClothItem> getItem() {
            return item;
        }

    }
}
