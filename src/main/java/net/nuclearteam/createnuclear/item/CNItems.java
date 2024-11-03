package net.nuclearteam.createnuclear.item;

import static net.nuclearteam.createnuclear.item.armor.AntiRadiationArmorItem.Boot;
import static net.nuclearteam.createnuclear.item.armor.AntiRadiationArmorItem.Chestplate;
import static net.nuclearteam.createnuclear.item.armor.AntiRadiationArmorItem.Helmet;
import static net.nuclearteam.createnuclear.item.armor.AntiRadiationArmorItem.Leggings;

import com.tterrag.registrate.util.entry.EntityEntry;
import com.tterrag.registrate.util.entry.ItemEntry;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.nuclearteam.createnuclear.CreateNuclear;
import net.nuclearteam.createnuclear.effects.CNEffects;
import net.nuclearteam.createnuclear.groups.CNGroup;
import net.nuclearteam.createnuclear.item.armor.AntiRadiationArmorItem;
import net.nuclearteam.createnuclear.item.cloth.ClothItem;
import net.nuclearteam.createnuclear.item.cloth.ClothItem.DyeItemList;
import net.nuclearteam.createnuclear.multiblock.configuredItem.ConfiguredReactorItem;
import net.nuclearteam.createnuclear.tags.CNTag;
import net.nuclearteam.createnuclear.tools.CustomSpawnEgg;
import net.nuclearteam.createnuclear.utils.TextUtils;
import net.nuclearteam.createnuclear.entity.CNMobEntityType;
import net.minecraft.world.entity.EntityType;

public class CNItems {

    public static final Item YELLOW_CAKE_NETHER_STAR = registerItem("yellow_cake_nether_star_wip", new Item(new FabricItemSettings()));

    public static final ItemEntry<Item>
        URANIUM_POWDER = CreateNuclear.REGISTRATE
                .item("uranium_powder", Item::new)
                .register(),

