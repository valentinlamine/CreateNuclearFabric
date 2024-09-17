package net.nuclearteam.createnuclear.datagen.recipe.mechanical_crafter;

import com.google.common.base.Supplier;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.data.recipe.CreateRecipeProvider;
import com.simibubi.create.foundation.data.recipe.MechanicalCraftingRecipeBuilder;
import com.simibubi.create.foundation.data.recipe.MechanicalCraftingRecipeGen;
import com.simibubi.create.foundation.utility.RegisteredObjects;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.nuclearteam.createnuclear.CreateNuclear;
import net.nuclearteam.createnuclear.item.CNItems;

import java.util.function.UnaryOperator;

public class CNMechanicalCraftingRecipeGen extends CreateRecipeProvider {

    GeneratedRecipe
        GRAPHITE_ROD = create(CNItems.GRAPHITE_ROD::get)
            .recipe(b -> b
                    .key('S', Ingredient.of(CNItems.STEEL_INGOT))
                    .key('G', Ingredient.of(CNItems.GRAPHENE))
                    .patternLine("SGS")
                    .patternLine("SGS")
                    .patternLine("SGS")
                    .patternLine("SGS")
            ),
    URANIUM_ROD = create(CNItems.URANIUM_ROD::get)
            .recipe(b -> b
                .key('U', Ingredient.of(CNItems.YELLOW_CAKE_ENRICHED))
                    .patternLine("    U")
                    .patternLine("   U ")
                    .patternLine("  U  ")
                    .patternLine(" U   ")
                    .patternLine("U    ")
            )
    ;

    GeneratedRecipeBuilder create(Supplier<ItemLike> result) {
        return new GeneratedRecipeBuilder(result);
    }

    class GeneratedRecipeBuilder {

        private String suffix;
        private Supplier<ItemLike> result;
        private int amount;

        public GeneratedRecipeBuilder(Supplier<ItemLike> result) {
            this.suffix = "";
            this.result = result;
            this.amount = 1;
        }

        GeneratedRecipeBuilder returns(int amount) {
            this.amount = amount;
            return this;
        }

        GeneratedRecipeBuilder withSuffix(String suffix) {
            this.suffix = suffix;
            return this;
        }

        GeneratedRecipe recipe(UnaryOperator<MechanicalCraftingRecipeBuilder> builder) {
            return register(consumer -> {
                MechanicalCraftingRecipeBuilder b =
                        builder.apply(MechanicalCraftingRecipeBuilder.shapedRecipe(result.get(), amount));
                ResourceLocation location = CreateNuclear.asResource("mechanical_crafting/" + RegisteredObjects.getKeyOrThrow(result.get()
                                .asItem())
                        .getPath() + suffix);
                b.build(consumer, location);
            });
        }
    }

    public CNMechanicalCraftingRecipeGen(FabricDataOutput output) {
        super(output);
    }

    @Override
    public String getName() {
        return "CreateNuclair's Mechanical Crafting Recipes";
    }
}
