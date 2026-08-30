package net.nuclearteam.createnuclear.foundation.advancement;

import com.google.common.collect.Sets;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.critereon.ConsumeItemTrigger;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.critereon.RecipeCraftedTrigger;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.EntityType;
import net.minecraft.advancements.critereon.EntityEquipmentPredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.tags.ItemTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.MethodsReturnNonnullByDefault;
import io.github.fabricators_of_create.porting_lib.tags.Tags;
import net.nuclearteam.createnuclear.*;
import net.nuclearteam.createnuclear.content.decoration.palettes.CNPaletteStoneTypes;
import net.nuclearteam.createnuclear.foundation.advancement.CreateNuclearAdvancement.Builder;


import javax.annotation.ParametersAreNonnullByDefault;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

import static net.nuclearteam.createnuclear.foundation.advancement.CreateNuclearAdvancement.TaskType.*;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
@SuppressWarnings("unused")
public class CNAdvancement implements DataProvider {

    public static final EntityEquipmentPredicate FULL_ARMOR = new EntityEquipmentPredicate.Builder()
        .head(ItemPredicate.Builder.item().of(CNItems.ANTI_RADIATION_HELMETS).build())
        .chest(ItemPredicate.Builder.item().of(CNItems.ANTI_RADIATION_CHESTPLATES).build())
        .legs(ItemPredicate.Builder.item().of(CNItems.ANTI_RADIATION_LEGGINGS).build())
        .feet(ItemPredicate.Builder.item().of(CNItems.ANTI_RADIATION_BOOTS).build())
        .build();

    private static final List<ItemPredicate> PREDICATES = List.of(
        ItemPredicate.Builder.item().of(CNBlocks.ENRICHED_SOUL_SOIL).build(),
        ItemPredicate.Builder.item().of(ItemTags.LOGS).build(),
        ItemPredicate.Builder.item().of(Tags.Items.RODS_WOODEN).build()
    );

    public static final List<CreateNuclearAdvancement> ENTRIES = new ArrayList<>();
    public static final CreateNuclearAdvancement START = null,

    ROOT = create("root", b -> b.icon(CNItems.URANIUM_POWDER)
        .title("Starting The Nuclear Journey")
        .description("Unlock the basics of nuclear energy and get your first uranium powder")
        .awardedForFree()
        .special(SILENT)
    ),

    CRAFT_ENRICHING_CAMPFIRE = create("craft_enriching_campfire", b -> b.icon(CNBlocks.ENRICHING_CAMPFIRE.asItem())
        .title("Does That Make Smoke?")
        .description("Craft an Enriching Campfire")
        .externalTrigger(RecipeCraftedTrigger.TriggerInstance.craftedItem(CreateNuclear.asResource("crafting/enriching_campfire"), PREDICATES))
        .after(ROOT)
    ),

    RAW_URANIUM = create("raw_uranium", b -> b.icon(CNItems.RAW_URANIUM)
        .title("The Raw Power")
        .description("Mine uranium ore to obtain raw uranium for further processing")
        .after(ROOT)
        .whenIconCollected()
    ),

    URANIUM_POWDER = create("uranium_powder", b -> b.icon(CNItems.URANIUM_POWDER)
        .title("Powdered Uranium")
        .description("Crush raw uranium into powder to prepare for further refining")
        .after(RAW_URANIUM)
        .whenIconCollected()
    ),

    AUTOMATIC_URANIUM = create("automatic_uranium", b -> b.icon(CNItems.URANIUM_POWDER)
        .title("Automating Uranium")
        .description("Obtain some uranium powder using the automatic uranium processing")
        .after(ROOT)
        .special(SECRET)
    ),

    URANIUM_LIQUID = create("uranium_liquid", b -> b.icon(CNFluids.URANIUM.getBucket().get())
        .title("Turning Solid To Liquid")
        .description("Obtain some uranium liquid by mixing uranium powder")
        .after(URANIUM_POWDER)
        .whenIconCollected()
    ),

