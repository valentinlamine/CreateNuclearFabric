package net.nuclearteam.createnuclear.entity.irradiatedbee;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.nuclearteam.createnuclear.CreateNuclear;
import net.nuclearteam.createnuclear.entity.CNModelLayers;

public class IrradiatedBeeRenderer extends MobRenderer<IrradiatedBee, IrradiatedBeeModel<IrradiatedBee>> {
    private static final ResourceLocation IRRADIATED_BEE_LOCATION = CreateNuclear.asResource("textures/entity/irradiated_bee.png");

    public IrradiatedBeeRenderer(EntityRendererProvider.Context context) {
        super(context, new IrradiatedBeeModel<>(context.bakeLayer(CNModelLayers.IRRADIATED_BEE)), .4f);
    }

    @Override
    public ResourceLocation getTextureLocation(IrradiatedBee entity) {
        /*if (entity.isAngry()) {
            if (entity.hasNectar()) {
                return ANGRY_NECTAR_IRRADIATED_BEE_LOCATION;
            }
            return ANGRY_IRRADIATED_BEE_LOCATION;
        }
        if (entity.hasNectar()) {
            return NECTAR_IRRADIATED_BEE_LOCATION;
        }*/
        return IRRADIATED_BEE_LOCATION;
    }
}
