package net.nuclearteam.createnuclear.content.contraptions.irradiated.cat;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.util.Mth;
import com.mojang.math.Axis;
import net.nuclearteam.createnuclear.CreateNuclear;
import net.nuclearteam.createnuclear.content.contraptions.irradiated.CNModelLayers;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class IrradiatedCatRenderer extends MobRenderer<IrradiatedCat, IrradiatedCatModel<IrradiatedCat>> {
    private static final ResourceLocation IRRADIATED_CAT_LOCATION = CreateNuclear.asResource("textures/entity/irradiated_cat.png");

    public IrradiatedCatRenderer(EntityRendererProvider.Context context) {
        super(context, new IrradiatedCatModel<>(context.getPart(CNModelLayers.IRRADIATED_CAT)), 0.4f);
    }

    @Override
    public ResourceLocation getTexture(IrradiatedCat entity) {
        return IRRADIATED_CAT_LOCATION;
    }
    protected void scale(IrradiatedCat livingEntity, PoseStack matrixStack, float partialTickTime) {
        super.scale(livingEntity, matrixStack, partialTickTime);
        matrixStack.scale(0.8F, 0.8F, 0.8F);
    }

    protected void setupTransforms(IrradiatedCat entityLiving, PoseStack matrixStack, float ageInTicks, float rotationYaw, float partialTicks) {
        super.setupTransforms(entityLiving, matrixStack, ageInTicks, rotationYaw, partialTicks);
        float f = entityLiving.getLieDownAmount(partialTicks);
        if (f > 0.0F) {
            matrixStack.translate(0.4F * f, 0.15F * f, 0.1F * f);
            matrixStack.multiply(Axis.POSITIVE_Z.rotationDegrees(Mth.lerpAngleDegrees(f, 0.0F, 90.0F)));
            BlockPos blockPos = entityLiving.blockPosition();
            List<Player> list = entityLiving.level().getNonSpectatingEntities(Player.class, (new AABB(blockPos)).expand(2.0, 2.0, 2.0));

            for (Player player : list) {
                if (player.isSleeping()) {
                    matrixStack.translate(0.15F * f, 0.0F, 0.0F);
                    break;
                }
            }
        }

    }
}
