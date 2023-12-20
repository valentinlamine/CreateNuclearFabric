package net.ynov.createnuclear.mixin;

import com.chocohead.mm.api.ClassTinkerers;
import net.ynov.createnuclear.tools.EnrichedRecipe;

import java.util.function.Supplier;

public class CNCreateFanRecipeMixin implements Runnable {
    @Override
    public void run() {
        ClassTinkerers.enumBuilder("com.simibubi.create.AllRecipeTypes$AllRecipeTypes")
                .addEnum("ENRICHED", () -> {
                    Supplier<Supplier<Object[]>> supplier = (() -> () -> new Object[] {EnrichedRecipe.class});
                    return supplier.get().get();
                }).build();

    }
}
