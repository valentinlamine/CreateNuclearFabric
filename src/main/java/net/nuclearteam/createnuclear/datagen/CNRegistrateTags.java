package net.nuclearteam.createnuclear.datagen;

import com.simibubi.create.foundation.data.TagGen;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.nuclearteam.createnuclear.CreateNuclear;
import net.nuclearteam.createnuclear.entity.CNMobEntityType;
import net.nuclearteam.createnuclear.entity.irradiatedcat.IrradiatedCat;
import net.nuclearteam.createnuclear.item.CNItems;
import net.nuclearteam.createnuclear.tags.CNTag;

import static net.nuclearteam.createnuclear.tags.CNTag.EntityTypeTags;

public class CNRegistrateTags {
    public static void addGenerators() {
        CreateNuclear.REGISTRATE.addDataGenerator(ProviderType.ENTITY_TAGS, CNRegistrateTags::genEntityTags);
        CreateNuclear.REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, CNRegistrateTags::genItemTags);
    }

    private static void genEntityTags(RegistrateTagsProvider<EntityType<?>> provIn) {
        TagGen.CreateTagsProvider<EntityType<?>> prov = new TagGen.CreateTagsProvider<>(provIn, EntityType::builtInRegistryHolder);

        /*prov.tag(EntityTypeTags.FALL_DAMAGE_IMMUNE.tag)
                .add(CNMobEntityType.IRRADIATED_CAT)
                .add(CNMobEntityType.IRRADIATED_CHICKEN)
        ;
        prov.tag(EntityTypeTags.IRRADIATED_IMMUNE.tag)
                .add(CNMobEntityType.IRRADIATED_CHICKEN)
                .add(CNMobEntityType.IRRADIATED_CAT)
                .add(CNMobEntityType.IRRADIATED_WOLF)
        ;*/
    }

    private static void genItemTags(RegistrateTagsProvider<Item> provIn) {
        TagGen.CreateTagsProvider<Item> prov = new TagGen.CreateTagsProvider<>(provIn, Item::builtInRegistryHolder);

       prov.tag(CNTag.ItemTags.ANTI_RADIATION_ARMOR.tag)
               .add(CNItems.ANTI_RADIATION_HELMETS.get(DyeColor.WHITE).asItem())
               .add(CNItems.ANTI_RADIATION_CHESTPLATES.get(DyeColor.WHITE).asItem())
               .add(CNItems.ANTI_RADIATION_LEGGINGS.get(DyeColor.WHITE).asItem())
               .add(CNItems.ANTI_RADIATION_BOOTS.get())
       ;

        for (CNTag.ItemTags tag : CNTag.ItemTags.values()) {
            if (tag.alwaysDatagen) {
                prov.getOrCreateRawBuilder(tag.tag);
            }
        }
    }


}
