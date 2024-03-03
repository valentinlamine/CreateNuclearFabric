package net.ynov.createnuclear.block;

import com.simibubi.create.content.kinetics.BlockStressDefaults;
import com.simibubi.create.foundation.data.SharedProperties;
import com.simibubi.create.foundation.utility.Couple;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.ynov.createnuclear.CreateNuclear;
import net.ynov.createnuclear.blockentity.ReinforcedGlassBlock;
import net.ynov.createnuclear.tags.CNTag;
//import net.ynov.createnuclear.tools.EnrichingCampfire;
import net.ynov.createnuclear.tools.EnrichingCampfire;
import net.ynov.createnuclear.tools.UraniumFireBlock;
import net.ynov.createnuclear.tools.UraniumOreBlock;
import net.ynov.createnuclear.tools.reactor.ReactorController;

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
                    .initialProperties(CNBlocks::getSoulSoil)
                    .simpleItem()
                    .transform(pickaxeOnly())
                    .tag(CNTag.BlockTags.ENRICHEING_FIRE_BASE_BLOCKS.tag)
                    .register();

    public static final BlockEntry<UraniumFireBlock> ENRICHING_FIRE =
            CreateNuclear.REGISTRATE.block("enriching_fire", UraniumFireBlock::new)
                    .properties(BlockBehaviour.Properties::replaceable)
                    .properties(BlockBehaviour.Properties::noCollission)
                    .properties(p -> p.lightLevel(UraniumFireBlock::getLight))
                    .tag(CNTag.BlockTags.FAN_PROCESSING_CATALYSTS_ENRICHED.tag)
                    .register();

    public static final BlockEntry<ReinforcedGlassBlock> REINFORCED_GLASS =
            CreateNuclear.REGISTRATE.block("reinforced_glass", ReinforcedGlassBlock::new)
                    .initialProperties(CNBlocks::getGlass)
                    .properties(p -> p.explosionResistance(1200F))
                    .properties(p -> p.destroyTime(2F))
                    .simpleItem()
                    .register();

    public static final BlockEntry<EnrichingCampfire> ENRICHING_CAMPFIRE =
            CreateNuclear.REGISTRATE.block("enriching_campfire", properties -> new EnrichingCampfire(true, 5, BlockBehaviour.Properties.of().mapColor(MapColor.PODZOL)
                            .instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD).lightLevel(litBlockEmission(10)).noOcclusion().ignitedByLava()))
            .properties(BlockBehaviour.Properties::replaceable)
            //.initialProperties(CNBlocks::DIAMOND_ORE)
            .simpleItem()
            .addLayer(() -> RenderType::cutoutMipped)
            .transform(pickaxeOnly())
            .tag(CNTag.BlockTags.FAN_PROCESSING_CATALYSTS_ENRICHED.tag)
            .register();

    public static final BlockEntry<ReactorController> REACTOR_CONTROLLER =
            CreateNuclear.REGISTRATE.block("reactor_controller", ReactorController::new)
                    .initialProperties(SharedProperties::stone)
                    .properties(p -> p.explosionResistance(1200F))
                    .properties(p -> p.destroyTime(2F))
                    .simpleItem()
                    .register();

    public static final BlockEntry<Block> REACTOR_CORE =
            CreateNuclear.REGISTRATE.block("reactor_core", Block::new)
                    .initialProperties(SharedProperties::stone)
                    .properties(p -> p.explosionResistance(1200F))
                    .properties(p -> p.destroyTime(2F))
                    .simpleItem()
                    .register();

    public static final BlockEntry<Block> REACTOR_COOLING_FRAME =
            CreateNuclear.REGISTRATE.block("reactor_cooling_frame", Block::new)
                    .initialProperties(SharedProperties::stone)
                    .properties(p -> p.explosionResistance(1200F))
                    .properties(p -> p.destroyTime(2F))
                    .simpleItem()
                    .register();

    public static final BlockEntry<Block> REACTOR_CASING =
            CreateNuclear.REGISTRATE.block("reactor_casing", Block::new)
                    .initialProperties(SharedProperties::stone)
                    .properties(p -> p.explosionResistance(1200F))
                    .properties(p -> p.destroyTime(2F))
                    .simpleItem()
                    .register();

    public static final BlockEntry<Block> REACTOR_MAIN_FRAME =
            CreateNuclear.REGISTRATE.block("reactor_main_frame", Block::new)
                    .initialProperties(SharedProperties::stone)
                    .properties(p -> p.explosionResistance(1200F))
                    .properties(p -> p.destroyTime(2F))
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
