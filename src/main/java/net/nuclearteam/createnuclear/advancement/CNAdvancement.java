package net.nuclearteam.createnuclear.advancement;

import com.google.common.collect.Sets;
import com.simibubi.create.AllItems;

import com.simibubi.create.foundation.advancement.CreateAdvancement;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.critereon.ConsumeItemTrigger;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.ItemLike;
import net.nuclearteam.createnuclear.advancement.CreateNuclearAdvancement.Builder;
import net.nuclearteam.createnuclear.block.CNBlocks;
import net.nuclearteam.createnuclear.fluid.CNFluids;
import net.nuclearteam.createnuclear.item.CNItems;
import net.nuclearteam.createnuclear.tags.CNTag;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;


import static net.nuclearteam.createnuclear.advancement.CreateNuclearAdvancement.TaskType.SECRET;
import static net.nuclearteam.createnuclear.advancement.CreateNuclearAdvancement.TaskType.SILENT;

public class CNAdvancement implements DataProvider {

    public static final List<CreateNuclearAdvancement> ENTRIES = new ArrayList<>();
    public static final CreateNuclearAdvancement START = null,

    ROOT = create("root", b -> b.icon(CNItems.URANIUM_POWDER)
            .title("Welcome to Create Nuclear")
			.description("New era of energy")
			.awardedForFree()
			.special(SILENT)),

    RAW_URANIUM = create("raw_uranium", b -> b.icon(CNItems.RAW_URANIUM)
            .title("Raw Uranium")
			.description("Obtain some raw uranium by mining uranium ore")
			.after(ROOT)
			.whenIconCollected()),

    URANIUM_POWDER = create("uranium_powder", b -> b.icon(CNItems.URANIUM_POWDER)
            .title("Uranium Powder")
            .description("Obtain some uranium powder by crushing raw uranium")
            .after(RAW_URANIUM)
            .whenIconCollected()),

    URANIUM_LIQUID = create("uranium_liquid", b -> b.icon(CNFluids.URANIUM.getBucket().get())
            .title("Uranium Liquid")
            .description("Obtain some uranium liquid by mixing uranium powder")
            .after(URANIUM_POWDER)
            .whenIconCollected()),

    YELLOWCAKE = create("yellowcake", b -> b.icon(CNItems.YELLOWCAKE)
            .title("Yellowcake")
            .description("Obtain some yellowcake by compacting uranium liquid")
            .after(URANIUM_LIQUID)
            .whenIconCollected()),

    ENRICHED_YELLOWCAKE = create("enriched_yellowcake", b -> b.icon(CNItems.ENRICHED_YELLOWCAKE)
            .title("Enriched Yellow Cake")
            .description("Obtain some enriched yellow cake by enriching yellowcake with a fan")
            .after(YELLOWCAKE)
            .whenIconCollected()),

    URANIUM_ROD = create("uranium_rod", b -> b.icon(CNItems.URANIUM_ROD)
            .title("Uranium Rod")
            .description("Craft your first uranium rod using enriched yellowcake in a mechanical crafter")
            .after(ENRICHED_YELLOWCAKE)
            .whenIconCollected()),




    COAL_DUST = create("coal_dust", b -> b.icon(CNItems.COAL_DUST)
            .title("Coal Dust")
            .description("Obtain some coal dust by crushing coal or charcoal")
            .after(ROOT)
            .whenIconCollected()),

    STEEL_INGOT = create("steel_ingot", b -> b.icon(CNItems.STEEL_INGOT)
            .title("Steel Ingot")
            .description("Obtain some steel ingot by mixing iron ingot and coal dust")
            .after(COAL_DUST)
            .whenIconCollected()),

    GRAPHENE = create("graphene", b -> b.icon(CNItems.GRAPHENE)
            .title("Graphene")
            .description("Obtain some graphene by pressing coal dust")
            .after(COAL_DUST)
            .whenIconCollected()),

    GRAPITE_ROD = create("graphite_rod", b -> b.icon(CNItems.GRAPHITE_ROD)
            .title("Graphite Rod")
            .description("Craft your first graphite rod using graphene and steel ingot in a mechanical crafter")
            .after(GRAPHENE)
            .whenIconCollected()),







    RAW_LEAD = create("raw_lead", b -> b.icon(CNItems.RAW_LEAD)
            .title("Raw Lead")
			.description("Obtain some raw lead by mining lead ore")
			.after(ROOT)
			.whenIconCollected()),

    LEAD_INGOT = create("lead_ingot", b -> b.icon(CNItems.LEAD_INGOT)
            .title("Lead ingot")
            .description("Smelt a raw lead to obtain a lead ingot")
            .after(RAW_LEAD)
            .whenIconCollected()),

    REINFORCED_GLASS = create("reinforced_glass", b -> b.icon(CNBlocks.REINFORCED_GLASS)
            .title("Reinforced Glass")
            .description("Craft some reinforced glass for the first time")
            .after(LEAD_INGOT)
            .whenIconCollected()),

    ANTI_RADIATION_ARMOR = create("anti_radiation_armor", b -> b.icon(CNItems.ANTI_RADIATION_HELMETS.get(DyeColor.WHITE))
            .title("Anti radiation armor")
            .description("Craft your first anti radiation armor piece to protect yourself from radiation")
            .after(LEAD_INGOT)
            .whenItemCollected(CNTag.ItemTags.ALL_ANTI_RADIATION_ARMORS.tag)),