    YELLOWCAKE = create("yellowcake", b -> b.icon(CNItems.YELLOWCAKE)
        .title("The Yellowcake Process")
        .description("Compact uranium liquid to create yellowcake")
        .after(URANIUM_LIQUID)
        .whenIconCollected()
    ),

    CRUNCHY_URANIUM = create("crunchy_uranium", b -> b.icon(CNItems.YELLOWCAKE)
        .title("What Did You Expect")
        .description("Bro..... you did what ??")
        .after(YELLOWCAKE)
        .externalTrigger(ConsumeItemTrigger.TriggerInstance.usedItem(CNItems.YELLOWCAKE))
        .special(SECRET)
    ),

    ENRICHED_YELLOWCAKE = create("enriched_yellowcake", b -> b.icon(CNItems.ENRICHED_YELLOWCAKE)
        .title("Enhancing Yellowcake")
        .description("Use a fan to enrich yellowcake and make it more powerful")
        .after(YELLOWCAKE)
        .whenIconCollected()
    ),

    URANIUM_ROD = create("uranium_rod", b -> b.icon(CNItems.URANIUM_ROD)
        .title("The Power Of The Atom")
        .description("Create your first uranium rod using enriched yellowcake in a mechanical crafter")
        .after(ENRICHED_YELLOWCAKE)
        .whenIconCollected()
    ),

    RAW_THORIUM = create("raw_thorium", b -> b.icon(CNItems.RAW_THORIUM)
        .title("Thorium's Glow")
        .description("Mine thorium ore to obtain raw thorium for further processing")
        .after(ROOT)
        .whenIconCollected()
    ),

    THORIUM_DUST = create("thorium_dust", b -> b.icon(CNItems.THORIUM_DUST)
        .title("Powdered Thorium")
        .description("Crush raw thorium into dust to prepare for further refining")
        .after(RAW_THORIUM)
        .whenIconCollected()
    ),

    THORIUM_LIQUID = create("thorium_liquid", b -> b.icon(CNFluids.THORIUM.getBucket().get())
        .title("Molten Thorium")
        .description("Obtain some thorium liquid by mixing thorium dust")
        .after(THORIUM_DUST)
        .whenIconCollected()
    ),

    THORIUM_INGOT = create("thorium_ingot", b -> b.icon(CNItems.THORIUM_INGOT)
        .title("Compacted Thorium")
        .description("Compact thorium liquid to create a thorium ingot")
        .after(THORIUM_LIQUID)
        .whenIconCollected()
    ),

    THORIUM_ROD = create("thorium_rod", b -> b.icon(CNItems.THORIUM_ROD)
        .title("What A Blue Stick")
        .description("Craft a thorium rod for the first time")
        .after(THORIUM_INGOT)
        .whenIconCollected()
    ),

    CHEMISTRY_101 = create("chemistry_101", b -> b.icon(CNItems.NITRATE)
        .title("Crushing It")
        .description("Mine nitrate ore or crush limestone to obtain nitrate")
        .after(ROOT)
        .whenIconCollected()
    ),

    GETTING_CONCENTRATED = create("getting_concentrated", b -> b.icon(CNItems.NITROGEN_CONCENTRATE)
        .title("Getting Concentrated")
        .description("Smelt nitrate in a blast furnace to obtain nitrogen concentrate")
        .after(CHEMISTRY_101)
        .whenIconCollected()
    ),

    CHILL_OUT = create("chill_out", b -> b.icon(CNItems.COOLED_NITROGEN_CONCENTRATE)
        .title("Chill Out")
        .description("Cool down nitrogen concentrate to prepare it for liquefaction")
        .after(GETTING_CONCENTRATED)
        .whenIconCollected()
    ),

    ABSOLUTE_ZERO = create("absolute_zero", b -> b.icon(CNFluids.LIQUID_NITROGEN.getBucket().get())
        .title("Absolute Zero")
        .description("Mix cooled nitrogen concentrate to obtain liquid nitrogen")
        .after(CHILL_OUT)
        .whenIconCollected()
    ),

