package net.nuclearteam.createnuclear;

import com.simibubi.create.foundation.data.recipe.CommonMetal;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.world.item.DyeColor;
import io.github.fabricators_of_create.porting_lib.util.LazySpawnEggItem;
import net.nuclearteam.createnuclear.content.biome.BiomeIrradiationExtractorItem;
import net.nuclearteam.createnuclear.foundation.data.CNBuilderTransformers;
import net.nuclearteam.createnuclear.infrastructure.config.CNConfigs;
import net.nuclearteam.createnuclear.api.data.recipe.SmithingClothRecipeBuilder;
import net.nuclearteam.createnuclear.content.radiation.RadiationItem;
import net.nuclearteam.createnuclear.content.equipment.cloth.ClothItem;
import net.nuclearteam.createnuclear.content.equipment.cloth.ClothItem.Cloths;
import net.nuclearteam.createnuclear.content.multiblock.bluePrintItem.ReactorBluePrintItem;
import net.nuclearteam.createnuclear.foundation.utility.TextUtils;
import net.nuclearteam.createnuclear.CNTags.CNItemTags;
import net.nuclearteam.createnuclear.api.ItemRodTypesValue;
import net.nuclearteam.createnuclear.api.multiblock.rods.RodType;
import net.nuclearteam.createnuclear.content.equipment.armor.AntiRadiationArmorItem.Boot;
import net.nuclearteam.createnuclear.content.equipment.armor.AntiRadiationArmorItem.Chestplate;
import net.nuclearteam.createnuclear.content.equipment.armor.AntiRadiationArmorItem.Helmet;
import net.nuclearteam.createnuclear.content.equipment.armor.AntiRadiationArmorItem.Leggings;
import net.nuclearteam.createnuclear.foundation.item.DyedItemsList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.nuclearteam.createnuclear.foundation.data.CNBuilderTransformers.biomeRestoreModel;
import static net.nuclearteam.createnuclear.foundation.data.CNBuilderTransformers.coloredArmorModel;

@SuppressWarnings({"unused", "deprecation"})
public class CNItems {

    public static final ItemEntry<? extends Item>
        YELLOWCAKE = CreateNuclear.REGISTRATE
            .item("yellowcake", p -> new RadiationItem(p, 4))
            .properties(p -> p.food(new FoodProperties.Builder()
                .nutrition(20)
                .saturationMod(0.3F)
                .alwaysEat()
                .effect(new MobEffectInstance(CNEffects.RADIATION.get(),600,2), 1.0F)
                .effect(new MobEffectInstance(MobEffects.HUNGER, 600, 1), 1.0F)
                .build())
            )
            .register(),

        GRAPHENE = CreateNuclear.REGISTRATE
            .item("graphene", Item::new)
            .register(),

        ENRICHED_YELLOWCAKE = CreateNuclear.REGISTRATE
            .item("enriched_yellowcake", p -> new RadiationItem(p, 2))
            .register(),

