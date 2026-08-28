package net.nuclearteam.createnuclear.content.redstone.displayLink.source;

import com.simibubi.create.content.redstone.displayLink.DisplayLinkContext;
import net.minecraft.world.item.Item;
import net.minecraft.ChatFormatting;
import net.nuclearteam.createnuclear.api.multiblock.rods.RodType.TypeRodPredicate;
import net.nuclearteam.createnuclear.content.multiblock.controller.ReactorControllerBlockEntity;

import java.util.Map;

public class FuelDisplaySource extends AbstractReactorStatDisplaySource {

    @Override
    protected String getLabelKey() {
        return "display_source.reactor.fuel";
    }

    @Override
    protected int getMax() {
        return ReactorDisplayConstants.MAX_FUEL;
    }

    @Override
    protected ChatFormatting getColor(int value, ReactorControllerBlockEntity controller) {
        return ChatFormatting.GREEN;
    }

    @Override
    protected int computeValue(ReactorControllerBlockEntity controller, DisplayLinkContext context) {
        int fuel = 0;
        if (controller.getDisplayState() != null && controller.getDisplayState().items() != null) {
            for (Map.Entry<Item, Integer> entry : controller.getDisplayState().items().entrySet()) {
                if (TypeRodPredicate.isFuel(entry.getKey().getDefaultInstance(), context.level())) {
                    fuel += entry.getValue();
                }
            }
        }
        return fuel;
    }

    @Override
    protected String getTranslationKey() {
        return "fuel";
    }
}