package net.ynov.createnuclear.blockentity;

import com.simibubi.create.content.kinetics.base.HalfShaftInstance;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import net.ynov.createnuclear.CreateNuclear;
import net.ynov.createnuclear.block.CNBlocks;
import net.ynov.createnuclear.multiblock.energy.ReactorOutputEntity;
import net.ynov.createnuclear.multiblock.energy.ReactorOutputRenderer;

public class CNEntityTypes {
    public static final BlockEntityEntry<ReactorOutputEntity> MOTOR2 = CreateNuclear.REGISTRATE
		.blockEntity("motor2", ReactorOutputEntity::new)
		.instance(() -> HalfShaftInstance::new, false)
		.validBlocks(CNBlocks.REACTOR_OUTPUT)
		.renderer(() -> ReactorOutputRenderer::new)
		.register();

	public static void register() {}
}
