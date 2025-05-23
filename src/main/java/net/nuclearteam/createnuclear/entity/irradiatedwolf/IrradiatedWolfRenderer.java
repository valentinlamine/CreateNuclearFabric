package net.nuclearteam.createnuclear.entity.irradiatedwolf;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.nuclearteam.createnuclear.CreateNuclear;
import net.nuclearteam.createnuclear.entity.CNModelLayers;

import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class IrradiatedWolfRenderer extends MobRenderer<IrradiatedWolf, IrradiatedWolfModel<IrradiatedWolf>> {
    private static final ResourceLocation WOLF_LOCATION = CreateNuclear.asResource("textures/entity/irradiated_wolf.png");
    private static final ResourceLocation WOLF_TAME_LOCATION = CreateNuclear.asResource("textures/entity/irradiated_wolf.png");
    private static final ResourceLocation WOLF_ANGRY_LOCATION = CreateNuclear.asResource("textures/entity/irradiated_wolf_angry.png");

    public IrradiatedWolfRenderer(EntityRendererProvider.Context context) {
        super(context, new IrradiatedWolfModel<>(context.bakeLayer(CNModelLayers.IRRADIATED_WOLF)), 0.5F);
    }

    protected float getBob(IrradiatedWolf livingBase, float partialTicks) {
        return livingBase.getTailAngle();
    }

    public void render(IrradiatedWolf entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight) {
        if (entity.isWet()) {
            float f = entity.getWetShade(partialTicks);
            this.model.setColor(f, f, f);
        }

        super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
        if (entity.isWet()) {
            this.model.setColor(1.0F, 1.0F, 1.0F);
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