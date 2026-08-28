package net.nuclearteam.createnuclear.content.equipment.armor;

import io.github.fabricators_of_create.porting_lib.item.ArmorTextureItem;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.model.HumanoidModel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.nuclearteam.createnuclear.content.contraptions.irradiated.CNModelLayers;

public final class AntiRadiationArmorRenderer implements ArmorRenderer {
    private AntiRadiationArmorModel model;

    @Override
    public void render(PoseStack matrices, MultiBufferSource consumers, ItemStack stack,
                       LivingEntity entity, EquipmentSlot slot, int light,
                       HumanoidModel<LivingEntity> contextModel) {
        if (!(stack.getItem() instanceof AntiRadiationArmorItem armorItem)) return;
        if (model == null) {
            model = new AntiRadiationArmorModel(Minecraft.getInstance()
                    .getEntityModelLoader().getModelPart(CNModelLayers.ANTI_IRRADIATION_ARMOR));
        }
        contextModel.copyBipedStateTo(model);
        model.currentSlot = slot;
        String texturePath = ((ArmorTextureItem) armorItem).getArmorTexture(stack, entity, slot, null);
        ArmorRenderer.renderPart(matrices, consumers, light, stack, model, new ResourceLocation(texturePath));
    }
}
