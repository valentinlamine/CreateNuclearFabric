package net.ynov.createnuclear.fan;

import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder.ProcessingRecipeParams;
import io.github.fabricators_of_create.porting_lib.transfer.item.ItemStackHandlerContainer;
import net.minecraft.world.level.Level;
import net.ynov.createnuclear.CreateNuclear;
import net.ynov.createnuclear.fan.EnrichedRecipe.EnrichedWrapper;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class EnrichedRecipe extends ProcessingRecipe<EnrichedWrapper> {

    public EnrichedRecipe(ProcessingRecipeParams params) {
        super(CNRecipeTypes.ENRICHED, params);
    }

    @Override
    public boolean matches(EnrichedWrapper inv, Level worldIn) {
        if (inv.isEmpty()) return false;
        return ingredients.get(0).test(inv.getItem(0));
    }
    @Override
    protected int getMaxInputCount() {
        return 1;
    }

    @Override
    protected int getMaxOutputCount() {
        return 12;
    }

    public static class EnrichedWrapper extends ItemStackHandlerContainer {
        public EnrichedWrapper() {
            super(1);
        }
    }
}
