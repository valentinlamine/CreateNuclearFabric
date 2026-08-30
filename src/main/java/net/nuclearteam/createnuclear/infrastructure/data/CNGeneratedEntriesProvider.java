package net.nuclearteam.createnuclear.infrastructure.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.HolderLookup;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.nuclearteam.createnuclear.CNDamageTypes;
import net.nuclearteam.createnuclear.api.CreateNuclearRegistries;
import net.nuclearteam.createnuclear.content.multiblock.fluid.CNReactorFluidTypes;
import net.nuclearteam.createnuclear.content.multiblock.rod.CNRodTypes;
import net.nuclearteam.createnuclear.infrastructure.worldgen.CNConfiguredFeatures;
import net.nuclearteam.createnuclear.infrastructure.worldgen.CNPlacedFeatures;
import net.nuclearteam.createnuclear.infrastructure.worldgen.biome.CNBiomes;
import net.nuclearteam.createnuclear.infrastructure.worldgen.biome.CNDensityFunctions;
import net.nuclearteam.createnuclear.infrastructure.worldgen.biome.CNNoiseData;
import net.nuclearteam.createnuclear.infrastructure.worldgen.biome.CNNoiseGeneratorSettings;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unused")
@MethodsReturnNonnullByDefault
public class CNGeneratedEntriesProvider extends FabricDynamicRegistryProvider {

    public CNGeneratedEntriesProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void configure(HolderLookup.Provider registries, Entries entries) {
        entries.addAll(registries.lookupOrThrow(Registries.DAMAGE_TYPE));
        entries.addAll(registries.lookupOrThrow(Registries.CONFIGURED_FEATURE));
        entries.addAll(registries.lookupOrThrow(Registries.PLACED_FEATURE));
        entries.addAll(registries.lookupOrThrow(Registries.BIOME));
        entries.addAll(registries.lookupOrThrow(Registries.DENSITY_FUNCTION));
        entries.addAll(registries.lookupOrThrow(Registries.NOISE_SETTINGS));
        entries.addAll(registries.lookupOrThrow(Registries.NOISE));
        entries.addAll(registries.lookupOrThrow(CreateNuclearRegistries.ROD_TYPE));
        entries.addAll(registries.lookupOrThrow(CreateNuclearRegistries.FLUID_TYPE));
    }

    public static RegistrySetBuilder addBootstraps(RegistrySetBuilder builder) {
        return builder.add(Registries.DAMAGE_TYPE, CNDamageTypes::bootstrap)
            .add(Registries.CONFIGURED_FEATURE, CNConfiguredFeatures::bootstrap)
            .add(Registries.PLACED_FEATURE, CNPlacedFeatures::bootstrap)
            .add(Registries.BIOME, CNBiomes::bootstrapRegistries)
            .add(Registries.DENSITY_FUNCTION, CNDensityFunctions::bootstrapRegistries)
            .add(Registries.NOISE_SETTINGS, CNNoiseGeneratorSettings::bootstrapRegistries)
            .add(Registries.NOISE, CNNoiseData::bootstrapRegistries)
            .add(CreateNuclearRegistries.ROD_TYPE, CNRodTypes::bootstrap)
            .add(CreateNuclearRegistries.FLUID_TYPE, CNReactorFluidTypes::bootstrap);
    }

    @Override
    public String getName() {
        return "CreateNuclear Generated Registry Entries";
    }
}