    CRYOGENIC_BAPTISM = create("cryogenic_baptism", b -> b.icon(CNFluids.LIQUID_NITROGEN.getBucket().get())
        .title("I can't Feel My Feet Anymore")
        .description("Swim in the liquid nitrogen for the first time")
        .after(ABSOLUTE_ZERO)
        .special(SECRET)
    ),

    COAL_DUST = create("coal_dust", b -> b.icon(CNItems.COAL_DUST)
        .title("Coal Dust")
        .description("Crush coal or charcoal to obtain coal dust, a key crafting material")
        .after(ROOT)
        .whenIconCollected()
    ),

    STEEL_INGOT = create("steel_ingot", b -> b.icon(CNItems.STEEL_INGOT)
        .title("Steel Ingot")
        .description("Combine coal dust and iron ingots to create steel ingots")
        .after(COAL_DUST)
        .whenIconCollected()
    ),

    GRAPHENE = create("graphene", b -> b.icon(CNItems.GRAPHENE)
        .title("Graphene")
        .description("Press coal dust to create graphene")
        .after(COAL_DUST)
        .whenIconCollected()
    ),

    GRAPHITE_ROD = create("graphite_rod", b -> b.icon(CNItems.GRAPHITE_ROD)
        .title("Don't Forget Those Ones")
        .description("Combine graphene and steel ingots in a mechanical crafter to make graphite rods")
        .after(GRAPHENE)
        .whenIconCollected()
    ),

    RAW_LEAD = create("raw_lead", b -> b.icon(CNItems.RAW_LEAD)
        .title("Raw Lead")
        .description("Obtain some raw lead by mining lead ore")
        .after(ROOT)
        .whenIconCollected()
    ),

    LEAD_INGOT = create("lead_ingot", b -> b.icon(CNItems.LEAD_INGOT)
        .title("Lead Ingot")
        .description("Smelt a raw lead to obtain a lead ingot")
        .after(RAW_LEAD)
        .whenIconCollected()
    ),

    REINFORCED_GLASS = create("reinforced_glass", b -> b.icon(CNBlocks.REINFORCED_GLASS)
        .title("Reinforced Glass")
        .description("Craft some reinforced glass for the first time")
        .after(LEAD_INGOT)
        .whenIconCollected()
    ),

    ANTI_RADIATION_ARMOR = create("anti_radiation_armor", b -> b.icon(CNItems.ANTI_RADIATION_HELMETS)
        .title("Anti Radiation Armor")
        .description("Craft your first anti-radiation armor piece to protect yourself from radiation")
        .after(LEAD_INGOT)
        .whenItemCollected(CNTags.CNItemTags.ANTI_RADIATION_ARMOR.tag)
    ),

    AVOIDING_CANCER = create("avoiding_cancer", b -> b.icon(CNItems.ANTI_RADIATION_HELMETS)
        .title("Best Keep Yourself Covered")
        .description("Equip anti-radiation armor for the first time")
        .after(ANTI_RADIATION_ARMOR)
        .externalTrigger(
            new PlayerTrigger.TriggerInstance(CriteriaTriggers.INVENTORY_CHANGED.getId(), EntityPredicate.wrap(EntityPredicate.Builder.entity().of(EntityType.PLAYER).equipment(FULL_ARMOR).build()))
        )
    ),

    DYE_ANTI_RADIATION_ARMOR = create("dye_anti_radiation_armor", b -> b.icon(CNItems.ANTI_RADIATION_HELMETS)
        .title("Pimp My Armor")
        .description("Dye your anti radiation armor to any color")
        .after(ANTI_RADIATION_ARMOR)
    ),

    REACTOR_CASING = create("reactor_casing", b -> b.icon(CNBlocks.REACTOR_CASING)
        .title("The Power Of The Reactor")
        .description("Craft a reactor casing to begin building your nuclear reactor")
        .after(ROOT)
        .whenIconCollected()
    ),

    SILENCE_THE_CORE = create("silence_the_core", b -> b.icon(CNBlocks.REACTOR_ALARM.asItem())
        .title("It Makes So Much Noise !")
        .description("sound the alarm for the first time")
        .after(REACTOR_CASING)
    ),

