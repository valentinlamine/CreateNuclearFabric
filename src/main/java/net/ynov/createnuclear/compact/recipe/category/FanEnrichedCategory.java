package net.ynov.createnuclear.compact.recipe.category;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.compat.jei.category.ProcessingViaFanCategory;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.gui.element.GuiGameElement;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.ynov.createnuclear.CreateNuclear;
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
        GuiGameElement.of(CNBlocks.ENRICHING_FIRE.getDefaultState())
                .scale(SCALE)
                .atLocal(0, 0, 2)
                .lighting(AnimatedKinetics.DEFAULT_LIGHTING)
                .render(graphics);
        CreateNuclear.LOGGER.warn("" + graphics);
    }

    @Override
    public Component getTitle() {
        return Component.translatable("createnuclear.enriched.fan.recipe");
    }
}
