package net.nuclearteam.createnuclear.entity.irradiatedchicken;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.function.BiConsumer;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
@SuppressWarnings("unused")
public class IrradiatedChickenModel<T extends IrradiatedChicken> extends AgeableListModel<T> {

    private final List<ModelPart> headParts;
    private final List<ModelPart> bodyParts;

    public IrradiatedChickenModel(ModelPart root) {
        ModelPart head = root.getChild("head");
        ModelPart beak = root.getChild("beak");
        ModelPart redThing = root.getChild("red_thing");
        ModelPart pustule1 = root.getChild("pustule1");

        this.headParts = ImmutableList.of(head, beak, redThing, pustule1);

        ModelPart body = root.getChild("body");
        ModelPart rightLeg = root.getChild("right_leg");
        ModelPart leftLeg = root.getChild("left_leg");
        ModelPart rightWing = root.getChild("right_wing");
        ModelPart leftWing = root.getChild("left_wing");
        ModelPart pustule2 = root.getChild("pustule2");
        ModelPart pustule3 = root.getChild("pustule3");

        this.bodyParts = ImmutableList.of(body, rightLeg, leftLeg, rightWing, leftWing, pustule2, pustule3);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        record PartData(CubeListBuilder builder, PartPose pose) {}

        BiConsumer<String, PartData> addPart = (name, data) -> root.addOrReplaceChild(name, data.builder, data.pose);

        // Parties fixes
        addPart.accept("head", new PartData(
                CubeListBuilder.create().texOffs(0, 0).addBox(-2F, -6F, -2F, 4F, 6F, 3F),
                PartPose.offset(0F, 15F, -4F)
        ));
        addPart.accept("beak", new PartData(
                CubeListBuilder.create().texOffs(14, 0).addBox(-2F, -4F, -4F, 4F, 2F, 2F),
                PartPose.offset(0F, 15F, -4F)
        ));
        addPart.accept("red_thing", new PartData(
                CubeListBuilder.create().texOffs(14, 4).addBox(-1F, -2F, -3F, 2F, 2F, 2F),
                PartPose.offset(0F, 15F, -4F)
        ));
        addPart.accept("body", new PartData(
                CubeListBuilder.create().texOffs(0, 9).addBox(-3F, -4F, -3F, 6F, 8F, 6F),
                PartPose.offsetAndRotation(0F, 16F, 0F, (float)Math.PI / 2, 0F, 0F)
        ));
        CubeListBuilder legCube = CubeListBuilder.create().texOffs(26, 0).addBox(-1F, 0F, -3F, 3F, 5F, 3F);
        addPart.accept("right_leg", new PartData(legCube, PartPose.offset(-2F, 19F, 1F)));
        addPart.accept("left_leg", new PartData(legCube, PartPose.offset(1F, 19F, 1F)));
        addPart.accept("right_wing", new PartData(
                CubeListBuilder.create().texOffs(24, 13).addBox(0F, 0F, -3F, 1F, 4F, 6F),
                PartPose.offset(-4F, 13F, 0F)
        ));
        addPart.accept("left_wing", new PartData(
                CubeListBuilder.create().texOffs(24, 13).addBox(-1F, 0F, -3F, 1F, 4F, 6F),
                PartPose.offset(4F, 13F, 0F)
        ));

        // Données des pustules (nom, builder, pose)
        List<PartData> pustules = List.of(
                new PartData(
                        CubeListBuilder.create().texOffs(19, 10).addBox(-2F, -6.5F, -0.5F, 2F, 2F, 2F),
                        PartPose.offsetAndRotation(0F, 15F, -4F, -0.2233F, 0.2129F, -0.0479F)
                ),
                new PartData(
                        CubeListBuilder.create().texOffs(19, 10).addBox(-2F, 0.3F, -1F, 2F, 2F, 2F),
                        PartPose.offsetAndRotation(-1.45F, 12F, 2.6F, 2.2787F, -1.327F, -2.3973F)
                ),
                new PartData(
                        CubeListBuilder.create().texOffs(19, 10).addBox(-1F, -1F, -0.6F, 2F, 2F, 2F),
                        PartPose.offsetAndRotation(1.05F, 17.5F, 3.1F, 0.1981F, -0.1324F, 3.0922F)
                )
        );

        for (int i = 0; i < pustules.size(); i++) {
            addPart.accept("pustule" + (i + 1), pustules.get(i));
        }

        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float headXRot = headPitch * ((float) Math.PI / 180);
        float headYRot = netHeadYaw * ((float) Math.PI / 180);

        // Apply to head-related parts
        for (ModelPart part : headParts) {
            part.xRot = headXRot;
            part.yRot = headYRot;
        }

        bodyParts.get(1).xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;                              // right_leg
        bodyParts.get(2).xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount; // left_leg

        bodyParts.get(3).zRot = ageInTicks;      // right_wing
        bodyParts.get(4).zRot = -ageInTicks;     // left_wing
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        headParts.forEach(part -> part.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha));
        bodyParts.forEach(part -> part.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha));
    }

    @Override
    protected Iterable<ModelPart> headParts() {
        return headParts;
    }

    @Override
    protected Iterable<ModelPart> bodyParts() {
        return bodyParts;
    }
}