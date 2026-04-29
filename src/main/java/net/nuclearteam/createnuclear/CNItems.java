package net.nuclearteam.createnuclear;

import net.nuclearteam.createnuclear.content.equipment.armor.AntiRadiationArmorItem;
import net.nuclearteam.createnuclear.content.equipment.armor.AntiRadiationArmorItem.Boot;
import net.nuclearteam.createnuclear.content.equipment.armor.AntiRadiationArmorItem.Chestplate;
import net.nuclearteam.createnuclear.content.equipment.armor.AntiRadiationArmorItem.Helmet;
import net.nuclearteam.createnuclear.content.equipment.armor.AntiRadiationArmorItem.Leggings;

import com.tterrag.registrate.util.entry.EntityEntry;
import com.tterrag.registrate.util.entry.ItemEntry;

import io.github.fabricators_of_create.porting_lib.util.LazySpawnEggItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.nuclearteam.createnuclear.content.equipment.cloth.ClothItem;
import net.nuclearteam.createnuclear.content.multiblock.bluePrintItem.ReactorBluePrint;
import net.nuclearteam.createnuclear.foundation.utility.TextUtils;
import net.nuclearteam.createnuclear.CNTags.CNItemTags;
import net.nuclearteam.createnuclear.content.effects.RadiationItem;

@SuppressWarnings("unused")
public class CNItems {
    public static final ItemEntry<? extends Item>
        URANIUM_POWDER = CreateNuclear.REGISTRATE
            .item("uranium_powder", RadiationItem::new)
            .register(),

        YELLOWCAKE = CreateNuclear.REGISTRATE
            .item("yellowcake", RadiationItem::new)
            .properties(p -> p.food(new FoodProperties.Builder()
                .nutrition(20)
                .saturationMod(0.3F)
                .alwaysEat()
                .effect(new MobEffectInstance(CNEffects.RADIATION.get(),600,2), 1.0F)
                .effect(new MobEffectInstance(MobEffects.HUNGER, 600, 1), 1.0F)
                .build())
            )
            .register(),

        ENRICHED_YELLOWCAKE = CreateNuclear.REGISTRATE
            .item("enriched_yellowcake", RadiationItem::new)
            .register(),

        COAL_DUST = CreateNuclear.REGISTRATE
            .item("coal_dust", Item::new)
            .tag(CNTags.forgeItemTag("dusts"), CNTags.forgeItemTag("coal_dusts"))
            .register(),

        GRAPHENE = CreateNuclear.REGISTRATE
            .item("graphene", Item::new)
            .register(),

        STEEL_INGOT = CreateNuclear.REGISTRATE
            .item("steel_ingot", Item::new)
            .tag(CNTags.forgeItemTag("ingots"), CNTags.forgeItemTag("ingots/steel"))
            .register(),

        RAW_URANIUM = CreateNuclear.REGISTRATE
            .item("raw_uranium", RadiationItem::new)
            .tag(CNTags.forgeItemTag("raw_ores"), CNTags.forgeItemTag("raw_materials"), CNTags.forgeItemTag("raw_materials/uranium"))
            .register(),

        RAW_LEAD = CreateNuclear.REGISTRATE
            .item("raw_lead", Item::new)
            .tag(CNTags.forgeItemTag("raw_ores"), CNTags.forgeItemTag("raw_materials"), CNTags.forgeItemTag("raw_materials/lead"))
            .register(),


        LEAD_INGOT = CreateNuclear.REGISTRATE
            .item("lead_ingot", Item::new)
            .tag(CNTags.forgeItemTag("ingots"), CNTags.forgeItemTag("ingots/lead"))
            .register(),

        LEAD_NUGGET = CreateNuclear.REGISTRATE
            .item("lead_nugget", Item::new)
            .tag(CNTags.forgeItemTag("nuggets"), CNTags.forgeItemTag("nuggets/lead"))
            .register(),

        STEEL_NUGGET = CreateNuclear.REGISTRATE
            .item("steel_nugget", Item::new)
            .tag(CNTags.forgeItemTag("nuggets"), CNTags.forgeItemTag("nuggets/steel"))
            .register(),

        URANIUM_ROD = CreateNuclear.REGISTRATE
            .item("uranium_rod", RadiationItem::new)
            .tag(CNTags.forgeItemTag("rods"), CNItemTags.FUEL.tag)
            .register(),


        GRAPHITE_ROD = CreateNuclear.REGISTRATE
            .item("graphite_rod", RadiationItem::new)
            .tag(CNTags.forgeItemTag("rods"), CNItemTags.COOLER.tag)
            .register()
    ;
  
    public static final Helmet.DyeItemHelmetList<Helmet> ANTI_RADIATION_HELMETS = new Helmet.DyeItemHelmetList<>(color -> {
       String colorName = color.getSerializedName();
        TagKey<Item> tag = !colorName.equals("white")
                ? CNItemTags.ANTI_RADIATION_HELMET_FULL_DYE.tag
                : CNItemTags.ANTI_RADIATION_ARMOR.tag;
        return CreateNuclear.REGISTRATE.item(colorName + "_anti_radiation_helmet", p -> new Helmet(p, color))
               .tag(
                   CNTags.forgeItemTag("helmets"),
                   CNTags.forgeItemTag("armors"),
                   tag,
                   CNItemTags.ALL_ANTI_RADIATION_ARMORS.tag,
                   CNItemTags.ANTI_RADIATION_HELMET_FULL_DYE.tag
               )
               .lang(TextUtils.titleCaseConversion(color.getName()) +" Anti Radiation Helmet")
               .model((c, p) -> p.generated(c, CreateNuclear.asResource("item/armors/helmets/" + colorName + "_anti_radiation_helmet")))
               .register();

    });

