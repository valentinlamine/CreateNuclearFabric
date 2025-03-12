package net.nuclearteam.createnuclear.advancement;

import com.google.common.collect.Sets;
import com.simibubi.create.AllItems;

import com.simibubi.create.foundation.advancement.CreateAdvancement;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.critereon.*;
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
            .title("Starting The Nuclear Journey")
			.description("Unlock the basics of nuclear energy and get your first uranium powder")
			.awardedForFree()
			.special(SILENT)),

    RAW_URANIUM = create("raw_uranium", b -> b.icon(CNItems.RAW_URANIUM)
            .title("The Raw Power")
			.description("Mine uranium ore to obtain raw uranium for further processing")
			.after(ROOT)
			.whenIconCollected()),

    URANIUM_POWDER = create("uranium_powder", b -> b.icon(CNItems.URANIUM_POWDER)
            .title("Powdered Uranium")
            .description("Crush raw uranium into powder to prepare for further refining")
            .after(RAW_URANIUM)
            .whenIconCollected()),

    URANIUM_LIQUID = create("uranium_liquid", b -> b.icon(CNFluids.URANIUM.getBucket().get())
            .title("Turning Solid To Liquid")
            .description("Obtain some uranium liquid by mixing uranium powder")
            .after(URANIUM_POWDER)
            .whenIconCollected()),

    YELLOWCAKE = create("yellowcake", b -> b.icon(CNItems.YELLOWCAKE)
            .title("The Yellowcake Process")
            .description("Compact uranium liquid to create yellowcake")
            .after(URANIUM_LIQUID)
            .whenIconCollected()),

    EATED_YELLOWCAKE = create("eated_yellowcake", b -> b.icon(CNItems.YELLOWCAKE)
            .title("Eating Yellowcake")
            .description("What did you expect ?")
            .after(YELLOWCAKE)
            .whenItemEaten(CNItems.YELLOWCAKE.get())
            .special(SECRET)),



    ENRICHED_YELLOWCAKE = create("enriched_yellowcake", b -> b.icon(CNItems.ENRICHED_YELLOWCAKE)
            .title("Enhancing Yellowcake")
            .description("Use a fan to enrich yellowcake and make it more powerful")
            .after(YELLOWCAKE)
            .whenIconCollected()),

    URANIUM_ROD = create("uranium_rod", b -> b.icon(CNItems.URANIUM_ROD)
            .title("The Power Of The Atom")
            .description("Create your first uranium rod using enriched yellowcake in a mechanical crafter")
            .after(ENRICHED_YELLOWCAKE)
            .whenIconCollected()),




    COAL_DUST = create("coal_dust", b -> b.icon(CNItems.COAL_DUST)
            .title("Coal Dust")
            .description("Crush coal or charcoal to obtain coal dust, a key crafting material")
            .after(ROOT)
            .whenIconCollected()),

    STEEL_INGOT = create("steel_ingot", b -> b.icon(CNItems.STEEL_INGOT)
            .title("Steel Ingot")
            .description("Combine coal dust and iron ingots to create steel ingots")
            .after(COAL_DUST)
            .whenIconCollected()),

    GRAPHENE = create("graphene", b -> b.icon(CNItems.GRAPHENE)
            .title("Graphene")
            .description("Press coal dust to create graphene")
            .after(COAL_DUST)
            .whenIconCollected()),

    GRAPITE_ROD = create("graphite_rod", b -> b.icon(CNItems.GRAPHITE_ROD)
            .title("Don't Forget Those Ones")
            .description("Combine graphene and steel ingots in a mechanical crafter to make graphite rods")
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
            .title("Anti radiation Armor")
            .description("Craft your first anti-radiation armor piece to protect yourself from radiation")
            .after(LEAD_INGOT)
            .whenItemCollected(CNTag.ItemTags.ALL_ANTI_RADIATION_ARMORS.tag)),

    FULL_ANTI_RADIATION_ARMOR = create("full_anti_radiation_armor", b -> b.icon(CNItems.ANTI_RADIATION_CHESTPLATES.get(DyeColor.WHITE))
            .title("Fully Protected")
            .description("Wear a full set of anti-radiation armor to fully protect yourself from radiation")
            .externalTrigger(
                    InventoryChangeTrigger.TriggerInstance.hasItems(
                            new ItemPredicate(CNTag.ItemTags.ANTI_RADIATION_HELMET_FULL_DYE.tag, null, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY,EnchantmentPredicate.NONE, EnchantmentPredicate.NONE, null, NbtPredicate.ANY),
                            new ItemPredicate(CNTag.ItemTags.ANTI_RADIATION_CHESTPLATE_FULL_DYE.tag, null, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY,EnchantmentPredicate.NONE, EnchantmentPredicate.NONE, null, NbtPredicate.ANY),
                            new ItemPredicate(CNTag.ItemTags.ANTI_RADIATION_LEGGINGS_FULL_DYE.tag, null, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY,EnchantmentPredicate.NONE, EnchantmentPredicate.NONE, null, NbtPredicate.ANY),
                            new ItemPredicate(CNTag.ItemTags.ANTI_RADIATION_BOOTS_DYE.tag, null, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY,EnchantmentPredicate.NONE, EnchantmentPredicate.NONE, null, NbtPredicate.ANY)
                    ))
            .after(ANTI_RADIATION_ARMOR)),

    DYE_ANTI_RADIATION_ARMOR = create("dye_anti_radiation_armor", b -> b.icon(CNItems.ANTI_RADIATION_HELMETS.get(DyeColor.RED))
            .title("Pimp My Armor")
            .description("Dye your anti radiation armor to any color")
            .whenItemCollected(CNTag.ItemTags.ANTI_RADIATION_HELMET_DYE.tag)
            .whenItemCollected(CNTag.ItemTags.ANTI_RADIATION_CHESTPLATE_DYE.tag)
            .whenItemCollected(CNTag.ItemTags.ANTI_RADIATION_LEGGINGS_DYE.tag)
            .after(ANTI_RADIATION_ARMOR)),


    AUTOMATIC_URANIUM = create("automatic_uranium", b -> b.icon(CNItems.URANIUM_POWDER)
            .title("Automating Uranium")
            .description("Obtain some uranium powder using the automatic uranium processing")
            .after(ROOT)
            .special(SECRET)),

    REACTOR_CASING = create("reactor_casing", b -> b.icon(CNBlocks.REACTOR_CASING)
            .title("The Power Of The Reactor")
            .description("Craft a reactor casing to begin building your nuclear reactor")
            .after(ROOT)
            .whenIconCollected()),

    REACTOR_CONTROLLER = create("reactor_controller", b -> b.icon(CNBlocks.REACTOR_CONTROLLER)
            .title("Controller Of The Core")
            .description("Craft a reactor controller to manage and regulate your reactor")
            .after(REACTOR_CASING)
            .whenIconCollected()),


    REACTOR_BLUEPRINT = create("reactor_blueprint", b -> b.icon(CNItems.REACTOR_BLUEPRINT)
            .title("Blueprint For Power")
            .description("Craft a reactor blueprint to design the layout of rods in your reactor")
            .after(REACTOR_CONTROLLER)
            .whenIconCollected()),

    REACTOR_COOLER = create("reactor_cooler", b -> b.icon(CNBlocks.REACTOR_COOLER)
            .title("Cooling The Reactor")
            .description("Craft a reactor cooler to cool your reactor")
            .after(REACTOR_CASING)
            .whenIconCollected()),

    REACTOR_FRAME = create("reactor_frame", b -> b.icon(CNBlocks.REACTOR_FRAME)
            .title("Reactor Frame")
            .description("Craft a reactor frame to build your nuclear reactor")
            .after(REACTOR_CASING)
            .whenIconCollected()),

    REACTOR_INPUT = create("reactor_input", b -> b.icon(CNBlocks.REACTOR_INPUT)
            .title("Fueling The Reactor")
            .description("Craft a reactor input to feed uranium and graphite rods into your reactor")
            .after(REACTOR_CASING)
            .whenIconCollected()),

    REACTOR_OUTPUT = create("reactor_output", b -> b.icon(CNBlocks.REACTOR_OUTPUT)
            .title("Power Output")
            .description("Craft a reactor output to transfer the energy produced by your reactor")
            .after(REACTOR_CASING)
            .whenIconCollected()),

    REACTOR_CORE = create("reactor_core", b -> b.icon(CNBlocks.REACTOR_CORE)
            .title("Core of Power")
            .description("Craft the reactor core to harness the full energy of your nuclear reactor")
            .after(REACTOR_CASING)
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
