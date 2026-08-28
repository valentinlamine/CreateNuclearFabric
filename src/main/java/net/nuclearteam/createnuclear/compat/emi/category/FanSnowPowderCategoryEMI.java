package net.nuclearteam.createnuclear.compat.emi.category;

import com.simibubi.create.compat.emi.CreateEmiAnimations;
import com.simibubi.create.compat.emi.recipes.fan.FanEmiRecipe;
import net.createmod.catnip.gui.element.GuiGameElement;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.nuclearteam.createnuclear.compat.emi.CNEmi;
import net.nuclearteam.createnuclear.content.kinetics.fan.processing.SnowPowderRecipe;

public class FanSnowPowderCategoryEMI extends FanEmiRecipe.MultiOutput<SnowPowderRecipe> {
    public FanSnowPowderCategoryEMI(SnowPowderRecipe recipe) {
        super(CNEmi.FAN_SNOW_POWDER, recipe);
    }

    @Override
    protected void renderAttachedBlock(GuiGraphics graphics) {
        GuiGameElement.of(Blocks.POWDER_SNOW.defaultBlockState())
            .scale(SCALE)
            .atLocal(0, 0, 2)
            .lighting(CreateEmiAnimations.DEFAULT_LIGHTING)
            .render(graphics);
    }

    public Component getTitle() {
        return Component.translatable("createnuclear.snow_powder.fan.recipe");
    }
}
