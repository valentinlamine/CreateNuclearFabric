package net.ynov.createnuclear.Tags;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.ynov.createnuclear.CreateNuclear;
import net.ynov.createnuclear.item.ModGroup;
import net.ynov.createnuclear.item.ModItems;

public class ModTag {
    public static final TagKey<Block> URANIUM_FIRE_BASE_BLOCKS = TagKey.create(Registries.BLOCK, new ResourceLocation(CreateNuclear.MOD_ID, "tags/uranium_fire_base_blocks"));

    public static void registerModItems() {
        CreateNuclear.LOGGER.info("Registering mod tags for " + CreateNuclear.MOD_ID);
    }
}
