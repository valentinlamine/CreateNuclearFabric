package net.ynov.createnuclear;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.ynov.createnuclear.datagen.CNWorldGenerator;
import net.ynov.createnuclear.world.CNConfiguredFeatures;
import net.ynov.createnuclear.world.CNPlacedFeatures;
import org.apache.http.config.RegistryBuilder;

public class CreateNuclearDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

		pack.addProvider(CNWorldGenerator::new);
	}

	@Override
	public void buildRegistry(RegistrySetBuilder registryBuilder) {
		registryBuilder.add(Registries.CONFIGURED_FEATURE, CNConfiguredFeatures::boostrap);
		registryBuilder.add(Registries.PLACED_FEATURE, CNPlacedFeatures::boostrap);
	}
}