package net.nuclearteam.createnuclear.foundation.data.recipe;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.data.recipe.ProcessingRecipeGen;
import io.github.fabricators_of_create.porting_lib.tags.Tags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import net.nuclearteam.createnuclear.CNFluids;
import net.nuclearteam.createnuclear.CNItems;
import net.nuclearteam.createnuclear.CNTags;
import net.nuclearteam.createnuclear.CreateNuclear;

import java.util.function.UnaryOperator;

@SuppressWarnings("unused")
@MethodsReturnNonnullByDefault
public class CNMixingRecipeGen extends ProcessingRecipeGen {

    GeneratedRecipe
        STEEL = create("steel", b -> b
            .require(CNTags.forgeItemTag("coal_dusts"))
            .require(Tags.Items.INGOTS_IRON)
            .output(CNItems.STEEL_INGOT)
        ),

        URANIUM_FLUID = create("uranium_fluid", b -> b
            .require(CNItems.URANIUM_POWDER)
            .output(CNFluids.URANIUM.get(), 2025)
            .requiresHeat(HeatCondition.HEATED)
        ),

        THORIUM_FLUID = create("thorium_fluid", b -> b
            .require(CNItems.THORIUM_DUST)
            .output(CNFluids.THORIUM.get(), 2025)
            .requiresHeat(HeatCondition.HEATED)
        )
    ;

    <T extends ProcessingRecipe<?>> GeneratedRecipe create(String name, UnaryOperator<ProcessingRecipeBuilder<T>> transform) {
        return create(CreateNuclear.asResource(name), transform);
    }

    protected <T extends ProcessingRecipe<?>> GeneratedRecipe create(ResourceLocation name, UnaryOperator<ProcessingRecipeBuilder<T>> transform) {
        return createWithDeferredId(() -> name, transform);
    }


    public CNMixingRecipeGen(FabricDataOutput generator) {
        super(generator);
    }

    @Override
    protected AllRecipeTypes getRecipeType() {
        return AllRecipeTypes.MIXING;
    }

    @Override
    public String getName() {
        return "CreateNuclear's Processing Recipes: " + getRecipeType().getId().getPath();
    }
}
