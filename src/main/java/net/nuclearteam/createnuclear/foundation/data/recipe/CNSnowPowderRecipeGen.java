package net.nuclearteam.createnuclear.foundation.data.recipe;

import net.createmod.catnip.platform.CatnipServices;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.crafting.Ingredient;
import net.nuclearteam.createnuclear.CNFluids;
import net.nuclearteam.createnuclear.CNItems;
import net.nuclearteam.createnuclear.CreateNuclear;
import net.nuclearteam.createnuclear.api.data.recipe.SnowPowderRecipeGen;

import java.util.function.Supplier;

public class CNSnowPowderRecipeGen extends SnowPowderRecipeGen {

    GeneratedRecipe
            COOLED_NITROGEN_CONCENTRATE = convert(CNItems.NITROGEN_CONCENTRATE, CNItems.COOLED_NITROGEN_CONCENTRATE)
    ;

    public GeneratedRecipe convert(ItemLike input, ItemLike result) {
        return convert(() -> Ingredient.of(input), () -> result);
    }

    public GeneratedRecipe convert(Supplier<Ingredient> input, Supplier<ItemLike> result) {
        return create(CreateNuclear.asResource(CatnipServices.REGISTRIES.getKeyOrThrow(result.get()
                                .asItem())
                        .getPath()),
                p -> p.withItemIngredients(input.get())
                        .output(result.get()));
    }

    public CNSnowPowderRecipeGen(PackOutput generator) {
        super(generator, CreateNuclear.MOD_ID);
    }
}
