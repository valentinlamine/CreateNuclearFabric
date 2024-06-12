package net.ynov.createnuclear.entity.irradiatedwolf;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.ynov.createnuclear.CreateNuclear;
import net.ynov.createnuclear.entity.CNModelLayers;
import org.jetbrains.annotations.NotNull;

public class IrradiatedWolfRenderer extends MobRenderer<IrradiatedWolf, IrradiatedWolfModel<IrradiatedWolf>> {
    private static final ResourceLocation WOLF_LOCATION = new ResourceLocation(CreateNuclear.MOD_ID, "textures/entity/irradiated_wolf.png");
    private static final ResourceLocation WOLF_TAME_LOCATION = new ResourceLocation(CreateNuclear.MOD_ID, "textures/entity/irradiated_wolf.png");
    private static final ResourceLocation WOLF_ANGRY_LOCATION = new ResourceLocation("textures/entity/wolf/wolf_angry.png");

    public IrradiatedWolfRenderer(EntityRendererProvider.Context context) {
        super(context, new IrradiatedWolfModel<>(context.bakeLayer(CNModelLayers.IRRADIATED_WOLF)), 0.5F);
        this.addLayer(new IrradiatedWoldCollarLayer(this));
    }

    protected float getBob(IrradiatedWolf livingBase, float partialTicks) {
        return livingBase.getTailAngle();
    }

    public void render(IrradiatedWolf entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight) {
        if (entity.isWet()) {
            float f = entity.getWetShade(partialTicks);
            ((IrradiatedWolfModel<?>)this.model).setColor(f, f, f);
        }

        super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
        if (entity.isWet()) {
            ((IrradiatedWolfModel<?>)this.model).setColor(1.0F, 1.0F, 1.0F);
        }

    }

    public @NotNull ResourceLocation getTextureLocation(IrradiatedWolf entity) {
        if (entity.isTame()) {
            return WOLF_TAME_LOCATION;
        } else {
            return entity.isAngry() ? WOLF_ANGRY_LOCATION : WOLF_LOCATION;
        }
    }
}