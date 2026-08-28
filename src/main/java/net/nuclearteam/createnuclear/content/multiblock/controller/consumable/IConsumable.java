package net.nuclearteam.createnuclear.content.multiblock.controller.consumable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.nuclearteam.createnuclear.content.multiblock.controller.manager.ReactorInputManagerI;

public interface IConsumable {
    String getId();

    int computeTimer(int countInPattern);

    boolean consume(ReactorInputManagerI manager, Level level);

    CompoundTag serializeNBT();

    static IConsumable deserializeNBT(CompoundTag tag) {
        return switch (tag.getString("type")) {
            case "item" -> ItemConsumable.deserializeNBT(tag);
            case "fluid" -> FluidConsumable.deserializeNBT(tag);
            default -> throw new IllegalArgumentException(
                    "Unknown consumable type: " + tag.getString("type"));
        };
    }
}
