package net.nuclearteam.createnuclear.content.redstone.displayLink.source;

import com.simibubi.create.content.redstone.displayLink.DisplayLinkContext;
import com.simibubi.create.content.redstone.displayLink.source.NumericSingleLineDisplaySource;
import com.simibubi.create.content.redstone.displayLink.target.DisplayTargetStats;
import com.simibubi.create.foundation.gui.ModularGuiLineBuilder;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.ChatFormatting;
import net.nuclearteam.createnuclear.content.logistics.BigFluidStack;
import net.nuclearteam.createnuclear.content.multiblock.MultiblockHelpers;
import net.nuclearteam.createnuclear.content.multiblock.controller.ReactorControllerBlockEntity;
import net.nuclearteam.createnuclear.foundation.utility.CreateNuclearLang;

import java.util.List;

public class LiquidLevelDisplaySource extends AbstractReactorStatDisplaySource {
    @Override
    protected String getLabelKey() {
        return "display_source.reactor.fluid";
    }

    @Override
    protected int getMax() {
        return ReactorDisplayConstants.MAX_FLUID;
    }

    @Override
    protected ChatFormatting getColor(int value, ReactorControllerBlockEntity controller) {
        return ChatFormatting.BLUE;
    }

    @Override
    protected int computeValue(ReactorControllerBlockEntity controller, DisplayLinkContext context) {
        List<BigFluidStack> fluidList = controller.getBigFluidStack();
        return (fluidList != null && !fluidList.isEmpty())
                ? (int) Math.min(Integer.MAX_VALUE, Math.max(0L, fluidList.get(0).amount))
                : 0;
    }

    @Override
    protected MutableComponent getUnitSuffix() {
        return CreateNuclearLang.translateDirect("generic.unit.fluid.value");
    }

    @Override
    protected String getTranslationKey() {
        return "liquid_level";
    }
}
