package net.nuclearteam.createnuclear.block;

import com.simibubi.create.AllTags;
import com.simibubi.create.content.decoration.encasing.EncasedCTBehaviour;
import com.simibubi.create.content.kinetics.BlockStressDefaults;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import com.tterrag.registrate.util.entry.BlockEntry;
import io.github.fabricators_of_create.porting_lib.models.generators.ConfiguredModel;
import io.github.fabricators_of_create.porting_lib.models.generators.ModelFile;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.nuclearteam.createnuclear.CreateNuclear;
import net.nuclearteam.createnuclear.blockentity.ReinforcedGlassBlock;
import net.nuclearteam.createnuclear.item.CNItems;
import net.nuclearteam.createnuclear.multiblock.controller.ReactorControllerBlock;
import net.nuclearteam.createnuclear.multiblock.controller.ReactorControllerGenerator;
import net.nuclearteam.createnuclear.multiblock.core.ReactorCoreBlock;
import net.nuclearteam.createnuclear.multiblock.output.ReactorOutput;
import net.nuclearteam.createnuclear.multiblock.output.ReactorOutputGenerator;
import net.nuclearteam.createnuclear.multiblock.casing.ReactorCasingBlock;
import net.nuclearteam.createnuclear.multiblock.frame.ReactorFrameBlock;
import net.nuclearteam.createnuclear.multiblock.frame.ReactorFrameItem;
import net.nuclearteam.createnuclear.multiblock.input.ReactorInput;
import net.nuclearteam.createnuclear.multiblock.input.ReactorInputGenerator;
import net.nuclearteam.createnuclear.multiblock.cooler.ReactorCoolerBlock;
import net.nuclearteam.createnuclear.tags.CNTag;
import net.nuclearteam.createnuclear.tools.EnrichingCampfireBlock;
import net.nuclearteam.createnuclear.tools.EnrichingFireBlock;
import net.nuclearteam.createnuclear.tools.EventTriggerBlock;
import net.nuclearteam.createnuclear.tools.UraniumOreBlock;

import static com.simibubi.create.foundation.data.CreateRegistrate.casingConnectivity;
import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.axeOrPickaxe;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;

public class CNBlocks {

