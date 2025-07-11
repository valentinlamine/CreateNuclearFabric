package net.nuclearteam.createnuclear.datagen.recipe.item_application;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.foundation.data.recipe.ProcessingRecipeGen;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.nuclearteam.createnuclear.CreateNuclear;
import net.nuclearteam.createnuclear.block.CNBlocks;
import net.nuclearteam.createnuclear.tags.CNTag;

@SuppressWarnings("unused")
@MethodsReturnNonnullByDefault
public class CNItemApplicationRecipeGen extends ProcessingRecipeGen {

    GeneratedRecipe REACTOR_CASING = itemApplication("reactor_casing_from_steel_and_brass_casing",
            Ingredient.of(CNTag.forgeItemTag("ingots/steel")),
            AllBlocks.BRASS_CASING.get(),
            CNBlocks.REACTOR_CASING.get()
    );

    GeneratedRecipe REACTOR_OUTPUT = itemApplication("reactor_output_from_shaft_and_reactor_casing",
            AllBlocks.SHAFT.asItem(),
            CNBlocks.REACTOR_CASING.get(),
            CNBlocks.REACTOR_OUTPUT.get()
    );

    GeneratedRecipe REACTOR_INPUT = itemApplication("reactor_input_from_hopper_and_reactor_casing",
            Items.HOPPER,
            CNBlocks.REACTOR_CASING.get(),
            CNBlocks.REACTOR_INPUT.get()
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

    public CNItemApplicationRecipeGen(FabricDataOutput generator) {
        super(generator);
    }

    @Override
    protected AllRecipeTypes getRecipeType() {
        return AllRecipeTypes.ITEM_APPLICATION;
    }

    @Override
    public String getName() {
        return "CreateNuclear's Processing: " + getRecipeType().getId().getPath();
    }
}
