
package net.nuclearteam.createnuclear.content.contraptions.irradiated.zombie;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.AbstractZombieRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.nuclearteam.createnuclear.CreateNuclear;
import net.nuclearteam.createnuclear.content.contraptions.irradiated.CNModelLayers;


public class IrradiatedZombieRenderer extends AbstractZombieRenderer<IrradiatedZombie, IrradiatedZombieModel<IrradiatedZombie>> {
    private static final ResourceLocation IRRADIATED_ZOMBIE_LOCATION = CreateNuclear.asResource("textures/entity/irradiated_zombie.png");

    public IrradiatedZombieRenderer(EntityRendererProvider.Context context) {
        super(context, new IrradiatedZombieModel<>(context.bakeLayer(CNModelLayers.IRRADIATED_ZOMBIE)), new IrradiatedZombieModel<>(context.bakeLayer(CNModelLayers.IRRADIATED_ZOMBIE)), new IrradiatedZombieModel<>(context.bakeLayer(CNModelLayers.IRRADIATED_ZOMBIE)));
    }

    @Override
    public ResourceLocation getTextureLocation(IrradiatedZombie pEntity) {
        return IRRADIATED_ZOMBIE_LOCATION;
    }

    protected void setupRotations(IrradiatedZombie pEntityLiving, PoseStack pPoseStack, float pAgeInTicks, float pRotationYaw, float pPartialTicks) {
        super.setupRotations(pEntityLiving, pPoseStack, pAgeInTicks, pRotationYaw, pPartialTicks);
        float f = pEntityLiving.getSwimAmount(pPartialTicks);
        if (f > 0.0F) {
            float f1 = -10.0F - pEntityLiving.getXRot();
            float f2 = Mth.lerp(f, 0.0F, f1);
            pPoseStack.rotateAround(Axis.XP.rotationDegrees(f2), 0.0F, pEntityLiving.getBbHeight() / 2.0F, 0.0F);
        }

    }

    @Override
    public void render(IrradiatedZombie pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        if (pEntity.isBaby()) {
            pPoseStack.scale(0.5F, 0.5F, 0.5F);
        }
        else {
            pPoseStack.scale(1F, 1F, 1F);
        }

        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }
}
