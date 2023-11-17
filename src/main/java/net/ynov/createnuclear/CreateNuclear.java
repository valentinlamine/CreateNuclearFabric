package net.ynov.createnuclear;

import com.simibubi.create.foundation.data.CreateRegistrate;
import net.fabricmc.api.ModInitializer;
import net.ynov.createnuclear.block.ModBlocks;
import net.ynov.createnuclear.item.ModGroup;
import net.ynov.createnuclear.item.ModItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CreateNuclear implements ModInitializer {
	public static final String MOD_ID = "createnuclear";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MOD_ID);

	@Override
	public void onInitialize() {
		ModItems.registerModItems();
		ModBlocks.registerModBlocks();
		//ModItemsGroups.registerModItems();
		ModGroup.registrer();
	}
}