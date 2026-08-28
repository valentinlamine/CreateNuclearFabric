package net.nuclearteam.createnuclear.impl.registry;

import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.nuclearteam.createnuclear.api.CreateNuclearRegistries;
import net.nuclearteam.createnuclear.api.multiblock.fluid.ReactorFluidType;
import net.nuclearteam.createnuclear.api.multiblock.rods.RodType;
import org.jetbrains.annotations.ApiStatus.Internal;

public class CreateNuclearRegistriesImpl {
    @Internal
    public static void registerDatapackRegistries() {
        DynamicRegistries.registerSynced(CreateNuclearRegistries.ROD_TYPE, RodType.CODEC);
        DynamicRegistries.registerSynced(CreateNuclearRegistries.FLUID_TYPE, ReactorFluidType.CODEC);
    }
}
