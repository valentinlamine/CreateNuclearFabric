package net.ynov.createnuclear.datagen;

import com.simibubi.create.foundation.data.recipe.CreateRecipeProvider;
import com.simibubi.create.foundation.data.recipe.ProcessingRecipeGen;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import io.github.fabricators_of_create.porting_lib.tags.Tags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.tags.BlockTags;
import net.ynov.createnuclear.datagen.recipe.compacting.CNCompactingRecipeGen;
import net.ynov.createnuclear.datagen.recipe.crushing.CNCrushingRecipeGen;
import net.ynov.createnuclear.datagen.recipe.enriched.CNEnrichedRecipeGen;
import net.ynov.createnuclear.datagen.recipe.mixing.CNMixingRecipeGen;
import net.ynov.createnuclear.datagen.recipe.pressing.CNPressingRecipeGen;
import net.ynov.createnuclear.tags.CNTag;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public abstract class CNProcessingRecipeGen {

    protected static final List<ProcessingRecipeGen> GENERATORS = new ArrayList<>();

    public static DataProvider registerAll(FabricDataOutput output) {
        GENERATORS.add(new CNCompactingRecipeGen(output));
        GENERATORS.add(new CNCrushingRecipeGen(output));
        GENERATORS.add(new CNEnrichedRecipeGen(output));
        GENERATORS.add(new CNMixingRecipeGen(output));
        GENERATORS.add(new CNPressingRecipeGen(output));

        return new DataProvider() {

            @Override
            public String getName() {
                return "CreateNuclair's Processing Recipes";
            }

            @Override
            public CompletableFuture<?> run(CachedOutput output) {
                return CompletableFuture.allOf(GENERATORS.stream()
                        .map(gen -> gen.run(output))
                        .toArray(CompletableFuture[]::new));
            }
        };
    }
}
