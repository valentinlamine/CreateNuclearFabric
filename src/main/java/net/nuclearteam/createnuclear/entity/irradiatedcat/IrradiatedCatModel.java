package net.nuclearteam.createnuclear.entity.irradiatedcat;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@SuppressWarnings("unused")
public class IrradiatedCatModel<T extends IrradiatedCat> extends AgeableListModel<T> {
	private static final float PI = (float) Math.PI;
	private static final float HALF_PI = PI / 2;

	private final ModelPart pustule3, pustule2, teeth2, teeth, pustule;
	private final ModelPart head, body;
	private final ModelPart leftFrontLeg, rightFrontLeg, leftHindLeg, rightHindLeg;
	private final ModelPart tail1, tail2;

	private final ImmutableList<ModelPart> headParts;
	private final ImmutableList<ModelPart> bodyParts;

	protected int state = 1;

	public IrradiatedCatModel(ModelPart root) {
		this.pustule = root.getChild("pustule");
		this.pustule3 = root.getChild("pustule3");
		this.pustule2 = root.getChild("pustule2");
		this.teeth2 = root.getChild("teeth2");
		this.teeth = root.getChild("teeth");
		this.head = root.getChild("head");
		this.body = root.getChild("body");
		this.leftFrontLeg = root.getChild("left_front_leg");
		this.rightFrontLeg = root.getChild("right_front_leg");
		this.leftHindLeg = root.getChild("left_hind_leg");
		this.rightHindLeg = root.getChild("right_hind_leg");
		this.tail1 = root.getChild("tail1");
		this.tail2 = root.getChild("tail2");

		this.headParts = ImmutableList.of(this.head, this.pustule);
		this.bodyParts = ImmutableList.of(body, leftHindLeg, rightHindLeg, leftFrontLeg, rightFrontLeg, tail1, tail2, pustule2, pustule3);
	}

	@Override
	protected Iterable<ModelPart> headParts() {
		return headParts;
	}

	@Override
	protected Iterable<ModelPart> bodyParts() {
		return bodyParts;
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition root = meshdefinition.getRoot();

		// Ajout des pustules
		addPustule(root, "pustule", -2.1F, 15.5F, -10.0F, -0.2597F, 0.0338F, 1.6973F);
		addPustule(root, "pustule2", 1.5F, 14.6F, 4.5F, 1.442F, 0.0338F, 1.6973F);
		addPustule(root, "pustule3", 1.5F, 19.6F, -4.5F, 1.8282F, -1.4377F, 3.0129F);

		// Ajout des dents
		addTooth(root, "teeth", 0.8F, 17.1F, -12.2F);
		addTooth(root, "teeth2", -0.8F, 17.1F, -12.2F);

		// Tête
		root.addOrReplaceChild("head", CubeListBuilder.create()
						.texOffs(0, 0).addBox(-2.5F, -2.0F, -3.0F, 5.0F, 4.0F, 5.0F)
						.texOffs(0, 24).addBox(-1.5F, -0.02F, -4.0F, 3.0F, 2.0F, 2.0F)
						.texOffs(0, 10).addBox(-2.0F, -3.0F, 0.0F, 1.0F, 1.0F, 2.0F)
						.texOffs(6, 10).addBox(1.0F, -3.0F, 0.0F, 1.0F, 1.0F, 2.0F),
				PartPose.offset(0.0F, 15.0F, -9.0F)
		);

		// Corps
		root.addOrReplaceChild("body", CubeListBuilder.create()
						.texOffs(20, 0).addBox(-2.0F, 3.0F, -8.0F, 4.0F, 16.0F, 6.0F),
				PartPose.offsetAndRotation(0.0F, 12.0F, -10.0F, HALF_PI, 0.0F, 0.0F)
		);

		// Jambes
		addLeg(root, "left_front_leg", 1.1F, 14.1F, -5.0F, 10, false);
		addLeg(root, "right_front_leg", -1.1F, 14.1F, -5.0F, 10, false);
		addLeg(root, "left_hind_leg", 1.1F, 18.0F, 5.0F, 6, true);
		addLeg(root, "right_hind_leg", -1.1F, 18.0F, 5.0F, 6, true);

		// Queue
		addTailSegment(root, "tail1", 0.0F, 15.0F, 8.0F, 0.9F);
		addTailSegment(root, "tail2", 0.0F, 20.0F, 14.0F, 0.0F);

		return LayerDefinition.create(meshdefinition, 64, 32);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.head.xRot = headPitch * (PI / 180);
		this.head.yRot = netHeadYaw * (PI / 180);

		if (this.state != 3) {
			this.body.xRot = HALF_PI;

			boolean isWalking = (state == 2);
			setupLegs(limbSwing, limbSwingAmount, isWalking);
			setupTail(limbSwing, limbSwingAmount, state);
		}
	}

	private void setupLegs(float swing, float amount, boolean offset) {
		float swingSpeed = 0.6662F;
		float frontOffset = offset ? 0.3F : PI;
		float backOffset = offset ? 0.3F : 0F;

		this.leftHindLeg.xRot = Mth.cos(swing * swingSpeed) * amount;
		this.rightHindLeg.xRot = Mth.cos(swing * swingSpeed + backOffset) * amount;
		this.leftFrontLeg.xRot = Mth.cos(swing * swingSpeed + PI + frontOffset) * amount;
		this.rightFrontLeg.xRot = Mth.cos(swing * swingSpeed + PI - frontOffset) * amount;
	}

	private void setupTail(float swing, float amount, int state) {
		float base = 1.7278761F;
		float factor = switch (state) {
			case 1 -> 0.7853982F;
			case 2 -> 0.31415927F;
			default -> 0.47123894F;
		};
		this.tail2.xRot = base + factor * Mth.cos(swing) * amount;
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		for (ModelPart part : headParts) part.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		for (ModelPart part : bodyParts) part.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	private static void addPustule(PartDefinition root, String name, float x, float y, float z, float rx, float ry, float rz) {
		root.addOrReplaceChild(name, CubeListBuilder.create().texOffs(46, 24)
				.addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F), PartPose.offsetAndRotation(x, y, z, rx, ry, rz));
	}

	private static void addTooth(PartDefinition root, String name, float x, float y, float z) {
		root.addOrReplaceChild(name, CubeListBuilder.create().texOffs(41, 23)
				.addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F), PartPose.offsetAndRotation(x, y, z, 0.3927F, 0.0F, 0.0F));
	}

	private static void addLeg(PartDefinition root, String name, float x, float y, float z, int height, boolean hind) {
		int texX = hind ? 8 : 40;
		int texY = hind ? 13 : 0;
		int dz = hind ? 1 : 0;

		root.addOrReplaceChild(name, CubeListBuilder.create().texOffs(texX, texY)
				.addBox(-1.0F, 0.0F, dz, 2.0F, height, 2.0F), PartPose.offset(x, y, z));
	}

	private static void addTailSegment(PartDefinition root, String name, float x, float y, float z, float rotationX) {
		int texY = name.equals("tail1") ? 15 : 15;
		int texX = name.equals("tail1") ? 0 : 4;
		root.addOrReplaceChild(name, CubeListBuilder.create().texOffs(texX, texY)
				.addBox(-0.5F, 0.0F, 0.0F, 1.0F, 8.0F, 1.0F), PartPose.offsetAndRotation(x, y, z, rotationX, 0.0F, 0.0F));
	}
}