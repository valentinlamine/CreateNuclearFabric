package net.ynov.createnuclear.block;

import com.simibubi.create.content.fluids.pipes.FluidPipeBlock;
import com.simibubi.create.content.kinetics.motor.CreativeMotorBlock;
import com.simibubi.create.content.kinetics.waterwheel.WaterWheelBlock;
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
import net.ynov.createnuclear.CreateNuclear;

public class ModBlocks {
    public static final Block DEEPSLATE_URANIUM_ORE = registerBlock("deepslate_uranium_ore", new DropExperienceBlock(FabricBlockSettings.copyOf(Blocks.DEEPSLATE).strength(4f), UniformInt.of(2, 4)));
    public static final Block URANIUM_ORE = registerBlock("uranium_ore", new DropExperienceBlock(FabricBlockSettings.copyOf(Blocks.STONE).strength(4f), UniformInt.of(2, 4)));
    public static final Block REACTOR_CONTROLLER = registerBlock("reactor_controller", new Block(FabricBlockSettings.copyOf(Blocks.COBBLESTONE).strength(4f)));
    public static final Block REACTOR_CASING = registerBlock("reactor_casing", new Block(FabricBlockSettings.copyOf(Blocks.STONE).strength(4f)));
    public static final Block REACTOR_MAIN_FRAME = registerBlock("reactor_main_frame", new Block(FabricBlockSettings.copyOf(Blocks.STONE).strength(4f)));
    public static final Block COOLING_FRAME = registerBlock("cooling_frame", new Block(FabricBlockSettings.copyOf(Blocks.STONE).strength(4f)));
    public static final Block REINFORCED_PIPE = registerBlock("reinforced_pipe", new FluidPipeBlock(FabricBlockSettings.copyOf(Blocks.STONE).strength(4f)));
    public static final Block REACTOR_CORE = registerBlock("reactor_core", new Block(FabricBlockSettings.copyOf(Blocks.COBBLESTONE).strength(4f)));
    public static final Block REACTOR_OUTPUT = registerBlock("reactor_output", new CreativeMotorBlock(FabricBlockSettings.copyOf(Blocks.STONE).strength(4f)));

    private static void AddBlockToBuildingBlockItemGroup(FabricItemGroupEntries entries) {
        entries.accept(DEEPSLATE_URANIUM_ORE, CreativeModeTab.TabVisibility.PARENT_TAB_ONLY);
        entries.accept(URANIUM_ORE, CreativeModeTab.TabVisibility.PARENT_TAB_ONLY);
        entries.accept(REACTOR_CONTROLLER, CreativeModeTab.TabVisibility.PARENT_TAB_ONLY);
        entries.accept(REACTOR_CORE, CreativeModeTab.TabVisibility.PARENT_TAB_ONLY);
        entries.accept(REACTOR_CASING, CreativeModeTab.TabVisibility.PARENT_TAB_ONLY);
        entries.accept(REACTOR_MAIN_FRAME, CreativeModeTab.TabVisibility.PARENT_TAB_ONLY);
        entries.accept(COOLING_FRAME, CreativeModeTab.TabVisibility.PARENT_TAB_ONLY);
        entries.accept(REINFORCED_PIPE, CreativeModeTab.TabVisibility.PARENT_TAB_ONLY);
        entries.accept(REACTOR_CONTROLLER, CreativeModeTab.TabVisibility.PARENT_TAB_ONLY);
        entries.accept(REACTOR_OUTPUT, CreativeModeTab.TabVisibility.PARENT_TAB_ONLY);
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
