package net.nuclearteam.createnuclear;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.item.TooltipModifier;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.resources.ResourceLocation;
import net.nuclearteam.createnuclear.advancement.CNAdvancement;
import net.nuclearteam.createnuclear.advancement.CNTriggers;
import net.nuclearteam.createnuclear.block.CNBlocks;
import net.nuclearteam.createnuclear.blockentity.CNBlockEntities;
import net.nuclearteam.createnuclear.effects.CNEffects;
import net.nuclearteam.createnuclear.entity.CNMobDefaultAttribute;
import net.nuclearteam.createnuclear.entity.CNMobEntityType;
import net.nuclearteam.createnuclear.fan.CNFanProcessingTypes;
import net.nuclearteam.createnuclear.fan.CNRecipeTypes;
import net.nuclearteam.createnuclear.fluid.CNFluids;
import net.nuclearteam.createnuclear.groups.CNGroup;
import net.nuclearteam.createnuclear.item.CNItems;
import net.nuclearteam.createnuclear.menu.CNMenus;
import net.nuclearteam.createnuclear.packets.CNPackets;
import net.nuclearteam.createnuclear.potion.CNPotions;
import net.nuclearteam.createnuclear.tags.CNTag;
import net.nuclearteam.createnuclear.world.gen.CNWorldGeneration;

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

	@Override
	public void onInitialize() {
		CNEffects.register();
		CNItems.registerCNItems();
		CNBlocks.registerCNBlocks();
		CNMenus.register();
		CNBlockEntities.register();
		CNGroup.registrer();
		CNFluids.register();
		CNTag.registerModItems();
		CNPackets.registerPackets();
		CNPackets.getChannel().initServerListener();
		CNPotions.init();
		CNWorldGeneration.generateModWorldGen();
		CNMobDefaultAttribute.register();
		CNMobEntityType.registerCNMod();

		REGISTRATE.register();
		POTION_REGISTRATE.register();
		CNRecipeTypes.register();
		CNPotions.registerPotionRecipes();

		CNAdvancement.register();
		CNTriggers.register();

		CNFanProcessingTypes.register();
		ServerTickEvents.START_WORLD_TICK.register(CNFluids::handleFluidEffect);

	}

	public static ResourceLocation asResource(String path) {
		return new ResourceLocation(MOD_ID, path);
	}

}