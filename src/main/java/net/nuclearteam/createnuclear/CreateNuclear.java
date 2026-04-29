package net.nuclearteam.createnuclear;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.item.TooltipModifier;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.resources.ResourceLocation;

import net.nuclearteam.createnuclear.content.decoration.palettes.CNPaletteStoneTypes;
import net.nuclearteam.createnuclear.content.kinetics.fan.processing.CNFanProcessingTypes;
import net.nuclearteam.createnuclear.content.test.TestPropa;
import net.nuclearteam.createnuclear.content.multiblock.itemRods.BuiltinRodTypes;
import net.nuclearteam.createnuclear.foundation.advancement.CNAdvancement;
import net.nuclearteam.createnuclear.foundation.advancement.CNTriggers;
import net.nuclearteam.createnuclear.foundation.data.CreateNuclearRegistrate;
import net.nuclearteam.createnuclear.foundation.events.CommonEvents;
import net.nuclearteam.createnuclear.infrastructure.config.CNConfigs;
import net.nuclearteam.createnuclear.infrastructure.worldgen.CNBiomeModifiers;
import net.nuclearteam.createnuclear.infrastructure.worldgen.CNPlacementModifiers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CreateNuclear implements ModInitializer {
	public static final String MOD_ID = "createnuclear";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MOD_ID);
	public static final CreateNuclearRegistrate POTION_REGISTRATE = CreateNuclearRegistrate.create(MOD_ID);



	static {
		REGISTRATE.setTooltipModifierFactory(item -> new ItemDescription.Modifier(item, TooltipHelper.Palette.STANDARD_CREATE)
                .andThen(TooltipModifier.mapNull(KineticStats.create(item))));
	}

    public static final TestPropa TEST_PROPA = new TestPropa();

	@Override
	public void onInitialize() {
		CNEffects.register();
		CNItems.registerCNItems();
		CNBlocks.registerCNBlocks();
		CNPaletteStoneTypes.register(CreateNuclear.REGISTRATE);
		CNMenus.register();
		CNBlockEntityTypes.register();
		CNCreativeModeTabs.register();
		CNFluids.register();
		CNTags.register();
        CommonEvents.register();
		CNPackets.registerPackets();
		CNPackets.getChannel().initServerListener();
		CNPotions.init();
		CNEntityType.registerCNMod();

		REGISTRATE.register();
		POTION_REGISTRATE.register();

		CNParticleTypes.register();
		CNRecipeTypes.register();
		CNPlacementModifiers.register();

		CNConfigs.register();
		CNPotions.registerPotionRecipes();

		CNFluids.registerFluidInteractions();
        BuiltinRodTypes.register();

		CNAdvancement.register();
		CNTriggers.register();

        CNSounds.register();

		CNFanProcessingTypes.register();
		CNBiomeModifiers.bootstrap();
		ServerTickEvents.START_WORLD_TICK.register(CNFluids::handleFluidEffect);

	}

	public static ResourceLocation asResource(String path) {
		return new ResourceLocation(MOD_ID, path);
	}

}