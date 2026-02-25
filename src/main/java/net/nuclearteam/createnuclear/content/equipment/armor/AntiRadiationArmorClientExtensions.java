package net.nuclearteam.createnuclear.content.equipment.armor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.nuclearteam.createnuclear.content.contraptions.irradiated.CNModelLayers;
import org.jetbrains.annotations.NotNull;

public final class AntiRadiationArmorClientExtensions implements IClientItemExtensions {
    private AntiRadiationArmorModel model;

    @Override
    public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
        if (this.model == null) {
            var entityModelSet = Minecraft.getInstance().getEntityModels();
            var root = entityModelSet.bakeLayer(CNModelLayers.ANTI_IRRADIATION_ARMOR);
            this.model = new AntiRadiationArmorModel(root);
        }

        this.model.currentSlot = equipmentSlot;
        this.model.setAllVisible(false);
//        this.model.right_leg_armor.visible = false;
//        this.model.right_boot.visible = false;
//        this.model.left_leg_armor.visible = false;
//        this.model.left_boot.visible = false;

        switch (equipmentSlot) {
            case HEAD -> {
                this.model.getHead().visible = true;
                this.model.hat.visible = true;
            }
            case CHEST -> {
                this.model.body.visible = true;
                this.model.rightArm.visible = true;
                this.model.leftArm.visible = true;
            }
            case LEGS -> {
                this.model.getHead().visible = false;
                this.model.body.visible = false;
                this.model.right_arm.visible = false;
                this.model.left_arm.visible = false;
                this.model.rightLeg.visible = false;
                this.model.leftLeg.visible = false;

                this.model.right_leg.visible = false;
                this.model.left_leg.visible = true;

                // On active juste la partie "Cuisse"
//                this.model.right_leg_armor.visible = true;
//                this.model.left_leg_armor.visible = true;
            }
            case FEET -> {
                this.model.rightLeg.visible = true;
                this.model.leftLeg.visible = true;

                // On active juste la partie "Botte"
//                this.model.right_boot.visible = true;
//                this.model.left_boot.visible = true;
            }
        }

        return this.model;
    }
}