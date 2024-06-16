package net.ynov.createnuclear.item;

import com.tterrag.registrate.util.entry.ItemEntry;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.ynov.createnuclear.CreateNuclear;
import net.ynov.createnuclear.effects.CNEffects;
import net.ynov.createnuclear.groups.CNGroup;
import net.ynov.createnuclear.item.armor.AntiRadiationArmorItem;
import net.ynov.createnuclear.item.cloth.ClothItem;
import net.ynov.createnuclear.item.cloth.ClothItem.DyeItemList;
import net.ynov.createnuclear.tags.CNTag;
import net.ynov.createnuclear.utils.TextUtils;


import static net.ynov.createnuclear.item.armor.AntiRadiationArmorItem.Helmet;
import static net.ynov.createnuclear.item.armor.AntiRadiationArmorItem.Chestplate;
import static net.ynov.createnuclear.item.armor.AntiRadiationArmorItem.Leggings;
import static net.ynov.createnuclear.item.armor.AntiRadiationArmorItem.Boot;

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

        CHARCOAL_DUST = CreateNuclear.REGISTRATE
                    .item("charcoal_dust", Item::new)
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
                .tag(CNTag.forgeItemTag("rods"))
                .register(),

        GRAPHITE_ROD = CreateNuclear.REGISTRATE
                .item("graphite_rod", Item::new)
                .tag(CNTag.forgeItemTag("rods"))
                .register()
    ;

    public static final ItemEntry<? extends AntiRadiationArmorItem.Helmet>
            ANTI_RADIATION_HELMET = CreateNuclear.REGISTRATE.item("anti_radiation_helmet", Helmet::new)
            .tag(CNTag.forgeItemTag("helmets"))
            .lang("Anti Radiation Helmet")
            .register();

    public static final ItemEntry<? extends AntiRadiationArmorItem.Chestplate>
            ANTI_RADIATION_CHESTPLATE = CreateNuclear.REGISTRATE.item("anti_radiation_chestplate", Chestplate::new)
            .tag(CNTag.forgeItemTag("chestplates"))
            .lang("Anti Radiation Chestplate")
            .register();

    public static final ItemEntry<? extends AntiRadiationArmorItem.Leggings>
            ANTI_RADIATION_LEGGINGS = CreateNuclear.REGISTRATE.item("anti_radiation_leggings", Leggings::new)
            .tag(CNTag.forgeItemTag("leggings"))
            .lang("Anti Radiation Leggings")
            .register();

    public static final ItemEntry<? extends AntiRadiationArmorItem.Boot>
            ANTI_RADIATION_BOOTS = CreateNuclear.REGISTRATE.item("anti_radiation_boots", Boot::new)
            .tag(CNTag.forgeItemTag("boots"))
            .lang("Anti Radiation Boots")
            .register();


    public static final DyeItemList<ClothItem> CLOTHS = new DyeItemList<>(color -> {
        String colorName = color.getSerializedName();
        return CreateNuclear.REGISTRATE.item(colorName+ "_cloth", p -> new ClothItem(p, color))
                .tag(CNTag.ItemTags.CLOTH.tag)
                .lang(TextUtils.titleCaseConversion(color.getName()) + " Cloth")
                .model((c, p) -> p.generated(c, CreateNuclear.asResource("item/cloth/" + colorName + "_cloth")))
                .register();
    });


    public static final Potion potion_1 = registerPotion("potion_of_radiation_1", new Potion(new MobEffectInstance(CNEffects.RADIATION.get(), 900)));
    public static final Potion potion_augment_1 = registerPotion("potion_of_radiation_augment_1", new Potion(new MobEffectInstance(CNEffects.RADIATION.get(), 1800)));
    public static final Potion potion_2 = registerPotion("potion_of_radiation_2", new Potion(new MobEffectInstance(CNEffects.RADIATION.get(), 410, 1)));



    private static void addItemToIngredientItemGroup(FabricItemGroupEntries entries) {
        /*entries.accept(YELLOW_CAKE_NETHER_STAR, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);*/
    }

    private static Item registerItem(String name, Item item) {
        return Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(CreateNuclear.MOD_ID, name), item);
    }

    private static ItemEntry<Item> registerItemEntry(String path) {
        return CreateNuclear.REGISTRATE.item(path + "_cloth", Item::new).tag(CNTag.ItemTags.CLOTH.tag).register();
    }

    public static void registerCNItems() {
        CreateNuclear.LOGGER.info("Registering mod items for " + CreateNuclear.MOD_ID);
        ItemGroupEvents.modifyEntriesEvent(CNGroup.MAIN_KEY).register(CNItems::addItemToIngredientItemGroup);
        registerPotionsRecipes();
    }

    public static Potion registerPotion(String name, Potion potion) {
        return Registry.register(BuiltInRegistries.POTION, new ResourceLocation(CreateNuclear.MOD_ID, name), potion);
    }

    public static void registerPotionsRecipes() {
        PotionBrewing.addMix(Potions.AWKWARD, CNItems.YELLOW_CAKE_ENRICHED.get(), CNItems.potion_1);
        PotionBrewing.addMix(potion_1, Items.REDSTONE, CNItems.potion_augment_1);
        PotionBrewing.addMix(potion_1, Items.GLOWSTONE_DUST, CNItems.potion_2);
    }
}
