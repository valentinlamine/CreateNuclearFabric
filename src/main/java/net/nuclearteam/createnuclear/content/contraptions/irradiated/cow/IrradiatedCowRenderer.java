package net.nuclearteam.createnuclear.content.contraptions.irradiated.cow;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.nuclearteam.createnuclear.CreateNuclear;
import net.nuclearteam.createnuclear.content.contraptions.irradiated.CNModelLayers;

public class IrradiatedCowRenderer extends MobRenderer<IrradiatedCow, IrradiatedCowModel<IrradiatedCow>> {
    private static final ResourceLocation COW_LOCATION = CreateNuclear.asResource("textures/entity/irradiated_cow.png");

    public IrradiatedCowRenderer(EntityRendererProvider.Context context) {
        super(context, new IrradiatedCowModel<>(context.getPart(CNModelLayers.IRRADIATED_COW)), 0.7f);
    }

    /**
     * Returns the location of an entity's texture.
     */
    public ResourceLocation getTexture(IrradiatedCow pEntity) {
        return COW_LOCATION;
    }
}
