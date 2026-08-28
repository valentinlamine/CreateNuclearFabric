package net.nuclearteam.createnuclear.compat.rei.category;

import com.simibubi.create.compat.rei.category.ProcessingViaFanCategory;
import com.simibubi.create.compat.rei.category.animations.AnimatedKinetics;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import net.createmod.catnip.gui.element.GuiGameElement;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.nuclearteam.createnuclear.content.kinetics.fan.processing.SnowPowderRecipe;

public class FanSnowPowderCategoryREI extends ProcessingViaFanCategory.MultiOutput<SnowPowderRecipe> {
    public FanSnowPowderCategoryREI(Info<SnowPowderRecipe> info) {
        super(info);
    }

    @Override
    protected AllGuiTextures getBlockShadow() {
        return AllGuiTextures.JEI_LIGHT;
    }

    @Override
    protected void renderAttachedBlock(GuiGraphics graphics) {
        GuiGameElement.of(Blocks.POWDER_SNOW.defaultBlockState())
            .scale(SCALE)
            .atLocal(0, 0, 2)
            .lighting(AnimatedKinetics.DEFAULT_LIGHTING)
            .render(graphics);
    }

    @Override
    public Component getTitle() {
        return Component.translatable("createnuclear.snow_powder.fan.recipe");
    }
}