    REACTOR_CONTROLLER = create("reactor_controller", b -> b.icon(CNBlocks.REACTOR_CONTROLLER)
        .title("Controller Of The Core")
        .description("Craft a reactor controller to manage and regulate your reactor")
        .after(REACTOR_CASING)
        .whenIconCollected()
    ),

    NO_TIME_TO_DIE = create("no_time_to_die", b -> b.icon(CNPaletteStoneTypes.AUTUNITE.baseBlock.get().asItem())
        .title("No Time To Die")
        .description("Stop the reactor before the KABOOOM !")
        .after(REACTOR_CONTROLLER)
    ),

    T1_REACTOR = create("t1", b -> b.icon(CNBlocks.REACTOR_CONTROLLER.asItem())
        .title("Reactor Controller T1")
        .description("Build a tier 1 Nuclear Reactor")
        .special(EXPERT)
        .after(REACTOR_CONTROLLER)
    ),

    T2_REACTOR = create("t2", b -> b.icon(CNBlocks.REACTOR_CONTROLLER.asItem())
        .title("Reactor Controller T2")
        .description("Build a tier 2 Nuclear Reactor")
        .special(EXPERT)
        .after(T1_REACTOR)
    ),

    T3_REACTOR = create("t3", b -> b.icon(CNBlocks.REACTOR_CONTROLLER.asItem())
        .title("Reactor Controller T3")
        .description("Build a tier 3 Nuclear Reactor")
        .special(EXPERT)
        .after(T2_REACTOR)
    ),

    REACTOR_BLUEPRINT = create("reactor_blueprint", b -> b.icon(CNItems.REACTOR_BLUEPRINT)
        .title("Blueprint For Power")
        .description("Craft a reactor blueprint to design the layout of rods in your reactor")
        .after(REACTOR_CONTROLLER)
        .whenIconCollected()
    ),

    REACTOR_ROD_INPUT = create("feeding_the_reactor", b -> b.icon(CNBlocks.REACTOR_ROD_INPUT)
        .title("Feeding The Reactor")
        .description("Craft a Reactor Rod Input to feed heating and cooling rods into your reactor")
        .after(REACTOR_CASING)
        .whenIconCollected()
    ),

    REACTOR_FLUID_INPUT = create("fueling_the_reactor", b -> b.icon(CNBlocks.REACTOR_FLUID_INPUT)
        .title("Fueling The Reactor")
        .description("Craft a Reactor Fluid Input to feed liquid coolant into your reactor")
        .after(REACTOR_ROD_INPUT)
        .whenIconCollected()
    ),

    REACTOR_OUTPUT = create("unlimited_power", b -> b.icon(CNBlocks.REACTOR_OUTPUT)
        .title("Unlimited Power")
        .description("Craft a Reactor Output to extract the energy produced by your reactor")
        .after(REACTOR_FLUID_INPUT)
        .whenIconCollected()
    ),

    REACTOR_CORE = create("reactor_core", b -> b.icon(CNBlocks.REACTOR_CORE)
        .title("Core Of Power")
        .description("Craft the reactor core to harness the full energy of your nuclear reactor")
        .after(REACTOR_CASING)
        .whenIconCollected()
    ),

    REACTOR_COOLER = create("reactor_cooler", b -> b.icon(CNBlocks.REACTOR_COOLER)
        .title("Cooling The Reactor")
        .description("Craft a reactor cooler to cool your reactor")
        .after(REACTOR_CORE)
        .whenIconCollected()
    ),

    REACTOR_FRAME = create("reactor_frame", b -> b.icon(CNBlocks.REACTOR_FRAME)
        .title("Reactor Frame")
        .description("Craft a reactor frame to build your nuclear reactor")
        .after(REACTOR_COOLER)
        .whenIconCollected()
    )

    ;

    private final PackOutput output;

    private static CreateNuclearAdvancement create(String id, UnaryOperator<Builder> b) {
        return new CreateNuclearAdvancement(id, b);
    }

    public CNAdvancement(PackOutput output) {
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

    public static void register() {}
}
