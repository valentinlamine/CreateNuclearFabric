package net.nuclearteam.createnuclear;

import com.mojang.logging.LogUtils;
import com.simibubi.create.content.equipment.goggles.GogglesItem;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipModifier;
import net.createmod.catnip.lang.FontHelper;
import net.fabricmc.api.ModInitializer;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.entity.EntityType;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.nuclearteam.createnuclear.content.decoration.palettes.CNPaletteBlocks;
import net.nuclearteam.createnuclear.content.contraptions.irradiated.IrradiatedAnimal;
import net.nuclearteam.createnuclear.content.equipment.armor.AntiRadiationArmorItem.IGoggleHelmet;
import net.nuclearteam.createnuclear.content.kinetics.fan.processing.CNFanProcessingTypes;
import net.nuclearteam.createnuclear.content.radiation.CNRadiationValues;
import net.nuclearteam.createnuclear.foundation.advancement.CNAdvancement;
import net.nuclearteam.createnuclear.foundation.advancement.CNTriggers;
import net.nuclearteam.createnuclear.foundation.data.CreateNuclearRegistrate;
import net.nuclearteam.createnuclear.foundation.events.CNCommonEvents;
import net.nuclearteam.createnuclear.foundation.item.RodsStats;
import net.nuclearteam.createnuclear.infrastructure.config.CNConfigs;
import net.nuclearteam.createnuclear.infrastructure.worldgen.CNBiomeModifiers;
import net.nuclearteam.createnuclear.infrastructure.worldgen.CNPlacementModifiers;
import net.nuclearteam.createnuclear.impl.registry.CreateNuclearRegistriesImpl;
import org.slf4j.Logger;


public class CreateNuclear implements ModInitializer {
	public static final String MOD_ID = "createnuclear";
    public static final String NAME = "Create Nuclear";

    public static final Logger LOGGER = LogUtils.getLogger();

    private static final StackWalker STACK_WALKER = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);

    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MOD_ID)
        .defaultCreativeTab((ResourceKey<CreativeModeTab>) null)
        .setTooltipModifierFactory(item ->
            new ItemDescription.Modifier(item, FontHelper.Palette.STANDARD_CREATE)
                .andThen(TooltipModifier.mapNull(KineticStats.create(item)))
                .andThen(RodsStats.create(item))
        );

    public static final CreateNuclearRegistrate POTION_REGISTRATE = CreateNuclearRegistrate.create(MOD_ID);


	@Override
	public void onInitialize() {
		LOGGER.info("{} initializing!", NAME);

		CNSoundEvents.prepare();
		CNSoundEvents.register();
		CNDisplaySources.register();
		CNTags.register();
		CNAttributes.register();
		CNParticleTypes.register();
		CNParticleRegistry.register();

		CNCreativeModeTabs.register();
		CNBlocks.register();
		CNItems.register();
		CNFluids.register();
		CNPaletteBlocks.register();
		CNMenus.register();
		CNEntityType.register();
		CNBlockEntityTypes.register();
		CNRecipeTypes.register();

		CNEffects.register();
		CNPotions.init();

		REGISTRATE.register();
		POTION_REGISTRATE.register();

		CNPackets.registerPackets();
		CNPlacementModifiers.register();

		CNConfigs.register();

		CreateNuclear.init();
		CreateNuclear.onRegister();

		CNCommonEvents.register();
		CNPackets.getChannel().initServerListener();
		CNBiomeModifiers.bootstrap();

		CreateNuclearRegistriesImpl.registerDatapackRegistries();

	}

	public static void init() {
		CNFluids.registerFluidInteractions();
		CNPotions.registerPotionRecipes();
		CNOpenPipeEffectHandlers.registerDefaults();
		CNRadiationValues.register();
		IrradiatedAnimal.VANILLA_TO_IRRADIATED.put(EntityType.CHICKEN, CNEntityType.IRRADIATED_CHICKEN.get());
		GogglesItem.addIsWearingPredicate(IGoggleHelmet::isGoggleHelmet);
		CNAdvancement.register();
		CNTriggers.register();
	}

	public static void onRegister() {
		CNFanProcessingTypes.register();
	}

	public static ResourceLocation asResource(String path) {
		return new ResourceLocation(MOD_ID, path);
	}

    public static CreateRegistrate registrate() {
        if (!STACK_WALKER.getCallerClass().getPackageName().startsWith("net.nuclearteam.createnuclear"))
            throw new UnsupportedOperationException("Other mods are not permitted to use createnuclear's registrate instance.");
        return REGISTRATE;
    }
}
