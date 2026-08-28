package net.nuclearteam.createnuclear.content.multiblock.controller.service;

import net.minecraft.nbt.CompoundTag;
import net.nuclearteam.createnuclear.content.multiblock.controller.ReactorControllerBlockEntity;

public interface IPersistenceService {
    void readBasicState(ReactorControllerBlockEntity owner, CompoundTag compound, boolean clientPacket);
    void writeBasicState(ReactorControllerBlockEntity owner, CompoundTag compound, boolean clientPacket);
}
