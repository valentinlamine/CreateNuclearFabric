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

@SuppressWarnings({"unused", "deprecated"})
public class CNRegistrateTags {
    public static void addGenerators() {
        CreateNuclear.REGISTRATE.addDataGenerator(ProviderType.ENTITY_TAGS, CNRegistrateTags::genEntityTags);
        CreateNuclear.REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, CNRegistrateTags::genItemTags);
    }

    private static void genEntityTags(RegistrateTagsProvider<EntityType<?>> provIn) {
        TagGen.CreateTagsProvider<EntityType<?>> prov = new TagGen.CreateTagsProvider<>(provIn, EntityType::builtInRegistryHolder);

        for (CNTag.EntityTypeTags tag : CNTag.EntityTypeTags.values()) {
            if (tag.alwaysDatagen) {
                prov.getOrCreateRawBuilder(tag.tag);
            }
        }
    }

    private static void genItemTags(RegistrateTagsProvider<Item> provIn) {
        TagGen.CreateTagsProvider<Item> prov = new TagGen.CreateTagsProvider<>(provIn, Item::builtInRegistryHolder);

        for (CNTag.ItemTags tag : CNTag.ItemTags.values()) {
            if (tag.alwaysDatagen) {
                prov.getOrCreateRawBuilder(tag.tag);
            }
        }
    }


}
