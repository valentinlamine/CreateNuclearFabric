package net.ynov.createnuclear.blockentity;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.kinetics.base.HalfShaftInstance;
import com.simibubi.create.content.kinetics.motor.CreativeMotorBlockEntity;
import com.simibubi.create.content.kinetics.motor.CreativeMotorRenderer;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import net.minecraft.client.renderer.blockentity.CampfireRenderer;
import net.ynov.createnuclear.CreateNuclear;
import net.ynov.createnuclear.block.CNBlocks;
import net.ynov.createnuclear.multiblock.ReactorOutput;
import net.ynov.createnuclear.multiblock.ReactorOutputEntity;
import net.ynov.createnuclear.multiblock.ReactorOutputRenderer;
import net.ynov.createnuclear.tools.EnrichingCampfireBlockEntity;

import static com.simibubi.create.Create.REGISTRATE;

public class CNEntityTypes {
    public static final BlockEntityEntry<ReactorOutputEntity> MOTOR2 = CreateNuclear.REGISTRATE
		.blockEntity("motor2", ReactorOutputEntity::new)
		.instance(() -> HalfShaftInstance::new, false)
		.validBlocks(CNBlocks.REACTOR_OUTPUT)
		.renderer(() -> ReactorOutputRenderer::new)
		.register();

	public static void register() {}
}
