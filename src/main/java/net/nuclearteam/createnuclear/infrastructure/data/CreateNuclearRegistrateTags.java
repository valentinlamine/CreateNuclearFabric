package net.nuclearteam.createnuclear.infrastructure.data;

import com.simibubi.create.foundation.data.TagGen;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.nuclearteam.createnuclear.CNBlocks;
import net.nuclearteam.createnuclear.CNEntityType;
import net.nuclearteam.createnuclear.CNTags;
import net.nuclearteam.createnuclear.CNTags.CNFluidTags;
import net.nuclearteam.createnuclear.CNTags.CNBlockTags;
import net.nuclearteam.createnuclear.CNTags.CNItemTags;
import net.nuclearteam.createnuclear.CNTags.CNEntityTypeTags;
import net.nuclearteam.createnuclear.CreateNuclear;

@SuppressWarnings({"unused", "deprecated"})
public class CreateNuclearRegistrateTags {
    public static <T extends Block, P> NonNullFunction<BlockBuilder<T, P>, BlockBuilder<T, P>> shovelOnly() {
        return b -> b.tag(BlockTags.MINEABLE_WITH_SHOVEL);
    }

    public static void addGenerators() {
        CreateNuclear.REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, CreateNuclearRegistrateTags::genBlocksTags);
        CreateNuclear.REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, CreateNuclearRegistrateTags::genItemTags);
        CreateNuclear.REGISTRATE.addDataGenerator(ProviderType.FLUID_TAGS, CreateNuclearRegistrateTags::genFluidTags);
        CreateNuclear.REGISTRATE.addDataGenerator(ProviderType.ENTITY_TAGS, CreateNuclearRegistrateTags::genEntityTags);
    }

    private static void genBlocksTags(RegistrateTagsProvider<Block> provIn) {
        TagGen.CreateTagsProvider<Block> prov = new TagGen.CreateTagsProvider<>(provIn, Block::builtInRegistryHolder);

        prov.tag(BlockTags.CAMPFIRES)
            .add(CNBlocks.ENRICHING_CAMPFIRE.get())
        ;

        prov.tag(BlockTags.FIRE)
            .add(CNBlocks.ENRICHING_FIRE.get())
        ;

        prov.tag(BlockTags.DRAGON_TRANSPARENT)
            .add(CNBlocks.ENRICHING_FIRE.get())
        ;


        for (CNBlockTags tag : CNBlockTags.values()) {
            if (tag.alwaysDatagen) {
                prov.getOrCreateRawBuilder(tag.tag);
            }
        }
    }

    private static void genItemTags(RegistrateTagsProvider<Item> provIn) {
        TagGen.CreateTagsProvider<Item> prov = new TagGen.CreateTagsProvider<>(provIn, Item::builtInRegistryHolder);

        for (CNItemTags tag : CNItemTags.values()) {
            if (tag.alwaysDatagen) {
                prov.getOrCreateRawBuilder(tag.tag);
            }
        }
    }

    private static void genFluidTags(RegistrateTagsProvider<Fluid> provider) {
        TagGen.CreateTagsProvider<Fluid> prov = new TagGen.CreateTagsProvider<>(provider, Fluid::builtInRegistryHolder);

        prov.tag(CNTags.forgeFluidTag("uranium"))
                .addTag(CNFluidTags.URANIUM.tag)
        ;

        prov.tag(FluidTags.LAVA)
                .addTag(CNFluidTags.URANIUM.tag)
        ;

        prov.tag(CNTags.forgeFluidTag("thorium"))
                .addTag(CNFluidTags.THORIUM.tag)
        ;

        prov.tag(FluidTags.LAVA)
                .addTag(CNFluidTags.THORIUM.tag)
        ;


        for (CNFluidTags tag : CNFluidTags.values()) {
            prov.getOrCreateRawBuilder(tag.tag);
        }
    }

    private static void genEntityTags(RegistrateTagsProvider<EntityType<?>> provIn) {
        TagGen.CreateTagsProvider<EntityType<?>> prov = new TagGen.CreateTagsProvider<>(provIn, EntityType::builtInRegistryHolder);

        prov.tag(EntityTypeTags.FALL_DAMAGE_IMMUNE)
            .add(CNEntityType.IRRADIATED_CAT.get())
            .add(CNEntityType.IRRADIATED_CHICKEN.get())
        ;

        for (CNEntityTypeTags tag : CNEntityTypeTags.values()) {
            if (tag.alwaysDatagen) {
                prov.getOrCreateRawBuilder(tag.tag);
            }
        }
    }




}
