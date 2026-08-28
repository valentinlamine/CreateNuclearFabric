package net.nuclearteam.createnuclear.api;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.nuclearteam.createnuclear.CreateNuclear;
import net.nuclearteam.createnuclear.api.multiblock.fluid.ReactorFluidType;
import net.nuclearteam.createnuclear.api.multiblock.rods.RodType;


public class CreateNuclearRegistries {
    public static final ResourceKey<Registry<RodType>> ROD_TYPE = key("rods/type");
    public static final ResourceKey<Registry<ReactorFluidType>> FLUID_TYPE = key("fluids/type");

    public static final ResourceKey<RodType> FALLBACK_ROD = ResourceKey.create(CreateNuclearRegistries.ROD_TYPE, CreateNuclear.asResource("fallback"));
    public static final ResourceKey<ReactorFluidType> FALLBACK_FLUID = ResourceKey.create(CreateNuclearRegistries.FLUID_TYPE, CreateNuclear.asResource("fallback"));

    private static <T> ResourceKey<Registry<T>> key(String name) {
        return ResourceKey.createRegistryKey(CreateNuclear.asResource(name));
    }
}
