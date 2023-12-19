package net.ynov.createnuclear;

import com.simibubi.create.foundation.data.CreateRegistrate;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.ynov.createnuclear.Tags.CNTag;
import net.ynov.createnuclear.block.CNBlocks;
import net.ynov.createnuclear.effects.CNEffects;
import net.ynov.createnuclear.fluid.CNFluids;
import net.ynov.createnuclear.groups.CNGroup;
import net.ynov.createnuclear.item.CNItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CreateNuclear implements ModInitializer {
	public static final String MOD_ID = "createnuclear";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MOD_ID);




	@Override
	public void onInitialize() {
		CNItems.registerCNItems();
		CNBlocks.registerCNBlocks();
		CNGroup.registrer();
		CNFluids.register();
		CNEffects.register();
		CNTag.registerModItems();
		REGISTRATE.register();

		ServerTickEvents.START_WORLD_TICK.register(CNFluids::handleFluidEffect);



	}

}
