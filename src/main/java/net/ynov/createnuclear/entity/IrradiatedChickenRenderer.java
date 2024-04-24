package net.ynov.createnuclear.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.Chicken;

@Environment(EnvType.CLIENT)
public class IrradiatedChickenRenderer extends MobRenderer<Chicken, IrradiatedChickenModel<Chicken>> {
    private static final ResourceLocation CHICKEN_LOCATION = new ResourceLocation("textures/entity/IrradiatedChicken.png");

    public IrradiatedChickenRenderer(EntityRendererProvider.Context context) {
        super(context, new IrradiatedChickenModel<>(context.bakeLayer(CNModelLayers.IRRADIATED_CHICKEN)), 0.3F);
    }

    public ResourceLocation getTextureLocation(Chicken entity) {
        return CHICKEN_LOCATION;
    }

    protected float getBob(Chicken livingBase, float partialTicks) {
        float f = Mth.lerp(partialTicks, livingBase.oFlap, livingBase.flap);
        float g = Mth.lerp(partialTicks, livingBase.oFlapSpeed, livingBase.flapSpeed);
        return (Mth.sin(f) + 1.0F) * g;
    }
}
