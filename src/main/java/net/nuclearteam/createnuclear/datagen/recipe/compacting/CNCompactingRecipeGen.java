package net.nuclearteam.createnuclear.datagen.recipe.compacting;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.data.recipe.ProcessingRecipeGen;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.world.level.ItemLike;
import net.nuclearteam.createnuclear.CreateNuclear;
import net.nuclearteam.createnuclear.fluid.CNFluids;
import net.nuclearteam.createnuclear.item.CNItems;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class CNCompactingRecipeGen extends ProcessingRecipeGen {

    GeneratedRecipe
        YELLOWCAKE = create(CreateNuclear.asResource("uranium_fluid_to_yellowcake"), b -> b.require(CNFluids.URANIUM.get(), 8100).output(CNItems.YELLOWCAKE, 1));


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
