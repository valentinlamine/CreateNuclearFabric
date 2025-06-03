package net.nuclearteam.createnuclear.datagen.recipe.washing;

import com.simibubi.create.AllItems;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.data.recipe.CompatMetals;
import com.simibubi.create.foundation.data.recipe.Mods;
import com.simibubi.create.foundation.data.recipe.ProcessingRecipeGen;
import com.simibubi.create.foundation.item.TagDependentIngredientItem;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.nuclearteam.createnuclear.item.CNItems;


import java.util.function.Supplier;
import java.util.function.UnaryOperator;

@SuppressWarnings("unused")
public class CNWashingRecipeGen extends ProcessingRecipeGen {
    GeneratedRecipe
        CRUSHED_LEAD = crushedOre(AllItems.CRUSHED_LEAD, () -> CNItems.LEAD_NUGGET, () -> AllItems.EXP_NUGGET, .5f)
    ;

    public GeneratedRecipe crushedOre(ItemEntry<TagDependentIngredientItem> crushed, Supplier<ItemLike> nugget, Supplier<ItemLike> secondary,
                                      float secondaryChance) {
        return create(crushed.getId(), b -> b
                .withItemIngredients(Ingredient.of(crushed.get()))
                .output(nugget.get(), 9)
                .output(secondaryChance, secondary.get(), 1));
    }

    public CNWashingRecipeGen(FabricDataOutput output) {
        super(output);
    }

    @Override
    protected AllRecipeTypes getRecipeType() {
        return AllRecipeTypes.SPLASHING;
    }

}