    public static final Chestplate.DyeItemChestplateList<Chestplate> ANTI_RADIATION_CHESTPLATES = new Chestplate.DyeItemChestplateList<>(color -> {
        String colorName = color.getSerializedName();

        TagKey<Item> tag = !colorName.equals("white")
                ? CNItemTags.ANTI_RADIATION_CHESTPLATE_DYE.tag
                : CNItemTags.ANTI_RADIATION_ARMOR.tag;
        return CreateNuclear.REGISTRATE.item(colorName + "_anti_radiation_chestplate",  p -> new Chestplate(p, color))
                .tag(
                    CNTags.forgeItemTag("chestplates"),
                    CNTags.forgeItemTag("armors"),
                    tag,
                    CNItemTags.ALL_ANTI_RADIATION_ARMORS.tag,
                    CNItemTags.ANTI_RADIATION_CHESTPLATE_FULL_DYE.tag
                )
                .lang(TextUtils.titleCaseConversion(color.getName()) +" Anti Radiation Chestplate")
                .model((c, p) -> p.generated(c, CreateNuclear.asResource("item/armors/chestplates/" + colorName + "_anti_radiation_chestplate")))
                .register();

    });

    public static final Leggings.DyeItemLeggingsList<Leggings> ANTI_RADIATION_LEGGINGS = new Leggings.DyeItemLeggingsList<>(color -> {

        String colorName = color.getSerializedName();
        TagKey<Item> tag = !colorName.equals("white")
                ? CNItemTags.ANTI_RADIATION_LEGGINGS_DYE.tag
                : CNItemTags.ANTI_RADIATION_ARMOR.tag;
        return CreateNuclear.REGISTRATE.item(colorName + "_anti_radiation_leggings",  p -> new Leggings(p, color))
                .tag(
                    CNTags.forgeItemTag("leggings"),
                    CNTags.forgeItemTag("armors"),
                    tag,
                    CNItemTags.ALL_ANTI_RADIATION_ARMORS.tag,
                    CNItemTags.ANTI_RADIATION_LEGGINGS_FULL_DYE.tag
                )
                .lang(TextUtils.titleCaseConversion(color.getName()) +" Anti Radiation Leggings")
                .model((c, p) -> p.generated(c, CreateNuclear.asResource("item/armors/leggings/" + colorName + "_anti_radiation_leggings")))
                .register();

    });

    public static final ItemEntry<? extends AntiRadiationArmorItem.Boot>
            ANTI_RADIATION_BOOTS = CreateNuclear.REGISTRATE.item("anti_radiation_boots", Boot::new)
            .tag(
                CNTags.forgeItemTag("boots"),
                CNTags.forgeItemTag("armors"),
                CNItemTags.ANTI_RADIATION_BOOTS_DYE.tag,
                CNItemTags.ANTI_RADIATION_ARMOR.tag,
                CNItemTags.ALL_ANTI_RADIATION_ARMORS.tag
            )
            .lang("Anti Radiation Boots")
            .model((c, p) -> p.generated(c, CreateNuclear.asResource("item/armors/anti_radiation_boots")))
            .register();


    public static final ClothItem.DyeItemList<ClothItem> CLOTHS = new ClothItem.DyeItemList<>(color -> {
        String colorName = color.getSerializedName();
        return CreateNuclear.REGISTRATE.item(colorName+ "_cloth", p -> new ClothItem(p, color))
                .tag(CNItemTags.CLOTH.tag)
                .lang(TextUtils.titleCaseConversion(color.getName()) + " Cloth")
                .model((c, p) -> p.generated(c, CreateNuclear.asResource("item/cloth/" + colorName + "_cloth")))
                .register();
    });

    public static final ItemEntry<ReactorBluePrint> REACTOR_BLUEPRINT = CreateNuclear.REGISTRATE
            .item("reactor_blueprint_item", ReactorBluePrint::new)
            .lang("Reactor Blueprint")
            .model((c, p) -> p.generated(c, CreateNuclear.asResource("item/reactor_blueprint")))
            .properties(p -> p.stacksTo(1))
            .register();

    public static final ItemEntry<LazySpawnEggItem> SPAWN_WOLF = registerSpawnEgg("wolf_irradiated_spawn_egg", CNEntityType.IRRADIATED_WOLF, 0x42452B,0x4C422B, "Irradiated Wolf Spawn Egg");
    public static final ItemEntry<LazySpawnEggItem> SPAWN_CAT = registerSpawnEgg("cat_irradiated_spawn_egg", CNEntityType.IRRADIATED_CAT, 0x382C19,0x742728, "Irradiated Cat Spawn Egg");
    public static final ItemEntry<LazySpawnEggItem> SPAWN_CHICKEN = registerSpawnEgg("chicken_irradiated_spawn_egg", CNEntityType.IRRADIATED_CHICKEN, 0x6B9455,0x95393C, "Irradiated Chicken Spawn Egg");

    private static ItemEntry<LazySpawnEggItem> registerSpawnEgg(String name, EntityEntry<? extends Mob> mobEntityType, int backgroundColor, int highlightColor, String nameItems) {
        return CreateNuclear.REGISTRATE
                .item(name, p -> new LazySpawnEggItem(mobEntityType, backgroundColor,highlightColor, p))
                .lang(nameItems)
                .model((c, p) -> p.withExistingParent(c.getName(), new ResourceLocation("item/template_spawn_egg")))
                .register();
    }

    public static void registerCNItems() {
        CreateNuclear.LOGGER.info("Registering mod items for {}", CreateNuclear.MOD_ID);
    }

}
