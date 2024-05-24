package net.ynov.createnuclear.entity.irradiatedcat;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.ModelUtils;
import net.minecraft.client.model.OcelotModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.ynov.createnuclear.CreateNuclear;


public class IrradiatedCatModel<T extends IrradiatedCat> extends AgeableListModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(CreateNuclear.MOD_ID, "irradiatedcatmodel"), "main");

	private final ModelPart pustule3;
	private final ModelPart pustule2;
	private final ModelPart teeth2;
	private final ModelPart teeth;
	private final ModelPart pustule;
	private final ModelPart head;
	private final ModelPart body;
	private final ModelPart left_front_leg;
	private final ModelPart right_front_leg;
	private final ModelPart left_hind_leg;
	private final ModelPart right_hind_leg;
	private final ModelPart tail1;
	private final ModelPart tail2;

	public IrradiatedCatModel(ModelPart root) {
		this.pustule = root.getChild("pustule");
        this.pustule3 = root.getChild("pustule3");
		this.pustule2 = root.getChild("pustule2");
		this.teeth2 = root.getChild("teeth2");
		this.teeth = root.getChild("teeth");
		this.head = root.getChild("head");
		this.body = root.getChild("body");
		this.left_front_leg = root.getChild("left_front_leg");
		this.right_front_leg = root.getChild("right_front_leg");
		this.left_hind_leg = root.getChild("left_hind_leg");
		this.right_hind_leg = root.getChild("right_hind_leg");
		this.tail1 = root.getChild("tail1");
		this.tail2 = root.getChild("tail2");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		partdefinition.addOrReplaceChild("pustule3", CubeListBuilder.create().texOffs(46, 24).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5F, 19.6F, -4.5F, 1.8282F, -1.4377F, 3.0129F));
		partdefinition.addOrReplaceChild("pustule2", CubeListBuilder.create().texOffs(46, 24).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5F, 14.6F, 4.5F, 1.442F, 0.0338F, 1.6973F));
		partdefinition.addOrReplaceChild("teeth2", CubeListBuilder.create().texOffs(41, 23).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.8F, 17.1F, -12.2F, 0.3927F, 0.0F, 0.0F));
		partdefinition.addOrReplaceChild("teeth", CubeListBuilder.create().texOffs(41, 23).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.8F, 17.1F, -12.2F, 0.3927F, 0.0F, 0.0F));
		partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, -2.0F, -3.0F, 5.0F, 4.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(0, 24).addBox(-1.5F, -0.02F, -4.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 10).addBox(-2.0F, -3.0F, 0.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(6, 10).addBox(1.0F, -3.0F, 0.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 15.0F, -9.0F));

		partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(20, 0).addBox(-2.0F, 3.0F, -8.0F, 4.0F, 16.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.0F, -10.0F, 1.5708F, 0.0F, 0.0F));
		partdefinition.addOrReplaceChild("left_front_leg", CubeListBuilder.create().texOffs(40, 0).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(1.1F, 14.1F, -5.0F));
		partdefinition.addOrReplaceChild("right_front_leg", CubeListBuilder.create().texOffs(40, 0).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.1F, 14.1F, -5.0F));
		partdefinition.addOrReplaceChild("left_hind_leg", CubeListBuilder.create().texOffs(8, 13).addBox(-1.0F, 0.0F, 1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(1.1F, 18.0F, 5.0F));
		partdefinition.addOrReplaceChild("right_hind_leg", CubeListBuilder.create().texOffs(8, 13).addBox(-1.0F, 0.0F, 1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.1F, 18.0F, 5.0F));
		partdefinition.addOrReplaceChild("tail1", CubeListBuilder.create().texOffs(0, 15).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.5F, 8.0F, 1.5708F, 0.0F, 0.0F));
		partdefinition.addOrReplaceChild("tail2", CubeListBuilder.create().texOffs(4, 15).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.5F, 16.0F, 1.5708F, 0.0F, 0.0F));
		partdefinition.addOrReplaceChild("pustule", CubeListBuilder.create().texOffs(46, 24).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.1F, 15.5F, -10.0F, -0.2597F, 0.0338F, 1.6973F));
		return LayerDefinition.create(meshdefinition, 64, 32);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.head.xRot = headPitch * 0.017453292F;
		this.head.yRot = netHeadYaw * 0.017453292F;
		this.body.xRot = 1.5707964F;
		this.left_hind_leg.xRot = Mth.cos(limbSwing * 0.6662F) * limbSwingAmount;
		this.right_hind_leg.xRot = Mth.cos(limbSwing * 0.6662F + 3.1415927F) * limbSwingAmount;
		this.left_front_leg.xRot = Mth.cos(limbSwing * 0.6662F + 3.1415927F) * limbSwingAmount;
		this.right_front_leg.xRot = Mth.cos(limbSwing * 0.6662F) * limbSwingAmount;
		this.tail1.xRot = 1.7278761F + 0.7853982F * Mth.cos(limbSwing) * limbSwingAmount;
		this.tail2.xRot = 1.7278761F + 0.47123894F * Mth.cos(limbSwing) * limbSwingAmount;
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		left_front_leg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		right_front_leg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		left_hind_leg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		right_hind_leg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		tail1.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		tail2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);

		pustule.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		pustule3.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		pustule2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		teeth2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		teeth.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	protected Iterable<ModelPart> headParts() {
		return ImmutableList.of(this.head, this.pustule);
	}

	@Override
	protected Iterable<ModelPart> bodyParts() {
		return ImmutableList.of(this.body, this.left_hind_leg, this.right_hind_leg, this.left_front_leg, this.right_front_leg,  this.tail1, this.tail2, this.pustule2, this.pustule3);

	}


}