package net.nuclearteam.createnuclear.api;

import com.simibubi.create.api.registry.SimpleRegistry;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.nuclearteam.createnuclear.api.multiblock.fluid.ReactorFluidType;

/**
 * Registry-backed accessors for {@link ReactorFluidType} mappings.
 * <p>
 * This class provides a simple registry that maps {@link Fluid} instances to
 * their corresponding {@link ReactorFluidType} definitions. It also exposes
 * helper methods that produce consumers suitable for registering reactor
 * fluid type information during mod initialization.
 */
public class ReactorFluidTypesValue {
    /**
     * Simple in-memory registry mapping fluids to {@link ReactorFluidType}.
     */
    public static final SimpleRegistry<Fluid, ReactorFluidType> REACTOR_FLUID_TYPE = SimpleRegistry.create();

    /**
     * Default {@link ReactorFluidType} returned when no mapping exists in the
     * registry. Constructed once to avoid allocating a new instance on each lookup.
     */
    public static final ReactorFluidType DEFAULT_REACTOR_FLUID_TYPE = new ReactorFluidType(Fluids.EMPTY.builtInRegistryHolder(), -1, -1);

    /**
     * Get the {@link ReactorFluidType} associated with the given {@link Fluid}.
     * If no mapping exists, a default empty {@link ReactorFluidType} is returned.
     *
     * @param fluid the fluid to look up (must not be null)
     * @return the associated {@link ReactorFluidType} or a default instance if none present
     * @throws NullPointerException if {@code fluid} is null
     */
    public static ReactorFluidType getReactorFluidType(Fluid fluid) {
        ReactorFluidType type = REACTOR_FLUID_TYPE.get(fluid);
        return type == null ? DEFAULT_REACTOR_FLUID_TYPE : type;
    }

    /**
     * Create a consumer that registers the given {@link ReactorFluidType}
     * builder for a fluid.
     * <p>
     * The returned consumer will call {@link ReactorFluidType.Builder#fluid(Fluid)}
     * with the provided fluid and then build and register the resulting
     * {@link ReactorFluidType} into the registry.
     *
     * @param type the reactor fluid type builder to register (must not be null)
     * @return a non-null consumer that registers the mapping
     */
    public static NonNullConsumer<Fluid> setReactorFluidTypeInfos(ReactorFluidType.Builder type) {
        return fluid -> REACTOR_FLUID_TYPE.register(fluid, type.fluid(fluid).build());
    }

    /**
     * Create a consumer that builds a {@link ReactorFluidType} using the
     * provided numeric parameters and registers it for the fluid passed to
     * the consumer.
     * <p>
     * The returned consumer will construct a builder pre-configured with the
     * provided {@code maxHeat} and {@code efficiency} values, then call
     * {@link ReactorFluidType.Builder#fluid(Fluid)} and register the built
     * instance for the supplied fluid.
     *
     * @param maxHeat maximum heat value to apply to the built type
     * @param efficiency efficiency value to apply to the built type
     * @return a non-null consumer that registers the built {@link ReactorFluidType}
     */
    public static NonNullConsumer<Fluid> setReactorFluidTypeInfos(int maxHeat, int efficiency) {
        ReactorFluidType.Builder builder = new ReactorFluidType.Builder()
                .maxHeat(maxHeat)
                .efficiency(efficiency);

        return fluid -> {
            ReactorFluidType built = builder.fluid(fluid).build();
            REACTOR_FLUID_TYPE.register(fluid, built);
        };
    }

}
