package net.ynov.createnuclear.blockentity;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import net.ynov.createnuclear.CreateNuclear;
import net.ynov.createnuclear.block.CNBlocks;
import net.ynov.createnuclear.multiblock.controller.ReactorControllerBlockEntity;
import net.ynov.createnuclear.multiblock.frame.ReactorBlock;
import net.ynov.createnuclear.multiblock.frame.ReactorBlockEntity;
import net.ynov.createnuclear.multiblock.input.ReactorInputEntity;
//import net.ynov.createnuclear.multiblock.input.ReactorInputEntity;

public class CNBlockEntities {
    public static final BlockEntityEntry<ReactorControllerBlockEntity> REACTOR_CONTROLLER =
            CreateNuclear.REGISTRATE.blockEntity("reactor_controller", ReactorControllerBlockEntity::new)
            .validBlocks(CNBlocks.REACTOR_CONTROLLER)
            .register();

    public static final BlockEntityEntry<ReactorInputEntity> REACTOR_INPUT =
            CreateNuclear.REGISTRATE.blockEntity("test", ReactorInputEntity::new)
                    .validBlocks(CNBlocks.REACTOR_INPUT)
                    .register();

    public static final BlockEntityEntry<ReactorBlockEntity> REACTOR_BLOCK =
            CreateNuclear.REGISTRATE.blockEntity("reactor_casing", ReactorBlockEntity::new)
                    .validBlocks(CNBlocks.REACTOR_CASING)
                    .register();

    public static void register() {
    }
}
