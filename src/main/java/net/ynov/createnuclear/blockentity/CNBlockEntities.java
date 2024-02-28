package net.ynov.createnuclear.blockentity;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.schematics.table.SchematicTableBlockEntity;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import net.ynov.createnuclear.CreateNuclear;
import net.ynov.createnuclear.block.CNBlocks;
import net.ynov.createnuclear.content.reactor.controller.ReactorControllerBlockEntity;
public class CNBlockEntities {
    public static final BlockEntityEntry<ReactorControllerBlockEntity> REACTOR_CONTROLLER =
            CreateNuclear.REGISTRATE.blockEntity("reactor_controller", ReactorControllerBlockEntity::new)
            .validBlocks(CNBlocks.REACTOR_CONTROLLER)
            .register();

    public static void register() {
    }
}
