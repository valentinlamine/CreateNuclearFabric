package net.ynov.createnuclear;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.simibubi.create.Create;
import com.simibubi.create.compat.archEx.ArchExCompat;
import com.simibubi.create.foundation.data.DamageTypeTagGen;
import com.simibubi.create.foundation.data.TagLangGen;
import com.simibubi.create.foundation.utility.FilesHelper;
import com.simibubi.create.infrastructure.data.CreateDatagen;
import com.simibubi.create.infrastructure.data.GeneratedEntriesProvider;
import com.simibubi.create.infrastructure.ponder.AllPonderTags;
import com.simibubi.create.infrastructure.ponder.GeneralText;
import com.simibubi.create.infrastructure.ponder.PonderIndex;
import com.simibubi.create.infrastructure.ponder.SharedText;
import com.tterrag.registrate.providers.ProviderType;
import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;
import net.ynov.createnuclear.datagen.CNGeneratedEntriesProvider;
import net.ynov.createnuclear.datagen.CNWorldGenerator;
import net.ynov.createnuclear.ponder.CNPonderIndex;
import com.simibubi.create.foundation.ponder.PonderLocalization;


import java.util.Map;
import java.util.function.BiConsumer;

public class CreateNuclearDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		ExistingFileHelper helper = ExistingFileHelper.withResourcesFromArg();
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		CreateNuclear.REGISTRATE.setupDatagen(pack, helper);
		getherData(pack, helper);
	}

	@Override
	public void buildRegistry(RegistrySetBuilder registryBuilder) {
		//registryBuilder.add(Registries.CONFIGURED_FEATURE, CNConfiguredFeatures::boostrap);
		//registryBuilder.add(Registries.PLACED_FEATURE, CNPlacedFeatures::boostrap);
	}

	public static void getherData(FabricDataGenerator.Pack pack, ExistingFileHelper existingFileHelper) {
		addExtraRegistrateData();

		//ArchExCompat.init(pack);

		pack.addProvider(CNGeneratedEntriesProvider::new);
	}

	private static void addExtraRegistrateData() {
		CreateNuclear.REGISTRATE.addDataGenerator(ProviderType.LANG, provider -> {
			BiConsumer<String, String> langConsummer = provider::add;

			provideDefaultLang("interface", langConsummer);
			provideDefaultLang("potion", langConsummer);
			providePonderLang(langConsummer);
		});
	}

	private static void provideDefaultLang(String fileName, BiConsumer<String, String> consumer) {
		String path = "assets/createnuclear/lang/default/" + fileName + ".json";
		JsonElement jsonElement = FilesHelper.loadJsonResource(path);
		if (jsonElement == null) throw  new IllegalStateException(String.format("Could not find default lang file: %s", path));

		JsonObject jsonObject = jsonElement.getAsJsonObject();
		for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue().getAsString();
			consumer.accept(key, value);
		}
	}

	private static void providePonderLang(BiConsumer<String, String> consumer) {
		CNPonderIndex.register();
		PonderLocalization.generateSceneLang();
		PonderLocalization.provideLang(CreateNuclear.MOD_ID, consumer);

	}
}