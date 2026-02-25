package net.nuclearteam.createnuclear.content.equipment.armor.trim;

import net.minecraft.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.armortrim.TrimPattern;
import net.nuclearteam.createnuclear.CreateNuclear;
import net.nuclearteam.createnuclear.content.equipment.cloth.ClothItem;

import java.util.Locale;
import java.util.Map;

public class ArmorTrimMaterials {
    public static final ResourceKey<TrimMaterial> CLOTH = ResourceKey.create(Registries.TRIM_MATERIAL, CreateNuclear.asResource("cloth_trim_materiels"));

    public static void bootstrap(BootstapContext<TrimMaterial> context) {
        for (ClothItem.Cloths cloths : ClothItem.Cloths.values()) {
            register(context, cloths.getrimMaterialResourceKey(), cloths.getItem().get(), cloths.getColor(), Style.EMPTY.withColor(cloths.getColor().getId()), 0.8F);
        }
    }

    private static void register(BootstapContext<TrimMaterial> context, ResourceKey<TrimMaterial> trimKey, Item item, Style style, float itemModelIndex, MutableComponent component) {
        TrimMaterial trimmaterial = TrimMaterial.create(trimKey.location().getPath(), item, itemModelIndex,
                component.withStyle(style), Map.of());
        context.register(trimKey, trimmaterial);
    }

    private static void register(BootstapContext<TrimMaterial> context, ResourceKey<TrimMaterial> trimKey, Item item, Style style, float itemModelIndex) {
        register(context, trimKey, item, style, itemModelIndex, Component.translatable(Util.makeDescriptionId("trim_pattern", trimKey.location())));
    }

    private static void register(BootstapContext<TrimMaterial> context, ResourceKey<TrimMaterial> trimKey, Item item, DyeColor color, Style style, float itemModelIndex) {
        register(context, trimKey, item, style, itemModelIndex, Component.translatable(Util.makeDescriptionId(String.join(".", "trim_pattern", color.name().toLowerCase(Locale.ROOT)), trimKey.location())));
    }
}