package net.nuclearteam.createnuclear.api.multiblock;

import net.minecraft.core.Direction;

public interface IMultiblockController {
    void setMultiblockFacing(Direction f);
    Direction getMultiblockFacing();
}
