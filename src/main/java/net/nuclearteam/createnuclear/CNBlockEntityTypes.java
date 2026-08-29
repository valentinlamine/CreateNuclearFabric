package net.nuclearteam.createnuclear;

import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.OrientedRotatingVisual;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import net.nuclearteam.createnuclear.content.enriching.campfire.EnrichingCampfireBlockEntity;
import net.nuclearteam.createnuclear.content.multiblock.alarm.ReactorAlarmEntity;
import net.nuclearteam.createnuclear.content.multiblock.casing.ReactorCasingEntity;
import net.nuclearteam.createnuclear.content.multiblock.controller.ReactorControllerBlockEntity;
import net.nuclearteam.createnuclear.content.multiblock.core.ReactorCoreEntity;
import net.nuclearteam.createnuclear.content.multiblock.frame.ReactorFrameEntity;
import net.nuclearteam.createnuclear.content.multiblock.frame.ReactorFrameRenderer;
import net.nuclearteam.createnuclear.content.multiblock.input.item.ReactorRodInputEntity;
import net.nuclearteam.createnuclear.content.multiblock.input.fluid.ReactorFluidInputEntity;
import net.nuclearteam.createnuclear.content.multiblock.output.ReactorOutputEntity;
import net.nuclearteam.createnuclear.content.multiblock.output.ReactorOutputRenderer;

public class CNBlockEntityTypes {
    private static final CreateRegistrate REGISTRATE = CreateNuclear.registrate();

    public static final BlockEntityEntry<EnrichingCampfireBlockEntity> ENRICHING_CAMPFIRE_BLOCK =
            REGISTRATE.blockEntity("enriching_campfire_block", EnrichingCampfireBlockEntity::new)
                    .validBlock(CNBlocks.ENRICHING_CAMPFIRE)
                    .register();

    public static final BlockEntityEntry<ReactorCasingEntity> REACTOR_CASING =
            REGISTRATE.blockEntity("reactor_casing", ReactorCasingEntity::new)
                    .validBlocks(CNBlocks.REACTOR_CASING)
                    .register();

    public static final BlockEntityEntry<ReactorCoreEntity> REACTOR_CORE =
            REGISTRATE.blockEntity("reactor_core", ReactorCoreEntity::new)
                    .validBlocks(CNBlocks.REACTOR_CORE)
                    .register();

    public static final BlockEntityEntry<ReactorFrameEntity> REACTOR_FRAME =
            REGISTRATE.blockEntity("reactor_frame", ReactorFrameEntity::new)
                    .validBlocks(CNBlocks.REACTOR_FRAME)
                    .renderer(() -> ReactorFrameRenderer::new)
                    .register();

    public static final BlockEntityEntry<ReactorRodInputEntity> REACTOR_ROD_INPUT =
            REGISTRATE.blockEntity("reactor_rod_input", ReactorRodInputEntity::new)
                    .validBlocks(CNBlocks.REACTOR_ROD_INPUT)
                    .register();

    public static final BlockEntityEntry<ReactorFluidInputEntity> REACTOR_FLUID_INPUT =
            REGISTRATE.blockEntity("reactor_fluid_input", ReactorFluidInputEntity::new)
                    .validBlocks(CNBlocks.REACTOR_FLUID_INPUT)
                    .register();

    public static final BlockEntityEntry<ReactorOutputEntity> REACTOR_OUTPUT =
            REGISTRATE.blockEntity("reactor_output", ReactorOutputEntity::new)
                    .visual(() -> OrientedRotatingVisual.of(AllPartialModels.SHAFT_HALF), false)
                    .validBlocks(CNBlocks.REACTOR_OUTPUT)
                    .renderer(() -> ReactorOutputRenderer::new)
                    .register();

    public static final BlockEntityEntry<ReactorControllerBlockEntity> REACTOR_CONTROLLER =
            REGISTRATE.blockEntity("reactor_controller", ReactorControllerBlockEntity::new)
                    .validBlocks(CNBlocks.REACTOR_CONTROLLER)
                    .register();

    public static final BlockEntityEntry<ReactorAlarmEntity> REACTOR_ALARM =
            REGISTRATE.blockEntity("reactor_alarm", ReactorAlarmEntity::new)
                    .validBlocks(CNBlocks.REACTOR_ALARM)
                    .register();

    public static void register() {}
}
