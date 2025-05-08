package net.nuclearteam.createnuclear.compact.archEx;

import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.world.level.material.MapColor;

import java.util.HashMap;
import java.util.Map;

public enum CNMapColorSerialization {
    COLOR_BLUE(MapColor.COLOR_BLUE, "blue"),
    COLOR_RED(MapColor.COLOR_RED, "red"),
    SAND(MapColor.SAND, "sand"),
    TERRACOTTA_YELLOW(MapColor.TERRACOTTA_YELLOW, "yellow_terracotta"),
    COLOR_BROWN(MapColor.COLOR_BROWN, "brown"),
    TERRACOTTA_GRAY(MapColor.TERRACOTTA_GRAY, "gray_terracotta"),
    WARPED_NYLIUM(MapColor.WARPED_NYLIUM, "warped_nylium"),
    DIRT(MapColor.DIRT, "dirt"),
    QUARTZ(MapColor.QUARTZ, "quartz"),
    STONE(MapColor.STONE, "stone"),
    TERRACOTTA_WHITE(MapColor.TERRACOTTA_WHITE, "white_terracotta"),
    TERRACOTTA_BROWN(MapColor.TERRACOTTA_BROWN, "brown_terracotta"),
    DEEPSLATE(MapColor.DEEPSLATE, "deepslate"),
    COLOR_GREEN(MapColor.COLOR_GREEN, "green"),
    ;

    private final MapColor color;
    private final String name;

    CNMapColorSerialization(MapColor color, String name) {
        this.color = color;
        this.name = name;
    }

    CNMapColorSerialization(MapColor color) {
        this.color = color;
        this.name = Lang.asId(name());
    }

    private static final Map<MapColor, CNMapColorSerialization> lookup = new HashMap<>();

    static {
        for (CNMapColorSerialization entry : CNMapColorSerialization.values()) {
            lookup.put(entry.color, entry);
        }
    }

    public static String getArchExName(MapColor color) {
        CNMapColorSerialization entry = lookup.get(color);
        if (entry == null) {
            throw new IllegalArgumentException("Unsupported MapColor: " + color);
        }
        return entry.name;
    }
}