    public static final BlockEntry<UraniumOreBlock> DEEPSLATE_URANIUM_ORE =
            CreateNuclear.REGISTRATE.block("deepslate_uranium_ore", UraniumOreBlock::new)
                    .initialProperties(CNBlocks::getDiamondOre)
                    .simpleItem()
                    .properties(UraniumOreBlock.litBlockEmission())
                    .transform(pickaxeOnly())
                    .loot((lt, b) -> lt.add(b,
                        RegistrateBlockLootTables.createSilkTouchDispatchTable(b,
                            lt.applyExplosionDecay(b, LootItem.lootTableItem(CNItems.RAW_URANIUM)
                                .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))
                    ))))
                    .tag(BlockTags.NEEDS_DIAMOND_TOOL,
                            BlockTags.NEEDS_IRON_TOOL,
                            CNTag.forgeBlockTag("ores"),
                            CNTag.forgeBlockTag("ores_in_ground/deepslate"),
                            CNTag.BlockTags.URANIUM_ORES.tag
                    )
                    .item()
                    .tag(CNTag.ItemTags.URANIUM_ORES.tag)
                    .build()
                    .register();

    public static final BlockEntry<UraniumOreBlock> URANIUM_ORE =
            CreateNuclear.REGISTRATE.block("uranium_ore", UraniumOreBlock::new)
                    .initialProperties(SharedProperties::stone)
                    .properties(UraniumOreBlock.litBlockEmission())
                    .simpleItem()
                    .transform(pickaxeOnly())
                    .loot((lt, b) -> lt.add(b,
                        RegistrateBlockLootTables.createSilkTouchDispatchTable(b,
                            lt.applyExplosionDecay(b, LootItem.lootTableItem(CNItems.RAW_URANIUM)
                                .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))
                    ))))
                    .tag(BlockTags.NEEDS_DIAMOND_TOOL,
                            BlockTags.NEEDS_IRON_TOOL,
                            CNTag.forgeBlockTag("ores"),
                            CNTag.forgeBlockTag("ores_in_ground/stone"),
                            CNTag.BlockTags.URANIUM_ORES.tag
                    )
                    .item()
                    .tag(CNTag.ItemTags.URANIUM_ORES.tag)
                    .build()
                    .register();

    public static final BlockEntry<Block> DEEPSLATE_LEAD_ORE =
            CreateNuclear.REGISTRATE.block("deepslate_lead_ore", Block::new)
                    .initialProperties(CNBlocks::getDiamondOre)
                    .simpleItem()
                    .transform(pickaxeOnly())
                    .loot((lt, b) -> lt.add(b,
                        RegistrateBlockLootTables.createSilkTouchDispatchTable(b,
                            lt.applyExplosionDecay(b, LootItem.lootTableItem(CNItems.RAW_LEAD)
                                .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))
                    ))))
                    .tag(BlockTags.NEEDS_IRON_TOOL,
                            CNTag.forgeBlockTag("ores"),
                            CNTag.forgeBlockTag("ores_in_ground/deepslate"),
                            CNTag.BlockTags.LEAD_ORES.tag

                    )
                    .item()
                    .tag(CNTag.ItemTags.LEAD_ORES.tag)
                    .build()
                    .register();

    public static final BlockEntry<Block> LEAD_ORE =
            CreateNuclear.REGISTRATE.block("lead_ore", Block::new)
                    .initialProperties(SharedProperties::stone)
                    .simpleItem()
                    .transform(pickaxeOnly())
                    .loot((lt, b) -> lt.add(b,
                        RegistrateBlockLootTables.createSilkTouchDispatchTable(b,
                            lt.applyExplosionDecay(b, LootItem.lootTableItem(CNItems.RAW_LEAD)
                                .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))
                    ))))
                    .tag(BlockTags.NEEDS_IRON_TOOL,
                            CNTag.forgeBlockTag("ores"),
                            CNTag.forgeBlockTag("ores_in_ground/stone"),
                            CNTag.BlockTags.LEAD_ORES.tag
                    )
                    .item()
                    .tag(CNTag.ItemTags.LEAD_ORES.tag)
                    .build()
                    .register();

    public static final BlockEntry<Block> RAW_URANIUM_BLOCK =
            CreateNuclear.REGISTRATE.block("raw_uranium_block", Block::new)
                    .initialProperties(SharedProperties::stone)
                    .simpleItem()
                    .transform(pickaxeOnly())
                    .tag(BlockTags.NEEDS_DIAMOND_TOOL,
                            CNTag.forgeBlockTag("storage_blocks/raw_uranium"))
                    .register();

    public static final BlockEntry<Block> RAW_LEAD_BLOCK =
            CreateNuclear.REGISTRATE.block("raw_lead_block", Block::new)
                    .initialProperties(SharedProperties::stone)
                    .simpleItem()
                    .transform(pickaxeOnly())
                    .tag(BlockTags.NEEDS_IRON_TOOL,
                            CNTag.forgeBlockTag("storage_blocks/raw_lead"))
                    .register();

    public static final BlockEntry<Block> LEAD_BLOCK =
            CreateNuclear.REGISTRATE.block("lead_block", Block::new)
                    .initialProperties(SharedProperties::stone)
                    .simpleItem()
                    .transform(pickaxeOnly())
                    .tag(CNTag.forgeBlockTag("storage_blocks/lead"))
                    .register();

    public static final BlockEntry<Block> STEEL_BLOCK =
            CreateNuclear.REGISTRATE.block("steel_block", Block::new)
                    .initialProperties(SharedProperties::stone)
                    .simpleItem()
                    .transform(pickaxeOnly())
                    .tag(CNTag.forgeBlockTag("storage_blocks/steel"))
                    .register();

    public static final BlockEntry<Block> ENRICHED_SOUL_SOIL =
            CreateNuclear.REGISTRATE.block("enriched_soul_soil", Block::new)
                    .initialProperties(CNBlocks::getSoulSoil)
                    .simpleItem()
                    .tag(BlockTags.MINEABLE_WITH_SHOVEL)
                    .tag(CNTag.BlockTags.ENRICHING_FIRE_BASE_BLOCKS.tag, BlockTags.NEEDS_DIAMOND_TOOL)
                    .register();

    public static final BlockEntry<EnrichingFireBlock> ENRICHING_FIRE =
            CreateNuclear.REGISTRATE.block("enriching_fire", properties ->  new EnrichingFireBlock(properties, 3.0f))
                    .initialProperties(() -> Blocks.FIRE)
                    .properties(BlockBehaviour.Properties::replaceable)
                    .properties(BlockBehaviour.Properties::noCollission)
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .properties(EnrichingFireBlock.getLight())
                    .tag(CNTag.BlockTags.FAN_PROCESSING_CATALYSTS_ENRICHED.tag, BlockTags.FIRE, BlockTags.DRAGON_TRANSPARENT)
                    .loot((lt, b) -> lt.add(b, BlockLootSubProvider.noDrop()))
                    .blockstate((c,p) -> {
                        String baseFolder = "block/enriching_fire/";
                        ModelFile Floor0 = p.models().getExistingFile(p.modLoc(baseFolder + "floor0"));
                        ModelFile Floor1 = p.models().getExistingFile(p.modLoc(baseFolder + "floor1"));
                        ModelFile Side0 = p.models().getExistingFile(p.modLoc(baseFolder + "side0"));
                        ModelFile Side1 = p.models().getExistingFile(p.modLoc(baseFolder + "side1"));
                        ModelFile SideAlt0 = p.models().getExistingFile(p.modLoc(baseFolder + "side_alt0"));
                        ModelFile SideAlt1 = p.models().getExistingFile(p.modLoc(baseFolder + "side_alt1"));

                        p.getMultipartBuilder(c.get())
                            .part()
                            .modelFile(Floor0)
                            .nextModel()
                            .modelFile(Floor1)
                            .addModel()
                            .end()
                            .part()
                            .modelFile(Side0)
                            .nextModel()
                            .modelFile(Side1)
                            .nextModel()
                            .modelFile(SideAlt0)
                            .nextModel()
                            .modelFile(SideAlt1)
                            .addModel()
                            .end()
                            .part()
                            .modelFile(Side0).rotationY(90).nextModel()
                            .modelFile(Side1).rotationY(90).nextModel()
                            .modelFile(SideAlt0).rotationY(90).nextModel()
                            .modelFile(SideAlt1).rotationY(90)
                            .addModel()
                            .end()
                            .part()
                            .modelFile(Side0).rotationY(180).nextModel()
                            .modelFile(Side1).rotationY(180).nextModel()
                            .modelFile(SideAlt0).rotationY(180).nextModel()
                            .modelFile(SideAlt1).rotationY(180)
                            .addModel()
                            .end()
                            .part()
                            .modelFile(Side0).rotationY(270).nextModel()
                            .modelFile(Side1).rotationY(270).nextModel()
                            .modelFile(SideAlt0).rotationY(270).nextModel()
                            .modelFile(SideAlt1).rotationY(270)
                            .addModel()
                            .end()
                        ;
                    })
                    .register();

    public static final BlockEntry<ReinforcedGlassBlock> REINFORCED_GLASS =
            CreateNuclear.REGISTRATE.block("reinforced_glass", ReinforcedGlassBlock::new)
                    .initialProperties(CNBlocks::getGlass)
                    .properties(p -> p.explosionResistance(7.0F).destroyTime(2F))
                    .onRegister(CreateRegistrate.connectedTextures(() -> new EncasedCTBehaviour(CNSpriteShifts.REACTOR_GLASS)))
                    .onRegister(casingConnectivity((block,cc) -> cc.makeCasing(block, CNSpriteShifts.REACTOR_GLASS)))
                    .loot(RegistrateBlockLootTables::dropWhenSilkTouch)
                    .tag(CNTag.forgeBlockTag("glass_blocks"))
                    .simpleItem()
                    .register();

    public static final BlockEntry<ReactorOutput> REACTOR_OUTPUT =
            CreateNuclear.REGISTRATE.block("reactor_output", ReactorOutput::new)
                .properties(p -> p.explosionResistance(6F).destroyTime(4F))
                .initialProperties(SharedProperties::stone)
                .properties(p -> p.mapColor(MapColor.COLOR_PURPLE).forceSolidOn())
                .tag(AllTags.AllBlockTags.SAFE_NBT.tag, BlockTags.NEEDS_DIAMOND_TOOL)
                .transform(pickaxeOnly())
                .blockstate(new ReactorOutputGenerator()::generate)
                .transform(BlockStressDefaults.setCapacity(10240))
                .item()
                .transform(customItemModel())
                .register();

    public static final BlockEntry<EnrichingCampfireBlock> ENRICHING_CAMPFIRE =
            CreateNuclear.REGISTRATE.block("enriching_campfire", properties -> new EnrichingCampfireBlock(true, 5, BlockBehaviour.Properties.of()
                .mapColor(MapColor.PODZOL)
                .instrument(NoteBlockInstrument.BASS)
                .lightLevel((state) -> state.getValue(EnrichingCampfireBlock.LIT) ? 15 : 0)
                .strength(2.0F)
                .sound(SoundType.WOOD)
                .noOcclusion()
                .ignitedByLava()
            ))
            .properties(BlockBehaviour.Properties::replaceable)
            .addLayer(() -> RenderType::cutoutMipped)
            .transform(axeOrPickaxe())
            .tag(BlockTags.CAMPFIRES)
            .loot((lt, b) -> lt.add(b, RegistrateBlockLootTables.createSilkTouchDispatchTable(b, lt.applyExplosionDecay(b, LootItem.lootTableItem(CNBlocks.ENRICHED_SOUL_SOIL)))))
            .blockstate((c, p) ->
                p.getVariantBuilder(c.getEntry()).forAllStatesExcept(state -> {
                    Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
                    return ConfiguredModel.builder()
                        .modelFile(p.models().getExistingFile(p.modLoc("block/enriching_campfire/" + (state.getValue(EnrichingCampfireBlock.LIT) ? "block" : "block_off"))))
                        .uvLock(false)
                        .rotationY(switch (facing) {
                            case NORTH -> 180;
                            case SOUTH -> 0;
                            case WEST -> 90;
                            case EAST -> 270;
                            default -> 0;
                        })
                        .build();
                    }, BlockStateProperties.SIGNAL_FIRE, BlockStateProperties.WATERLOGGED
                )
            )
            .tag(CNTag.BlockTags.FAN_PROCESSING_CATALYSTS_ENRICHED.tag)
            .item()
            .model((c, p) ->
                p.withExistingParent(c.getName(), new ResourceLocation("item/generated"))
                    .texture("layer0", p.modLoc("item/enriching_flame_campfire"))
            )
            .build()
            .register();




    public static final BlockEntry<ReactorControllerBlock> REACTOR_CONTROLLER =
            CreateNuclear.REGISTRATE.block("reactor_controller", ReactorControllerBlock::new)
                    .initialProperties(SharedProperties::stone)
                    .properties(p -> p.explosionResistance(6F))
                    .properties(p -> p.destroyTime(4F))
                    .transform(pickaxeOnly())
                    .tag(BlockTags.NEEDS_DIAMOND_TOOL, AllTags.AllBlockTags.SAFE_NBT.tag)
                    .blockstate(new ReactorControllerGenerator()::generate)
                    .item()
                    .transform(customItemModel())
                    .register();

    public static final BlockEntry<ReactorCoreBlock> REACTOR_CORE =
            CreateNuclear.REGISTRATE.block("reactor_core", ReactorCoreBlock::new)
                    .properties(p -> p.explosionResistance(6F))
                    .properties(p -> p.destroyTime(4F))
                    .tag(BlockTags.NEEDS_DIAMOND_TOOL)
                    .blockstate((c, p) ->
                        p.getVariantBuilder(c.getEntry())
                        .forAllStates(state -> ConfiguredModel.builder()
                            .modelFile(p.models().getExistingFile(p.modLoc("block/reactor_core/reactor_core")))
                            .uvLock(false)
                            .build())
                    )
                    .transform(pickaxeOnly())
                    .simpleItem()
                    .register();

    public static final BlockEntry<ReactorCoolerBlock> REACTOR_COOLER =
            CreateNuclear.REGISTRATE.block("reactor_cooler", ReactorCoolerBlock::new)
                    .initialProperties(SharedProperties::stone)
                    .properties(p -> p.explosionResistance(3F))
                    .properties(p -> p.destroyTime(4F))
                    .tag(BlockTags.NEEDS_DIAMOND_TOOL)
                    .simpleItem()
                    .transform(pickaxeOnly())
                    .blockstate((c, p) ->
                        p.getVariantBuilder(c.getEntry())
                            .forAllStatesExcept(state -> ConfiguredModel.builder()
                                .modelFile(p.models().getExistingFile(p.modLoc("block/reactor_cooler/reactor_cooler")))
                                .uvLock(false)
                                .build())
                    )
                    .register();

    public static final BlockEntry<ReactorCasingBlock> REACTOR_CASING =
            CreateNuclear.REGISTRATE.block("reactor_casing", p -> new ReactorCasingBlock(p, ReactorCasingBlock.TypeBlock.CASING))
                    .properties(p -> p.explosionResistance(3F).destroyTime(4F))
                    .transform(pickaxeOnly())
                    .blockstate((c,p) ->
                        p.getVariantBuilder(c.getEntry()).forAllStates((state) -> ConfiguredModel.builder()
                            .modelFile(p.models().getExistingFile(p.modLoc("block/reactor_casing/reactor_casing")))
                            .build()))
                    .onRegister(CreateRegistrate.connectedTextures(() -> new EncasedCTBehaviour(CNSpriteShifts.REACTOR_CASING)))
                    .onRegister(casingConnectivity((block,cc) -> cc.makeCasing(block, CNSpriteShifts.REACTOR_CASING)))
                    .tag(BlockTags.NEEDS_DIAMOND_TOOL)
                    .simpleItem()
                    .transform(pickaxeOnly())
                    .register();

    public static final BlockEntry<ReactorFrameBlock> REACTOR_FRAME =
            CreateNuclear.REGISTRATE.block("reactor_frame", ReactorFrameBlock::new)
                    .initialProperties(SharedProperties::stone)
                    .properties(p -> p.explosionResistance(3F))
                    .properties(p -> p.destroyTime(2F))
                    .addLayer(() -> RenderType::cutoutMipped)
                    .transform(pickaxeOnly())
                    .tag(BlockTags.NEEDS_DIAMOND_TOOL)
                    .blockstate((c, p) ->
                        p.getVariantBuilder(c.getEntry())
                        .forAllStatesExcept(state -> {
                            ReactorFrameBlock.Part part = state.getValue(ReactorFrameBlock.PART);
                            String baseFile = "block/reactor_frame/reactor_frame_";
                            ModelFile start = p.models().getExistingFile(p.modLoc(baseFile + "top"));
                            ModelFile middle = p.models().getExistingFile(p.modLoc(baseFile + "middle"));
                            ModelFile bottom = p.models().getExistingFile(p.modLoc(baseFile + "bottom"));
                            ModelFile none = p.models().getExistingFile(p.modLoc(baseFile + "none"));
                            return ConfiguredModel.builder()
                                .modelFile(switch (part) {
                                    case START -> start;
                                    case MIDDLE -> middle;
                                    case END -> bottom;
                                    default -> none;
                                }
                            )
                            .uvLock(false)
                            .build();
                        })
                    )
                    .item(ReactorFrameItem::new)
                    .model(AssetLookup::customItemModel)
                    .build()
                    .register();

    public static final BlockEntry<ReactorInput> REACTOR_INPUT =
            CreateNuclear.REGISTRATE.block("reactor_input", ReactorInput::new)
                    .initialProperties(SharedProperties::stone)
                    .properties(p -> p.explosionResistance(6F))
                    .properties(p -> p.destroyTime(2F))
                    .addLayer(() -> RenderType::cutoutMipped)
                    .transform(pickaxeOnly())
                    .tag(BlockTags.NEEDS_DIAMOND_TOOL, AllTags.AllBlockTags.SAFE_NBT.tag)
                    .blockstate(new ReactorInputGenerator()::generate)
                    .item()
                    .transform(customItemModel())
                    .register();

    /*public static final BlockEntry<EventTriggerBlock> TEST_EVENT_TRIGGER_BLOCK = CreateNuclear.REGISTRATE.block("test_event_trigger_block", EventTriggerBlock::new)
            .simpleItem()
            .register();*/

    public static Block getSoulSoil() {
        return Blocks.SOUL_SOIL;
    }

    public static Block getGlass() {
        return Blocks.GLASS;
    }

    public static Block getDiamondOre() {
        return Blocks.DIAMOND_ORE;
    }

    public static void registerCNBlocks() {
        CreateNuclear.LOGGER.info("Registering ModBlocks for " + CreateNuclear.MOD_ID);
    }


}
