package net.nuclearteam.createnuclear.foundation.data.recipe;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.api.data.recipe.ItemApplicationRecipeGen;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.nuclearteam.createnuclear.CNBlocks;
import net.nuclearteam.createnuclear.CNTags;
import net.nuclearteam.createnuclear.CreateNuclear;

@SuppressWarnings("unused")
@MethodsReturnNonnullByDefault
public class CNItemApplicationRecipeGen extends ItemApplicationRecipeGen {

    GeneratedRecipe REACTOR_CASING = itemApplication("reactor_casing_from_steel_and_brass_casing",
            Ingredient.of(CNTags.forgeItemTag("ingots/steel")),
            AllBlocks.BRASS_CASING.get(),
            CNBlocks.REACTOR_CASING.get()
    );

    protected GeneratedRecipe itemApplication(String name, Ingredient ingredient, ItemLike input, ItemLike output) {
        return create(CreateNuclear.asResource(name), b ->
                b.require(input)
                .require(ingredient)
                .output(output)
        );
    }

    protected GeneratedRecipe itemApplication(String name, Item ingredient, ItemLike input, ItemLike output) {
        return create(CreateNuclear.asResource(name), b ->
                b.require(input)
                        .require(ingredient)
                        .output(output)
        );
    }

    public CNItemApplicationRecipeGen(FabricDataOutput output) {
        super(output, CreateNuclear.MOD_ID);
    }

    @Override
    public String getName() {
        return "CreateNuclear's Processing: " + getRecipeType().getId().getPath();
    }
}
