package net.nuclearteam.createnuclear.api.multiblock.fluid;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Holder.Reference;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import io.github.fabricators_of_create.porting_lib.fluids.FluidStack;
import net.nuclearteam.createnuclear.api.CreateNuclearRegistries;
import net.nuclearteam.createnuclear.api.ReactorFluidTypesValue;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Represents a reactor fluid type used by the mod's multiblock.
 * <p>
 * A {@code ReactorFluidType} holds the single fluid that identifies this
 * fluid type, along with its heat-related and efficiency values.
 */
public record ReactorFluidType(Holder<Fluid> fluid, int maxHeat, int efficiency) {

    /** Serialization codec for saving/loading {@link ReactorFluidType} instances. */
    public static final Codec<ReactorFluidType> CODEC = RecordCodecBuilder.create(i -> i.group(
            RegistryFixedCodec.create(Registries.FLUID).fieldOf("fluid").forGetter(ReactorFluidType::fluid),
            Codec.INT.fieldOf("maxHeat").forGetter(ReactorFluidType::maxHeat),
            Codec.INT.fieldOf("efficiency").forGetter(ReactorFluidType::efficiency)
    ).apply(i, ReactorFluidType::new));

    /**
     * Searches the {@code FLUID_TYPE} registry for a {@link ReactorFluidType}
     * matching the provided {@link Fluid}.
     *
     * @param registryAccess    registry access for the world
     * @param fluid             the fluid to look up
     * @return an {@link Optional} containing a {@link Reference} to the matching
     *         {@code ReactorFluidType} if found, otherwise {@link Optional#empty()}
     */
    public static Optional<Reference<ReactorFluidType>> getTypeForFluid(RegistryAccess registryAccess, Fluid fluid) {
        ResourceLocation fluidKey = BuiltInRegistries.FLUID.getKey(fluid);

        return registryAccess.lookup(CreateNuclearRegistries.FLUID_TYPE)
                .stream()
                .flatMap(HolderLookup.RegistryLookup::listElements)
                .filter(ref -> ref.value().fluid.unwrapKey()
                    .map(k -> k.location().equals(fluidKey))
                    .orElse(false))
                .findFirst();
    }

    /**
     * Resolves the {@code ReactorFluidType} for a given {@link Fluid}.
     * <p>
     * The resolution first checks the world's registries, then falls back to
     * {@link ReactorFluidTypesValue}, and finally returns the registry fallback
     * value if none is found.
     *
     * @param fluid the fluid whose reactor type should be resolved
     * @param world the level used to access registries
     * @return the resolved {@code ReactorFluidType} (never {@code null})
     */
    public static ReactorFluidType resolveReactorFluidType(Fluid fluid, Level world) {
        return ReactorFluidType.getTypeForFluid(world.registryAccess(), fluid)
                .map(Holder.Reference::value)
                .orElseGet(() -> {
                    ReactorFluidType fromFluid = ReactorFluidTypesValue.getReactorFluidType(fluid);
                    return fromFluid.isNotEmptyFluid()
                        ? fromFluid
                        : world.registryAccess()
                            .registryOrThrow(CreateNuclearRegistries.FLUID_TYPE)
                            .getHolderOrThrow(CreateNuclearRegistries.FALLBACK_FLUID)
                            .value();
                });
    }


    /**
     * Returns whether this {@code ReactorFluidType} has an associated fluid
     * (i.e. is not the empty/sentinel fluid).
     *
     * @return {@code true} if a real fluid is defined, {@code false} otherwise
     */
    public boolean isNotEmptyFluid() {
        return this.fluid.value() != Fluids.EMPTY;
    }

    /**
     * Fluent builder for creating an immutable {@link ReactorFluidType} instance.
     * <p>
     * Use the configuration methods and class {@link #build()} to obtain the
     * resulting instance.
     */
    public static class Builder {
        private Holder<Fluid> fluid = null;
        private int maxHeat = 0;
        private int efficiency = 0;
        private boolean maxHeatSet = false;
        private boolean efficiencySet = false;

        /**
         * Sets the maximum heat value for the built {@link ReactorFluidType}.
         *
         * @param maxHeat maximum heat value
         * @return this builder
         */
        public Builder maxHeat(int maxHeat) {
            this.maxHeat = maxHeat;
            this.maxHeatSet = true;
            return this;
        }

        /**
         * Sets the efficiency value for the built {@link ReactorFluidType}.
         *
         * @param efficiency efficiency value
         * @return this builder
         */
        public Builder efficiency(int efficiency) {
            this.efficiency = efficiency;
            this.efficiencySet = true;
            return this;
        }

        /**
         * Sets the fluid identifying this type from a {@link FluidStack}.
         *
         * @param fluidStack the fluid stack providing the fluid
         * @return this builder
         */
        public Builder fluid(FluidStack fluidStack) {
            return fluid(fluidStack.getFluid());
        }

        /**
         * Sets the fluid identifying this type. Calling this again replaces
         * the previously set fluid.
         *
         * @param fluid the fluid to associate with this type
         * @return this builder
         */
        public Builder fluid(Fluid fluid) {
            this.fluid = fluid.builtInRegistryHolder();
            return this;
        }

        /**
         * Builds the immutable {@link ReactorFluidType} instance.
         *
         * @throws IllegalStateException if required fields are missing
         * @return the created instance
         */
        public ReactorFluidType build() {
            List<String> missing = new ArrayList<>();
            if (fluid == null) missing.add("fluid");
            if (!maxHeatSet) missing.add("maxHeat");
            if (!efficiencySet) missing.add("efficiency");

            if (!missing.isEmpty())
                throw new IllegalStateException("Missing required ReactorFluidType fields: " + String.join(", ", missing));

            return new ReactorFluidType(fluid, maxHeat, efficiency);
        }
    }

    @Override
    public @NotNull String toString() {
        String fluidName = this.fluid.unwrapKey()
            .map(k -> k.location().toString())
            .orElseGet(() -> {
                ResourceLocation rl = BuiltInRegistries.FLUID.getKey(this.fluid.value());
                return rl != null ? rl.toString() : this.fluid.value().toString();
            });

        return "ReactorFluidType{fluid=" + fluidName + ", maxHeat=" + maxHeat() + ", efficiency=" + efficiency() + "}";
    }
}
