package net.ynov.createnuclear.blockentity;

import com.simibubi.create.content.kinetics.gearbox.GearboxInstance;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.ynov.createnuclear.CreateNuclear;
import net.ynov.createnuclear.block.CNBlocks;
import net.ynov.createnuclear.groups.CNGroup;

public class CNBlocksEntity {
    static {
        CreateNuclear.REGISTRATE.useCreativeTab(CNGroup.MAIN_KEY);
    }

    public static final BlockEntityEntry<ReinforcedGlassEntity> REINFORCED_GLASS_ENTITY = CreateNuclear.REGISTRATE
            .blockEntity("reinforced_glass_entity", ReinforcedGlassEntity::new)
            .validBlocks(CNBlocks.REINFORCED_GLASS)
            .register();


    public static void registerCNBlocksEntity() {
        CreateNuclear.LOGGER.info("Registering ModBlocksEntity for " + CreateNuclear.MOD_ID);

    }
}
