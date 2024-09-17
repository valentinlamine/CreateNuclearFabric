package net.nuclearteam.createnuclear.compact.recipe.category;

import com.simibubi.create.compat.rei.category.ProcessingViaFanCategory;
import com.simibubi.create.compat.rei.category.animations.AnimatedKinetics;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.gui.element.GuiGameElement;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.nuclearteam.createnuclear.block.CNBlocks;
import net.nuclearteam.createnuclear.fan.EnrichedRecipe;

public class FanEnrichedCategoryREI extends ProcessingViaFanCategory.MultiOutput<EnrichedRecipe> {
    public FanEnrichedCategoryREI(Info<EnrichedRecipe> info) {
        super(info);
    }

    @Override
    protected AllGuiTextures getBlockShadow() {
        return AllGuiTextures.JEI_LIGHT;
    }

    @Override
    protected void renderAttachedBlock(GuiGraphics graphics) {
        GuiGameElement.of(CNBlocks.ENRICHING_FIRE.getDefaultState())
                .scale(SCALE)
                .atLocal(0, 0, 2)
                .lighting(AnimatedKinetics.DEFAULT_LIGHTING)
                .render(graphics);
    }

    @Override
    public Component getTitle() {
        return Component.translatable("createnuclear.enriched.fan.recipe");
    }
}
