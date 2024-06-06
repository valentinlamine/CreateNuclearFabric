package net.ynov.createnuclear.entity.irradiatedwolf;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;

public class IrradiatedWoldCollarLayer extends RenderLayer<IrradiatedWolf, IrradiatedWolfModel<IrradiatedWolf>> {
    private static final ResourceLocation WOLF_COLLAR_LOCATION = new ResourceLocation("textures/entity/wolf/wolf_collar.png");

    public IrradiatedWoldCollarLayer(RenderLayerParent<IrradiatedWolf, IrradiatedWolfModel<IrradiatedWolf>> renderer) {
        super(renderer);
    }

    @Override
    public void render(PoseStack matrixStack, MultiBufferSource buffer, int packedLight, IrradiatedWolf livingEntity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        if (livingEntity.isTame() && !livingEntity.isInvisible()) {
            float[] fs = livingEntity.getCollarColor().getTextureDiffuseColors();
            renderColoredCutoutModel(this.getParentModel(), WOLF_COLLAR_LOCATION, matrixStack, buffer, packedLight, livingEntity, fs[0], fs[1], fs[2]);
        }
    }
}
