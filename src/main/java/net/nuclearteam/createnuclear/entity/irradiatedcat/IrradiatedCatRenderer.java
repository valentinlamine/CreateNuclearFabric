package net.nuclearteam.createnuclear.entity.irradiatedcat;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.nuclearteam.createnuclear.CreateNuclear;
import net.nuclearteam.createnuclear.entity.CNModelLayers;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@Environment(EnvType.CLIENT)
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class IrradiatedCatRenderer extends MobRenderer<IrradiatedCat, IrradiatedCatModel<IrradiatedCat>> {

    private static final ResourceLocation IRRADIATED_CAT_LOCATION =
            CreateNuclear.asResource("textures/entity/irradiated_cat.png");

    public IrradiatedCatRenderer(EntityRendererProvider.Context context) {
        super(context, new IrradiatedCatModel<>(context.bakeLayer(CNModelLayers.IRRADIATED_CAT)), 0.4F);
    }

    @Override
    public ResourceLocation getTextureLocation(IrradiatedCat entity) {
        return IRRADIATED_CAT_LOCATION;
    }

    @Override
    protected void scale(IrradiatedCat entity, PoseStack matrixStack, float partialTicks) {
        super.scale(entity, matrixStack, partialTicks);
        matrixStack.scale(0.8F, 0.8F, 0.8F);
    }

    @Override
    protected void setupRotations(IrradiatedCat entity, PoseStack matrixStack, float ageInTicks, float rotationYaw, float partialTicks) {
        super.setupRotations(entity, matrixStack, ageInTicks, rotationYaw, partialTicks);

        float lieDownAmount = entity.getLieDownAmount(partialTicks);
        if (lieDownAmount > 0.0F) {
            matrixStack.translate(0.4F * lieDownAmount, 0.15F * lieDownAmount, 0.1F * lieDownAmount);
            matrixStack.mulPose(Axis.ZP.rotationDegrees(Mth.rotLerp(lieDownAmount, 0.0F, 90.0F)));

            BlockPos pos = entity.blockPosition();
            AABB area = new AABB(pos).inflate(2.0, 2.0, 2.0);
            List<Player> nearbyPlayers = entity.level().getEntitiesOfClass(Player.class, area);

            for (Player player : nearbyPlayers) {
                if (player.isSleeping()) {
                    matrixStack.translate(0.15F * lieDownAmount, 0.0F, 0.0F);
                    break;
                }
            }
        }
    }
}
