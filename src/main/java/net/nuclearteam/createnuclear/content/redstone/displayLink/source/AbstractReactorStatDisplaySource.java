package net.nuclearteam.createnuclear.content.redstone.displayLink.source;

import com.simibubi.create.content.redstone.displayLink.DisplayLinkContext;
import com.simibubi.create.content.redstone.displayLink.source.NumericSingleLineDisplaySource;
import com.simibubi.create.content.redstone.displayLink.target.DisplayTargetStats;
import com.simibubi.create.foundation.gui.ModularGuiLineBuilder;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.nuclearteam.createnuclear.content.multiblock.MultiblockHelpers;
import net.nuclearteam.createnuclear.content.multiblock.controller.ReactorControllerBlockEntity;
import net.nuclearteam.createnuclear.foundation.utility.CreateNuclearLang;

public abstract class AbstractReactorStatDisplaySource extends NumericSingleLineDisplaySource {
    protected abstract String getLabelKey();
    protected abstract int getMax();
    protected abstract int computeValue(ReactorControllerBlockEntity controller, DisplayLinkContext context);
    protected abstract ChatFormatting getColor(int value, ReactorControllerBlockEntity controller);
    protected MutableComponent getUnitSuffix() { return null; }

    @Override
    protected MutableComponent provideLine(DisplayLinkContext context, DisplayTargetStats stats) {
        ReactorControllerBlockEntity controller = MultiblockHelpers.getControllerForPart(context.level(), context.getSourcePos());
        if (controller == null || controller.isRemoved()) return ZERO.copy();

        MutableComponent label = CreateNuclearLang.translateDirect(getLabelKey()).append(" ");
        int mode = context.sourceConfig().getInt("display_mode");
        int value = computeValue(controller, context);
        int max = getMax();
        ChatFormatting color = getColor(value, controller);

        return label.append(switch (mode) {
            case 1 -> Component.literal((value * 100 / max) + "%").withStyle(color);
            case 2 -> ReactorGaugeRenderer.drawGauge(value, max, color, 6);
            default -> {
                MutableComponent base = Component.literal(String.valueOf(value));
                MutableComponent unit = getUnitSuffix();
                yield (unit != null ? base.append(unit) : base).withStyle(color);
            }
        });
    }

    @Override
    public void initConfigurationWidgets(DisplayLinkContext context, ModularGuiLineBuilder builder, boolean isFirstLine) {
        if (isFirstLine) return;
        builder.addSelectionScrollInput(0, 100, (input, l) -> input
                .forOptions(CreateNuclearLang.translatedOptions("display_source.reactor.mode", "value", "percent", "gauge")), "display_mode");
    }

    @Override protected boolean allowsLabeling(DisplayLinkContext context) { return true; }
}
