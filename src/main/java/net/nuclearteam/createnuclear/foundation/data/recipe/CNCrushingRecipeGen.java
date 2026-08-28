package net.nuclearteam.createnuclear.foundation.data.recipe;

import com.simibubi.create.AllItems;
import com.simibubi.create.api.data.recipe.CrushingRecipeGen;
import com.simibubi.create.content.decoration.palettes.AllPaletteStoneTypes;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeSerializer;
import net.createmod.catnip.lang.Lang;
import net.createmod.catnip.platform.CatnipServices;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.data.CachedOutput;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.resources.ResourceLocation;
import net.nuclearteam.createnuclear.CNBlocks;
import net.nuclearteam.createnuclear.CNItems;
import net.nuclearteam.createnuclear.CreateNuclear;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class CNCrushingRecipeGen extends CrushingRecipeGen {

    GeneratedRecipe
        COAL_DUST = create("coal", b -> b
            .duration(250)
            .require(ItemTags.COALS)
            .output(0.50f, CNItems.COAL_DUST)),

        GRANITE_URANIUM_POWDER = create(() -> Items.GRANITE, b -> b
            .duration(250)
            .output(0.5f, CNItems.URANIUM_POWDER)
            .output(1f, Blocks.RED_SAND)),

        RAW_URANIUM_BLOCK = create(() -> CNBlocks.RAW_URANIUM_BLOCK, b -> b
            .duration(400)
            .output(1, AllItems.CRUSHED_URANIUM, 9)
            .output(0.75f, AllItems.EXP_NUGGET, 9)),

        RAW_URANIUM_ITEM = create(() -> CNItems.RAW_URANIUM, b -> b
            .duration(400)
            .output(1, AllItems.CRUSHED_URANIUM, 1)
            .output(0.75f, AllItems.EXP_NUGGET, 1)),

        FIX_RAW_URANIUM = createFix(CreateNuclear.MOD_ID, AllItems.CRUSHED_URANIUM::get, b -> b
            .duration(255)
            .output(1, CNItems.URANIUM_POWDER, 9)),

        RAW_THORIUM_BLOCK = create(() -> CNBlocks.RAW_THORIUM_BLOCK, b -> b
            .duration(250)
            .output(1, CNItems.THORIUM_DUST, 9)
            .output(0.75f, AllItems.EXP_NUGGET, 9)),

        RAW_THORIUM_ITEM = create(() -> CNItems.RAW_THORIUM, b -> b
            .duration(125)
            .output(1, CNItems.THORIUM_DUST, 1)
            .output(0.75f, AllItems.EXP_NUGGET, 1)),

        RAW_ZINC = create(() -> AllItems.RAW_ZINC, b -> b
            .duration(250)
            .output(1, AllItems.CRUSHED_ZINC, 1)
            .output(0.75f, AllItems.EXP_NUGGET, 1)
            .output(0.25f, CNItems.LEAD_NUGGET, 1)),

        RAW_COPPER = create(() -> Items.RAW_COPPER, b -> b
            .duration(250)
            .output(1, AllItems.CRUSHED_COPPER, 1)
            .output(0.75f, AllItems.EXP_NUGGET, 1)
            .output(0.15f, CNItems.LEAD_NUGGET, 1)),

        NITRATE = create("nitrate", b -> b
            .require(AllPaletteStoneTypes.LIMESTONE.materialTag)
            .duration(250)
            .output(0.6f, CNItems.NITRATE, 1)
            .output(0.4f, CNItems.LEAD_NUGGET, 1));

    public CNCrushingRecipeGen(CachedOutput output) {
        super(output, CreateNuclear.MOD_ID);
    }

    protected GeneratedRecipe mineralRecycling(AllPaletteStoneTypes type,
                                                UnaryOperator<ProcessingRecipeBuilder<ProcessingRecipe<?>>> transform) {
        create(Lang.asId(type.name()) + "_recycling", b -> transform.apply(b.require(type.materialTag)));
        return create(type.getBaseBlock()::get, transform);
    }

    protected <T extends ProcessingRecipe<?>> GeneratedRecipe createFix(
        String namespace,
        Supplier<ItemLike> singleIngredient,
        UnaryOperator<ProcessingRecipeBuilder<T>> transform
    ) {
        ProcessingRecipeSerializer<T> serializer = getSerializer();
        GeneratedRecipe generatedRecipe = consumer -> {
            ItemLike item = singleIngredient.get();
            transform.apply(new ProcessingRecipeBuilder<>(serializer.getFactory(),
                    new ResourceLocation(namespace, "fix/" + CatnipServices.REGISTRIES.getKeyOrThrow(item.asItem()).getPath()))
                .withItemIngredients(Ingredient.of(item)))
                .build(consumer);
        };
        all.add(generatedRecipe);
        return generatedRecipe;
    }
}
