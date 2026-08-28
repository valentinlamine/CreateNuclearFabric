package net.nuclearteam.createnuclear.content.multiblock.controller.service;

import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.core.Direction;
import net.nuclearteam.createnuclear.content.multiblock.controller.ReactorControllerBlockEntity;
import net.nuclearteam.createnuclear.content.multiblock.controller.display.ReactorDisplayState;

public class DefaultPersistenceService implements IPersistenceService {
    @Override
    public void readBasicState(ReactorControllerBlockEntity owner, CompoundTag compound, boolean clientPacket) {
        owner.setMultiblockSize(compound.getInt("reactorSize"));
        owner.setMultiblockFacing(Direction.byName(compound.getString("reactorFacing")));

        owner.setMultiblockStructure(compound.contains("reactorPose")
            ? BoundingBox.CODEC.parse(NbtOps.INSTANCE, compound.get("reactorPose")).result().orElse(null)
            : null
        );

        if (!clientPacket) {
            owner.deserializeInventory(compound.getCompound("pattern"));
        } else {
            owner.setDisplayState(compound.contains("displayState")
                    ? ReactorDisplayState.deserializeNBT(compound.getCompound("displayState"))
                    : ReactorDisplayState.EMPTY
            );
        }
        owner.setConfiguredPattern(ItemStack.fromNbt(compound.getCompound("items")));

    }

    @Override
    public void writeBasicState(ReactorControllerBlockEntity owner, CompoundTag compound, boolean clientPacket) {
        compound.putInt("reactorSize", owner.getMultiblockSize());
        compound.putString("reactorFacing", owner.getMultiblockFacing() != null ? owner.getMultiblockFacing().getSerializedName() : "");
        if (owner.getMultiblockPos() != null) {
            compound.put("reactorPose", BoundingBox.CODEC.encodeStart(NbtOps.INSTANCE, owner.getMultiblockPos()).getOrThrow(false, IllegalStateException::new));
        }

        if (!clientPacket) {
            compound.put("pattern", owner.serializeInventory());
        } else {
            compound.put("displayState", owner.getDisplayState().serializeNBT());
        }
        compound.put("items", owner.getConfiguredPattern().serializeNBT());

    }
}
