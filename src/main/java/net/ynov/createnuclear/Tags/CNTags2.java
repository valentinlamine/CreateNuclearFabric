package net.ynov.createnuclear.Tags;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.ynov.createnuclear.CreateNuclear;
import org.apache.commons.compress.compressors.lz77support.LZ77Compressor;

public class CNTags2 {

    public static final TagKey<Block> URANIUM_FIRE_BASE_BLOCKS = createItem("tags/uranium_fire_base_blocks");
    public static final TagKey<Fluid> URANIUM_FLUID = createFluid("tags/uranium");

    private static TagKey<Block> createItem (String name) {
        return TagKey.create(Registries.BLOCK, new ResourceLocation(CreateNuclear.MOD_ID, name));
    }
    private static TagKey<Fluid> createFluid (String name) {
        return TagKey.create(Registries.FLUID, new ResourceLocation(CreateNuclear.MOD_ID, name));
    }


    public static void register() {
    }
}
