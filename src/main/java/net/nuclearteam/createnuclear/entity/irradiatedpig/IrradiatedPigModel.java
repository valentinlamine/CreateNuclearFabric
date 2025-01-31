
package net.nuclearteam.createnuclear.entity.irradiatedpig;
// Made with Blockbench 4.11.2
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;

public class IrradiatedPigModel<T extends IrradiatedPig> extends AgeableListModel<T> {
	private final ModelPart body_grp;
	private final ModelPart ribs;
	private final ModelPart pustules;
	private final ModelPart head;
	private final ModelPart pustule_head;
	private final ModelPart legs;
	private final ModelPart leg1;
	private final ModelPart leg2;
	private final ModelPart leg3;
	private final ModelPart leg4;
	private final ModelPart bone_ribs1;
	private final ModelPart bone_ribs2;
	private final ModelPart bone_ribs3;

	public IrradiatedPigModel(ModelPart root) {
		this.body_grp = root.getChild("body_grp");
		this.ribs = this.body_grp.getChild("ribs");
		this.pustules = this.body_grp.getChild("pustules");
		this.head = root.getChild("head");
		this.pustule_head = this.head.getChild("pustule_head");
		this.legs = root.getChild("legs");
		this.leg1 = this.legs.getChild("leg1");
		this.leg2 = this.legs.getChild("leg2");
		this.leg3 = this.legs.getChild("leg3");
		this.leg4 = this.legs.getChild("leg4");
		this.bone_ribs1 = root.getChild("bone_ribs1");
		this.bone_ribs2 = root.getChild("bone_ribs2");
		this.bone_ribs3 = root.getChild("bone_ribs3");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body_grp = partdefinition.addOrReplaceChild("body_grp", CubeListBuilder.create().texOffs(28, 8).addBox(-5.0F, -10.0F, -7.0F, 10.0F, 16.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.0F, 2.0F, 1.5708F, 0.0F, 0.0F));

		PartDefinition ribs = body_grp.addOrReplaceChild("ribs", CubeListBuilder.create().texOffs(28, 8).addBox(-5.0F, -9.0F, -5.0F, 10.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(33, 13).addBox(-5.0F, -18.0F, -5.0F, 10.0F, 9.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(29, 9).addBox(-5.0F, -18.0F, -2.0F, 9.0F, 9.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(28, 8).addBox(-5.0F, -18.0F, 2.0F, 10.0F, 9.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(28, 8).addBox(-5.0F, -23.0F, -5.0F, 10.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 13.0F, -2.0F));

		PartDefinition pustules = body_grp.addOrReplaceChild("pustules", CubeListBuilder.create(), PartPose.offset(0.0F, 13.0F, -2.0F));

		PartDefinition pustule3_r1 = pustules.addOrReplaceChild("pustule3_r1", CubeListBuilder.create().texOffs(53, 2).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, -7.0F, -1.0F, -0.829F, 0.0F, -0.829F));

		PartDefinition pustule2_r1 = pustules.addOrReplaceChild("pustule2_r1", CubeListBuilder.create().texOffs(16, 21).addBox(-2.0F, -3.0F, -1.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, -14.0F, -1.0F, 0.0F, 0.4363F, -0.7854F));

		PartDefinition pustule1_r1 = pustules.addOrReplaceChild("pustule1_r1", CubeListBuilder.create().texOffs(27, 2).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -16.0F, 3.0F, 0.5724F, 0.7863F, 0.4279F));

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(1, 0).addBox(-4.0F, -4.0F, -8.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(16, 16).addBox(-2.0F, 0.0F, -9.0F, 4.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 12.0F, -6.0F));

		PartDefinition pustule_head = head.addOrReplaceChild("pustule_head", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition pustule_head_r1 = pustule_head.addOrReplaceChild("pustule_head_r1", CubeListBuilder.create().texOffs(40, 2).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, -3.0F, -3.0F, -0.3491F, 0.0F, 0.6981F));

		PartDefinition legs = partdefinition.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(0.0F, 11.0F, 2.0F));

		PartDefinition leg1 = legs.addOrReplaceChild("leg1", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, 7.0F, 5.0F));

		PartDefinition leg2 = legs.addOrReplaceChild("leg2", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 7.0F, 5.0F));

		PartDefinition leg3 = legs.addOrReplaceChild("leg3", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, 7.0F, -7.0F));

		PartDefinition leg4 = legs.addOrReplaceChild("leg4", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 7.0F, -7.0F));

		PartDefinition bone_ribs1 = partdefinition.addOrReplaceChild("bone_ribs1", CubeListBuilder.create().texOffs(1, 28).addBox(2.5F, -3.9F, -1.0F, -1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(9, 27).addBox(2.5F, -2.9F, -1.0F, -1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(22, 30).addBox(1.5F, -0.9F, -1.0F, -1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 14.0F, 0.0F));

		PartDefinition bone_ribs2 = partdefinition.addOrReplaceChild("bone_ribs2", CubeListBuilder.create().texOffs(3, 28).addBox(2.5F, -3.9F, -1.0F, -1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(11, 27).addBox(2.5F, -2.9F, -1.0F, -1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(16, 30).addBox(1.5F, -0.9F, -1.0F, -1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 14.0F, 2.0F));

		PartDefinition bone_ribs3 = partdefinition.addOrReplaceChild("bone_ribs3", CubeListBuilder.create().texOffs(5, 28).addBox(2.5F, -3.9F, -1.0F, -1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(13, 27).addBox(2.5F, -2.9F, -1.0F, -1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(19, 30).addBox(1.5F, -0.9F, -1.0F, -1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 14.0F, 4.0F));

		return LayerDefinition.create(meshdefinition, 64, 32);
	}

	@Override
	public void setupAnim(IrradiatedPig entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.head.xRot = headPitch * 0.017453292F;
		this.head.yRot = netHeadYaw * 0.017453292F;
		this.leg1.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
		this.leg2.xRot = Mth.cos(limbSwing * 0.6662F + 3.1415927F) * 1.4F * limbSwingAmount;
		this.leg3.xRot = Mth.cos(limbSwing * 0.6662F + 3.1415927F) * 1.4F * limbSwingAmount;
		this.leg4.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		body_grp.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		legs.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		bone_ribs1.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		bone_ribs2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		bone_ribs3.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	protected Iterable<ModelPart> headParts() {
		return ImmutableList.of(this.head);
	}

	@Override
	protected Iterable<ModelPart> bodyParts() {
		return ImmutableList.of(this.body_grp, this.leg2, this.leg1, this.leg3, this.leg4);
	}
}