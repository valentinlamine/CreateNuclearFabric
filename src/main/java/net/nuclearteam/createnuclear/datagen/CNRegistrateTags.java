package net.nuclearteam.createnuclear.datagen;

import com.simibubi.create.foundation.data.TagGen;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import net.minecraft.world.entity.EntityType;
import net.nuclearteam.createnuclear.CreateNuclear;
import net.nuclearteam.createnuclear.entity.CNMobEntityType;
import net.nuclearteam.createnuclear.entity.irradiatedcat.IrradiatedCat;

import static net.nuclearteam.createnuclear.tags.CNTag.EntityTypeTags;

public class CNRegistrateTags {
    public static void addGenerators() {
        CreateNuclear.REGISTRATE.addDataGenerator(ProviderType.ENTITY_TAGS, CNRegistrateTags::genEntityTags);
    }

    private static void genEntityTags(RegistrateTagsProvider<EntityType<?>> provIn) {
        TagGen.CreateTagsProvider<EntityType<?>> prov = new TagGen.CreateTagsProvider<>(provIn, EntityType::builtInRegistryHolder);

        prov.tag(EntityTypeTags.FALL_DAMAGE_IMMUNE.tag)
                .add(CNMobEntityType.IRRADIATED_CAT)
                .add(CNMobEntityType.IRRADIATED_CHICKEN)
        ;
    }


}
