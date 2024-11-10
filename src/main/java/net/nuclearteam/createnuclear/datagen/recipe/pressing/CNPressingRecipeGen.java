package net.nuclearteam.createnuclear.datagen.recipe.pressing;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.Create;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeSerializer;
import com.simibubi.create.foundation.data.recipe.ProcessingRecipeGen;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.nuclearteam.createnuclear.CreateNuclear;
import net.nuclearteam.createnuclear.item.CNItems;
import net.nuclearteam.createnuclear.tags.CNTag;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class CNPressingRecipeGen extends ProcessingRecipeGen {

    GeneratedRecipe
        GRAPHENE = create("graphene", b -> b
            .require(Ingredient.of(CNTag.forgeItemTag("coal_dusts")))
            .output(CNItems.GRAPHENE)
        )
    ;



    <T extends ProcessingRecipe<?>> GeneratedRecipe create(String name, UnaryOperator<ProcessingRecipeBuilder<T>> transform) {
        return create(CreateNuclear.asResource(name), transform);
    }


    public CNPressingRecipeGen(FabricDataOutput generator) {
        super(generator);
    }

    @Override
    protected AllRecipeTypes getRecipeType() {
        return AllRecipeTypes.PRESSING;
    }


    @Override
    public String getName() {
        return "CreateNuclear's Processing Recipes: " + getRecipeType().getId().getPath();
    }
}
