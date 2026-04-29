package net.nuclearteam.createnuclear.content.contraptions.irradiated.cow;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class IrradiatedCowModel<T extends IrradiatedCow> extends AgeableListModel<T> {
	private final ModelPart head;
	private final ModelPart pustule;
	private final ModelPart body;
	private final ModelPart pustule3;
	private final ModelPart legs;
	private final ModelPart leg1;
	private final ModelPart leg2;
	private final ModelPart leg3;
	private final ModelPart leg4;
	private final ModelPart pustule2;

	public IrradiatedCowModel(ModelPart root) {
        this.head = root.getChild("head");
        this.pustule = this.head.getChild("pustule");
        this.body = root.getChild("body");
        this.pustule3 = this.body.getChild("pustule3");
        this.legs = root.getChild("legs");
        this.leg1 = this.legs.getChild("leg1");
        this.leg2 = this.legs.getChild("leg2");
        this.leg3 = this.legs.getChild("leg3");
        this.leg4 = this.legs.getChild("leg4");
        this.pustule2 = this.leg4.getChild("pustule2");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -4.0F, -6.0F, 8.0F, 8.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(22, 0).addBox(4.0F, -5.0F, -4.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(22, 0).addBox(-5.0F, -5.0F, -4.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.0F, -8.0F));

        PartDefinition pustule = head.addOrReplaceChild("pustule", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition pustule_head_r1 = pustule.addOrReplaceChild("pustule_head_r1", CubeListBuilder.create().texOffs(54, 9).addBox(-1.1F, -0.8F, -0.6F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.4F, -4.0F, -1.0F, -0.2739F, 0.2947F, -0.0814F));

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(18, 4).addBox(-6.0F, -10.0F, -7.0F, 12.0F, 18.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(52, 0).addBox(-2.0F, 2.0F, -8.0F, 4.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 5.0F, 2.0F, 1.5708F, 0.0F, 0.0F));

        PartDefinition pustule3 = body.addOrReplaceChild("pustule3", CubeListBuilder.create(), PartPose.offsetAndRotation(1.5724F, 9.1193F, -7.1515F, 2.8289F, -0.5338F, 2.1369F));

        PartDefinition pustule_back_r1 = pustule3.addOrReplaceChild("pustule_back_r1", CubeListBuilder.create().texOffs(54, 9).addBox(3.4F, 0.0F, -4.7F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.309F, -2.9436F, -13.0622F, 2.0131F, -0.1273F, -1.056F));

        PartDefinition pustule_belly_r1 = pustule3.addOrReplaceChild("pustule_belly_r1", CubeListBuilder.create().texOffs(54, 9).addBox(-2.0F, -0.6F, 5.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -2.0159F, 0.1667F, 2.0666F));

        PartDefinition legs = partdefinition.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition leg1 = legs.addOrReplaceChild("leg1", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, -12.0F, 7.0F));

        PartDefinition leg2 = legs.addOrReplaceChild("leg2", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, -12.0F, 7.0F));

        PartDefinition leg3 = legs.addOrReplaceChild("leg3", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -1.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, -12.0F, -6.0F));

        PartDefinition leg4 = legs.addOrReplaceChild("leg4", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -1.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, -12.0F, -6.0F));

        PartDefinition pustule2 = leg4.addOrReplaceChild("pustule2", CubeListBuilder.create(), PartPose.offsetAndRotation(0.4249F, 7.2997F, 2.3458F, -2.5307F, 0.0F, 0.0F));

        PartDefinition pustule_leg_r1 = pustule2.addOrReplaceChild("pustule_leg_r1", CubeListBuilder.create().texOffs(54, 9).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.1064F, 2.3852F, -0.6084F, 0.1272F, 0.3997F, 1.5682F));

        return LayerDefinition.create(meshdefinition, 64, 32);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.xRot = headPitch * ((float)Math.PI / 180F);
        this.head.yRot = netHeadYaw * ((float)Math.PI / 180F);
        this.leg3.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.leg4.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
        this.leg1.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
        this.leg2.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        legs.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
    @Override
    protected Iterable<ModelPart> headParts() {
        return ImmutableList.of(this.head);
    }

    @Override
    protected Iterable<ModelPart> bodyParts() {
        return ImmutableList.of(this.body, this.leg1, this.leg2, this.leg3, this.leg4);
    }

}