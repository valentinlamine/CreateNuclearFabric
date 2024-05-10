package net.ynov.createnuclear;

import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.ynov.createnuclear.datagen.CNWorldGenerator;
import net.ynov.createnuclear.world.CNConfiguredFeatures;
import net.ynov.createnuclear.world.CNPlacedFeatures;

public class CreateNuclearDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		ExistingFileHelper helper = ExistingFileHelper.withResourcesFromArg();
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		//CreateNuclear.REGISTRATE.setupDatagen(pack, helper);
		CreateNuclear.LOGGER.warn(System.getProperty("porting_lib.datagen.existing_resources") + " " + System.getProperty("porting_lib.datagen") + " " + System.getProperty("porting_lib"));
		//pack.addProvider(CNWorldGenerator::new);
	}

	@Override
	public void buildRegistry(RegistrySetBuilder registryBuilder) {
		//registryBuilder.add(Registries.CONFIGURED_FEATURE, CNConfiguredFeatures::boostrap);
		//registryBuilder.add(Registries.PLACED_FEATURE, CNPlacedFeatures::boostrap);
	}
}