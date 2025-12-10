package net.nuclearteam.createnuclear.content.contraptions.irradiated.cow;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.nuclearteam.createnuclear.CreateNuclear;
import net.nuclearteam.createnuclear.content.contraptions.irradiated.CNModelLayers;

import javax.annotation.ParametersAreNonnullByDefault;

@Environment(EnvType.CLIENT)
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class IrradiatedCowRenderer extends MobRenderer<IrradiatedCow, IrradiatedCowModel<IrradiatedCow>> {
    private static final ResourceLocation IRRADIATED_COW_LOCATION = new ResourceLocation(CreateNuclear.MOD_ID, "textures/entity/irradiated_cow.png");

    public IrradiatedCowRenderer(EntityRendererProvider.Context context) {
        super(context, new IrradiatedCowModel<>(context.bakeLayer(CNModelLayers.IRRADIATED_COW)), 0.3F);
    }

    @Override
    public ResourceLocation getTextureLocation(final IrradiatedCow entity) {
        return IRRADIATED_COW_LOCATION;
    }

    @Override
    public void render(final IrradiatedCow entity, final float entityYaw, final float partialTicks, final PoseStack matrixStack, final MultiBufferSource buffer, final int packedLight) {
        final float scale = entity.isBaby() ? 0.5F : 1.0F;
        if (scale != 1.0F) {
            matrixStack.scale(scale, scale, scale);
        }

        super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
    }
}