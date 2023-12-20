package net.ynov.createnuclear.block;

import com.simibubi.create.AllItems;
import com.simibubi.create.AllMovementBehaviours;
import com.simibubi.create.AllTags;
import com.simibubi.create.Create;
import com.simibubi.create.content.kinetics.BlockStressDefaults;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.BlockEntry;
import io.github.fabricators_of_create.porting_lib.tags.Tags;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import net.ynov.createnuclear.CreateNuclear;
import net.ynov.createnuclear.item.ModGroup;

import static com.simibubi.create.Create.REGISTRATE;
import static net.minecraft.world.level.block.Blocks.litBlockEmission;
import net.minecraft.world.level.material.PushReaction;
import net.ynov.createnuclear.tools.UraniumFireBlock;
import net.ynov.createnuclear.tools.UraniumOreBlock;

public class ModBlocks {

    public static final Block DEEPSLATE_URANIUM_ORE = registerBlock("deepslate_uranium_ore", new UraniumOreBlock(FabricBlockSettings.copyOf(Blocks.DEEPSLATE).strength(5f, 3f)));
    public static final Block URANIUM_ORE = registerBlock("uranium_ore", new UraniumOreBlock(FabricBlockSettings.copyOf(Blocks.STONE).strength(5f, 3f)));
    public static final Block RAW_URANIUM_BLOCK = registerBlock("raw_uranium_block", new Block(FabricBlockSettings.copyOf(Blocks.STONE).strength(6f, 6.5f)));
    public static final Block ENRICHED_SOUL_SOIL = registerBlock("enriched_soul_soil", new Block(FabricBlockSettings.copyOf(Blocks.SOUL_SOIL).strength(8f, 7f)));
    public static final Block ENRICHING_FIRE = registerBlockNoItem("enriching_fire", new UraniumFireBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WATER).replaceable().noCollission().instabreak().lightLevel(state -> 15).sound(SoundType.MOSS).pushReaction(PushReaction.DESTROY)));
    public static final Block ENRICHING_CAMPFIRE = registerBlock("enriching_campfire", new CampfireBlock(false, 5, BlockBehaviour.Properties.of().mapColor(MapColor.PODZOL).instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD).lightLevel(litBlockEmission(10)).noOcclusion().ignitedByLava()));

    private static void AddBlockToCreateNuclearItemGroup(FabricItemGroupEntries entries) {
        entries.accept(DEEPSLATE_URANIUM_ORE, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        entries.accept(URANIUM_ORE, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        entries.accept(RAW_URANIUM_BLOCK, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        entries.accept(ENRICHED_SOUL_SOIL, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        entries.accept(ENRICHING_CAMPFIRE, CreativeModeTab.TabVisibility.PARENT_TAB_ONLY);

    }

    // COBBLESTONE = register("cobblestone", new Block(Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(2.0F, 6.0F)));

 
    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(CreateNuclear.MOD_ID, name), block);
    }
     private static Block registerBlockNoItem(String name, Block block) {
        return Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(CreateNuclear.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block) {
        return Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(CreateNuclear.MOD_ID, name), new BlockItem(block, new FabricItemSettings()));
    }


    public static void registerModBlocks() {
        CreateNuclear.LOGGER.info("Registering ModBlocks for " + CreateNuclear.MOD_ID);
        ItemGroupEvents.modifyEntriesEvent(ModGroup.MAIN_KEY).register(ModBlocks::AddBlockToCreateNuclearItemGroup);
    }
}
