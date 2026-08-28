package net.nuclearteam.createnuclear.foundation.data.recipe;

import com.simibubi.create.api.data.recipe.ProcessingRecipeGen;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataProvider;
import net.minecraft.MethodsReturnNonnullByDefault;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unused")
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public abstract class CNProcessingRecipeGen {

    protected static final List<ProcessingRecipeGen> GENERATORS = new ArrayList<>();

    public static DataProvider registerAll(FabricDataOutput output) {
        GENERATORS.add(new CNCompactingRecipeGen(output));
        GENERATORS.add(new CNCrushingRecipeGen(output));
        GENERATORS.add(new CNEnrichedRecipeGen(output));
        GENERATORS.add(new CNMixingRecipeGen(output));
        GENERATORS.add(new CNPressingRecipeGen(output));
        GENERATORS.add(new CNItemApplicationRecipeGen(output));
        GENERATORS.add(new CNSnowPowderRecipeGen(output));
        GENERATORS.add(new CNWashingRecipeGen(output));
        GENERATORS.add(new CNDeployingRecipeGen(output));

        return new DataProvider() {

            @Override
            public String getName() {
                return "CreateNuclear's Processing Recipes";
            }

            @Override
            public CompletableFuture<?> run(DataProvider output) {
                return CompletableFuture.allOf(GENERATORS.stream()
                        .map(gen -> gen.run(output))
                        .toArray(CompletableFuture[]::new));
            }
        };
    }
}
