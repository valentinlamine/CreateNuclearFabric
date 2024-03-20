package net.ynov.createnuclear.item;

import com.tterrag.registrate.util.entry.ItemEntry;

import io.github.fabricators_of_create.porting_lib.brewing.BrewingRecipeRegistry;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeableArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.ynov.createnuclear.CreateNuclear;
import net.ynov.createnuclear.effects.CNEffects;
import net.ynov.createnuclear.groups.CNGroup;

public class CNItems {
    public static final Item URANIUM_POWDER = registerItem("uranium_powder", new Item(new FabricItemSettings()));
    public static final Item YELLOW_CAKE = registerItem("yellow_cake", new Item(new FabricItemSettings().food(new FoodProperties.Builder().nutrition(20).alwaysEat().saturationMod(0.3F).effect(new MobEffectInstance(MobEffects.POISON, 6000, 25), 1.0F).effect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 6000, 5), 1.0F).effect(new MobEffectInstance(MobEffects.HUNGER, 6000, 1000), 1.0F).effect(new MobEffectInstance(MobEffects.CONFUSION, 6000, 5), 1.0F).effect(new MobEffectInstance(MobEffects.WITHER, 6000, 8), 1.0F).effect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 6000, 5), 1.0F).build())));
    public static final Item YELLOW_CAKE_ENRICHED = registerItem("yellow_cake_enriched", new Item(new FabricItemSettings()));
    public static final Item COAL_DUST = registerItem("coal_dust", new Item(new FabricItemSettings()));
    public static final Item CHARCOAL_DUST = registerItem("charcoal_dust", new Item(new FabricItemSettings()));
    public static final Item GRAPHENE = registerItem("graphene", new Item(new FabricItemSettings()));
    public static final Item STEEL_INGOT = registerItem("steel_ingot", new Item(new FabricItemSettings()));
    public static final Item YELLOW_CAKE_NETHER_STAR = registerItem("yellow_cake_nether_star_wip", new Item(new FabricItemSettings()));
    public static final ItemEntry<Item> URANIUM_ROD = CreateNuclear.REGISTRATE.item("uranium_rod", Item::new).register();
    public static final ItemEntry<Item> GRAPHITE_ROD = CreateNuclear.REGISTRATE.item("graphite_rod", Item::new).register();
    public static final Item RAW_URANIUM = registerItem("raw_uranium", new Item(new FabricItemSettings()));
    public static final Item RAW_LEAD = registerItem("raw_lead", new Item(new FabricItemSettings()));
    public static final Item LEAD_INGOT = registerItem("lead_ingot", new Item(new FabricItemSettings()));
    public static final Item LEAD_NUGGET = registerItem("lead_nugget", new Item(new FabricItemSettings()));
    public static final Item ANTI_RADIATION_HELMET = registerItem("anti_radiation_helmet", new ArmorItem(CNArmorMaterials.ANTI_RADIATION_SUIT, ArmorItem.Type.HELMET, new FabricItemSettings()));
    public static final Item ANTI_RADIATION_CHESTPLATE = registerItem("anti_radiation_chestplate", new ArmorItem(CNArmorMaterials.ANTI_RADIATION_SUIT, ArmorItem.Type.CHESTPLATE, new FabricItemSettings()));
    public static final Item ANTI_RADIATION_LEGGINGS = registerItem("anti_radiation_leggings", new ArmorItem(CNArmorMaterials.ANTI_RADIATION_SUIT, ArmorItem.Type.LEGGINGS, new FabricItemSettings()));
    public static final Item ANTI_RADIATION_BOOTS = registerItem("anti_radiation_boots", new ArmorItem(CNArmorMaterials.ANTI_RADIATION_SUIT, ArmorItem.Type.BOOTS, new FabricItemSettings()));
    public static final Item WHITE_CLOTH = registerItem("white_cloth", new Item(new FabricItemSettings()));
    public static final Item YELLOW_CLOTH = registerItem("yellow_cloth", new Item(new FabricItemSettings()));
    public static final Item RED_CLOTH = registerItem("red_cloth", new Item(new FabricItemSettings()));
    public static final Item BLUE_CLOTH = registerItem("blue_cloth", new Item(new FabricItemSettings()));
    public static final Item GREEN_CLOTH = registerItem("green_cloth", new Item(new FabricItemSettings()));
    public static final Item BLACK_CLOTH = registerItem("black_cloth", new Item(new FabricItemSettings()));
    public static final Item ORANGE_CLOTH = registerItem("orange_cloth", new Item(new FabricItemSettings()));
    public static final Item PURPLE_CLOTH = registerItem("purple_cloth", new Item(new FabricItemSettings()));
    public static final Item BROWN_CLOTH = registerItem("brown_cloth", new Item(new FabricItemSettings()));
    public static final Item PINK_CLOTH = registerItem("pink_cloth", new Item(new FabricItemSettings()));
    public static final Item CYAN_CLOTH = registerItem("cyan_cloth", new Item(new FabricItemSettings()));
    public static final Item LIGHT_GREY_CLOTH = registerItem("light_grey_cloth", new Item(new FabricItemSettings()));
    public static final Item GREY_CLOTH = registerItem("grey_cloth", new Item(new FabricItemSettings()));
    public static final Item LIGHT_BLUE_CLOTH = registerItem("light_blue_cloth", new Item(new FabricItemSettings()));
    public static final Item LIME_CLOTH = registerItem("lime_cloth", new Item(new FabricItemSettings()));
    public static final Item MAGENTA_CLOTH = registerItem("magenta_cloth", new Item(new FabricItemSettings()));


    public static final Potion potion_1 = registerPotion("potion_of_radiation_1", new Potion(new MobEffectInstance(CNEffects.RADIATION.get(), 900)));
    public static final Potion potion_augment_1 = registerPotion("potion_of_radiation_augment_1", new Potion(new MobEffectInstance(CNEffects.RADIATION.get(), 1800)));
    public static final Potion potion_2 = registerPotion("potion_of_radiation_2", new Potion(new MobEffectInstance(CNEffects.RADIATION.get(), 410, 1)));



    private static void addItemToIngredientItemGroup(FabricItemGroupEntries entries) {
        entries.accept(URANIUM_POWDER, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        entries.accept(YELLOW_CAKE, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        entries.accept(YELLOW_CAKE_ENRICHED, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        entries.accept(COAL_DUST, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        entries.accept(CHARCOAL_DUST, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        entries.accept(GRAPHENE, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        entries.accept(STEEL_INGOT, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        //entries.accept(YELLOW_CAKE_NETHER_STAR, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        entries.accept(URANIUM_ROD, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        entries.accept(GRAPHITE_ROD, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        entries.accept(RAW_URANIUM,CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        entries.accept(RAW_LEAD,CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        entries.accept(LEAD_INGOT,CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        entries.accept(LEAD_NUGGET,CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        entries.accept(ANTI_RADIATION_HELMET,CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        entries.accept(ANTI_RADIATION_CHESTPLATE,CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        entries.accept(ANTI_RADIATION_LEGGINGS,CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        entries.accept(ANTI_RADIATION_BOOTS,CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        entries.accept(WHITE_CLOTH,CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        entries.accept(YELLOW_CLOTH,CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        entries.accept(RED_CLOTH,CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        entries.accept(BLUE_CLOTH,CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        entries.accept(GREEN_CLOTH,CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        entries.accept(BLACK_CLOTH,CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        entries.accept(ORANGE_CLOTH,CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        entries.accept(PURPLE_CLOTH,CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        entries.accept(BROWN_CLOTH,CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        entries.accept(PINK_CLOTH,CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        entries.accept(CYAN_CLOTH,CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        entries.accept(LIGHT_GREY_CLOTH,CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        entries.accept(GREY_CLOTH,CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        entries.accept(LIGHT_BLUE_CLOTH,CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        entries.accept(LIME_CLOTH,CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        entries.accept(MAGENTA_CLOTH,CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
    }

    private static Item registerItem(String name, Item item) {
        return Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(CreateNuclear.MOD_ID, name), item);
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
        PotionBrewing.addMix(Potions.AWKWARD, CNItems.YELLOW_CAKE_ENRICHED, CNItems.potion_1);
        PotionBrewing.addMix(potion_1, Items.REDSTONE, CNItems.potion_augment_1);
        PotionBrewing.addMix(potion_1, Items.GLOWSTONE_DUST, CNItems.potion_2);
    }
}
