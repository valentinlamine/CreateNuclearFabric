package net.nuclearteam.createnuclear.content.equipment.armor.trim;

import com.simibubi.create.AllItems;
import net.minecraft.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.armortrim.TrimPattern;
import net.minecraftforge.registries.ForgeRegistries;
import net.nuclearteam.createnuclear.CreateNuclear;

import java.util.Locale;

public class ArmorTrimPatterns {
    public static final ResourceKey<TrimPattern> ANTI_RADIATION_TRIM_PATTERN = ResourceKey.create(Registries.TRIM_PATTERN, CreateNuclear.asResource("anti_radiation_trim_pattern"));

    public static void bootstrap(BootstapContext<TrimPattern> context) {
        register(context, AllItems.PRECISION_MECHANISM.get(), ANTI_RADIATION_TRIM_PATTERN);
    }

    private static void register(BootstapContext<TrimPattern> context, Item item, ResourceKey<TrimPattern> key, Component component) {
        TrimPattern trimPattern = new TrimPattern(
                key.location(),
                ForgeRegistries.ITEMS.getHolder(item).get(),
                component);
        context.register(key, trimPattern);
    }

    private static void register(BootstapContext<TrimPattern> context, Item item, ResourceKey<TrimPattern> key) {
        register(context, item, key, Component.translatable(Util.makeDescriptionId("trim_pattern", key.location())));
    }

    private static void register(BootstapContext<TrimPattern> context, Item item, DyeColor color, ResourceKey<TrimPattern> key) {
        register(context, item, key, Component.translatable(Util.makeDescriptionId(String.join(".", "trim_pattern", color.name().toLowerCase(Locale.ROOT)), key.location())));
    }
}
