// Made with Blockbench 4.12.2
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports
package net.nuclearteam.createnuclear.entity.irradiatedbee;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.ModelUtils;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class IrradiatedBeeModel<T extends IrradiatedBee> extends AgeableListModel<T> {
	//public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "irradiated_bee_corrigé"), "main");
	private final ModelPart body;
	private final ModelPart torso;
	private final ModelPart stinger;
	private final ModelPart antenna;
	private final ModelPart left_antenna;
	private final ModelPart right_antenna;
	private final ModelPart legs;
	private final ModelPart front_legs;
	private final ModelPart middle_legs;
	private final ModelPart back_legs;
	private final ModelPart wings;
	private final ModelPart right_wing;
	private final ModelPart left_wing;
	private final ModelPart pustules;
	private final ModelPart pustule;
	private final ModelPart pustule2;
	private final ModelPart pustule3;
	private float rollAmount;

	public IrradiatedBeeModel(ModelPart root) {
		super(false, 24.0f, 0.0f);
		this.body = root.getChild("body");
		this.torso = this.body.getChild("torso");
		this.stinger = this.body.getChild("stinger");
		this.antenna = this.body.getChild("antenna");
		this.left_antenna = this.antenna.getChild("left_antenna");
		this.right_antenna = this.antenna.getChild("right_antenna");
		this.legs = this.body.getChild("legs");
		this.front_legs = this.legs.getChild("front_legs");
		this.middle_legs = this.legs.getChild("middle_legs");
		this.back_legs = this.legs.getChild("back_legs");
		this.wings = this.body.getChild("wings");
		this.right_wing = this.wings.getChild("right_wing");
		this.left_wing = this.wings.getChild("left_wing");
		this.pustules = this.body.getChild("pustules");
		this.pustule = this.pustules.getChild("pustule");
		this.pustule2 = this.pustules.getChild("pustule2");
		this.pustule3 = this.pustules.getChild("pustule3");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition torso = body.addOrReplaceChild("torso", CubeListBuilder.create().texOffs(0, 0).addBox(-3.5F, -4.0F, -5.0F, 7.0F, 7.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -5.0F, 0.0F));

		PartDefinition stinger = body.addOrReplaceChild("stinger", CubeListBuilder.create().texOffs(24, 7).addBox(0.0F, -1.0F, 5.0F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -5.0F, 0.0F));

		PartDefinition antenna = body.addOrReplaceChild("antenna", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition left_antenna = antenna.addOrReplaceChild("left_antenna", CubeListBuilder.create().texOffs(2, 0).addBox(1.5F, -2.0F, -3.0F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -7.0F, -5.0F));

		PartDefinition right_antenna = antenna.addOrReplaceChild("right_antenna", CubeListBuilder.create().texOffs(2, 3).addBox(-2.5F, -2.0F, -3.0F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -7.0F, -5.0F));

		PartDefinition legs = body.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition front_legs = legs.addOrReplaceChild("front_legs", CubeListBuilder.create().texOffs(28, 1).addBox(-3.0F, 0.0F, 0.0F, 3.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5F, -2.0F, -2.0F));

		PartDefinition middle_legs = legs.addOrReplaceChild("middle_legs", CubeListBuilder.create().texOffs(27, 3).addBox(-4.0F, 0.0F, 0.0F, 5.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5F, -2.0F, 0.0F));

		PartDefinition back_legs = legs.addOrReplaceChild("back_legs", CubeListBuilder.create().texOffs(27, 5).addBox(-4.0F, 0.0F, 0.0F, 5.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5F, -2.0F, 2.0F));

		PartDefinition wings = body.addOrReplaceChild("wings", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition right_wing = wings.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(0, 18).addBox(-9.0F, 0.0F, 0.0F, 9.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.5F, -9.0F, -3.0F));

		PartDefinition left_wing = wings.addOrReplaceChild("left_wing", CubeListBuilder.create().texOffs(0, 18).mirror().addBox(0.0F, 0.0F, 0.0F, 9.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(1.5F, -9.0F, -3.0F));

		PartDefinition pustules = body.addOrReplaceChild("pustules", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition pustule = pustules.addOrReplaceChild("pustule", CubeListBuilder.create().texOffs(35, 17).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, -9.0F, -3.0F, 0.2986F, 0.0651F, -0.2084F));

		PartDefinition pustule2 = pustules.addOrReplaceChild("pustule2", CubeListBuilder.create().texOffs(35, 17).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -2.5F, -3.0F, 3.0809F, -1.0806F, -1.4133F));

		PartDefinition pustule3 = pustules.addOrReplaceChild("pustule3", CubeListBuilder.create().texOffs(35, 17).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, -2.5F, 4.5F, 2.4457F, 1.5263F, -2.1623F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void prepareMobModel(T entity, float limbSwing, float limbSwingAmount, float partialTick) {
		super.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTick);
		this.rollAmount = entity.getRollAmount(partialTick);
		this.stinger.visible = !entity.hasStung();
	}

	@Override
	public void setupAnim(IrradiatedBee entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		float f;
		boolean bl;
		this.right_wing.xRot = 0.0f;
		this.left_antenna.xRot = 0.0f;
		this.right_antenna.xRot = 0.0f;
		this.body.xRot = 0.0f;
		boolean bl2 = bl = entity.onGround() && entity.getDeltaMovement().lengthSqr() < 1.0E-7;
		if (bl) {
			this.right_wing.yRot = -0.2618f;
			this.right_wing.zRot = 0.0f;
			this.left_wing.xRot = 0.0f;
			this.left_wing.yRot = 0.2618f;
			this.left_wing.zRot = 0.0f;
			this.front_legs.xRot = 0.0f;
			this.middle_legs.xRot = 0.0f;
			this.back_legs.xRot = 0.0f;
		} else {
			f = ageInTicks * 120.32113f * ((float)Math.PI / 180);
			this.right_wing.yRot = 0.0f;
			this.right_wing.zRot = Mth.cos(f) * (float)Math.PI * 0.15f;
			this.left_wing.xRot = this.right_wing.xRot;
			this.left_wing.yRot = this.right_wing.yRot;
			this.left_wing.zRot = -this.right_wing.zRot;
			this.front_legs.xRot = 0.7853982f;
			this.middle_legs.xRot = 0.7853982f;
			this.back_legs.xRot = 0.7853982f;
			this.body.xRot = 0.0f;
			this.body.yRot = 0.0f;
			this.body.zRot = 0.0f;
		}
		if (!entity.isAngry()) {
			this.body.xRot = 0.0f;
			this.body.yRot = 0.0f;
			this.body.zRot = 0.0f;
			if (!bl) {
				f = Mth.cos(ageInTicks * 0.18f);
				this.body.xRot = 0.1f + f * (float)Math.PI * 0.025f;
				this.left_antenna.xRot = f * (float)Math.PI * 0.03f;
				this.right_antenna.xRot = f * (float)Math.PI * 0.03f;
				this.front_legs.xRot = -f * (float)Math.PI * 0.1f + 0.3926991f;
				this.back_legs.xRot = -f * (float)Math.PI * 0.05f + 0.7853982f;
				this.body.y = 19.0f - Mth.cos(ageInTicks * 0.18f) * 0.9f;
			}
		}
		if (this.rollAmount > 0.0f) {
			this.body.xRot = ModelUtils.rotlerpRad(this.body.xRot, 3.0915928f, this.rollAmount);
		}
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	protected Iterable<ModelPart> headParts() {
		return ImmutableList.of();
	}

	@Override
	protected Iterable<ModelPart> bodyParts() {
		return ImmutableList.of(this.body);
	}
}