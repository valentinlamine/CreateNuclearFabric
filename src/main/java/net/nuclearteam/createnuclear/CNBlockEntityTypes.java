package net.nuclearteam.createnuclear;

import com.simibubi.create.content.kinetics.base.HalfShaftInstance;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.level.block.Block;
import net.nuclearteam.createnuclear.content.enriching.campfire.EnrichingCampfireBlockEntity;
import net.nuclearteam.createnuclear.content.multiblock.alarm.ReactorAlarmEntity;
import net.nuclearteam.createnuclear.content.multiblock.casing.ReactorCasingBlockEntity;
import net.nuclearteam.createnuclear.content.multiblock.controller.ReactorControllerBlockEntity;
import net.nuclearteam.createnuclear.content.multiblock.core.ReactorCoreBlockEntity;
import net.nuclearteam.createnuclear.content.multiblock.input.ReactorInputEntity;
import net.nuclearteam.createnuclear.content.multiblock.output.ReactorOutputEntity;
import net.nuclearteam.createnuclear.content.multiblock.output.ReactorOutputRenderer;
import net.nuclearteam.createnuclear.content.test.TestBlockEntity;

import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;


public class CNBlockEntityTypes {
    public static final BlockEntityEntry<ReactorControllerBlockEntity> REACTOR_CONTROLLER =
            CreateNuclear.REGISTRATE.blockEntity("reactor_controller", ReactorControllerBlockEntity::new)
            .validBlocks(CNBlocks.REACTOR_CONTROLLER)
            .register();

    public static final BlockEntityEntry<ReactorInputEntity> REACTOR_INPUT =
            CreateNuclear.REGISTRATE.blockEntity("reactor_input", ReactorInputEntity::new)
                    .validBlocks(CNBlocks.REACTOR_INPUT)
                    .register();

    public static final BlockEntityEntry<ReactorOutputEntity> REACTOR_OUTPUT =
            CreateNuclear.REGISTRATE.blockEntity("reactor_output", ReactorOutputEntity::new)
                    .instance(() -> HalfShaftInstance::new, false)
                    .validBlocks(CNBlocks.REACTOR_OUTPUT)
                    .renderer(() -> ReactorOutputRenderer::new)
                    .register();

        public static final BlockEntityEntry<ReactorAlarmEntity> REACTOR_ALARM =
            CreateNuclear.REGISTRATE.blockEntity("reactor_alarm", ReactorAlarmEntity::new)
                    .validBlocks(CNBlocks.REACTOR_ALARM)
                    .register();

    public static final BlockEntityEntry<EnrichingCampfireBlockEntity> ENRICHING_CAMPFIRE_BLOCK =
            CreateNuclear.REGISTRATE.blockEntity("enriching_campfire_block", EnrichingCampfireBlockEntity::new)
                    .validBlocks(CNBlocks.ENRICHING_CAMPFIRE)
                    .register();

    public static final BlockEntityEntry<ReactorCasingBlockEntity> REACTOR_CASING =
            CreateNuclear.REGISTRATE.blockEntity("reactor_casing", ReactorCasingBlockEntity::new)
                    .validBlocks(CNBlocks.REACTOR_CASING)
                    .register();

    public static final BlockEntityEntry<ReactorCoreBlockEntity> REACTOR_CORE =
            CreateNuclear.REGISTRATE.blockEntity("reactor_core", ReactorCoreBlockEntity::new)
                    .validBlocks(CNBlocks.REACTOR_CORE)
                    .register();

    public static void register() {
    }
}
