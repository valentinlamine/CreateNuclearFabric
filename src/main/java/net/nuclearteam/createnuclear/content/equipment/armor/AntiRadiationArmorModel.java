package net.nuclearteam.createnuclear.content.equipment.armor;


import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;

public class AntiRadiationArmorModel extends HumanoidModel<LivingEntity> {
	private final ModelPart head;
	private final ModelPart body;
	private final ModelPart right_arm;
	private final ModelPart left_arm;
	private final ModelPart right_leg;
	private final ModelPart left_leg;
	private final ModelPart left_boot;
	private final ModelPart right_boot;

	// Set per render pass by AntiRadiationArmorClientExtensions so renderToBuffer can show
	// the legs for LEGS (leggings) but only the boots for FEET (boots) — Forge makes the
	// vanilla leg parts visible for BOTH slots, so we disambiguate here.
	public EquipmentSlot currentSlot = EquipmentSlot.HEAD;

	public AntiRadiationArmorModel(ModelPart root) {
		super(root);
		this.head = root.getChild("head");
		this.body = root.getChild("body");
		this.right_arm = root.getChild("right_arm");
		this.left_arm = root.getChild("left_arm");
		this.right_leg = root.getChild("right_leg");
		this.left_leg = root.getChild("left_leg");
		this.left_boot = root.getChild("left_boot");
		this.right_boot = root.getChild("right_boot");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		// HumanoidModel's constructor requires a "hat" child; keep it empty.
		partdefinition.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.NONE);

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(30, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.2F))
		.texOffs(30, 16).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 19).addBox(-3.7F, 0.0F, -2.8F, 8.0F, 12.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-4.4F, -1.0F, -3.1F, 9.0F, 13.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(0, 36).addBox(-4.0F, 0.0F, -2.7F, 8.0F, 12.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(64, 0).addBox(-4.5F, -1.0F, -3.0F, 9.0F, 13.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition right_arm = partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(45, 32).mirrored().addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.45F)).mirrored(false)
		.texOffs(29, 32).mirrored().addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.55F)).mirrored(false), PartPose.offset(-5.0F, 2.0F, 0.0F));

		PartDefinition left_arm = partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(45, 32).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.4F))
		.texOffs(29, 32).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offset(5.0F, 2.0F, 0.0F));

		PartDefinition right_leg = partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(29, 48).mirrored().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirrored(false)
		.texOffs(45, 48).mirrored().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)).mirrored(false), PartPose.offset(1.9F, 12.0F, 0.0F));

		PartDefinition left_leg = partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(29, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(45, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(-1.9F, 12.0F, 0.0F));

		partdefinition.addOrReplaceChild("left_boot", CubeListBuilder.create().texOffs(61, 39).addBox(-2.0F, 8.3F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(-1.9F, 12.0F, 0.0F));

		partdefinition.addOrReplaceChild("right_boot", CubeListBuilder.create().texOffs(61, 39).mirrored().addBox(-2.0F, 8.3F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.25F)).mirrored(false), PartPose.offset(1.9F, 12.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 96, 96);
	}

	@Override
	public void render(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		// Make the boots follow the (already animated) legs.
		this.right_boot.copyTransform(this.right_leg);
		this.left_boot.copyTransform(this.left_leg);

		// Control part visibility based on the slot being rendered
		this.head.visible = this.currentSlot == EquipmentSlot.HEAD;
		this.body.visible = this.currentSlot == EquipmentSlot.CHEST;
		this.right_arm.visible = this.currentSlot == EquipmentSlot.CHEST;
		this.left_arm.visible = this.currentSlot == EquipmentSlot.CHEST;
		this.right_leg.visible = this.currentSlot == EquipmentSlot.LEGS;
		this.left_leg.visible = this.currentSlot == EquipmentSlot.LEGS;
		this.right_boot.visible = this.currentSlot == EquipmentSlot.FEET;
		this.left_boot.visible = this.currentSlot == EquipmentSlot.FEET;

		if (this.head.visible) head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		if (this.body.visible) body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		if (this.right_arm.visible) right_arm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		if (this.left_arm.visible) left_arm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		if (this.right_leg.visible) right_leg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		if (this.left_leg.visible) left_leg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		if (this.right_boot.visible) right_boot.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		if (this.left_boot.visible) left_boot.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}
