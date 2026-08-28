package net.nuclearteam.createnuclear.api.data.recipe;

import com.simibubi.create.api.data.recipe.DatagenMod;
import com.simibubi.create.api.data.recipe.ProcessingRecipeGen;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import net.nuclearteam.createnuclear.CNRecipeTypes;

public abstract class EnrichedRecipeGen extends ProcessingRecipeGen {

    protected GeneratedRecipe moddedCompacting(DatagenMod mod, String input, String output) {
        return create("compat/" + mod.getId() + "/" + output, b -> b.require(mod, input)
                .output(mod, output)
                .whenModLoaded(mod.getId()));
    }

    protected GeneratedRecipe moddedPaths(DatagenMod mod, String... blocks) {
        for(String block : blocks) {
            moddedCompacting(mod, block, block + "_path");
        }
        return null;
    }

    public EnrichedRecipeGen(PackOutput generator, String defaultNamespace) {
        super(generator, defaultNamespace);
    }

    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return CNRecipeTypes.ENRICHED;
    }
}
