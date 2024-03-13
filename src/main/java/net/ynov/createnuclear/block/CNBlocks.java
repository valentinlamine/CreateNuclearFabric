package net.ynov.createnuclear.block;

import com.simibubi.create.AllTags;
import com.simibubi.create.content.kinetics.BlockStressDefaults;
import com.simibubi.create.content.kinetics.motor.CreativeMotorGenerator;
import com.simibubi.create.foundation.data.SharedProperties;
import com.simibubi.create.foundation.utility.Couple;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.ynov.createnuclear.CreateNuclear;
import net.ynov.createnuclear.blockentity.ReinforcedGlassBlock;
import net.ynov.createnuclear.groups.CNGroup;
import net.ynov.createnuclear.energy.ReactorOutput;
import net.ynov.createnuclear.tools.EnrichingCampfire;
import net.ynov.createnuclear.tools.UraniumFireBlock;
import net.ynov.createnuclear.tools.UraniumOreBlock;

import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;
import static net.minecraft.world.level.block.Blocks.litBlockEmission;

public class CNBlocks {

    static {
        CreateNuclear.REGISTRATE.useCreativeTab(CNGroup.MAIN_KEY);
    }

    public static final BlockEntry<UraniumOreBlock> DEEPSLATE_URANIUM_ORE =
            CreateNuclear.REGISTRATE.block("deepslate_uranium_ore", UraniumOreBlock::new)
                    .initialProperties(CNBlocks::DIAMOND_ORE)
                    .simpleItem()
                    .transform(pickaxeOnly())
                    .register();

    public static final BlockEntry<UraniumOreBlock> URANIUM_ORE =
            CreateNuclear.REGISTRATE.block("uranium_ore", UraniumOreBlock::new)
                    .initialProperties(SharedProperties::stone)
                    .simpleItem()
                    .transform(pickaxeOnly())
                    .register();

    public static final BlockEntry<Block> LEAD_ORE =
            CreateNuclear.REGISTRATE.block("lead_ore", Block::new)
                    .initialProperties(SharedProperties::stone)
                    .simpleItem()
                    .transform(pickaxeOnly())
                    .register();

    public static final BlockEntry<Block> RAW_URANIUM_BLOCK =
            CreateNuclear.REGISTRATE.block("raw_uranium_block", Block::new)
                    .initialProperties(SharedProperties::stone)
                    .simpleItem()
                    .transform(pickaxeOnly())
                    .transform(BlockStressDefaults.setCapacity(16384.0))
                    .transform(BlockStressDefaults.setGeneratorSpeed(() -> Couple.create(0, 256)))
                    .register();

    public static final BlockEntry<Block> RAW_LEAD_BLOCK =
            CreateNuclear.REGISTRATE.block("raw_lead_block", Block::new)
                    .initialProperties(SharedProperties::stone)
                    .simpleItem()
                    .transform(pickaxeOnly())
                    .register();

    public static final BlockEntry<Block> ENRICHED_SOUL_SOIL =
            CreateNuclear.REGISTRATE.block("enriched_soul_soil", Block::new)
                    .initialProperties(CNBlocks::SOUL_SOIL)
                    .simpleItem()
                    .transform(pickaxeOnly())
                    .register();

    public static final BlockEntry<UraniumFireBlock> ENRICHING_FIRE =
            CreateNuclear.REGISTRATE.block("enriching_fire", UraniumFireBlock::new)
                    .properties(BlockBehaviour.Properties::replaceable)
                    .properties(BlockBehaviour.Properties::noCollission)
                    .properties(p -> p.lightLevel(UraniumFireBlock::getLight))
                    .register();

