package net.nuclearteam.createnuclear.entity.irradiatedwolf;// Made with Blockbench 4.10.1
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.model.ColorableAgeableListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
@SuppressWarnings("unused")
public class IrradiatedWolfModel<T extends IrradiatedWolf> extends ColorableAgeableListModel<T> {
	private final ModelPart head;
	private final ModelPart pustule1;
	private final ModelPart pustule2;
	private final ModelPart croc1;
	private final ModelPart croc2;
	private final ModelPart body;
	private final ModelPart body_rotation;
	private final ModelPart pustule4;
	private final ModelPart pustule5;
	private final ModelPart mane;
	private final ModelPart mane_rotation;
	private final ModelPart leg1;
	private final ModelPart pustule3;
	private final ModelPart leg2;
	private final ModelPart leg3;
	private final ModelPart leg4;
	private final ModelPart tail;

	public IrradiatedWolfModel(ModelPart root) {
		this.head = root.getChild("head");
		this.pustule1 = this.head.getChild("pustule1");
		this.pustule2 = this.head.getChild("pustule2");
		this.croc1 = this.head.getChild("croc1");
		this.croc2 = this.head.getChild("croc2");
		this.body = root.getChild("body");
		this.body_rotation = this.body.getChild("body_rotation");
		this.pustule4 = this.body_rotation.getChild("pustule4");
		this.pustule5 = this.body_rotation.getChild("pustule5");
		this.mane = root.getChild("mane");
		this.mane_rotation = this.mane.getChild("mane_rotation");
		this.leg1 = root.getChild("leg1");
		this.pustule3 = this.leg1.getChild("pustule3");
		this.leg2 = root.getChild("leg2");
		this.leg3 = root.getChild("leg3");
		this.leg4 = root.getChild("leg4");
		this.tail = root.getChild("tail");
	}

	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		PartDefinition head = modelPartData.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -3.0F, -2.0F, 6.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(16, 14).addBox(2.0F, -5.0F, 0.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(16, 14).addBox(-2.0F, -5.0F, 0.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 10).addBox(-0.5F, -0.02F, -5.0F, 3.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, 13.5F, -7.0F));

		PartDefinition pustule1 = head.addOrReplaceChild("pustule1", CubeListBuilder.create().texOffs(37, 15).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.7373F, -2.3286F, -1.7075F, 0.2892F, 0.858F, -0.0023F));

		PartDefinition pustule2 = head.addOrReplaceChild("pustule2", CubeListBuilder.create().texOffs(37, 15).addBox(-1.0F, -0.2F, -0.9F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.4627F, 0.6714F, -0.7075F, 0.15F, -0.2024F, -0.0367F));

		PartDefinition croc1 = head.addOrReplaceChild("croc1", CubeListBuilder.create().texOffs(47, 16).addBox(-0.5F, -1.4F, -1.6F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.2F, 2.9F, -2.8F, 0.1745F, 0.0F, 0.0F));

		PartDefinition croc2 = head.addOrReplaceChild("croc2", CubeListBuilder.create().texOffs(47, 16).addBox(-0.6F, -1.4F, -1.6F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.3F, 2.9F, -2.8F, 0.1745F, 0.0F, 0.0F));

		PartDefinition body = modelPartData.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 14.0F, 2.0F));

		PartDefinition body_rotation = body.addOrReplaceChild("body_rotation", CubeListBuilder.create().texOffs(18, 14).addBox(-3.0F, -2.0F, -3.0F, 6.0F, 9.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.5708F, 0.0F, 0.0F));

		PartDefinition pustule4 = body_rotation.addOrReplaceChild("pustule4", CubeListBuilder.create().texOffs(37, 15).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.3651F, 0.3994F, 0.6F, -1.7257F, -0.1643F, 0.0375F));

		PartDefinition pustule5 = body_rotation.addOrReplaceChild("pustule5", CubeListBuilder.create().texOffs(37, 15).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5127F, 5.3402F, 2.6436F, -2.2397F, -1.2467F, 2.3103F));

		PartDefinition mane = modelPartData.addOrReplaceChild("mane", CubeListBuilder.create(), PartPose.offset(-1.0F, 14.0F, 2.0F));

		PartDefinition mane_rotation = mane.addOrReplaceChild("mane_rotation", CubeListBuilder.create().texOffs(21, 0).addBox(-4.0F, -5.5F, -0.5F, 8.0F, 6.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 2.5F, -2.5F, 1.5708F, 0.0F, 0.0F));

		PartDefinition leg1 = modelPartData.addOrReplaceChild("leg1", CubeListBuilder.create().texOffs(0, 18).addBox(0.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.5F, 16.0F, 7.0F));

		PartDefinition pustule3 = leg1.addOrReplaceChild("pustule3", CubeListBuilder.create().texOffs(39, 15).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.495F, 2.9934F, 0.6936F, 3.0237F, 0.8634F, -3.0314F));

		PartDefinition leg2 = modelPartData.addOrReplaceChild("leg2", CubeListBuilder.create().texOffs(0, 18).addBox(0.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 16.0F, 7.0F));

		PartDefinition leg3 = modelPartData.addOrReplaceChild("leg3", CubeListBuilder.create().texOffs(0, 18).addBox(0.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.5F, 16.0F, -4.0F));

		PartDefinition leg4 = modelPartData.addOrReplaceChild("leg4", CubeListBuilder.create().texOffs(0, 18).addBox(0.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 16.0F, -4.0F));

		PartDefinition tail = modelPartData.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(9, 18).addBox(0.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, 12.0F, 10.0F));
		return LayerDefinition.create(modelData, 64, 32);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.head.xRot = headPitch * 0.017453292F;
		this.head.yRot = netHeadYaw * 0.017453292F;
		this.tail.xRot = ageInTicks;

		if (entity.isAngry()) {
			this.tail.yRot = 0.0F;
		} else {
			this.tail.yRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
		}


		this.tail.setPos(-1.0F, 12.0F, 8.0F);
		this.leg1.setPos(-2.5F, 16.0F, 7.0F);
		this.leg2.setPos(0.5F, 16.0F, 7.0F);
		this.leg3.setPos(-2.5F, 16.0F, -4.0F);
		this.leg4.setPos(0.5F, 16.0F, -4.0F);
		this.leg1.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
		this.leg2.xRot = Mth.cos(limbSwing * 0.6662F + 3.1415927F) * 1.4F * limbSwingAmount;
		this.leg3.xRot = Mth.cos(limbSwing * 0.6662F + 3.1415927F) * 1.4F * limbSwingAmount;
		this.leg4.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;

		this.body.zRot = entity.getBodyRollAngle(headPitch, -0.16F);
		this.tail.zRot = entity.getBodyRollAngle(headPitch, -0.2F);
	}

	@Override
	public void renderToBuffer(PoseStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		head.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		body.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		mane.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		leg1.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		leg2.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		leg3.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		leg4.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		tail.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
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