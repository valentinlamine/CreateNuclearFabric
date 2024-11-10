package net.nuclearteam.createnuclear.entity.irradiatedcat;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.nuclearteam.createnuclear.CreateNuclear;
import net.nuclearteam.createnuclear.entity.CNModelLayers;

import java.util.Iterator;
import java.util.List;

public class IrradiatedCatRenderer extends MobRenderer<IrradiatedCat, IrradiatedCatModel<IrradiatedCat>> {
    private static final ResourceLocation IRRADIATED_CAT_LOCATION = CreateNuclear.asResource("textures/entity/irradiated_cat.png");

    public IrradiatedCatRenderer(EntityRendererProvider.Context context) {
        super(context, new IrradiatedCatModel<>(context.bakeLayer(CNModelLayers.IRRADIATED_CAT)), 0.4F);
    }

    @Override
    public ResourceLocation getTextureLocation(IrradiatedCat entity) {
        return IRRADIATED_CAT_LOCATION;
    }

    protected void scale(IrradiatedCat livingEntity, PoseStack matrixStack, float partialTickTime) {
        super.scale(livingEntity, matrixStack, partialTickTime);
        matrixStack.scale(0.8F, 0.8F, 0.8F);
    }

    protected void setupRotations(IrradiatedCat entityLiving, PoseStack matrixStack, float ageInTicks, float rotationYaw, float partialTicks) {
        super.setupRotations(entityLiving, matrixStack, ageInTicks, rotationYaw, partialTicks);
        float f = entityLiving.getLieDownAmount(partialTicks);
        if (f > 0.0F) {
            matrixStack.translate(0.4F * f, 0.15F * f, 0.1F * f);
            matrixStack.mulPose(Axis.ZP.rotationDegrees(Mth.rotLerp(f, 0.0F, 90.0F)));
            BlockPos blockPos = entityLiving.blockPosition();
            List<Player> list = entityLiving.level().getEntitiesOfClass(Player.class, (new AABB(blockPos)).inflate(2.0, 2.0, 2.0));
            Iterator var9 = list.iterator();

            while(var9.hasNext()) {
                Player player = (Player)var9.next();
                if (player.isSleeping()) {
                    matrixStack.translate(0.15F * f, 0.0F, 0.0F);
                    break;
                }
            }
        }

    }
}
