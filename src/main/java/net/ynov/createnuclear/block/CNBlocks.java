package net.ynov.createnuclear.block;

import com.simibubi.create.AllTags;
import com.simibubi.create.content.kinetics.BlockStressDefaults;
import com.simibubi.create.foundation.data.SharedProperties;
import com.simibubi.create.foundation.utility.Couple;
import com.tterrag.registrate.util.entry.BlockEntry;
import io.github.fabricators_of_create.porting_lib.models.generators.ConfiguredModel;
import io.github.fabricators_of_create.porting_lib.models.generators.ModelFile;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.ynov.createnuclear.CreateNuclear;
import net.ynov.createnuclear.blockentity.ReinforcedGlassBlock;
import net.ynov.createnuclear.multiblock.cooling.ReactorCoolingBlock;
import net.ynov.createnuclear.multiblock.energy.ReactorOutputGenerator;
import net.ynov.createnuclear.multiblock.gauge.ReactorGaugeBlock;
import net.ynov.createnuclear.multiblock.controller.ReactorControllerBlock;
import net.ynov.createnuclear.multiblock.frame.ReactorBlock;
import net.ynov.createnuclear.multiblock.input.ReactorInput;
import net.ynov.createnuclear.tags.CNTag;
import net.ynov.createnuclear.multiblock.energy.ReactorOutput;
import net.ynov.createnuclear.tools.EnrichingCampfireBlock;
import net.ynov.createnuclear.tools.EnrichingFireBlock;
import net.ynov.createnuclear.tools.UraniumOreBlock;

import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;
import static net.minecraft.world.level.block.Blocks.litBlockEmission;

public class CNBlocks {

    static {
        //CreateNuclear.REGISTRATE.setCreativeTab(CNGroup.MAIN_KEY);
    }

    public static final BlockEntry<UraniumOreBlock> DEEPSLATE_URANIUM_ORE =
            CreateNuclear.REGISTRATE.block("deepslate_uranium_ore", UraniumOreBlock::new)
                    .initialProperties(CNBlocks::getDiamondOre)
                    .simpleItem()
                    .transform(pickaxeOnly())
                    .register();

    public static final BlockEntry<UraniumOreBlock> URANIUM_ORE =
            CreateNuclear.REGISTRATE.block("uranium_ore", UraniumOreBlock::new)
                    .initialProperties(SharedProperties::stone)
                    .simpleItem()
                    .transform(pickaxeOnly())
                    .register();

    public static final BlockEntry<Block> DEEPSLATE_LEAD_ORE =
            CreateNuclear.REGISTRATE.block("deepslate_lead_ore", Block::new)
                    .initialProperties(CNBlocks::getDiamondOre)
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
                    .register();

    public static final BlockEntry<Block> RAW_LEAD_BLOCK =
            CreateNuclear.REGISTRATE.block("raw_lead_block", Block::new)
                    .initialProperties(SharedProperties::stone)
                    .simpleItem()
                    .transform(pickaxeOnly())
                    .register();

    public static final BlockEntry<Block> LEAD_BLOCK =
            CreateNuclear.REGISTRATE.block("lead_block", Block::new)
                    .initialProperties(SharedProperties::stone)
                    .simpleItem()
                    .transform(pickaxeOnly())
                    .register();

    public static final BlockEntry<Block> ENRICHED_SOUL_SOIL =
            CreateNuclear.REGISTRATE.block("enriched_soul_soil", Block::new)
                    .initialProperties(CNBlocks::getSoulSoil)
                    .simpleItem()
                    .transform(pickaxeOnly())
                    .tag(CNTag.BlockTags.ENRICHING_FIRE_BASE_BLOCKS.tag)
                    .register();

    public static final BlockEntry<EnrichingFireBlock> ENRICHING_FIRE =
            CreateNuclear.REGISTRATE.block("enriching_fire", properties ->  new EnrichingFireBlock(properties, 3.0f))
                    .initialProperties(() -> Blocks.FIRE)
                    .properties(BlockBehaviour.Properties::replaceable)
                    .properties(BlockBehaviour.Properties::noCollission)
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .properties(p -> p.lightLevel(EnrichingFireBlock::getLight))
                    .tag(CNTag.BlockTags.FAN_PROCESSING_CATALYSTS_ENRICHED.tag)
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
                    .properties(p -> p.explosionResistance(1200F))
                    .properties(p -> p.destroyTime(2F))
                    .simpleItem()
                    .register();

    public static final BlockEntry<ReactorOutput> REACTOR_OUTPUT =
            CreateNuclear.REGISTRATE.block("reactor_output", ReactorOutput::new)
                .properties(p -> p.explosionResistance(1200F).destroyTime(4F))
                .initialProperties(SharedProperties::stone)
                .properties(p -> p.mapColor(MapColor.COLOR_PURPLE).forceSolidOn())
                .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
                .transform(pickaxeOnly())
                .blockstate(new ReactorOutputGenerator()::generate)
                .transform(BlockStressDefaults.setCapacity(500))
                .transform(BlockStressDefaults.setGeneratorSpeed(() -> Couple.create(0, 256)))
                .item()
                .properties(p -> p.rarity(Rarity.EPIC))
                .transform(customItemModel())
                .register();

