package net.ynov.createnuclear.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.ynov.createnuclear.CreateNuclear;

public class ModGroup {
    public static final CreativeModeTab MAIN = FabricItemGroup.builder().icon(ModGroup::MakeIcon).title(Component.translatable("itemGroup.createnuclear.main")).build();
    public static final ResourceKey<CreativeModeTab> MAIN_KEY = ResourceKey.create(Registries.CREATIVE_MODE_TAB, new ResourceLocation(CreateNuclear.MOD_ID));

    private static ItemStack MakeIcon() {
        return new ItemStack(ModItems.URANIUM_POWDER);
    }

    public static void registrer() {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, MAIN_KEY, MAIN);
        ItemGroupEvents.modifyEntriesEvent(MAIN_KEY).register(content -> {
            CreateNuclear.REGISTRATE.getAll(Registries.ITEM).forEach(entry -> {
                content.accept(entry.get());
            });
        });
    }
}
