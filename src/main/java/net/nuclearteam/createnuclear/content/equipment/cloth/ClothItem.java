package net.nuclearteam.createnuclear.content.equipment.cloth;

import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.DyeColor;
import net.nuclearteam.createnuclear.CNItems;

import java.util.*;

@SuppressWarnings("unused")
public class ClothItem extends Item {

    private final DyeColor color;

    public ClothItem(Item.Properties properties, DyeColor color) {
        super(properties);
        this.color = color;
    }

    public DyeColor getColor() {
      return color;
    }

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

        public ItemEntry<ClothItem> getItem() {
            return clothMap.get(this.color);
        }

        public DyeColor getColor() {
            return color;
        }

        public static ItemEntry<ClothItem> getByColor(DyeColor color) {
            return clothMap.get(color);
        }

    }
}