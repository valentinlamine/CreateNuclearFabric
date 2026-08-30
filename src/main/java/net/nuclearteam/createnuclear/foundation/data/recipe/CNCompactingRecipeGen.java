package net.nuclearteam.createnuclear.foundation.data.recipe;

import com.simibubi.create.api.data.recipe.CompactingRecipeGen;
import net.minecraft.data.CachedOutput;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.data.PackOutput;
import net.nuclearteam.createnuclear.CNItems;
import net.nuclearteam.createnuclear.CNTags;
import net.nuclearteam.createnuclear.CreateNuclear;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
@SuppressWarnings("unused")
public class CNCompactingRecipeGen extends CompactingRecipeGen {

    public CNCompactingRecipeGen(PackOutput generator) {
        super(generator, CreateNuclear.MOD_ID);
    }

    GeneratedRecipe
        YELLOWCAKE = create(CreateNuclear.asResource("uranium_fluid_to_yellowcake"), b -> b
            .require(CNTags.CNFluidTags.URANIUM.tag, 8100)
            .output(CNItems.YELLOWCAKE, 1)),

        THORIUM = create(CreateNuclear.asResource("thorium_fluid_to_thorium_ingot"), b -> b
            .require(CNTags.CNFluidTags.THORIUM.tag, 16200)
            .output(CNItems.THORIUM_INGOT, 1));

    @Override
    public String getName() {
        return "CreateNuclear's Processing Recipes: " + getRecipeType().getId().getPath();
    }
}
