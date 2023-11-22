package net.ynov.createnuclear.block;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FlowingFluid;
import net.ynov.createnuclear.CreateNuclear;

public class ModBlocks {


    public static final Block DEEPSLATE_URANIUM_ORE = registerBlock("deepslate_uranium_ore", new DropExperienceBlock(FabricBlockSettings.copyOf(Blocks.DEEPSLATE).strength(4f), UniformInt.of(2, 4)));
    public static final Block URANIUM_ORE = registerBlock("uranium_ore", new DropExperienceBlock(FabricBlockSettings.copyOf(Blocks.STONE).strength(4f), UniformInt.of(2, 4)));
    public static final Block REACTOR_CONTROLLER = registerBlock("reactor_controller", new Block(FabricBlockSettings.copyOf(Blocks.STONE).strength(4f)));

    //nouvelle flamme plus feu de camp enrichime flame
    public static final Block REACTOR_CORE = registerBlock("reactor_core", new Block(FabricBlockSettings.copyOf(Blocks.COBBLESTONE).strength(4f)));

    private static void AddBlockToBuildingBlockItemGroup(FabricItemGroupEntries entries) {
        entries.accept(DEEPSLATE_URANIUM_ORE, CreativeModeTab.TabVisibility.PARENT_TAB_ONLY);
        entries.accept(URANIUM_ORE, CreativeModeTab.TabVisibility.PARENT_TAB_ONLY);
<<<<<<< HEAD
        entries.accept(REACTOR_CONTROLLER, CreativeModeTab.TabVisibility.PARENT_TAB_ONLY);
=======
        entries.accept(REACTOR_CORE, CreativeModeTab.TabVisibility.PARENT_TAB_ONLY);
>>>>>>> 4679b5c (block Reactor_core creation)
    }

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(CreateNuclear.MOD_ID, name), block);
    }
    private static Item registerBlockItem(String name, Block block) {
        return Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(CreateNuclear.MOD_ID, name), new BlockItem(block, new FabricItemSettings()));
    }




    public static void registerModBlocks() {
        CreateNuclear.LOGGER.info("Registering ModBlocks for " + CreateNuclear.MOD_ID);
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.NATURAL_BLOCKS).register(ModBlocks::AddBlockToBuildingBlockItemGroup);
    }
}
