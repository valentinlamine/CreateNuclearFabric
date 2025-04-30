package net.nuclearteam.createnuclear.datagen.recipe.crushing;

import com.simibubi.create.AllItems;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.Create;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeSerializer;
import com.simibubi.create.foundation.data.recipe.ProcessingRecipeGen;
import com.simibubi.create.foundation.utility.RegisteredObjects;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.nuclearteam.createnuclear.CreateNuclear;
import net.nuclearteam.createnuclear.block.CNBlocks;
import net.nuclearteam.createnuclear.item.CNItems;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class CNCrushingRecipeGen extends ProcessingRecipeGen {

    GeneratedRecipe
        COAL_DUST = create(() -> Items.COAL, b -> b.duration(250)
                .output(.50f, CNItems.COAL_DUST)
        ),

        CHARCOAL_DUST = create(() -> Items.CHARCOAL, b -> b.duration(250)
                .output(.50f, CNItems.COAL_DUST)
        ),

        GRANITE_URANIUM_POWDER = create(() -> Items.GRANITE, b -> b.duration(250)
                .output(.5f, CNItems.URANIUM_POWDER)
                .output(1f, Blocks.RED_SAND)
        )

    ;

    GeneratedRecipe
        RAW_URANIUM = create(() -> CNItems.RAW_URANIUM, b -> b.duration(250)
                .output(1, CNItems.URANIUM_POWDER,9)




        ),
        RAW_URANIUM_BLOCK = create(() -> CNBlocks.RAW_URANIUM_BLOCK, b -> b.duration(250)
            .output(1, CNItems.URANIUM_POWDER,81)
        ),

        RAW_LEAD_BLOCK = create(() -> CNBlocks.RAW_LEAD_BLOCK, b -> b.duration(250)
            .output(1, CNItems.RAW_LEAD,9)
            .output(.75f, AllItems.EXP_NUGGET, 18)
        ),

        RAW_LEAD = create(() -> CNItems.RAW_LEAD, b -> b.duration(250)
                .output(1, AllItems.CRUSHED_LEAD,1)
                .output(.75f, AllItems.EXP_NUGGET, 2)
        ),

        RAW_ZINC_ORE = rawOre(AllItems.RAW_ZINC::get, AllItems.CRUSHED_ZINC::get, 1),
        RAW_COPPER_ORE = rawOre(() -> Items.RAW_COPPER, AllItems.CRUSHED_COPPER::get, 1)



    ;

    public CNCrushingRecipeGen(FabricDataOutput generator) {
        super(generator);
    }

    @Override
    protected AllRecipeTypes getRecipeType() {
        return AllRecipeTypes.CRUSHING;
    }

    protected <T extends ProcessingRecipe<?>> GeneratedRecipe create(String namespace,
                                                                     Supplier<ItemLike> singleIngredient, UnaryOperator<ProcessingRecipeBuilder<T>> transform) {
        ProcessingRecipeSerializer<T> serializer = getSerializer();
        GeneratedRecipe generatedRecipe = c -> {
            ItemLike itemLike = singleIngredient.get();
            transform
                    .apply(new ProcessingRecipeBuilder<>(serializer.getFactory(),
                            new ResourceLocation(namespace, RegisteredObjects.getKeyOrThrow(itemLike.asItem())
                                    .getPath())).withItemIngredients(Ingredient.of(itemLike)))
                    .build(c);
        };
        all.add(generatedRecipe);
        return generatedRecipe;
    }

    <T extends ProcessingRecipe<?>> GeneratedRecipe create(Supplier<ItemLike> singleIngredient,
                                                           UnaryOperator<ProcessingRecipeBuilder<T>> transform) {
        return create(CreateNuclear.MOD_ID, singleIngredient, transform);
    }

    <T extends ProcessingRecipe<?>> GeneratedRecipe createC(Supplier<ItemLike> singleIngredient,
                                                           UnaryOperator<ProcessingRecipeBuilder<T>> transform) {
        return create(Create.ID, singleIngredient, transform);
    }

    protected GeneratedRecipe rawOre(Supplier<ItemLike> input, Supplier<ItemLike> result, int amount) {
        return createC(input, b -> b.duration(400)
                .output(result.get(), amount)
                .output(.75f, AllItems.EXP_NUGGET.get(), (result.get() == AllItems.CRUSHED_GOLD.get() ? 2 : 1) * amount)
                .output(.15f, CNItems.LEAD_NUGGET, 1));
    }

    @Override
    public String getName() {
        return "CreateNuclear Processing Recipes: " + getRecipeType().getId().getPath();
    }
}
