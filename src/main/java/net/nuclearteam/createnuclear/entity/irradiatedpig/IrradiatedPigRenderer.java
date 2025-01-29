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

@Environment(EnvType.CLIENT)
public class IrradiatedPigRenderer extends MobRenderer<IrradiatedPig, IrradiatedPigModel<IrradiatedPig>> {
    private static final ResourceLocation IRRADIATED_PIG_LOCATION = new ResourceLocation(CreateNuclear.MOD_ID, "textures/entity/irradiated_pig.png");

    public IrradiatedPigRenderer(EntityRendererProvider.Context context) {
        super(context, new IrradiatedPigModel(context.bakeLayer(ModelLayers.PIG)), 0.7F);
        this.addLayer(new SaddleLayer(this, new IrradiatedPigModel(context.bakeLayer(ModelLayers.PIG_SADDLE)), new ResourceLocation("textures/entity/pig/pig_saddle.png")));
    }

    public ResourceLocation getTextureLocation(IrradiatedPig entity) {
        return IRRADIATED_PIG_LOCATION;
    }
}
