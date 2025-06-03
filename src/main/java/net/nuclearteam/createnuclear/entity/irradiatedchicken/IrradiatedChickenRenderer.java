package net.nuclearteam.createnuclear.entity.irradiatedchicken;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.nuclearteam.createnuclear.CreateNuclear;
import net.nuclearteam.createnuclear.entity.CNModelLayers;

import javax.annotation.ParametersAreNonnullByDefault;

@Environment(EnvType.CLIENT)
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class IrradiatedChickenRenderer extends MobRenderer<IrradiatedChicken, IrradiatedChickenModel<IrradiatedChicken>> {
    private static final ResourceLocation IRRADIATED_CHICKEN_LOCATION = new ResourceLocation(CreateNuclear.MOD_ID, "textures/entity/irradiated_chicken.png");

    public IrradiatedChickenRenderer(EntityRendererProvider.Context context) {
        super(context, new IrradiatedChickenModel<>(context.bakeLayer(CNModelLayers.IRRADIATED_CHICKEN)), 0.3F);
    }

    @Override
    public ResourceLocation getTextureLocation(final IrradiatedChicken entity) {
        return IRRADIATED_CHICKEN_LOCATION;
    }

    @Override
    protected float getBob(final IrradiatedChicken entity, final float partialTicks) {
        final float flap = Mth.lerp(partialTicks, entity.oFlap, entity.flap);
        final float flapSpeed = Mth.lerp(partialTicks, entity.oFlapSpeed, entity.flapSpeed);
        return (Mth.sin(flap) + 1.0F) * flapSpeed;
    }

    @Override
    public void render(final IrradiatedChicken entity, final float entityYaw, final float partialTicks, final PoseStack matrixStack, final MultiBufferSource buffer, final int packedLight) {
        final float scale = entity.isBaby() ? 0.5F : 1.0F;
        if (scale != 1.0F) {
            matrixStack.scale(scale, scale, scale);
        }

        super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
    }
}