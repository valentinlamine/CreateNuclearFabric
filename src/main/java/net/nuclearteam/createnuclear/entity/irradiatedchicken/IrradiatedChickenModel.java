package net.nuclearteam.createnuclear.entity.irradiatedchicken;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class IrradiatedChickenModel<T extends IrradiatedChicken> extends AgeableListModel<T> {

    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart rightLeg;
    private final ModelPart leftLeg;
    private final ModelPart rightWing;
    private final ModelPart leftWing;
    private final ModelPart beak;
    private final ModelPart redThing;
    private final ModelPart pustule1;
    private final ModelPart pustule2;
    private final ModelPart pustule3;

    public IrradiatedChickenModel(ModelPart root) {
        this.head = root.getChild("head");
        this.beak = root.getChild("beak");
        this.redThing = root.getChild("red_thing");
        this.body = root.getChild("body");
        this.rightLeg = root.getChild("right_leg");
        this.leftLeg = root.getChild("left_leg");
        this.rightWing = root.getChild("right_wing");
        this.leftWing = root.getChild("left_wing");
        this.pustule1 = root.getChild("pustule1");
        this.pustule2 = root.getChild("pustule2");
        this.pustule3 = root.getChild("pustule3");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinition = meshDefinition.getRoot();

        partDefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -6.0F, -2.0F, 4.0F, 6.0F, 3.0F), PartPose.offset(0.0F, 15.0F, -4.0F));
        partDefinition.addOrReplaceChild("beak", CubeListBuilder.create().texOffs(14, 0).addBox(-2.0F, -4.0F, -4.0F, 4.0F, 2.0F, 2.0F), PartPose.offset(0.0F, 15.0F, -4.0F));
        partDefinition.addOrReplaceChild("red_thing", CubeListBuilder.create().texOffs(14, 4).addBox(-1.0F, -2.0F, -3.0F, 2.0F, 2.0F, 2.0F), PartPose.offset(0.0F, 15.0F, -4.0F));
        partDefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 9).addBox(-3.0F, -4.0F, -3.0F, 6.0F, 8.0F, 6.0F), PartPose.offsetAndRotation(0.0F, 16.0F, 0.0F, 1.5707964F, 0.0F, 0.0F));
        CubeListBuilder cubeListBuilder = CubeListBuilder.create().texOffs(26, 0).addBox(-1.0F, 0.0F, -3.0F, 3.0F, 5.0F, 3.0F);
        partDefinition.addOrReplaceChild("right_leg", cubeListBuilder, PartPose.offset(-2.0F, 19.0F, 1.0F));
        partDefinition.addOrReplaceChild("left_leg", cubeListBuilder, PartPose.offset(1.0F, 19.0F, 1.0F));
        partDefinition.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(24, 13).addBox(0.0F, 0.0F, -3.0F, 1.0F, 4.0F, 6.0F), PartPose.offset(-4.0F, 13.0F, 0.0F));
        partDefinition.addOrReplaceChild("left_wing", CubeListBuilder.create().texOffs(24, 13).addBox(-1.0F, 0.0F, -3.0F, 1.0F, 4.0F, 6.0F), PartPose.offset(4.0F, 13.0F, 0.0F));

        partDefinition.addOrReplaceChild("pustule1", CubeListBuilder.create().texOffs(19, 10).addBox(-2.0F, -6.5F, -0.5F, 2.0F, 2.0F, 2.0F), PartPose.offsetAndRotation(0.0F, 15.0F, -4.0F, -0.2233F, 0.2129F, -0.0479F));
        partDefinition.addOrReplaceChild("pustule2", CubeListBuilder.create().texOffs(19, 10).addBox(-2.0F, 0.3F, -1.0F, 2.0F, 2.0F, 2.0F), PartPose.offsetAndRotation(-1.45F, 12.0F, 2.6F, 2.2787F, -1.327F, -2.3973F));
        partDefinition.addOrReplaceChild("pustule3", CubeListBuilder.create().texOffs(19, 10).addBox(-1.0F, -1.0F, -0.6F, 2.0F, 2.0F, 2.0F), PartPose.offsetAndRotation(1.05F, 17.5F, 3.1F, 0.1981F, -0.1324F, 3.0922F));

        return LayerDefinition.create(meshDefinition, 64, 32);
    }

    @Override
    public void setupAnim(@NotNull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.xRot = headPitch * ((float)Math.PI / 180);
        this.head.yRot = netHeadYaw * ((float)Math.PI / 180);
        this.pustule1.xRot = this.head.xRot;
        this.pustule1.yRot = this.head.yRot;
        this.beak.xRot = this.head.xRot;
        this.beak.yRot = this.head.yRot;
        this.redThing.xRot = this.head.xRot;
        this.redThing.yRot = this.head.yRot;
        this.rightLeg.xRot = Mth.cos(limbSwing * 0.6662f) * 1.4f * limbSwingAmount;
        this.leftLeg.xRot = Mth.cos(limbSwing * 0.6662f + (float)Math.PI) * 1.4f * limbSwingAmount;
        this.rightWing.zRot = ageInTicks;
        this.leftWing.zRot = -ageInTicks;
    }

    @Override
    public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        beak.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        redThing.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        rightLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        leftLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        rightWing.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        leftWing.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        pustule1.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        pustule2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        pustule3.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    protected Iterable<ModelPart> headParts() {
        return ImmutableList.of(this.head, this.beak, this.redThing, this.pustule1);
    }

    protected Iterable<ModelPart> bodyParts() {
        return ImmutableList.of(this.body, this.rightLeg, this.leftLeg, this.rightWing, this.leftWing, this.pustule2, this.pustule3);
    }
}
