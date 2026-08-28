package net.nuclearteam.createnuclear.foundation.data.recipe;

import com.simibubi.create.api.data.recipe.PressingRecipeGen;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.nuclearteam.createnuclear.CNItems;
import net.nuclearteam.createnuclear.CNTags;
import net.nuclearteam.createnuclear.CreateNuclear;

@MethodsReturnNonnullByDefault
@SuppressWarnings("unused")
public class CNPressingRecipeGen extends PressingRecipeGen {

    GeneratedRecipe
        GRAPHENE = create("graphene", b -> b
            .require(Ingredient.of(CNTags.forgeItemTag("dusts/coal")))
            .output(CNItems.GRAPHENE)
        )
    ;

    public CNPressingRecipeGen(FabricDataOutput output) {
        super(output, CreateNuclear.MOD_ID);
    }

    @Override
    public String getName() {
        return "CreateNuclear's Processing Recipes: " + getRecipeType().getId().getPath();
    }
}
