package net.nuclearteam.createnuclear;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.simibubi.create.foundation.utility.FilesHelper;
import com.tterrag.registrate.providers.ProviderType;
import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.nuclearteam.createnuclear.advancement.CNAdvancement;
import net.nuclearteam.createnuclear.compact.archEx.CNArchExCompat;
import net.nuclearteam.createnuclear.datagen.CNGeneratedEntriesProvider;
import net.nuclearteam.createnuclear.datagen.CNProcessingRecipeGen;
import net.nuclearteam.createnuclear.datagen.CNRegistrateTags;
import net.nuclearteam.createnuclear.datagen.recipe.cooking.CNCookingRecipeGen;
import net.nuclearteam.createnuclear.datagen.recipe.crafting.CNStandardRecipeGen;
import net.nuclearteam.createnuclear.datagen.recipe.mechanical_crafter.CNMechanicalCraftingRecipeGen;
import net.nuclearteam.createnuclear.datagen.recipe.shapeless.CNShapelessRecipeGen;
import net.nuclearteam.createnuclear.ponder.CNPonderIndex;

import com.simibubi.create.foundation.ponder.PonderLocalization;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;
import java.util.function.BiConsumer;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
@SuppressWarnings("unused")
public class CreateNuclearDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		ExistingFileHelper helper = ExistingFileHelper.withResourcesFromArg();
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		CreateNuclear.REGISTRATE.setupDatagen(pack, helper);
		gatherData(pack, helper);
		CNArchExCompat.init(pack);
	}

    public static void gatherData(FabricDataGenerator.Pack pack, ExistingFileHelper existingFileHelper) {
		addExtraRegistrateData();

		pack.addProvider(CNAdvancement::new);
		pack.addProvider(CNGeneratedEntriesProvider::new);
		pack.addProvider(CNProcessingRecipeGen::registerAll);
		pack.addProvider(CNStandardRecipeGen::new);
		pack.addProvider(CNCookingRecipeGen::new);
		pack.addProvider(CNMechanicalCraftingRecipeGen::new);
		pack.addProvider(CNShapelessRecipeGen::new);
	}

	private static void addExtraRegistrateData() {
		CNRegistrateTags.addGenerators();

		CreateNuclear.REGISTRATE.addDataGenerator(ProviderType.LANG, provider -> {
			BiConsumer<String, String> langConsumer = provider::add;

			provideDefaultLang("interface", langConsumer);
			provideDefaultLang("potion", langConsumer);
			provideDefaultLang("entity", langConsumer);
			provideDefaultLang("tooltips", langConsumer);
			provideDefaultLang("reactor", langConsumer);
			CNAdvancement.provideLang(langConsumer);
			providePonderLang(langConsumer);
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