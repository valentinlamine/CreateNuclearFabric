package net.nuclearteam.createnuclear.entity.irradiatedwolf;// Made with Blockbench 4.10.1
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.ColorableAgeableListModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.nuclearteam.createnuclear.CreateNuclear;

import org.jetbrains.annotations.NotNull;

public class IrradiatedWolfModel<T extends IrradiatedWolf> extends ColorableAgeableListModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(CreateNuclear.asResource( "irradiated_wolf"), "main");
	private final ModelPart head;
	private final ModelPart pustule1;
	private final ModelPart pustule2;
	private final ModelPart pustule3;
	private final ModelPart pustule4;
	private final ModelPart pustule5;
	private final ModelPart croc1;
	private final ModelPart croc2;
	private final ModelPart body;
	private final ModelPart upper_body;
	private final ModelPart body_rotation;
	private final ModelPart mane;
	private final ModelPart mane_rotation;
	private final ModelPart leg1;
	private final ModelPart leg2;
	private final ModelPart leg3;
	private final ModelPart leg4;
	private final ModelPart tail;

	public IrradiatedWolfModel(ModelPart root) {
		this.head = root.getChild("head");
		this.pustule1 = root.getChild("pustule1");
		this.pustule2 = root.getChild("pustule2");
		this.pustule3 = root.getChild("pustule3");
		this.pustule4 = root.getChild("pustule4");
		this.pustule5 = root.getChild("pustule5");
		this.croc1 = root.getChild("croc1");
		this.croc2 = root.getChild("croc2");
		this.body = root.getChild("body");
		this.upper_body = root.getChild("upper_body");
		this.body_rotation = root.getChild("body_rotation");
		this.mane = root.getChild("mane");
		this.mane_rotation = root.getChild("mane_rotation");
		this.leg1 = root.getChild("leg1");
		this.leg2 = root.getChild("leg2");
		this.leg3 = root.getChild("leg3");
		this.leg4 = root.getChild("leg4");
		this.tail = root.getChild("tail");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -3.0F, -2.0F, 6.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(16, 14).addBox(2.0F, -5.0F, 0.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(16, 14).addBox(-2.0F, -5.0F, 0.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 10).addBox(-0.5F, -0.02F, -5.0F, 3.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, 13.5F, -7.0F));
		partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(18, 14).addBox(-3.0F, -2.0F, -3.0F, 6.0F, 9.0F, 6.0F), PartPose.offsetAndRotation(0.0F, 14.0F, 2.0F, 1.5707964F, 0.0F, 0.0F));
		partdefinition.addOrReplaceChild("upper_body", CubeListBuilder.create().texOffs(21, 0).addBox(-3.0F, -3.0F, -3.0F, 8.0F, 6.0F, 7.0F), PartPose.offsetAndRotation(-1.0F, 14.0F, -3.0F, 1.5707964F, 0.0F, 0.0F));
		partdefinition.addOrReplaceChild("body_rotation", CubeListBuilder.create().texOffs(18, 14).addBox(-3.0F, -2.0F, -3.0F, 6.0F, 9.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.5708F, 0.0F, 0.0F));
		partdefinition.addOrReplaceChild("mane", CubeListBuilder.create(), PartPose.offset(-1.0F, 14.0F, 2.0F));
		partdefinition.addOrReplaceChild("mane_rotation", CubeListBuilder.create().texOffs(21, 0).addBox(-4.0F, -5.5F, -0.5F, 8.0F, 6.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 2.5F, -2.5F, 1.5708F, 0.0F, 0.0F));
		partdefinition.addOrReplaceChild("leg1", CubeListBuilder.create().texOffs(0, 18).addBox(0.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.5F, 16.0F, 7.0F));
		partdefinition.addOrReplaceChild("leg2", CubeListBuilder.create().texOffs(0, 18).addBox(0.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 16.0F, 7.0F));
		partdefinition.addOrReplaceChild("leg3", CubeListBuilder.create().texOffs(0, 18).addBox(0.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.5F, 16.0F, -4.0F));
		partdefinition.addOrReplaceChild("leg4", CubeListBuilder.create().texOffs(0, 18).addBox(0.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 16.0F, -4.0F));
		partdefinition.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(9, 18).addBox(0.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, 12.0F, 10.0F));


		partdefinition.addOrReplaceChild("croc2", CubeListBuilder.create().texOffs(47, 16).addBox(-0.6F, -1.4F, -1.6F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.3F, 16.4F, -9.8F, 0.1745F, 0.0F, 0.0F));
		partdefinition.addOrReplaceChild("croc1", CubeListBuilder.create().texOffs(47, 16).addBox(-0.5F, -1.4F, -1.6F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.2F, 16.4F, -9.8F, 0.1745F, 0.0F, 0.0F));
		partdefinition.addOrReplaceChild("pustule3", CubeListBuilder.create().texOffs(39, 15).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.005F, 18.9934F, 7.6936F, 3.0237F, 0.8634F, -3.0314F));
		partdefinition.addOrReplaceChild("pustule1", CubeListBuilder.create().texOffs(37, 15).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.7373F, 11.1714F, -8.7075F, 0.2892F, 0.858F, -0.0023F));
		partdefinition.addOrReplaceChild("pustule2", CubeListBuilder.create().texOffs(37, 15).addBox(-1.0F, -0.2F, -0.9F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.4627F, 14.1714F, -7.7075F, 0.15F, -0.2024F, -0.0367F));
		partdefinition.addOrReplaceChild("pustule4", CubeListBuilder.create().texOffs(37, 15).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.3651F, 14.3994F, 2.6F, -1.7257F, -0.1643F, 0.0375F));
		partdefinition.addOrReplaceChild("pustule5", CubeListBuilder.create().texOffs(37, 15).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5127F, 11.3402F, 4.6436F, -2.2397F, -1.2467F, 2.3103F));
		return LayerDefinition.create(meshdefinition, 64, 32);
	}

	public void prepareMobModel(T entity, float limbSwing, float limbSwingAmount, float partialTick) {
		if (entity.isAngry()) {
			this.tail.yRot = 0.0F;
		} else {
			this.tail.yRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
		}

		if (entity.isInSittingPose()) {
			this.upper_body.setPos(-1.0F, 16.0F, -3.0F);
			this.upper_body.xRot = 1.2566371F;
			this.upper_body.yRot = 0.0F;
			this.body.setPos(0.0F, 18.0F, 0.0F);
			this.body.xRot = 0.7853982F;
			this.tail.setPos(-1.0F, 21.0F, 6.0F);
			this.leg1.setPos(-2.5F, 22.7F, 2.0F);
			this.leg1.xRot = 4.712389F;
			this.leg2.setPos(0.5F, 22.7F, 2.0F);
			this.leg2.xRot = 4.712389F;
			this.leg3.xRot = 5.811947F;
			this.leg3.setPos(-2.49F, 17.0F, -4.0F);
			this.leg4.xRot = 5.811947F;
			this.leg4.setPos(0.51F, 17.0F, -4.0F);
		} else {
			this.body.setPos(0.0F, 14.0F, 2.0F);
			this.body.xRot = 1.5707964F;
			this.upper_body.setPos(-1.0F, 14.0F, -3.0F);
			this.upper_body.xRot = this.body.xRot;
			this.tail.setPos(-1.0F, 12.0F, 8.0F);
			this.leg1.setPos(-2.5F, 16.0F, 7.0F);
			this.leg2.setPos(0.5F, 16.0F, 7.0F);
			this.leg3.setPos(-2.5F, 16.0F, -4.0F);
			this.leg4.setPos(0.5F, 16.0F, -4.0F);
			this.leg1.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
			this.leg2.xRot = Mth.cos(limbSwing * 0.6662F + 3.1415927F) * 1.4F * limbSwingAmount;
			this.leg3.xRot = Mth.cos(limbSwing * 0.6662F + 3.1415927F) * 1.4F * limbSwingAmount;
			this.leg4.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
		}

		this.upper_body.zRot = entity.getBodyRollAngle(partialTick, -0.08F);
		this.body.zRot = entity.getBodyRollAngle(partialTick, -0.16F);
		this.tail.zRot = entity.getBodyRollAngle(partialTick, -0.2F);
	}

	public void setupAnim(@NotNull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.head.xRot = headPitch * 0.017453292F;
		this.head.yRot = netHeadYaw * 0.017453292F;
		this.tail.xRot = ageInTicks;
	}

	@Override
	public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		upper_body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		mane.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		leg1.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		leg2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		leg3.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		leg4.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		tail.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		pustule1.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		pustule2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		pustule3.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		pustule4.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		pustule5.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		croc1.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		croc2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	protected Iterable<ModelPart> headParts() {
		return null;
	}

	@Override
	protected Iterable<ModelPart> bodyParts() {
		return null;
	}
}