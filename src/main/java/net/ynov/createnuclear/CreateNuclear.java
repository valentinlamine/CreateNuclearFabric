package net.ynov.createnuclear;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.simibubi.create.content.fluids.tank.BoilerData;
import com.simibubi.create.content.fluids.tank.BoilerHeaters;
import com.simibubi.create.foundation.blockEntity.IMultiBlockEntityContainer;
import com.simibubi.create.foundation.data.CreateRegistrate;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.client.model.WitherBossModel;
import net.minecraft.world.level.block.BeaconBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.ynov.createnuclear.Tags.CNTag;
import net.ynov.createnuclear.block.CNBlocks;
import net.ynov.createnuclear.blockentity.CNEntityTypes;
import net.ynov.createnuclear.effects.CNEffects;
import net.ynov.createnuclear.energy.ReactorOutputEntity;
import net.ynov.createnuclear.fluid.CNFluids;
import net.ynov.createnuclear.groups.CNGroup;
import net.ynov.createnuclear.item.CNItems;
import net.ynov.createnuclear.world.gen.CNWorldGeneration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Text;


public class CreateNuclear implements ModInitializer {
	public static final String MOD_ID = "createnuclear";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MOD_ID);


	@Override
	public void onInitialize() {
		CNItems.registerCNItems();
		CNBlocks.registerCNBlocks();
		CNEntityTypes.register();
		CNGroup.registrer();
		CNFluids.register();
		CNEffects.register();
		CNTag.registerModItems();
		CNWorldGeneration.generateModWorldGen();
		REGISTRATE.register();

		ServerTickEvents.START_WORLD_TICK.register(CNFluids::handleFluidEffect);
	}

}
