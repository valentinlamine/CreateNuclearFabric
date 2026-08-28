package net.nuclearteam.createnuclear.infrastructure.data;

import io.github.fabricators_of_create.porting_lib.data.DatapackBuiltinEntriesProvider;
import net.minecraft.data.CachedOutput;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.HolderLookup;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.nuclearteam.createnuclear.CNDamageTypes;
import net.nuclearteam.createnuclear.CreateNuclear;
import net.nuclearteam.createnuclear.api.CreateNuclearRegistries;
import net.nuclearteam.createnuclear.content.multiblock.fluid.CNReactorFluidTypes;
import net.nuclearteam.createnuclear.content.multiblock.rod.CNRodTypes;
import net.nuclearteam.createnuclear.infrastructure.worldgen.CNConfiguredFeatures;
import net.nuclearteam.createnuclear.infrastructure.worldgen.CNPlacedFeatures;
import net.nuclearteam.createnuclear.infrastructure.worldgen.biome.CNBiomes;
import net.nuclearteam.createnuclear.infrastructure.worldgen.biome.CNDensityFunctions;
import net.nuclearteam.createnuclear.infrastructure.worldgen.biome.CNNoiseData;
import net.nuclearteam.createnuclear.infrastructure.worldgen.biome.CNNoiseGeneratorSettings;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unused")
@MethodsReturnNonnullByDefault
public class GeneratedEntriesProvider extends DatapackBuiltinEntriesProvider {

    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
        .addRegistry(Registries.DAMAGE_TYPE, CNDamageTypes::bootstrap)
        .addRegistry(Registries.CONFIGURED_FEATURE, CNConfiguredFeatures::bootstrap)
        .addRegistry(Registries.PLACED_FEATURE, CNPlacedFeatures::bootstrap)
        .addRegistry(Registries.BIOME, CNBiomes::bootstrapRegistries)
        .addRegistry(Registries.DENSITY_FUNCTION, CNDensityFunctions::bootstrapRegistries)
        .addRegistry(Registries.CHUNK_GENERATOR_SETTINGS, CNNoiseGeneratorSettings::bootstrapRegistries)
        .addRegistry(Registries.NOISE_PARAMETERS, CNNoiseData::bootstrapRegistries)
        .addRegistry(CreateNuclearRegistries.ROD_TYPE, CNRodTypes::bootstrap)
        .addRegistry(CreateNuclearRegistries.FLUID_TYPE, CNReactorFluidTypes::bootstrap)
    ;

    public GeneratedEntriesProvider(CachedOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(CreateNuclear.MOD_ID));
    }

    @Override
    public String getName() {
        return "CreateNuclear Generated Registry Entries";
    }
}
