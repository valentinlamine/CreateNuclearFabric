package net.nuclearteam.createnuclear.foundation.data.recipe;

import com.simibubi.create.AllItems;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.foundation.data.recipe.ProcessingRecipeGen;
import com.simibubi.create.foundation.item.TagDependentIngredientItem;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.nuclearteam.createnuclear.CNItems;


import java.util.function.Supplier;

@SuppressWarnings("unused")
public class CNWashingRecipeGen extends ProcessingRecipeGen {
    GeneratedRecipe
        CRUSHED_LEAD = crushedOre(AllItems.CRUSHED_LEAD, () -> CNItems.LEAD_NUGGET)
    ;

    public GeneratedRecipe crushedOre(ItemEntry<TagDependentIngredientItem> crushed, Supplier<ItemLike> nugget) {
        return create(crushed.getId(), b -> b
                .withItemIngredients(Ingredient.of(crushed.get()))
                .output(nugget.get(), 9));
    }

    public CNWashingRecipeGen(FabricDataOutput output) {
        super(output);
    }

    @Override
    protected AllRecipeTypes getRecipeType() {
        return AllRecipeTypes.SPLASHING;
    }

}
