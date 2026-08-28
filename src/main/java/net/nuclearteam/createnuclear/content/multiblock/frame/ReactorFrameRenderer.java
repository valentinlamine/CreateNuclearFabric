package net.nuclearteam.createnuclear.content.multiblock.frame;

import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import net.createmod.catnip.render.FluidRenderHelper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.fabricators_of_create.porting_lib.fluids.FluidStack;
import net.nuclearteam.createnuclear.content.multiblock.controller.ReactorControllerBlockEntity;
import net.nuclearteam.createnuclear.content.multiblock.controller.manager.ReactorFrameDisplayManagerI;

/**
 * Renders the reactor fluid dynamically inside the {@link ReactorFrame} window.
 * The fluid (texture + tint) is resolved from the owning reactor controller, so
 * the visible liquid reflects whichever fluid the reactor actually uses
 * (water, liquid nitrogen, ...) instead of a texture baked into the model.
 */
public class ReactorFrameRenderer extends SafeBlockEntityRenderer<ReactorFrameEntity> {
    private static final FluidRenderHelper<FluidStack> FLUID_RENDERER = new FluidRenderHelper<>();

    // Horizontal interior of the window (1..15 px on a 16 px block).
    private static final float X_MIN = 1f / 16f;
    private static final float X_MAX = 15f / 16f;
    private static final float Z_MIN = 1f / 16f;
    private static final float Z_MAX = 15f / 16f;

    public ReactorFrameRenderer(BlockEntityRendererProvider.Context context) { }

    @Override
    protected void renderSafe(ReactorFrameEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer,
                              int light, int overlay) {
        ReactorControllerBlockEntity controller = be.getControllerEntity();
        if (controller == null) return;

        ReactorFrameDisplayManagerI frameDisplay = controller.getFrameDisplayManager();

        FluidStack fluid = frameDisplay.getDisplayedFluid(controller.getLevel(), controller.getInputFluidManager());
        if (fluid == null || fluid.isEmpty()) return;

        // Local vertical bounds of the liquid volume inside this block, matching
        // what used to be baked into each frame part model
        // (frame_top / frame_middle / frame_bottom / frame_none).
        float boxYMin;
        float boxYMax;
        switch (be.getBlockState().getValue(ReactorFrame.PART)) {
            case START -> { boxYMin = 0f;          boxYMax = 9f / 16f; }
            case MIDDLE -> { boxYMin = 0f;         boxYMax = 1f; }
            case END -> { boxYMin = 4f / 16f;      boxYMax = 1f; }
            default -> { boxYMin = 2.9f / 16f;     boxYMax = 9.9f / 16f; }
        }

        // Clamp the liquid to the reactor's global fill level so the whole wall
        // shares one continuous surface that rises from the bottom as the input
        // fills. The level is mapped over the range that liquid actually occupies:
        // from the bottom frame's lip (frameMinY + 4/16) to the top frame's cap
        // (frameMaxY + 9/16), so even a nearly-empty reactor still shows a sliver.
        float yMax = boxYMax;
        if (frameDisplay.hasFrameColumn()) {
            float ratio = frameDisplay.getDisplayedFluidFillRatio(controller.getLevel(), controller.getInputFluidManager());
            double liquidBottomWorldY = frameDisplay.getFrameColumnMinY() + 4.0 / 16.0;
            double liquidTopWorldY = frameDisplay.getFrameColumnMaxY() + 9.0 / 16.0;
            double surfaceWorldY = liquidBottomWorldY + ratio * (liquidTopWorldY - liquidBottomWorldY);
            double localSurface = surfaceWorldY - be.getBlockPos().getY();
            if (localSurface <= boxYMin) return; // liquid level is below this block
            yMax = (float) Math.min(boxYMax, localSurface);
        }

        // Last arg (invertGasses) must be false: liquid nitrogen has density 0, so
        // it counts as "lighter than air" and would otherwise be flipped 180°,
        // hiding the top surface. We always fill bottom-to-top here.
        FLUID_RENDERER.renderFluidBox(fluid,
                X_MIN, boxYMin, Z_MIN, X_MAX, yMax, Z_MAX,
                buffer, ms, light, false, false);
    }
}
