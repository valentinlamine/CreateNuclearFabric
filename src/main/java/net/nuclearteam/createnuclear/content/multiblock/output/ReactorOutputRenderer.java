package net.nuclearteam.createnuclear.content.multiblock.output;

import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class ReactorOutputRenderer extends KineticBlockEntityRenderer<ReactorOutputEntity> {

    public ReactorOutputRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected SuperByteBuffer getRotatedModel(ReactorOutputEntity be, BlockState state) {
        return CachedBuffers.partialFacing(AllPartialModels.SHAFT_HALF, state);
    }

}