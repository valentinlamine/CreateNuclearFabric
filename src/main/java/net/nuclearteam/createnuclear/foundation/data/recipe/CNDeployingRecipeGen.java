package net.nuclearteam.createnuclear.foundation.data.recipe;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.api.data.recipe.DeployingRecipeGen;
import net.minecraft.data.CachedOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.nuclearteam.createnuclear.CNBlocks;
import net.nuclearteam.createnuclear.CreateNuclear;

public class CNDeployingRecipeGen extends DeployingRecipeGen {

    GeneratedRecipe REACTOR_ROD_INPUT = deploying("reactor_rod_input_from_hopper_and_reactor_casing",
            Items.HOPPER,
            CNBlocks.REACTOR_CASING.get(),
            CNBlocks.REACTOR_ROD_INPUT.get()
    );

    GeneratedRecipe REACTOR_FLUID_INPUT = deploying("reactor_fluid_input_from_fluid_pipe_and_reactor_casing",
            AllBlocks.FLUID_PIPE.asItem(),
            CNBlocks.REACTOR_CASING.get(),
            CNBlocks.REACTOR_FLUID_INPUT.get()
    );

    GeneratedRecipe REACTOR_OUTPUT = deploying("reactor_output_from_shaft_and_reactor_casing",
            AllBlocks.SHAFT.asItem(),
            CNBlocks.REACTOR_CASING.get(),
            CNBlocks.REACTOR_OUTPUT.get()
    );

    protected GeneratedRecipe deploying(String name, Ingredient ingredient, ItemLike input, ItemLike output) {
        return create(CreateNuclear.asResource(name), b ->
                b.require(input)
                        .require(ingredient)
                        .output(output)
        );
    }

    protected GeneratedRecipe deploying(String name, Item ingredient, ItemLike input, ItemLike output) {
        return create(CreateNuclear.asResource(name), b ->
                b.require(input)
                        .require(ingredient)
                        .output(output)
        );
    }

    public CNDeployingRecipeGen(CachedOutput generator) {
        super(generator, CreateNuclear.MOD_ID);
    }
}