        YELLOW_CAKE = CreateNuclear.REGISTRATE
                .item("yellow_cake", Item::new)
                .properties(p -> p.food(new FoodProperties.Builder()
                    .nutrition(20)
                    .saturationMod(0.3F)
                    .alwaysEat()
                    .effect(new MobEffectInstance(CNEffects.RADIATION.get(), 6000, 25), 1.0F)
                    .effect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 6000, 5), 1.0F)
                    .effect(new MobEffectInstance(MobEffects.HUNGER, 6000, 1000), 1.0F)
                    .effect(new MobEffectInstance(MobEffects.CONFUSION, 6000, 5), 1.0F)
                    .effect(new MobEffectInstance(MobEffects.WITHER, 6000, 8), 1.0F)
                    .effect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 6000, 5), 1.0F)
                    .build())
                )
                .register(),

        YELLOW_CAKE_ENRICHED = CreateNuclear.REGISTRATE
                    .item("yellow_cake_enriched", Item::new)
                    .register(),

        COAL_DUST = CreateNuclear.REGISTRATE
                    .item("coal_dust", Item::new)
                    .tag(CNTag.forgeItemTag("dusts"), CNTag.forgeItemTag("coal_dusts"))
                    .register(),

        GRAPHENE = CreateNuclear.REGISTRATE
                    .item("graphene", Item::new)
                    .register(),

        STEEL_INGOT = CreateNuclear.REGISTRATE
                    .item("steel_ingot", Item::new)
                    .tag(CNTag.forgeItemTag("ingots"), CNTag.forgeItemTag("ingots/steel"))
                    .register(),

        RAW_URANIUM = CreateNuclear.REGISTRATE
                    .item("raw_uranium", Item::new)
                    .tag(CNTag.forgeItemTag("raw_ores"), CNTag.forgeItemTag("raw_materials"))
                    .register(),

        RAW_LEAD = CreateNuclear.REGISTRATE
                .item("raw_lead", Item::new)
                .tag(CNTag.forgeItemTag("raw_ores"), CNTag.forgeItemTag("raw_materials"))
                .register(),

        LEAD_INGOT = CreateNuclear.REGISTRATE
                .item("lead_ingot", Item::new)
                .tag(CNTag.forgeItemTag("ingots"), CNTag.forgeItemTag("ingots/lead"), CNTag.forgeItemTag("lead_ingots"))
                .register(),

        LEAD_NUGGET = CreateNuclear.REGISTRATE
                .item("lead_nugget", Item::new)
                .tag(CNTag.forgeItemTag("nuggets"), CNTag.forgeItemTag("lead_nuggets"))
                .register(),

        URANIUM_ROD = CreateNuclear.REGISTRATE
                .item("uranium_rod", Item::new)
                .tag(CNTag.forgeItemTag("rods"), CNTag.ItemTags.FUEL.tag)
                .register(),

        GRAPHITE_ROD = CreateNuclear.REGISTRATE
                .item("graphite_rod", Item::new)
                .tag(CNTag.forgeItemTag("rods"), CNTag.ItemTags.COOLER.tag)
                .register()
    ;
  
    public static final Helmet.DyeItemHelmetList<Helmet> ANTI_RADIATION_HELMETS = new Helmet.DyeItemHelmetList<>(color -> {
       String colorName = color.getSerializedName();
       return CreateNuclear.REGISTRATE.item(colorName + "_antiradiation_helmet", p -> new Helmet(p, color))
               .tag(CNTag.forgeItemTag("helmets"))
               .lang(TextUtils.titleCaseConversion(color.getName()) +" Anti Radiation Helmet")
               .model((c, p) -> p.generated(c, CreateNuclear.asResource("item/armors/helmets/" + colorName + "_antiradiation_helmet")))
               .register();

    });

    public static final Chestplate.DyeItemChestplateList<Chestplate> ANTI_RADIATION_CHESTPLATES = new Chestplate.DyeItemChestplateList<>(color -> {
        String colorName = color.getSerializedName();
        return CreateNuclear.REGISTRATE.item(colorName + "_antiradiation_chestplate",  p -> new Chestplate(p, color))
                .tag(CNTag.forgeItemTag("chestplates"))
                .lang(TextUtils.titleCaseConversion(color.getName()) +" Anti Radiation Chestplate")
                .model((c, p) -> p.generated(c, CreateNuclear.asResource("item/armors/chestplates/" + colorName + "_antiradiation_chestplate")))
                .register();

    });

    public static final Leggings.DyeItemLeggingsList<Leggings> ANTI_RADIATION_LEGGINGS = new Leggings.DyeItemLeggingsList<>(color -> {
        String colorName = color.getSerializedName();
        return CreateNuclear.REGISTRATE.item(colorName + "_antiradiation_leggings",  p -> new Leggings(p, color))
                .tag(CNTag.forgeItemTag("leggings"))
                .lang(TextUtils.titleCaseConversion(color.getName()) +" Anti Radiation Leggings")
                .model((c, p) -> p.generated(c, CreateNuclear.asResource("item/armors/leggings/" + colorName + "_antiradiation_leggings")))
                .register();

    });

    public static final ItemEntry<? extends AntiRadiationArmorItem.Boot>
            ANTI_RADIATION_BOOTS = CreateNuclear.REGISTRATE.item("anti_radiation_boots", Boot::new)
            .tag(CNTag.forgeItemTag("boots"))
            .lang("Anti Radiation Boots")
            .model((c, p) -> p.generated(c, CreateNuclear.asResource("item/armors/anti_radiation_boots")))
            .register();


    public static final DyeItemList<ClothItem> CLOTHS = new DyeItemList<>(color -> {
        String colorName = color.getSerializedName();
        return CreateNuclear.REGISTRATE.item(colorName+ "_cloth", p -> new ClothItem(p, color))
                .tag(CNTag.ItemTags.CLOTH.tag)
                .lang(TextUtils.titleCaseConversion(color.getName()) + " Cloth")
                .model((c, p) -> p.generated(c, CreateNuclear.asResource("item/cloth/" + colorName + "_cloth")))
                .register();
    });

    public static final ItemEntry<ConfiguredReactorItem> CONFIGURED_REACTOR_ITEM = CreateNuclear.REGISTRATE
            .item("configured_reactor_item", ConfiguredReactorItem::new)
            .lang("Configured Reactor")
            .model((c, p) -> p.generated(c, CreateNuclear.asResource("item/enriched_flint_and_steel")))
            .properties(p -> p.stacksTo(1))
            .register();

    public static final ItemEntry<CustomSpawnEgg> SPAWN_WOLF = registerSpawnEgg("wolf_irradiated_spawn_egg", CNMobEntityType.IRRADIATED_WOLF, 0x42452B,0x4C422B, "Irradiated Wolf Spawn Egg");
    public static final ItemEntry<CustomSpawnEgg> SPAWN_CAT = registerSpawnEgg("cat_irradiated_spawn_egg", CNMobEntityType.IRRADIATED_CAT, 0x382C19,0x742728, "Irradiated Cat Spawn Egg");
    public static final ItemEntry<CustomSpawnEgg> SPAWN_CHICKEN = registerSpawnEgg("chicken_irradiated_spawn_egg", CNMobEntityType.IRRADIATED_CHICKEN, 0x6B9455,0x95393C, "Irradiated Chicken Spawn Egg");


    public static final Potion potion_1 = registerPotion("potion_of_radiation_1", new Potion(new MobEffectInstance(CNEffects.RADIATION.get(), 900)));
    public static final Potion potion_augment_1 = registerPotion("potion_of_radiation_augment_1", new Potion(new MobEffectInstance(CNEffects.RADIATION.get(), 1800)));
    public static final Potion potion_2 = registerPotion("potion_of_radiation_2", new Potion(new MobEffectInstance(CNEffects.RADIATION.get(), 410, 1)));

    private static void addItemToIngredientItemGroup(FabricItemGroupEntries entries) {
        /*entries.accept(YELLOW_CAKE_NETHER_STAR, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);*/
    }

    private static Item registerItem(String name, Item item) {
        return Registry.register(BuiltInRegistries.ITEM, CreateNuclear.asResource(name), item);
    }

    private static ItemEntry<Item> registerItemEntry(String path) {
        return CreateNuclear.REGISTRATE.item(path + "_cloth", Item::new).tag(CNTag.ItemTags.CLOTH.tag).register();
    }

    private static ItemEntry<CustomSpawnEgg> registerSpawnEgg(String name, EntityEntry<? extends Mob> mobEntityType, int backgroundColor, int highlightColor, String nameItems) {
        return CreateNuclear.REGISTRATE
                .item(name, p -> new CustomSpawnEgg(p, backgroundColor,highlightColor,  mobEntityType))
                .lang(nameItems)
                .model((c, p) -> p.withExistingParent(c.getName(), new ResourceLocation("item/template_spawn_egg")))
                .register();
    }

    public static void registerCNItems() {
        CreateNuclear.LOGGER.info("Registering mod items for " + CreateNuclear.MOD_ID);
        ItemGroupEvents.modifyEntriesEvent(CNGroup.MAIN_KEY).register(CNItems::addItemToIngredientItemGroup);
        registerPotionsRecipes();
    }

    public static Potion registerPotion(String name, Potion potion) {
        return Registry.register(BuiltInRegistries.POTION, CreateNuclear.asResource(name), potion);
    }

    public static void registerPotionsRecipes() {
        PotionBrewing.addMix(Potions.AWKWARD, CNItems.YELLOW_CAKE_ENRICHED.get(), CNItems.potion_1);
        PotionBrewing.addMix(potion_1, Items.REDSTONE, CNItems.potion_augment_1);
        PotionBrewing.addMix(potion_1, Items.GLOWSTONE_DUST, CNItems.potion_2);
    }
}
