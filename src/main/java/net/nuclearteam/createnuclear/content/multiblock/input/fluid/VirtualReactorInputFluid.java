package net.nuclearteam.createnuclear.content.multiblock.input.fluid;

import io.github.fabricators_of_create.porting_lib.fluids.FluidStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.nuclearteam.createnuclear.content.logistics.BigFluidStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record VirtualReactorInputFluid(Map<ResourceLocation, Long> fluids) {
    public VirtualReactorInputFluid() {
        this(new HashMap<>());
    }

    public void addFluid(@NotNull FluidStack stack) {
        if (stack.isEmpty()) return;
        fluids.merge(BuiltInRegistries.FLUID.getKey(stack.getFluid()), stack.getAmount(), Long::sum);
    }

    public FluidStack removeFluid(@NotNull ResourceLocation fluidId, long amount) {
        if (amount <= 0) return FluidStack.EMPTY;
        long current = fluids.getOrDefault(fluidId, 0L);
        long removed = Math.min(current, amount);
        if (removed == 0) return FluidStack.EMPTY;
        long remaining = current - removed;
        if (remaining == 0) fluids.remove(fluidId);
        else fluids.put(fluidId, remaining);
        return new FluidStack(BuiltInRegistries.FLUID.get(fluidId), removed);
    }

    public long getAmount(@NotNull ResourceLocation fluidId) {
        return fluids.getOrDefault(fluidId, 0L);
    }

    public static List<BigFluidStack> toBigList(Map<ResourceLocation, Long> map) {
        List<BigFluidStack> list = new ArrayList<>();
        for (Map.Entry<ResourceLocation, Long> entry : map.entrySet()) {
            Fluid fluid = BuiltInRegistries.FLUID.get(entry.getKey());
            long amount = entry.getValue();
            if (fluid == null || amount <= 0) continue;
            list.add(new BigFluidStack(new FluidStack(fluid, amount), amount));
        }
        return list;
    }
}
