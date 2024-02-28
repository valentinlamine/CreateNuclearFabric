package net.ynov.createnuclear.compact.jei.category;

import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.compat.jei.category.ProcessingViaFanCategory;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;
import com.simibubi.create.content.kinetics.fan.processing.HauntingRecipe;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.gui.element.GuiGameElement;
import net.minecraft.client.gui.GuiGraphics;
import net.ynov.createnuclear.block.CNBlocks;
import net.ynov.createnuclear.fan.EnrichedRecipe;

public class FanEnrichedCategory extends ProcessingViaFanCategory.MultiOutput<EnrichedRecipe>{

    public FanEnrichedCategory(Info<EnrichedRecipe> info) {
        super(info);
    }


    @Override
    protected AllGuiTextures getBlockShadow() {
        return AllGuiTextures.JEI_LIGHT;
    }

    @Override
    protected void renderAttachedBlock(GuiGraphics graphics) {
        GuiGameElement.of(CNBlocks.ENRICHED_SOUL_SOIL)
                .scale(SCALE)
                .atLocal(0, 0, 2)
                .lighting(AnimatedKinetics.DEFAULT_LIGHTING)
                .render(graphics);
    }
}
