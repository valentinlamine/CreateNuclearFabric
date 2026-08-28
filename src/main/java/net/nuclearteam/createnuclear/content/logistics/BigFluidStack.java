package net.nuclearteam.createnuclear.content.logistics;

import io.github.fabricators_of_create.porting_lib.fluids.FluidStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.nuclearteam.createnuclear.api.ReactorFluidTypesValue;
import net.nuclearteam.createnuclear.api.multiblock.fluid.ReactorFluidType;

import javax.annotation.Nullable;
import java.util.Objects;

public class BigFluidStack {
    public static final long INF = Long.MAX_VALUE;

    public FluidStack stack;
    public long amount;

    public BigFluidStack(FluidStack stack) {
        this(stack, stack.getAmount());
    }

    public BigFluidStack(FluidStack stack, long amount) {
        this.stack = stack;
        this.amount = amount;
    }

    public CompoundTag write() {
        CompoundTag tag = new CompoundTag();
        tag.put("Fluid", stack.writeToNBT(new CompoundTag()));
        tag.putLong("Amount", amount);
        return tag;
    }

    public static BigFluidStack read(CompoundTag tag) {
        return new BigFluidStack(FluidStack.loadFluidStackFromNBT(tag.getCompound("Fluid")), tag.getLong("Amount"));
    }

    public void send(FriendlyByteBuf buffer) {
        stack.writeToPacket(buffer);
        buffer.writeVarLong(amount);
    }

    public static BigFluidStack receive(FriendlyByteBuf buffer) {
        return new BigFluidStack(FluidStack.readFromPacket(buffer), buffer.readVarLong());
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this
                || obj instanceof BigFluidStack other
                && Objects.equals(stack, other.stack)
                && amount == other.amount;
    }

    @Override
    public int hashCode() {
        return (Objects.hashCode(stack) * 31) ^ Long.hashCode(amount);
    }

    @Override
    public String toString() {
        return "(" + stack.getDisplayName().getString() + " x" + amount + ")";
    }

    public ReactorFluidType getFluidtype(@Nullable Level level) {
        if (level == null) return ReactorFluidTypesValue.getReactorFluidType(stack.getFluid());
        return ReactorFluidType.resolveReactorFluidType(stack.getFluid(), level);
    }
}
