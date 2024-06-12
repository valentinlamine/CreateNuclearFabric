package net.ynov.createnuclear.datagen.recipe.compacting;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.data.recipe.ProcessingRecipeGen;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.world.level.ItemLike;
import net.ynov.createnuclear.CreateNuclear;
import net.ynov.createnuclear.fluid.CNFluids;
import net.ynov.createnuclear.item.CNItems;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class CNCompactingRecipeGen extends ProcessingRecipeGen {

    GeneratedRecipe
        YELLOW_CAKE = create(CreateNuclear.asResource("uranium_fluid_to_yellow_cake"), b -> b.require(CNFluids.URANIUM.get(), 8100).output(CNItems.YELLOW_CAKE, 1));


    public CNCompactingRecipeGen(FabricDataOutput output) {
        super(output);
    }

    @Override
    protected AllRecipeTypes getRecipeType() {
        return AllRecipeTypes.COMPACTING;
    }

    @Override
    public String getName() {
        return "CreateNuclear's Processing Recipes: " + getRecipeType().getId().getPath();
    }
}
