package net.ynov.createnuclear;

import com.simibubi.create.AllPackets;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.item.TooltipModifier;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.resources.ResourceLocation;
//import net.ynov.createnuclear.block.CNBlockEntityType;
//import net.ynov.createnuclear.blockentity.CNBlockEntity;
import net.ynov.createnuclear.entity.CNMobEntityType;
import net.ynov.createnuclear.entity.irradiatedcat.IrradiatedCat;
import net.ynov.createnuclear.entity.irradiatedchicken.IrradiatedChicken;
import net.ynov.createnuclear.entity.irradiatedwolf.IrradiatedWolf;
import net.ynov.createnuclear.fan.CNFanProcessingTypes;
import net.ynov.createnuclear.fan.CNRecipeTypes;
import net.ynov.createnuclear.packets.CNPackets;
import net.ynov.createnuclear.tags.CNTag;
import net.ynov.createnuclear.block.CNBlocks;
import net.ynov.createnuclear.blockentity.CNBlockEntities;
import net.ynov.createnuclear.effects.CNEffects;
import net.ynov.createnuclear.fluid.CNFluids;
import net.ynov.createnuclear.groups.CNGroup;
import net.ynov.createnuclear.item.CNItems;
import net.ynov.createnuclear.menu.CNMenus;
import net.ynov.createnuclear.world.gen.CNWorldGeneration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CreateNuclear implements ModInitializer {
	public static final String MOD_ID = "createnuclear";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MOD_ID);



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

		CNWorldGeneration.generateModWorldGen();
		REGISTRATE.register();
		CNRecipeTypes.register();

		CNFanProcessingTypes.register();
		ServerTickEvents.START_WORLD_TICK.register(CNFluids::handleFluidEffect);
		FabricDefaultAttributeRegistry.register(CNMobEntityType.IRRADIATED_CHICKEN, IrradiatedChicken.createAttributes());
		FabricDefaultAttributeRegistry.register(CNMobEntityType.IRRADIATED_WOLF, IrradiatedWolf.createAttributes());
		FabricDefaultAttributeRegistry.register(CNMobEntityType.IRRADIATED_CAT, IrradiatedCat.createAttributes());
	}

	public static ResourceLocation asResource(String path) {
		return new ResourceLocation(MOD_ID, path);
	}


}