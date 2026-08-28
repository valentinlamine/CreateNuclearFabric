package net.nuclearteam.createnuclear.content.redstone.displayLink.source;

import com.simibubi.create.content.redstone.displayLink.DisplayLinkContext;
import net.minecraft.world.item.Item;
import net.minecraft.ChatFormatting;
import net.nuclearteam.createnuclear.api.multiblock.rods.RodType;
import net.nuclearteam.createnuclear.content.multiblock.controller.ReactorControllerBlockEntity;

import java.util.Map;

public class CoolerDisplaySource extends AbstractReactorStatDisplaySource {


    @Override
    protected String getLabelKey() {
        return "display_source.reactor.cooler";
    }

    @Override
    protected int getMax() {
        return ReactorDisplayConstants.MAX_COOLER;
    }

    @Override
    protected ChatFormatting getColor(int value, ReactorControllerBlockEntity controller) {
        return ChatFormatting.AQUA;
    }

    @Override
    protected int computeValue(ReactorControllerBlockEntity controller, DisplayLinkContext context) {
        int cooler = 0;
        if (controller.getDisplayState() != null && controller.getDisplayState().items() != null) {
            for (Map.Entry<Item, Integer> entry : controller.getDisplayState().items().entrySet()) {
                if (RodType.TypeRodPredicate.isCooled(entry.getKey().getDefaultInstance(), context.level())) {
                    cooler += entry.getValue();
                }
            }
        }
        return cooler;
    }

    @Override
    protected String getTranslationKey() {
        return "cooler";
    }
}