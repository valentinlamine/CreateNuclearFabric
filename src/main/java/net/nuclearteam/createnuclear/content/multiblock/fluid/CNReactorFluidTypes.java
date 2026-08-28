package net.nuclearteam.createnuclear.content.multiblock.fluid;

import net.nuclearteam.createnuclear.CNFluids;
import net.nuclearteam.createnuclear.CreateNuclear;
import net.nuclearteam.createnuclear.api.CreateNuclearRegistries;
import net.nuclearteam.createnuclear.api.multiblock.fluid.ReactorFluidType;

import static net.nuclearteam.createnuclear.api.ReactorFluidTypesValue.DEFAULT_REACTOR_FLUID_TYPE;

import net.minecraft.world.level.material.Fluids;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;

public class CNReactorFluidTypes {
    /**
     * Static registry of reactor fluid types ({@link ReactorFluidType}) for the CreateNuclear mod.
     *
     * <p>This class exposes the {@link ResourceKey}s and the {@code bootstrap}
     * method invoked by the datapack system to register reactor fluid types used
     * by the multiblock (for example, specific fluids with heat/efficiency
     * characteristics). The example below shows how to create a
     * {@code ReactorFluidType} via its {@code ReactorFluidType.Builder}.</p>
     *
     * <p>Example — Custom Reactor Fluid:</p>
     * <pre>{@code
     * register(ctx, "name", new ReactorFluidType.Builder()
     *             .maxHeat(int)
     *             .efficiency(int)
     *             .fluid(Fluid)
     *             .build());
     * }</pre>
     *
     * @see net.nuclearteam.createnuclear.api.multiblock.fluid.ReactorFluidType
     * @see net.nuclearteam.createnuclear.api.CreateNuclearRegistries
     */
    public static void bootstrap(BootstapContext<ReactorFluidType> ctx) {
        register(ctx, "fallback", DEFAULT_REACTOR_FLUID_TYPE);
        register(ctx, "water", new ReactorFluidType.Builder()
            .fluid(Fluids.WATER)
            .maxHeat(2048)
            .efficiency(1000)
            .build()
        );
        register(ctx, "nitrogen", new ReactorFluidType.Builder()
                .fluid(CNFluids.LIQUID_NITROGEN.getSource())
                .maxHeat(8196)
                .efficiency(100)
                .build()
        );
    }

    private static void register(BootstapContext<ReactorFluidType> ctx, String name, ReactorFluidType type) {
        ctx.register(ResourceKey.create(CreateNuclearRegistries.FLUID_TYPE, CreateNuclear.asResource(name)), type);
    }
}
