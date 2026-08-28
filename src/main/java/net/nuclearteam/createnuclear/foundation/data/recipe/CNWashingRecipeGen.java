package net.nuclearteam.createnuclear.foundation.data.recipe;

import com.simibubi.create.AllItems;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.api.data.recipe.WashingRecipeGen;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.crafting.Ingredient;
import net.nuclearteam.createnuclear.CNItems;
import net.nuclearteam.createnuclear.CreateNuclear;


import java.util.function.Supplier;

@SuppressWarnings("unused")
public class CNWashingRecipeGen extends WashingRecipeGen {
    GeneratedRecipe
        CRUSHED_LEAD = moddedCrushedOre(AllItems.CRUSHED_LEAD, () -> CNItems.LEAD_NUGGET, () -> AllItems.EXP_NUGGET, .5f)
    ;

    public GeneratedRecipe moddedCrushedOre(ItemEntry<? extends Item> crushed, Supplier<ItemLike> nugget, Supplier<ItemLike> secondary,
                                            float secondaryChance) {
        return create("/" + crushed.getId().getPath(),b ->
                b.withItemIngredients(Ingredient.of(crushed::get))
                        .output(secondaryChance, secondary.get(), 1)
                        .output(1, nugget.get(), 9)
        );
    }


    public CNWashingRecipeGen(FabricDataOutput output) {
        super(output, CreateNuclear.MOD_ID);
    }

}
