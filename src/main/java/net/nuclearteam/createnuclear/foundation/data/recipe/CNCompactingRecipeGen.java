package net.nuclearteam.createnuclear.foundation.data.recipe;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.foundation.data.recipe.ProcessingRecipeGen;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.nuclearteam.createnuclear.CNFluids;
import net.nuclearteam.createnuclear.CNItems;
import net.nuclearteam.createnuclear.CreateNuclear;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
@SuppressWarnings("unused")
public class CNCompactingRecipeGen extends ProcessingRecipeGen {

    GeneratedRecipe
        YELLOWCAKE = create(CreateNuclear.asResource("uranium_fluid_to_yellowcake"), b -> b.require(CNFluids.URANIUM.get(), 8100).output(CNItems.YELLOWCAKE, 1)),
        THORIUM_INGOT = create(CreateNuclear.asResource("thorium_fluid_to_thorium_ingot"), b -> b.require(CNFluids.THORIUM.get(), 8100).output(CNItems.THORIUM_INGOT, 1));


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
