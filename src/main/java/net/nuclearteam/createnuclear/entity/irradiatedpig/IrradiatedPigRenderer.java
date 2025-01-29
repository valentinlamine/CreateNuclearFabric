package net.nuclearteam.createnuclear.entity.irradiatedpig;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.PigModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.SaddleLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Pig;

@Environment(EnvType.CLIENT)
public class IrradiatedPigRenderer extends MobRenderer<Pig, PigModel<Pig>> {
    private static final ResourceLocation PIG_LOCATION = new ResourceLocation("textures/entity/irradiated_pig.png");

    public IrradiatedPigRenderer(EntityRendererProvider.Context context) {
        super(context, new PigModel(context.bakeLayer(ModelLayers.PIG)), 0.7F);
        this.addLayer(new SaddleLayer(this, new PigModel(context.bakeLayer(ModelLayers.PIG_SADDLE)), new ResourceLocation("textures/entity/pig/pig_saddle.png")));
    }

    public ResourceLocation getTextureLocation(Pig entity) {
        return PIG_LOCATION;
    }
}