        RAW_LEAD = CreateNuclear.REGISTRATE
            .item("raw_lead", Item::new)
            .tag(CNTags.forgeItemTag("raw_ores"), CNTags.forgeItemTag("raw_materials"), CNTags.forgeItemTag("raw_materials/lead"))
            .recipe((c, p) -> ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, c.get(), 9)
                .unlockedBy("has_storage_blocks_raw_lead", RegistrateRecipeProvider.has(CNTags.forgeItemTag("storage_blocks/raw_lead")))
                .requires(CNTags.forgeItemTag("storage_blocks/raw_lead"))
                .save(p, CreateNuclear.asResource("crafting/" + c.getName() + "_from_decompacting"))
            )
            .register(),

        RAW_URANIUM = CreateNuclear.REGISTRATE
            .item("raw_uranium", p -> new RadiationItem(p, 3))
            .tag(CNTags.forgeItemTag("raw_ores"), CNTags.forgeItemTag("raw_materials"), CNTags.forgeItemTag("raw_materials/uranium"))
            .recipe((c, p) -> ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, c.get(), 9)
                    .unlockedBy("has_storage_blocks_raw_uranium", RegistrateRecipeProvider.has(CNTags.forgeItemTag("storage_blocks/raw_uranium")))
                    .requires(CNTags.forgeItemTag("storage_blocks/raw_uranium"))
                    .save(p, CreateNuclear.asResource("crafting/" + c.getName() + "_from_decompacting"))
            )
            .register(),

        RAW_THORIUM = CreateNuclear.REGISTRATE
            .item("raw_thorium", Item::new)
            .tag(CNTags.forgeItemTag("raw_ores"), CNTags.forgeItemTag("raw_materials"), CNTags.forgeItemTag("raw_materials/thorium"))
            .recipe((c, p) -> ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, c.get(), 9)
                .unlockedBy("has_storage_blocks_raw_thorium", RegistrateRecipeProvider.has(CNTags.forgeItemTag("storage_blocks/raw_thorium")))
                .requires(CNTags.forgeItemTag("storage_blocks/raw_thorium"))
                .save(p, CreateNuclear.asResource("crafting/" + c.getName() + "_from_decompacting"))
            )
            .register(),

        THORIUM_DUST = CreateNuclear.REGISTRATE
            .item("thorium_dust", Item::new)
            .tag(CNTags.forgeItemTag("dusts"), CNTags.forgeItemTag("dusts/thorium"))
            .register(),

        URANIUM_POWDER = CreateNuclear.REGISTRATE
            .item("uranium_powder", p -> new RadiationItem(p, 2))
            .tag(CNTags.forgeItemTag("dusts"), CNTags.forgeItemTag("dusts/uranium"))
            .register(),

        COAL_DUST = CreateNuclear.REGISTRATE
            .item("coal_dust", Item::new)
            .tag(CNTags.forgeItemTag("dusts"), CNTags.forgeItemTag("dusts/coal"))
            .register(),


        STEEL_INGOT = CreateNuclear.REGISTRATE
            .item("steel_ingot", Item::new)
            .tag(CNTags.forgeItemTag("ingots"), CNTags.forgeItemTag("ingots/steel"), CommonMetal.STEEL.ingots)
            .recipe((c, p) -> ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, c.get(), 9)
                    .unlockedBy("has_storage_blocks_steel", RegistrateRecipeProvider.has(CNTags.forgeItemTag("storage_blocks/steel")))
                    .requires(CNTags.forgeItemTag("storage_blocks/steel"))
                    .save(p, CreateNuclear.asResource("crafting/" + c.getName() + "_from_decompacting"))
            )
            .register(),

        GRAPHITE_ROD = CreateNuclear.REGISTRATE
            .item("graphite_rod", Item::new)
            .onRegister(ItemRodTypesValue.setRodTypeInfos(new RodType.Builder()
                .baseRodHeat(() -> CNConfigs.server().rods.graphiteBaseValue.get())
                .proximityRodHeat(() -> CNConfigs.server().rods.graphiteProximityMalus.getF())
                .rodTimer(() -> CNConfigs.server().rods.graphiteRodLifetime.get())
                .ratio(() -> CNConfigs.server().rods.graphiteHeatRatio.get())
                .coolerRodType()))
            .tag(CNTags.forgeItemTag("rods"), CNItemTags.COOLER.tag)
            .register(),

        LEAD_INGOT = CreateNuclear.REGISTRATE
            .item("lead_ingot", Item::new)
            .tag(CNTags.forgeItemTag("ingots"), CNTags.forgeItemTag("ingots/lead"), CommonMetal.LEAD.ingots)
            .recipe((c, p) -> ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, c.get(),9)
                    .unlockedBy("has_storage_blocks_lead", RegistrateRecipeProvider.has(CNTags.forgeItemTag("storage_blocks/lead")))
                    .requires(CNTags.forgeItemTag("storage_blocks/lead"))
                    .save(p, CreateNuclear.asResource("crafting/" + c.getName() + "_from_decompacting"))
            )
            .register(),

        THORIUM_INGOT = CreateNuclear.REGISTRATE
            .item("thorium_ingot", Item::new)
            .model((c, p) -> p.generated(c, CreateNuclear.asResource("item/thorium_ingot")))
            .tag(CNTags.forgeItemTag("ingots"), CNTags.forgeItemTag("ingots/thorium"))
            .recipe((c, p) -> ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, c.get(), 9)
                .unlockedBy("has_storage_blocks_thorium", RegistrateRecipeProvider.has(CNTags.forgeItemTag("storage_blocks/thorium")))
                .requires(CNTags.forgeItemTag("storage_blocks/thorium"))
                .save(p, CreateNuclear.asResource("crafting/" + c.getName() + "_from_decompacting"))
            )
            .register(),


        STEEL_NUGGET = CreateNuclear.REGISTRATE
            .item("steel_nugget", Item::new)
            .tag(CNTags.forgeItemTag("nuggets"), CNTags.forgeItemTag("nuggets/steel"), CommonMetal.STEEL.nuggets)
            .recipe((c, p) -> ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, c.get(), 9)
                    .unlockedBy("has_storage_blocks_steel_nugget", RegistrateRecipeProvider.has(CNTags.forgeItemTag("ingots/steel")))
                    .requires(CNTags.forgeItemTag("ingots/steel"))
                    .save(p, CreateNuclear.asResource("crafting/" + c.getName() + "_from_decompacting"))
            )
            .register(),

        URANIUM_ROD = CreateNuclear.REGISTRATE
            .item("uranium_rod", p -> new RadiationItem(p, 100))
            .onRegister(ItemRodTypesValue.setRodTypeInfos(new RodType.Builder()
                .baseRodHeat(() -> CNConfigs.server().rods.uraniumBaseValue.get())
                .proximityRodHeat(() ->(float) CNConfigs.server().rods.uraniumProximityBonus.get())
                .rodTimer(() -> CNConfigs.server().rods.uraniumRodLifetime.get())
                .ratio(() -> CNConfigs.server().rods.uraniumHeatRatio.get())
                .fuelRodType()))
            .tag(CNTags.forgeItemTag("rods"), CNItemTags.FUEL.tag)
            .register(),

        LEAD_NUGGET = CreateNuclear.REGISTRATE
            .item("lead_nugget", Item::new)
            .tag(CNTags.forgeItemTag("nuggets"), CNTags.forgeItemTag("nuggets/lead"), CommonMetal.LEAD.nuggets)
            .recipe((c, p) -> ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, c.get(), 9)
                    .unlockedBy("has_storage_blocks_lead_nugget", RegistrateRecipeProvider.has(CNTags.forgeItemTag("ingots/lead")))
                    .requires(CNTags.forgeItemTag("ingots/lead"))
                    .save(p, CreateNuclear.asResource("crafting/" + c.getName() + "_from_decompacting"))
            )
            .register(),

        THORIUM_NUGGET = CreateNuclear.REGISTRATE
            .item("thorium_nugget", Item::new)
            .model((c, p) -> p.generated(c, CreateNuclear.asResource("item/thorium_nugget")))
            .tag(CNTags.forgeItemTag("nuggets"), CNTags.forgeItemTag("nuggets/thorium"))
            .recipe((c, p) -> ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, c.get(), 9)
                .unlockedBy("has_storage_blocks_steel_nugget", RegistrateRecipeProvider.has(CNTags.forgeItemTag("ingots/thorium")))
                .requires(CNTags.forgeItemTag("ingots/thorium"))
                .save(p, CreateNuclear.asResource("crafting/" + c.getName() + "_from_decompacting"))
            )
            .register(),

        NITRATE = CreateNuclear.REGISTRATE
            .item("nitrate", Item::new)
            .lang("Nitrate")
            .register(),

        NITROGEN_CONCENTRATE = CreateNuclear.REGISTRATE
            .item("nitrogen_concentrate", Item::new)
            .lang("Nitrogen Concentrate")
            .register(),

        COOLED_NITROGEN_CONCENTRATE = CreateNuclear.REGISTRATE
            .item("cooled_nitrogen_concentrate", Item::new)
            .lang("Cooled Nitrogen Concentrate")
            .register(),

        THORIUM_ROD = CreateNuclear.REGISTRATE
            .item("thorium_rod", Item::new)
            .onRegister(ItemRodTypesValue.setRodTypeInfos(new RodType.Builder()
                .baseRodHeat(() -> CNConfigs.server().rods.baseValueThorium.get())
                .proximityRodHeat(() -> (float) CNConfigs.server().rods.thoriumProxyBonus.get())
                .rodTimer(() -> CNConfigs.server().rods.thoriumRodLifetime.get())
                .ratio(() -> CNConfigs.server().rods.thoriumHeatRatio.get())
                .fuelRodType()))
            .tag(CNTags.forgeItemTag("rods"), CNItemTags.FUEL.tag)
            .register()
    ;

    public static final ItemEntry<Helmet> ANTI_RADIATION_HELMETS = CreateNuclear.REGISTRATE
        .item("default_anti_radiation_helmet", p -> new Helmet(p, DyeColor.BLACK))
        .tag(
            CNTags.forgeItemTag("armors/helmets"),
            CNItemTags.ANTI_RADIATION_ARMOR.tag
        )
        .recipe((c, p) -> {
            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, c.get())
                .unlockedBy("has_cloth", RegistrateRecipeProvider.has(CNItemTags.CLOTH.tag))
                .define('X', CommonMetal.LEAD.ingots)
                .define('Y', CommonMetal.BRASS.ingots)
                .define('Z', CNBlocks.REINFORCED_GLASS.asItem())
                .pattern("YXY")
                .pattern("XZX")
                .showNotification(true)
                .save(p, CreateNuclear.asResource("crafting/items/armors/" + c.getName()));

            for (Cloths cloth : Cloths.values()) {
                SmithingClothRecipeBuilder
                    .smithingCloth(
                        Ingredient.EMPTY,
                        Ingredient.of(c.get()),
                        Ingredient.of(cloth.getItem()),
                        RecipeCategory.COMBAT,
                        new ItemStack(c.get())
                    )
                    .unlocks("has_cloth", RegistrateRecipeProvider.has(CNItemTags.CLOTH.tag))
                    .save(p, CreateNuclear.asResource("smithing/" + c.getName() + "_" + cloth.getColor().getSerializedName()));
            }
        })
        .lang("Anti Radiation Helmet")
        .model(coloredArmorModel("helmet", "layer0", "particle"))
        .register();

    public static final ItemEntry<Chestplate> ANTI_RADIATION_CHESTPLATES = CreateNuclear.REGISTRATE
        .item("default_anti_radiation_chestplate",  p -> new Chestplate(p, DyeColor.BLACK))
        .tag(
            CNTags.forgeItemTag("armors/chestplates"),
            CNTags.CNItemTags.ANTI_RADIATION_ARMOR.tag
        )
        .recipe((c, p) -> {
            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, c.get())
                    .unlockedBy("has_cloth", RegistrateRecipeProvider.has(CNItemTags.CLOTH.tag))
                    .define('X', CommonMetal.LEAD.ingots)
                    .define('Y', CommonMetal.BRASS.ingots)
                    .define('Z', CNItems.GRAPHITE_ROD)
                    .pattern("Y Y")
                    .pattern("XXX")
                    .pattern("ZXZ")
                    .showNotification(true)
                    .save(p, CreateNuclear.asResource("crafting/items/armors/" + c.getName()));
            for (Cloths cloth : Cloths.values()) {
                SmithingClothRecipeBuilder
                    .smithingCloth(
                        Ingredient.EMPTY,
                        Ingredient.of(c.get()),
                        Ingredient.of(cloth.getItem()),
                        RecipeCategory.COMBAT,
                        new ItemStack(c.get())
                    )
                    .unlocks("has_cloth", RegistrateRecipeProvider.has(CNItemTags.CLOTH.tag))
                    .save(p, CreateNuclear.asResource("smithing/" + c.getName() + "_" + cloth.getColor().getSerializedName()));
            }
        })
        .lang("Anti Radiation Chestplate")
        .model(coloredArmorModel("chestplate", "14"))
        .register();

    public static final ItemEntry<Leggings> ANTI_RADIATION_LEGGINGS = CreateNuclear.REGISTRATE
        .item("default_anti_radiation_leggings",  p -> new Leggings(p, DyeColor.BLACK))
        .tag(
            CNTags.forgeItemTag("armors/leggings"),
                CNTags.CNItemTags.ANTI_RADIATION_ARMOR.tag
        )
        .recipe((c, p) -> {
            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, c.get())
                .unlockedBy("has_cloth", RegistrateRecipeProvider.has(CNItemTags.CLOTH.tag))
                .define('X', CommonMetal.LEAD.ingots)
                .define('Y', CommonMetal.BRASS.ingots)
                .pattern("YXY")
                .pattern("X X")
                .pattern("Y Y")
                .showNotification(true)
                .save(p, CreateNuclear.asResource("crafting/items/armors/" + c.getName()));

            for (Cloths cloth : Cloths.values()) {
                SmithingClothRecipeBuilder
                    .smithingCloth(
                        Ingredient.EMPTY,
                        Ingredient.of(c.get()),
                        Ingredient.of(cloth.getItem()),
                        RecipeCategory.COMBAT,
                        new ItemStack(c.get())
                    )
                    .unlocks("has_cloth", RegistrateRecipeProvider.has(CNItemTags.CLOTH.tag))
                    .save(p, CreateNuclear.asResource("smithing/" + c.getName() + "_" + cloth.getColor().getSerializedName()));
            }
        })
        .lang("Anti Radiation Leggings")
        .model(coloredArmorModel("leggings", "14"))
        .register();

    public static final ItemEntry<Boot> ANTI_RADIATION_BOOTS = CreateNuclear.REGISTRATE
        .item("default_anti_radiation_boots", p -> new Boot(p, DyeColor.BLACK))
        .tag(
            CNTags.forgeItemTag("armors/boots"),
            CNTags.CNItemTags.ANTI_RADIATION_ARMOR.tag
        )
        .recipe((c, p) -> {
            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, c.get())
                .unlockedBy("has_cloth", RegistrateRecipeProvider.has(CNItemTags.CLOTH.tag))
                .define('X', CommonMetal.LEAD.ingots)
                .define('Y', CommonMetal.BRASS.ingots)
                .pattern("Y Y")
                .pattern("X X")
                .showNotification(true)
                .save(p, CreateNuclear.asResource("crafting/items/armors/" + c.getName()));

            for (Cloths cloth : Cloths.values()) {
                SmithingClothRecipeBuilder
                    .smithingCloth(
                        Ingredient.EMPTY,
                        Ingredient.of(c.get()),
                        Ingredient.of(cloth.getItem()),
                        RecipeCategory.COMBAT,
                        new ItemStack(c.get())
                    )
                    .unlocks("has_cloth", RegistrateRecipeProvider.has(CNItemTags.CLOTH.tag))
                    .save(p, CreateNuclear.asResource("smithing/" + c.getName() + "_" + cloth.getColor().getSerializedName()));
            }
        })
        .lang("Anti Radiation Boots")
        .model(coloredArmorModel("boots", "14"))
        .register();

    public static final DyedItemsList<ClothItem> CLOTHS = new DyedItemsList<>(color -> {
        String colorName = color.getSerializedName();
        List<Item> ingredients = new ArrayList<>(Arrays.asList(Items.WHITE_DYE, Items.ORANGE_DYE, Items.MAGENTA_DYE, Items.LIGHT_BLUE_DYE, Items.YELLOW_DYE, Items.LIME_DYE, Items.PINK_DYE, Items.GRAY_DYE, Items.LIGHT_GRAY_DYE, Items.CYAN_DYE, Items.PURPLE_DYE, Items.BLUE_DYE, Items.BROWN_DYE, Items.GREEN_DYE, Items.RED_DYE, Items.BLACK_DYE));

        return CreateNuclear.REGISTRATE.item(colorName+ "_cloth", p -> new ClothItem(p, color))
            .tag(CNItemTags.CLOTH.tag)
            .recipe((c, p) -> ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, c.get())
                    .unlockedBy("has_white_cloth", RegistrateRecipeProvider.has(ClothItem.Cloths.WHITE_CLOTH.getItem()))
                    // Accept any cloth EXCEPT one already of the target color, otherwise the
                    // recipe would let you "dye" a cloth into the color it already is.
                    .requires(Ingredient.of(Arrays.stream(DyeColor.values())
                            .filter(other -> other != color)
                            .map(other -> ClothItem.Cloths.getByColor(other).get())
                            .toArray(ClothItem[]::new)))
                    .requires(ingredients.get(color.ordinal()))
                    .save(p, CreateNuclear.asResource("shapeless/cloth/" + c.getName()))
            )
            .lang(TextUtils.titleCaseConversion(color.getName()) + " Cloth")
            .model((c, p) -> p.generated(c, CreateNuclear.asResource("item/cloth/" + colorName + "_cloth")))
            .register();
    });

    public static final ItemEntry<LazySpawnEggItem> SPAWN_WOLF = CNBuilderTransformers.spawnEgg("wolf_irradiated_spawn_egg", CNEntityType.IRRADIATED_WOLF, 0x42452B, 0x4C422B, "Irradiated Wolf Spawn Egg");
    public static final ItemEntry<LazySpawnEggItem> SPAWN_CAT = CNBuilderTransformers.spawnEgg("cat_irradiated_spawn_egg", CNEntityType.IRRADIATED_CAT, 0x382C19, 0x742728, "Irradiated Cat Spawn Egg");
    public static final ItemEntry<LazySpawnEggItem> SPAWN_CHICKEN = CNBuilderTransformers.spawnEgg("chicken_irradiated_spawn_egg", CNEntityType.IRRADIATED_CHICKEN, 0x6B9455, 0x95393C, "Irradiated Chicken Spawn Egg");

    public static final ItemEntry<ReactorBluePrintItem> REACTOR_BLUEPRINT = CreateNuclear.REGISTRATE
        .item("reactor_blueprint_item", ReactorBluePrintItem::new)
        .lang("Reactor Blueprint")
        .recipe((c, p) -> {
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, c.get())
                    .unlockedBy("has_reactor_controller", RegistrateRecipeProvider.has(CNBlocks.REACTOR_CONTROLLER.get()))
                    .define('S', CNTags.forgeItemTag("ingots/steel"))
                    .define('D', AllBlocks.DISPLAY_BOARD)
                    .define('P', AllItems.PRECISION_MECHANISM)
                    .define('E', AllItems.EMPTY_SCHEMATIC)
                    .pattern("SDS")
                    .pattern("SPS")
                    .pattern("SES")
                    .save(p, CreateNuclear.asResource("crafting/" + c.getName()));
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, c.get())
                    .unlockedBy("has_reactor_blueprint", RegistrateRecipeProvider.has(CNBlocks.REACTOR_CONTROLLER.get()))
                    .requires(CNItems.REACTOR_BLUEPRINT)
                    .save(p, CreateNuclear.asResource("shapeless/" + c.getName() + "_clear"));
        })
        .model((c, p) -> p.generated(c, CreateNuclear.asResource("item/reactor_blueprint")))
        .properties(p -> p.stacksTo(1))
        .register();

    public static final ItemEntry<Item> REINFORCED_GLASS_BOTTLE = CreateNuclear.REGISTRATE
        .item("reinforced_glass_bottle", Item::new)
        .lang("Reinforced Glass Bottle")
        .recipe((c, p) -> ShapedRecipeBuilder.shaped(RecipeCategory.BREWING, c.get(), 3)
            .unlockedBy("has_reinforced_glass", RegistrateRecipeProvider.has(CNBlocks.REINFORCED_GLASS.get()))
            .define('G', CNBlocks.REINFORCED_GLASS)
            .pattern("G G")
            .pattern(" G ")
            .save(p, CreateNuclear.asResource("crafting/" + c.getName())))
        .properties(p -> p.stacksTo(16))
        .register();

    public static final ItemEntry<BiomeIrradiationExtractorItem> IRRADIATION_BIOME_EXTRACTOR = CreateNuclear.REGISTRATE
        .item("biome_irradiation_extractor", BiomeIrradiationExtractorItem::new)
        .lang("Biome Irradiation Extractor")
        .recipe((c, p) -> ShapedRecipeBuilder.shaped(RecipeCategory.MISC, c.get(), 8)
            .unlockedBy("has_reinforced_glass_bottle", RegistrateRecipeProvider.has(CNItems.REINFORCED_GLASS_BOTTLE.get()))
            .define('B', CNItems.REINFORCED_GLASS_BOTTLE)
            .define('S', Items.NETHER_STAR)
            .pattern("BBB")
            .pattern("BSB")
            .pattern("BBB")
            .save(p, CreateNuclear.asResource("crafting/" + c.getName())))
        .properties(p -> p.stacksTo(16).fireResistant())
        .model(biomeRestoreModel())
        .register();

    public static void register() {}
}