    FULL_ANTI_RADIATION_ARMOR = create("full_anti_radiation_armor", b -> b.icon(CNItems.ANTI_RADIATION_CHESTPLATES.get(DyeColor.WHITE))
            .title("Anti radiation armor")
            .description("Wear a full anti radiation armor to protect yourself from radiation")
            .externalTrigger(InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[]{CNItems.ANTI_RADIATION_LEGGINGS.get(DyeColor.WHITE), CNItems.ANTI_RADIATION_CHESTPLATES.get(DyeColor.WHITE), CNItems.ANTI_RADIATION_HELMETS.get(DyeColor.WHITE), CNItems.ANTI_RADIATION_BOOTS}))
            .after(ANTI_RADIATION_ARMOR)),

    DYE_ANTI_RADIATION_ARMOR = create("dye_anti_radiation_armor", b -> b.icon(CNItems.ANTI_RADIATION_HELMETS.get(DyeColor.RED))
            .title("Anti radiation armor")
            .description("Dye your anti radiation armor to any color")
            .whenItemCollected(CNTag.ItemTags.ANTI_RADIATION_HELMET_DYE.tag)
            .whenItemCollected(CNTag.ItemTags.ANTI_RADIATION_CHESTPLATE_DYE.tag)
            .whenItemCollected(CNTag.ItemTags.ANTI_RADIATION_LEGGINGS_DYE.tag)
            .whenItemCollected(CNTag.ItemTags.ANTI_RADIATION_BOOTS_DYE.tag)
            .after(ANTI_RADIATION_ARMOR)),


    AUTOMATIC_URANIUM = create("automatic_uranium", b -> b.icon(CNItems.URANIUM_POWDER)
            .title("Uranium Powder")
            .description("Obtain some uranium powder using the automatic uranium processing")
            .after(ROOT)
            .special(SECRET)),

    REACTOR_CASING = create("reactor_casing", b -> b.icon(CNBlocks.REACTOR_CASING)
            .title("Reactor Casing")
            .description("Craft a reactor casing to build your nuclear reactor")
            .after(ROOT)

            .whenIconCollected()),

    REACTOR_CONTROLLER = create("reactor_controller", b -> b.icon(CNBlocks.REACTOR_CONTROLLER)
            .title("Reactor Controller")
            .description("Craft a reactor controller to control your nuclear reactor")
            .after(REACTOR_CASING)
            .special(SECRET)
            .whenIconCollected()),


    REACTOR_BLUEPRINT = create("reactor_blueprint", b -> b.icon(CNItems.CONFIGURED_REACTOR_ITEM)
            .title("Reactor Blueprint")
            .description("Craft a reactor blueprint to create your pattern of rods !")
            .after(REACTOR_CONTROLLER)
            .whenIconCollected()),

    REACTOR_COOLING_FRAME = create("reactor_cooling_frame", b -> b.icon(CNBlocks.REACTOR_COOLING_FRAME)
            .title("Reactor Colling Frame")
            .description("Craft a reactor colling frame to cool your nuclear reactor")
            .after(REACTOR_CASING)
            .special(SECRET)
            .whenIconCollected()),

    REACTOR_MAIN_FRAME = create("reactor_main_frame", b -> b.icon(CNBlocks.REACTOR_MAIN_FRAME)
            .title("Reactor Main Frame")
            .description("Craft a reactor main frame to build your nuclear reactor")
            .after(REACTOR_CASING)
            .special(SECRET)
            .whenIconCollected()),

    REACTOR_INPUT = create("reactor_input", b -> b.icon(CNBlocks.REACTOR_INPUT)
            .title("Reactor Input")
            .description("Craft a reactor input to input uranium and graphite rod to your nuclear reactor")
            .after(REACTOR_CASING)
            .special(SECRET)
            .whenIconCollected()),

    REACTOR_OUTPUT = create("reactor_output", b -> b.icon(CNBlocks.REACTOR_OUTPUT)
            .title("Reactor Output")
            .description("Craft a reactor output to output SU to your nuclear reactor")
            .after(REACTOR_CASING)
            .special(SECRET)
            .whenIconCollected()),

    REACTOR_CORE = create("reactor_core", b -> b.icon(CNBlocks.REACTOR_CORE)
            .title("Reactor Core")
            .description("Craft a reactor core to build your nuclear reactor")
            .after(REACTOR_CASING)
            .special(SECRET)
            .whenIconCollected())








    ;

    private final PackOutput output;

    private static CreateNuclearAdvancement create(String id, UnaryOperator<Builder> b) {
        return new CreateNuclearAdvancement(id, b);
    }

    public CNAdvancement(FabricDataOutput output) {
        this.output = output;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        PackOutput.PathProvider pathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, "advancements");
        List<CompletableFuture<?>> futures = new ArrayList<>();

        Set<ResourceLocation> set = Sets.newHashSet();
        Consumer<Advancement> consumer = (advancement) -> {
            ResourceLocation id = advancement.getId();
            if (!set.add(id))
                throw new IllegalStateException("Duplicate advancement " + id);
            Path path = pathProvider.json(id);
            futures.add(DataProvider.saveStable(cache, advancement.deconstruct()
                    .serializeToJson(), path));
        };

        for (CreateNuclearAdvancement advancement : ENTRIES)
            advancement.save(consumer);

        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    @Override
    public String getName() {
        return "Create Nuclear Advancements";
    }

    public static void provideLang(BiConsumer<String, String> consumer) {
        for (CreateNuclearAdvancement advancement : ENTRIES)
            advancement.provideLang(consumer);
    }
}
