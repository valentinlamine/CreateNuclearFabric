package net.nuclearteam.createnuclear.content.multiblock.controller.manager;

import io.github.fabricators_of_create.porting_lib.transfer.TransferUtil;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.ListTag;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.nuclearteam.createnuclear.api.multiblock.rods.RodType.TypeRodPredicate;
import net.nuclearteam.createnuclear.content.multiblock.input.item.ReactorRodInputEntity;
import net.nuclearteam.createnuclear.content.multiblock.input.item.VirtualReactorInputsItem;

import java.util.ArrayList;
import java.util.List;

public class ReactorInputManager extends AbstractReactorIOManager implements ReactorInputManagerI {
    private static final String NBT_KEY = "ReactorInput";

    @Override
    public void write(CompoundTag compound) {
        ListTag list = new ListTag();
        for (BlockPos pos : positions) {
            CompoundTag tag = new CompoundTag();
            tag.putInt("x", pos.getX());
            tag.putInt("y", pos.getY());
            tag.putInt("z", pos.getZ());
            list.add(tag);
        }
        compound.put(NBT_KEY, list);
    }

    @Override
    public void read(CompoundTag compound) {
        positions.clear();
        if (!compound.contains(NBT_KEY)) return;
        ListTag list = compound.getList(NBT_KEY, Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            CompoundTag tag = list.getCompound(i);
            positions.add(new BlockPos(tag.getInt("x"), tag.getInt("y"), tag.getInt("z")));
        }
    }

    @Override
    public List<Storage<ItemVariant>> getItemHandlers(Level level) {
        if (level == null) return List.of();
        List<Storage<ItemVariant>> handlers = new ArrayList<>();
        for (BlockPos pos : List.copyOf(positions)) {
            Storage<ItemVariant> storage = TransferUtil.getItemStorage(level, pos, null);
            if (storage != null) handlers.add(storage);
        }
        return List.copyOf(handlers);
    }

    @Override
    public VirtualReactorInputsItem getInventory(Level level) {
        int fuel = 0;
        int cooler = 0;
        for (Storage<ItemVariant> storage : getItemHandlers(level)) {
            for (StorageView<ItemVariant> view : storage.nonEmptyViews()) {
                ItemStack stack = view.getResource().toStack((int) Math.min(view.getAmount(), Integer.MAX_VALUE));
                if (TypeRodPredicate.isFuel(stack, level)) {
                    fuel = saturatedAdd(fuel, view.getAmount());
                } else if (TypeRodPredicate.isCooled(stack, level)) {
                    cooler = saturatedAdd(cooler, view.getAmount());
                }
            }
        }
        return new VirtualReactorInputsItem(fuel, cooler);
    }

    private static int saturatedAdd(int current, long amount) {
        return (int) Math.min(Integer.MAX_VALUE, current + Math.min(amount, Integer.MAX_VALUE));
    }

    @Override
    public boolean extractItems(Level level, int fuelNeeded, int coolerNeeded) {
        if (level == null || fuelNeeded < 0 || coolerNeeded < 0) return false;
        if (fuelNeeded == 0 && coolerNeeded == 0) return true;

        long fuelRemaining = fuelNeeded;
        long coolerRemaining = coolerNeeded;
        try (Transaction transaction = Transaction.openOuter()) {
            for (Storage<ItemVariant> storage : getItemHandlers(level)) {
                for (StorageView<ItemVariant> view : storage.nonEmptyViews()) {
                    ItemVariant variant = view.getResource();
                    ItemStack stack = variant.toStack(1);
                    if (fuelRemaining > 0 && TypeRodPredicate.isFuel(stack, level)) {
                        fuelRemaining -= view.extract(variant, Math.min(fuelRemaining, view.getAmount()), transaction);
                    } else if (coolerRemaining > 0 && TypeRodPredicate.isCooled(stack, level)) {
                        coolerRemaining -= view.extract(variant, Math.min(coolerRemaining, view.getAmount()), transaction);
                    }
                    if (fuelRemaining == 0 && coolerRemaining == 0) {
                        transaction.commit();
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean extractItemByName(Level level, String itemName) {
        if (level == null || itemName == null) return false;
        try (Transaction transaction = Transaction.openOuter()) {
            for (Storage<ItemVariant> storage : getItemHandlers(level)) {
                for (StorageView<ItemVariant> view : storage.nonEmptyViews()) {
                    String path = BuiltInRegistries.ITEM.getKey(view.getResource().getItem()).getPath();
                    if (isMatching(path, itemName)
                            && view.extract(view.getResource(), 1, transaction) == 1) {
                        transaction.commit();
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean isMatching(String registryPath, String configName) {
        return registryPath.replace("_", "").equalsIgnoreCase(configName.replace("_", ""));
    }

    @Override
    public void clearInvalid(Level level) {
        if (level == null) {
            positions.clear();
            return;
        }
        positions.removeIf(pos -> TransferUtil.getItemStorage(level, pos, null) == null);
    }

    @Override
    public List<BlockPos> getBlocksPosition(Level level) {
        if (level == null) return List.of();
        return positions.stream()
                .filter(pos -> level.getBlockEntity(pos) instanceof ReactorRodInputEntity)
                .toList();
    }
}
