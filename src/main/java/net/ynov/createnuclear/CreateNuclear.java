package net.ynov.createnuclear;

import net.fabricmc.api.ModInitializer;
import net.ynov.createnuclear.block.ModBlocks;
import net.ynov.createnuclear.item.ModItems;
import net.ynov.createnuclear.item.ModItemsGroups;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CreateNuclear implements ModInitializer {
	public static final String MOD_ID = "createnuclear";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItems.registerModItems();
		ModBlocks.registerModBlocks();
		ModItemsGroups.registerItemGroups();
	}
}