    public static final BlockEntry<EnrichingCampfireBlock> ENRICHING_CAMPFIRE =
            CreateNuclear.REGISTRATE.block("enriching_campfire", properties -> new EnrichingCampfireBlock(true, 5, BlockBehaviour.Properties.of()
                .mapColor(MapColor.PODZOL)
                .instrument(NoteBlockInstrument.BASS)
                .strength(2.0F)
                .sound(SoundType.WOOD)
                .lightLevel(litBlockEmission(10))
                .noOcclusion()
                .ignitedByLava()
            ))
            .properties(BlockBehaviour.Properties::replaceable)
            //.initialProperties(CNBlocks::DIAMOND_ORE)
            .simpleItem()
            .addLayer(() -> RenderType::cutoutMipped)
            .transform(pickaxeOnly())
            .blockstate((c, p) ->
                p.getVariantBuilder(c.getEntry())
                .forAllStatesExcept(state -> {
                    Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
                    return ConfiguredModel.builder()
                        .modelFile(p.models().getExistingFile(p.modLoc("block/enriching_campfire/" +
                                (state.getValue(EnrichingCampfireBlock.LIT) ? "block" : "block_off")
                        )))
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
            .register();



    public static final BlockEntry<ReactorControllerBlock> REACTOR_CONTROLLER =
            CreateNuclear.REGISTRATE.block("reactor_controller", ReactorControllerBlock::new)
                    .initialProperties(SharedProperties::stone)
                    .properties(p -> p.explosionResistance(1200F))
                    .properties(p -> p.destroyTime(4F))
                    .blockstate((ctx, prov) -> prov.horizontalBlock(ctx.getEntry(), prov.models().getExistingFile(ctx.getId()), 0))
                    .simpleItem()
                    .register();

    public static final BlockEntry<ReactorBlock> REACTOR_CORE =
            CreateNuclear.REGISTRATE.block("reactor_core", ReactorBlock::new)
                    .properties(p -> p.explosionResistance(1200F))
                    .properties(p -> p.destroyTime(4F))
                    .blockstate((c, p) ->
                        p.getVariantBuilder(c.getEntry())
                        .forAllStates(state -> ConfiguredModel.builder()
                                .modelFile(p.models().getExistingFile(p.modLoc("block/reactor_core")))
                                .uvLock(false)
                                .build())
                    )
                    .simpleItem()
                    /*.model((c, p) -> {
                        p.withExistingParent("block/reactor_core", p.modLoc("block/reactor_core"))
                                .texture("1", p.modLoc("block/reactor/core/reactor_core_casing"))
                                .texture("2", p.modLoc("block/reactor/core/reactor_core_center"))
                                .texture("3", p.modLoc("block/reactor/core/reactor_core_bars"))
                                .texture("particle", p.modLoc("block/reactor/core/reactor_core_center"));
                    })*/
                    .register();

    public static final BlockEntry<ReactorCoolingBlock> REACTOR_COOLING_FRAME =
            CreateNuclear.REGISTRATE.block("reactor_cooling_frame", ReactorCoolingBlock::new)
                    .initialProperties(SharedProperties::stone)
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

    public static final BlockEntry<ReactorGaugeBlock> REACTOR_MAIN_FRAME =
            CreateNuclear.REGISTRATE.block("reactor_main_frame", ReactorGaugeBlock::new)
                    .initialProperties(SharedProperties::stone)
                    .properties(p -> p.explosionResistance(1200F))
                    .properties(p -> p.destroyTime(2F))
                    .addLayer(() -> RenderType::cutoutMipped)
                    .transform(pickaxeOnly())
                    .blockstate((c, p) ->
                        p.getVariantBuilder(c.getEntry())
                        .forAllStatesExcept(state -> {
                            ReactorGaugeBlock.Part part = state.getValue(ReactorGaugeBlock.PART);
                            String baseFile = "block/reactor_main_frame/reactor_gauge_";
                            ModelFile start = p.models().getExistingFile(p.modLoc(baseFile + "top"));
                            ModelFile middle = p.models().getExistingFile(p.modLoc(baseFile + "middle"));
                            ModelFile bottom = p.models().getExistingFile(p.modLoc(baseFile + "bottom"));
                            ModelFile none = p.models().getExistingFile(p.modLoc(baseFile + "none"));
                            return ConfiguredModel.builder()
                                .modelFile(switch (part) {
                                    case START -> start;
                                    case MIDDLE -> middle;
                                    case END -> bottom;
                                    case NONE -> none;
                                }
                            )
                            .uvLock(false)
                            .build();
                        })
                    )
                    .simpleItem()
                    .register();

    public static final BlockEntry<ReactorInput> REACTOR_INPUT =
            CreateNuclear.REGISTRATE.block("reactor_input", ReactorInput::new)
                    .initialProperties(SharedProperties::stone)
                    .properties(p -> p.explosionResistance(1200F))
                    .properties(p -> p.destroyTime(2F))
                    .addLayer(() -> RenderType::cutoutMipped)
                    .transform(pickaxeOnly())
                    .blockstate((ctx, prov) -> prov.horizontalBlock(ctx.getEntry(), prov.models().getExistingFile(ctx.getId()), 0))
                    .simpleItem()
                    .register();

    public static Block getSoulSoil() {
        return Blocks.SOUL_SOIL;
    }

    public static Block getGlass() {
        return Blocks.GLASS;
    }

    public static Block getDiamondOre() {
        return Blocks.DIAMOND_ORE;
    }

    private static void addBlockToCreateNuclearItemGroup(FabricItemGroupEntries entries) {
        //entries.accept(ENRICHING_CAMPFIRE, CreativeModeTab.TabVisibility.PARENT_TAB_ONLY);
    }

    public static void registerCNBlocks() {
        CreateNuclear.LOGGER.info("Registering ModBlocks for " + CreateNuclear.MOD_ID);

        //ItemGroupEvents.modifyEntriesEvent(CNGroup.MAIN_KEY).register(CNBlocks::addBlockToCreateNuclearItemGroup);
    }


}
