package net.ynov.createnuclear.item;

import com.simibubi.create.AllCreativeModeTabs;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.ynov.createnuclear.CreateNuclear;

import java.util.function.Supplier;


public class ModItemsGroups {

    private static AllCreativeModeTabs.TabInfo MAIN_TAB = register("main",
            () -> FabricItemGroup.builder()
                    .title(Component.translatable("itemGroup.createnuclear"))
                    .icon(() -> new ItemStack(ModItems.URANIUM_POWDER))
                    .build());

    private static AllCreativeModeTabs.TabInfo register(String name, Supplier<CreativeModeTab> supplier) {
        ResourceLocation id = new ResourceLocation(CreateNuclear.MOD_ID, name);
        ResourceKey<CreativeModeTab> key = ResourceKey.create(Registries.CREATIVE_MODE_TAB, id);
        CreativeModeTab tab = supplier.get();
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, key, tab);
        return new AllCreativeModeTabs.TabInfo(key, tab);
    }

    CreativeModeTab getBaseTab() {
        return MAIN_TAB.tab();
    }

    public static ResourceKey<CreativeModeTab> getBaseTabKey() {
        return MAIN_TAB.key();
    }



    public static void registerModItems() {
        CreateNuclear.LOGGER.info("Registering mod creative tab for " + CreateNuclear.MOD_ID);
    }

}