    public static final BlockEntry<ReinforcedGlassBlock> REINFORCED_GLASS =
            CreateNuclear.REGISTRATE.block("reinforced_glass", ReinforcedGlassBlock::new)
                    .initialProperties(CNBlocks::GLASS)
                    .properties(p -> p.explosionResistance(1200F))
                    .properties(p -> p.destroyTime(2F))
                    .simpleItem()
                    .register();
    public static final BlockEntry<ReactorOutput> REACTOR_OUTPUT =
            CreateNuclear.REGISTRATE.block("reactor_output", ReactorOutput::new)
                    .properties(p -> p.explosionResistance(1200F))
                    .properties(p -> p.destroyTime(4F))
			.tag(AllTags.AllBlockTags.SAFE_NBT.tag)
			.transform(pickaxeOnly())
			.blockstate(new CreativeMotorGenerator()::generate)
			.transform(BlockStressDefaults.setCapacity(200))
			.transform(BlockStressDefaults.setGeneratorSpeed(() -> Couple.create(0, 256)))
			.item()
			.transform(customItemModel())
			.register();
    public static final BlockEntry<EnrichingCampfire> ENRICHING_CAMPFIRE = CreateNuclear.REGISTRATE
            .block("enriching_campfire", (properties) -> new EnrichingCampfire(true, 5, BlockBehaviour.Properties.of().mapColor(MapColor.PODZOL).instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD).lightLevel(litBlockEmission(10)).noOcclusion().ignitedByLava()))
            .properties(BlockBehaviour.Properties::replaceable)
            //.initialProperties(CNBlocks::DIAMOND_ORE)
            .simpleItem()
            .addLayer(() -> RenderType::cutoutMipped)
            .transform(pickaxeOnly())
            .register();
    public static final BlockEntry<ReactorController> REACTOR_CONTROLLER =
            CreateNuclear.REGISTRATE.block("reactor_controller", ReactorController::new)
                    .properties(p -> p.explosionResistance(1200F))
                    .properties(p -> p.destroyTime(4F))
                    .simpleItem()
                    .register();
    public static final BlockEntry<ReactorBlock> REACTOR_CORE =
            CreateNuclear.REGISTRATE.block("reactor_core", ReactorBlock::new)
                    .properties(p -> p.explosionResistance(1200F))
                    .properties(p -> p.destroyTime(4F))
                    .simpleItem()
                    .register();

    public static final BlockEntry<ReactorBlock> COOLING_FRAME =
            CreateNuclear.REGISTRATE.block("cooling_frame", ReactorBlock::new)
                    .properties(p -> p.explosionResistance(1200F))
                    .properties(p -> p.destroyTime(4F))
                    .simpleItem()
                    .register();

    public static final BlockEntry<ReactorBlock> REACTOR_CASING =
            CreateNuclear.REGISTRATE.block("reactor_casing", ReactorBlock::new)
                    .properties(p -> p.explosionResistance(1200F))
                    .properties(p -> p.destroyTime(4F))
                    .simpleItem()
                    .register();

    public static final BlockEntry<ReactorBlock> REACTOR_MAIN_FRAME =
            CreateNuclear.REGISTRATE.block("reactor_main_frame", ReactorBlock::new)
                    .properties(p -> p.explosionResistance(1200F))
                    .properties(p -> p.destroyTime(4F))
                    .simpleItem()
                    .register();

    public static Block SOUL_SOIL() {
        return Blocks.SOUL_SOIL;
    }

    public static Block GLASS() {
        return Blocks.GLASS;
    }

    public static Block DIAMOND_ORE() {
        return Blocks.DIAMOND_BLOCK;
    }

    private static void AddBlockToCreateNuclearItemGroup(FabricItemGroupEntries entries) {
        entries.accept(ENRICHING_CAMPFIRE, CreativeModeTab.TabVisibility.PARENT_TAB_ONLY);
    }

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(CreateNuclear.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block) {
        return Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(CreateNuclear.MOD_ID, name), new BlockItem(block, new FabricItemSettings()));
    }

    public static void registerCNBlocks() {
        CreateNuclear.LOGGER.info("Registering ModBlocks for " + CreateNuclear.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(CNGroup.MAIN_KEY).register(CNBlocks::AddBlockToCreateNuclearItemGroup);
    }
}
