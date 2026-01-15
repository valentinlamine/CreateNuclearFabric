package net.nuclearteam.createnuclear.content.contraptions.irradiated.zombie;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.ZombieModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class IrradiatedZombieModel<T extends IrradiatedZombie> extends ZombieModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	private final ModelPart head;
	private final ModelPart hat;
	private final ModelPart body;
	private final ModelPart left_arm;
	private final ModelPart right_arm;
	private final ModelPart left_leg;
	private final ModelPart right_leg;

	public IrradiatedZombieModel(ModelPart root) {
		super(root);
		this.head = root.getChild("head");
		this.hat = root.getChild("hat");
		this.body = root.getChild("body");
		this.left_arm = root.getChild("left_arm");
		this.right_arm = root.getChild("right_arm");
		this.left_leg = root.getChild("left_leg");
		this.right_leg = root.getChild("right_leg");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(14, 8).addBox(1.0F, -2.0F, -4.0F, 3.0F, 2.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(8, 5).addBox(1.0F, -4.0F, 1.0F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(8, 3).addBox(1.0F, -4.0F, 2.0F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 32).addBox(0.0F, -8.0F, 1.0F, 4.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(5, 4).addBox(-1.0F, -2.0F, -4.0F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(7, 6).addBox(-4.0F, -2.0F, -4.0F, 3.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-4.0F, -4.0F, -4.0F, 5.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(3, 4).addBox(-4.0F, -6.0F, -4.0F, 4.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(13, 33).addBox(-3.0F, -8.0F, 0.0F, 3.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(6, 4).addBox(-4.0F, -8.0F, -4.0F, 1.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(26, 52).addBox(-3.0F, -7.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Pustule_8_r1 = head.addOrReplaceChild("Pustule_8_r1", CubeListBuilder.create().texOffs(2, 1).addBox(0.0F, -1.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, -3.0F, 5.0F, 0.8418F, 0.2192F, -1.7126F));

		PartDefinition Pustule_2_r1 = head.addOrReplaceChild("Pustule_2_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -7.0F, 4.0F, 0.7432F, -0.5094F, -0.7858F));

		PartDefinition pustule_1_r1 = head.addOrReplaceChild("pustule_1_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, -5.0F, 4.0F, 0.5603F, 0.3736F, -1.2419F));

		PartDefinition hat = partdefinition.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition left_arm = partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(40, 32).mirror().addBox(-1.0F, 8.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(15, 40).mirror().addBox(1.0F, 3.0F, 0.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(1, 39).mirror().addBox(1.0F, -2.0F, -2.0F, 2.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(40, 16).mirror().addBox(-1.0F, -2.0F, -2.0F, 2.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(60, 26).addBox(1.0F, 3.0F, -1.0F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, 2.0F, 0.0F));

		PartDefinition Pustule_7_r1 = left_arm.addOrReplaceChild("Pustule_7_r1", CubeListBuilder.create().texOffs(2, 1).addBox(0.0F, -1.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 8.0F, 2.0F, -0.0083F, -0.6783F, 0.0285F));

		PartDefinition Pustule_6_r1 = left_arm.addOrReplaceChild("Pustule_6_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 7.0F, 2.0F, 0.7432F, -0.5094F, -0.7858F));

		PartDefinition right_arm = partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(0, 48).addBox(-3.0F, 8.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(24, 42).addBox(-3.0F, 5.0F, -2.0F, 4.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(19, 1).addBox(-1.0F, 5.0F, 0.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 53).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(56, 28).addBox(-2.0F, 5.0F, 0.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 2.0F, 0.0F));

		PartDefinition Pustule_3_r1 = right_arm.addOrReplaceChild("Pustule_3_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 6.0F, -2.0F, 0.0F, 0.0F, -0.3927F));

		PartDefinition Pustule_4_r1 = right_arm.addOrReplaceChild("Pustule_4_r1", CubeListBuilder.create().texOffs(2, 1).addBox(0.0F, -1.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 7.0F, -2.0F, 0.0F, 0.0F, 0.3491F));

		PartDefinition left_leg = partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(48, 38).mirror().addBox(-1.9F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(1.9F, 12.0F, 0.0F));

		PartDefinition Pustule_5_r1 = left_leg.addOrReplaceChild("Pustule_5_r1", CubeListBuilder.create().texOffs(2, 1).addBox(0.0F, -1.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.1F, 12.0F, -2.0F, 0.0F, 0.0F, -0.5672F));

		PartDefinition right_leg = partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.1F, 9.0F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(1, 17).addBox(-0.1F, 0.0F, -2.0F, 2.0F, 9.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(56, 57).addBox(-2.1F, 4.0F, 0.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(19, 49).addBox(-2.1F, 0.0F, -2.0F, 2.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(60, 33).addBox(-1.1F, 4.0F, -1.0F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.9F, 12.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		hat.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		left_arm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		right_arm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		left_leg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		right_leg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}