package net.nuclearteam.createnuclear.content.equipment.armor;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;


public class AntiRadiationArmorModel extends HumanoidModel<LivingEntity> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor;
	public final ModelPart head;
	public final ModelPart body;
	public final ModelPart right_arm;
	public final ModelPart left_arm;
	public final ModelPart right_leg;
	public final ModelPart right_boot;
	public final ModelPart left_leg;
	public final ModelPart left_boot;

	public EquipmentSlot currentSlot = EquipmentSlot.HEAD;

	public AntiRadiationArmorModel(ModelPart root) {
		super(root);
		this.head = root.getChild("head");
		this.body = root.getChild("body");
		this.right_arm = root.getChild("right_arm");
		this.left_arm = root.getChild("left_arm");
		this.right_leg = root.getChild("right_leg");
		this.right_boot = this.right_leg.getChild("right_boot");
		this.left_leg = root.getChild("left_leg");
		this.left_boot = this.left_leg.getChild("left_boot");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		partdefinition.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);


		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(30, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.2F))
		.texOffs(30, 16).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, -8.0F, 1.0F));

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 19).addBox(-3.875F, 0.825F, -2.65F, 8.0F, 12.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-4.575F, -0.175F, -2.95F, 9.0F, 13.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(0, 36).addBox(-3.875F, 0.825F, -2.55F, 8.0F, 12.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(64, 0).addBox(-4.675F, -0.175F, -2.85F, 9.0F, 13.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.175F, -0.825F, 0.85F));

		PartDefinition right_arm = partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(45, 32).mirror().addBox(-2.1F, -2.5F, -2.1F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.45F)).mirror(false)
		.texOffs(29, 32).mirror().addBox(-2.1F, -2.5F, -2.1F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.55F)).mirror(false), PartPose.offset(-5.8F, 4.5F, 0.8F));

		PartDefinition left_arm = partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(45, 32).addBox(-1.9F, -2.5F, -2.1F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.4F))
		.texOffs(29, 32).addBox(-1.9F, -2.5F, -2.1F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offset(6.2F, 4.5F, 0.8F));

		PartDefinition right_leg = partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(29, 48).mirror().addBox(-2.0733F, -0.3333F, -1.8667F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(45, 48).mirror().addBox(-1.9633F, -0.3333F, -2.0667F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)).mirror(false), PartPose.offset(-1.6367F, 19.3333F, 0.7667F));

		right_leg.addOrReplaceChild("right_boot", CubeListBuilder.create().texOffs(61, 39).mirror().addBox(-1.9633F, 7.6334F, -2.0667F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.25F)).mirror(false), PartPose.offset(-1.6367F, 19.3333F, 0.7667F));

		PartDefinition left_leg = partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(29, 48).addBox(-1.9667F, -0.3333F, -1.8667F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(45, 48).addBox(-2.0167F, -0.3333F, -2.0667F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(2.2167F, 19.3333F, 0.7667F));

		left_leg.addOrReplaceChild("left_boot", CubeListBuilder.create().texOffs(61, 39).addBox(-2.0167F, 7.6334F, -2.0667F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(2.2167F, 19.3333F, 0.7667F));

		return LayerDefinition.create(meshdefinition, 96, 96);
	}

	@Override
	public void setAllVisible(boolean pVisible) {
		super.setAllVisible(pVisible);
		this.head.visible = false;
		this.hat.visible = false;
		this.body.visible = false;
		this.right_arm.visible = false;
		this.left_arm.visible = false;
		this.right_leg.visible = false;
		this.right_boot.visible = false;
		this.left_leg.visible = false;
		this.left_boot.visible = false;
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		// Copier les animations des jambes vers les boots
		this.right_boot.copyFrom(this.right_leg);
		this.left_boot.copyFrom(this.left_leg);

		if (this.currentSlot == EquipmentSlot.LEGS) {
            this.body.visible = false;
			this.right_boot.visible = false;
			this.left_boot.visible = false;
        } else if (this.currentSlot == EquipmentSlot.FEET) {
			this.right_leg.visible = false;
			this.left_leg.visible = false;
			this.right_boot.visible = true;
			this.left_boot.visible = true;
		}

		head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		right_arm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		left_arm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		right_leg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		left_leg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		right_boot.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		left_boot.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}