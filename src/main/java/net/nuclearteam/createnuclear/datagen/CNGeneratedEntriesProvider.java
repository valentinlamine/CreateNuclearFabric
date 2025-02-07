package net.nuclearteam.createnuclear.datagen;

import io.github.fabricators_of_create.porting_lib.data.DatapackBuiltinEntriesProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.nuclearteam.createnuclear.CreateNuclear;
import net.nuclearteam.createnuclear.world.CNConfiguredFeatures;
import net.nuclearteam.createnuclear.world.CNPlacedFeatures;
import net.nuclearteam.createnuclear.world.biome.CNBiomeData;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class CNGeneratedEntriesProvider extends DatapackBuiltinEntriesProvider {

    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.CONFIGURED_FEATURE, CNConfiguredFeatures::boostrap)
            .add(Registries.PLACED_FEATURE, CNPlacedFeatures::boostrap)
            .add(Registries.BIOME, CNBiomeData::bootstrap)
            ;

    public CNGeneratedEntriesProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(CreateNuclear.MOD_ID));
    }

    @Override
    public String getName() {
        return "CreateNuclear Generated Registry Entries";
    }
}
