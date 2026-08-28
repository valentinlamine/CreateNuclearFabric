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

public class ReactorSizeDisplaySource extends NumericSingleLineDisplaySource {

    @Override
    protected MutableComponent provideLine(DisplayLinkContext context, DisplayTargetStats stats) {
        ReactorControllerBlockEntity controller = MultiblockHelpers.getControllerForPart(context.level(), context.getSourcePos());
        if (controller == null || controller.isRemoved()) return ZERO.copy();

        // Label + trailing space
        MutableComponent label = CreateNuclearLang.translateDirect("display_source.reactor.size").append(" ");

        int mode = context.sourceConfig().getInt("display_mode");
        int size = controller.getMultiblockSize();
        int tier = size <= 5 ? 1 : size <= 7 ? 2 : 3;

        return label.append(switch (mode) {
            case 1 -> Component.literal((tier * 100 / 3) + "%").withStyle(ChatFormatting.BLUE);
            case 2 -> {
                // Short 3-segment gauge representing the tier
                yield Component.literal("█".repeat(tier) + "▒".repeat(3 - tier)).withStyle(ChatFormatting.BLUE);
            }
            default -> {
                String key = tier == 1 ? "small" : tier == 2 ? "medium" : "large";
                yield CreateNuclearLang.translateDirect("display_source.reactor.size." + key).withStyle(ChatFormatting.BLUE);
            }
        });
    }

    @Override protected String getTranslationKey() { return "size"; }

    @Override
    public void initConfigurationWidgets(DisplayLinkContext context, ModularGuiLineBuilder builder, boolean isFirstLine) {
        if (isFirstLine) return;
        builder.addSelectionScrollInput(0, 100, (selectionScrollInput, l) -> selectionScrollInput
                .forOptions(CreateNuclearLang.translatedOptions("display_source.reactor.mode", "value", "percent", "gauge")), "display_mode");
    }

    @Override protected boolean allowsLabeling(DisplayLinkContext context) { return true; }
}
