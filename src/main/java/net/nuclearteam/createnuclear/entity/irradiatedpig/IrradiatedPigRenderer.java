package net.nuclearteam.createnuclear.entity.irradiatedpig;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.PigModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.SaddleLayer;
import net.minecraft.resources.ResourceLocation;
import net.nuclearteam.createnuclear.CreateNuclear;
import net.nuclearteam.createnuclear.entity.CNModelLayers;

@Environment(EnvType.CLIENT)
public class IrradiatedPigRenderer extends MobRenderer<IrradiatedPig, IrradiatedPigModel<IrradiatedPig>> {
    private static final ResourceLocation IRRADIATED_PIG_LOCATION = new ResourceLocation(CreateNuclear.MOD_ID, "textures/entity/irradiated_pig.png");

    public IrradiatedPigRenderer(EntityRendererProvider.Context context) {
        super(context, new IrradiatedPigModel<>(context.bakeLayer(CNModelLayers.IRRADIATED_PIG)), 0.7F);
    }

    public ResourceLocation getTextureLocation(IrradiatedPig entity) {
        return IRRADIATED_PIG_LOCATION;
    }
}
