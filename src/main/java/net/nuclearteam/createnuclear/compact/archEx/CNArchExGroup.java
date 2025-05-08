package net.nuclearteam.createnuclear.compact.archEx;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.simibubi.create.Create;
import com.simibubi.create.compat.archEx.*;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.MapColor;
import net.nuclearteam.createnuclear.CreateNuclear;
import net.nuclearteam.createnuclear.block.palette.CNPaletteBlockPattern;
import net.nuclearteam.createnuclear.block.palette.CNPalettesStoneTypes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public record CNArchExGroup(String name, Block base, Textures textures, Recipes recipes, MapColor color, BlockType[] types) {
    public static Builder builder() {
        return new Builder();
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("name", name);
        ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(base);
        json.addProperty("base_block", blockId.toString());
        json.addProperty("textures", textures.typeOrId());
        json.addProperty("recipes", recipes.name);
        String colorName = CNMapColorSerialization.getArchExName(color);
        json.addProperty("map_color", colorName);
        JsonArray typesArray = new JsonArray();
        for (BlockType type : types) {
            typesArray.add(type.name);
        }
        json.add("types_to_generate", typesArray);
        return json;
    }

    public static class Builder {
        private String name;
        private Block base;
        private Textures textures;
        private Recipes recipes;
        private MapColor color;
        private final List<BlockType> types = new ArrayList<>();

        public Builder fromStoneTypeAndPattern(CNPalettesStoneTypes type, CNPaletteBlockPattern pattern) {
            String variant = Lang.asId(type.name());
            String baseBlockName = pattern.createName(variant);
            Block baseBlock = BuiltInRegistries.BLOCK.get(CreateNuclear.asResource(baseBlockName));
            if (baseBlock == Blocks.AIR)
                throw new IllegalStateException("Unknown block: " + baseBlockName + " " + baseBlockName);

            ResourceLocation texture = CNPaletteBlockPattern.toLocation(variant, pattern.getTexture(0));
            MapColor color = baseBlock.defaultMapColor();

            return this.named(baseBlockName)
                    .basedOn(baseBlock)
                    .textured(Textures.ofTexture(texture))
                    .withRecipes(Recipes.STONECUTTING_AND_CRAFTING)
                    .colored(color)
                    .withTypes(BlockType.FOR_STONES);
        }

        public Builder named(String name) {
            this.name = name;
            return this;
        }

        public Builder basedOn(Block block) {
            this.base = block;
            return this;
        }

        public Builder textured(Textures textures) {
            this.textures = textures;
            return this;
        }

        public Builder withRecipes(Recipes recipes) {
            this.recipes = recipes;
            return this;
        }

        public Builder colored(MapColor color) {
            this.color = color;
            return this;
        }

        private Builder withTypes(BlockType... types) {
            Collections.addAll(this.types, types);
            return this;
        }

        public CNArchExGroup build() {
            if (types.isEmpty())
                throw new IllegalArgumentException("types not set");
            return new CNArchExGroup(
                    Objects.requireNonNull(name, "name not set"),
                    Objects.requireNonNull(base, "base not set"),
                    Objects.requireNonNull(textures, "textures not set"),
                    Objects.requireNonNull(recipes, "recipes not set"),
                    Objects.requireNonNull(color, "color not set"),
                    types.toArray(BlockType[]::new)
            );
        }
    }
}
