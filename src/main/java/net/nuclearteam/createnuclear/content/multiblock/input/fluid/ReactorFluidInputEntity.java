package net.nuclearteam.createnuclear.content.multiblock.input.fluid;

import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.fluid.SmartFluidTank;
import io.github.fabricators_of_create.porting_lib.fluids.FluidStack;
import io.github.fabricators_of_create.porting_lib.transfer.callbacks.TransactionCallback;
import net.createmod.catnip.animation.LerpedFloat;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SidedStorageBlockEntity;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.nuclearteam.createnuclear.content.logistics.FluidUnits;
import net.nuclearteam.createnuclear.content.multiblock.MultiblockHelpers;
import net.nuclearteam.createnuclear.content.multiblock.controller.ReactorControllerBlockEntity;
import net.nuclearteam.createnuclear.foundation.block.MultiDirectionalReactorBlock;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;

public class ReactorFluidInputEntity extends SmartBlockEntity
        implements IHaveGoggleInformation, SidedStorageBlockEntity {
    public static final long DEFAULT_CAPACITY = FluidUnits.milliBucketsToDroplets(16_000);

    private final SmartFluidTank internalTank;
    private final Storage<FluidVariant> filteredStorage = new FilteredFluidStorage();
    private LerpedFloat fluidLevel;

    public ReactorFluidInputEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        internalTank = new SmartFluidTank(DEFAULT_CAPACITY, this::onTankContentsChanged);
    }

    public static long getCapacityForReactorSize(int reactorSize) {
        return FluidUnits.milliBucketsToDroplets(switch (reactorSize) {
            case 5 -> 144_000;
            case 7 -> 448_000;
            case 9 -> 848_000;
            default -> 16_000;
        });
    }

    public void applyCapacity(long capacity) {
        if (internalTank.getCapacity() == capacity) return;
        internalTank.setCapacity(capacity);
        if (getLevel() != null && !getLevel().isClientSide) {
            setChanged();
            sendData();
        }
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
    }

    @Override
    protected void write(CompoundTag tag, boolean clientPacket) {
        super.write(tag, clientPacket);
        tag.put("tank", internalTank.writeToNBT(new CompoundTag()));
        tag.putLong("capacity", internalTank.getCapacity());
    }

    @Override
    protected void read(CompoundTag tag, boolean clientPacket) {
        super.read(tag, clientPacket);
        if (tag.contains("capacity")) internalTank.setCapacity(tag.getLong("capacity"));
        internalTank.readFromNBT(tag.getCompound("tank"));
        if (tag.contains("ForceFluidLevel") || fluidLevel == null) {
            fluidLevel = LerpedFloat.linear().startWithValue(getFillState());
        }
    }

    public float getFillState() {
        if (internalTank.getCapacity() == 0) return 0;
        return (float) ((double) internalTank.getFluidAmount() / internalTank.getCapacity());
    }

    protected void onTankContentsChanged(FluidStack contents) {
        if (getLevel() == null) {
            if (fluidLevel == null) fluidLevel = LerpedFloat.linear().startWithValue(getFillState());
            return;
        }
        if (!getLevel().isClientSide) {
            setChanged();
            sendData();
        }
        if (isVirtual()) {
            if (fluidLevel == null) fluidLevel = LerpedFloat.linear().startWithValue(getFillState());
            fluidLevel.chase(getFillState(), .5f, LerpedFloat.Chaser.EXP);
        }
        if (!getLevel().isClientSide && internalTank.getFluidAmount() == 0) {
            ReactorControllerBlockEntity controller = MultiblockHelpers.getControllerForPart(getLevel(), pos);
            if (controller != null) controller.clearLockIfAllInputsEmpty();
        }
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        return containedFluidTooltip(tooltip, isPlayerSneaking, filteredStorage);
    }

    @Override
    public Storage<FluidVariant> getFluidStorage(@Nullable Direction side) {
        if (side != null && side != getBlockState().getValue(MultiDirectionalReactorBlock.FACING)) {
            return Storage.empty();
        }
        return filteredStorage;
    }

    public FluidStack getFluid() {
        return internalTank.getFluid();
    }

    public long getTankCapacity() {
        return internalTank.getCapacity();
    }

    private final class FilteredFluidStorage implements Storage<FluidVariant> {
        @Override
        public long insert(FluidVariant resource, long maxAmount, TransactionContext transaction) {
            if (resource.isBlank() || maxAmount <= 0 || getLevel() == null) return 0;
            ReactorControllerBlockEntity controller = MultiblockHelpers.getControllerForPart(getLevel(), pos);
            BlockPos controllerPos = controller == null ? null : controller.getBlockPos();

            if (controllerPos != null && getLevel() instanceof ServerLevel serverWorld
                    && !PersistentFluidLocks.get(serverWorld)
                    .canAccept(controllerPos, resource.getFluid())) {
                return 0;
            }

            long inserted = internalTank.insert(resource, maxAmount, transaction);
            if (inserted > 0 && controllerPos != null && getLevel() instanceof ServerLevel serverWorld) {
                TransactionCallback.onSuccess(transaction, () ->
                        PersistentFluidLocks.get(serverWorld)
                                .tryLock(controllerPos, resource.getFluid()));
            }
            return inserted;
        }

        @Override
        public long extract(FluidVariant resource, long maxAmount, TransactionContext transaction) {
            long extracted = internalTank.extract(resource, maxAmount, transaction);
            if (extracted > 0 && internalTank.getFluidAmount() == 0
                    && getLevel() instanceof ServerLevel serverWorld) {
                TransactionCallback.onSuccess(transaction, () -> {
                    ReactorControllerBlockEntity controller =
                            MultiblockHelpers.getControllerForPart(getLevel(), pos);
                    if (controller != null) {
                        PersistentFluidLocks.get(serverWorld).clearLock(controller.getBlockPos());
                    }
                });
            }
            return extracted;
        }

        @Override
        public Iterator<StorageView<FluidVariant>> iterator() {
            return internalTank.iterator();
        }

        @Override
        public boolean supportsInsertion() {
            return internalTank.supportsInsertion();
        }

        @Override
        public boolean supportsExtraction() {
            return internalTank.supportsExtraction();
        }
    }
}
