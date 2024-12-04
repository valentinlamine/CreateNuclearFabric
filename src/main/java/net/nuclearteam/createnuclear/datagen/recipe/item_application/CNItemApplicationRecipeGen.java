package net.nuclearteam.createnuclear.datagen.recipe.item_application;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.foundation.data.CreateBlockEntityBuilder;
import com.simibubi.create.foundation.data.recipe.ProcessingRecipeGen;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.nuclearteam.createnuclear.CreateNuclear;
import net.nuclearteam.createnuclear.block.CNBlocks;
import net.nuclearteam.createnuclear.item.CNItems;

public class CNItemApplicationRecipeGen extends ProcessingRecipeGen {

    GeneratedRecipe REACTOR_CORE = itemApplication("reactor_core_from_steel_and_reactor_main_frame",
            CNItems.ENRICHED_YELLOWCAKE.get(),
            CNBlocks.REACTOR_MAIN_FRAME.get(),
            CNBlocks.REACTOR_CORE.get()
    );

    GeneratedRecipe REACTOR_CASING = itemApplication("reactor_casing_from_steel_and_brass_casing",
            CNItems.STEEL_INGOT.get(),
            AllBlocks.BRASS_CASING.get(),
            CNBlocks.REACTOR_CASING.get()
    );

    GeneratedRecipe REACTOR_OUPUT = itemApplication("reactor_output_from_shaft_and_reactor_casing",
            AllBlocks.SHAFT.asItem(),
            CNBlocks.REACTOR_CASING.get(),
            CNBlocks.REACTOR_OUTPUT.get()
    );
    GeneratedRecipe REACTOR_INPUT = itemApplication("reactor_intput_from_hopper_and_reactor_casing",
            Blocks.HOPPER.asItem(),
            CNBlocks.REACTOR_CASING.get(),
            CNBlocks.REACTOR_OUTPUT.get()
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
