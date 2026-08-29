package net.nuclearteam.createnuclear.content.explosion;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class CNAdvancedModelBox extends CNBasicModelPart {
    public float defaultRotationX;
    public float defaultRotationY;
    public float defaultRotationZ;
    public float defaultPositionX;
    public float defaultPositionY;
    public float defaultPositionZ;
    public float scaleX;
    public float scaleY;
    public float scaleZ;
    public int textureOffsetX;
    public int textureOffsetY;
    public boolean scaleChildren;
    private CNAdvancedEntityModel model;
    private CNAdvancedModelBox parent;
    public ObjectList<CNTabulaModelRenderUtils.ModelBox> cubeList;
    public ObjectList<CNBasicModelPart> childModels;
    private float textureWidth;
    private float textureHeight;
    public String boxName;

    public CNAdvancedModelBox(CNAdvancedEntityModel model, String name) {
        super(model);
        this.scaleX = 1.0F;
        this.scaleY = 1.0F;
        this.scaleZ = 1.0F;
        this.boxName = "";
        this.textureWidth = (float)model.texWidth;
        this.textureHeight = (float)model.texHeight;
        this.model = model;
        this.cubeList = new ObjectArrayList();
        this.childModels = new ObjectArrayList();
        this.boxName = name;
    }

    public CNAdvancedModelBox(CNAdvancedEntityModel model) {
        this(model, null);
        this.textureWidth = (float)model.texWidth;
        this.textureHeight = (float)model.texHeight;
        this.cubeList = new ObjectArrayList();
        this.childModels = new ObjectArrayList();
    }

    public CNBasicModelPart addBox(String p_217178_1_, float p_217178_2_, float p_217178_3_, float p_217178_4_, int p_217178_5_, int p_217178_6_, int p_217178_7_, float p_217178_8_, int p_217178_9_, int p_217178_10_) {
        this.setTextureOffset(p_217178_9_, p_217178_10_);
        this.addBox(this.textureOffsetX, this.textureOffsetY, p_217178_2_, p_217178_3_, p_217178_4_, (float)p_217178_5_, (float)p_217178_6_, (float)p_217178_7_, p_217178_8_, p_217178_8_, p_217178_8_, this.mirror, false);
        return this;
    }

    public CNBasicModelPart addBox(float p_228300_1_, float p_228300_2_, float p_228300_3_, float p_228300_4_, float p_228300_5_, float p_228300_6_) {
        this.addBox(this.textureOffsetX, this.textureOffsetY, p_228300_1_, p_228300_2_, p_228300_3_, p_228300_4_, p_228300_5_, p_228300_6_, 0.0F, 0.0F, 0.0F, this.mirror, false);
        return this;
    }

    public CNBasicModelPart addBox(float p_228304_1_, float p_228304_2_, float p_228304_3_, float p_228304_4_, float p_228304_5_, float p_228304_6_, boolean p_228304_7_) {
        this.addBox(this.textureOffsetX, this.textureOffsetY, p_228304_1_, p_228304_2_, p_228304_3_, p_228304_4_, p_228304_5_, p_228304_6_, 0.0F, 0.0F, 0.0F, p_228304_7_, false);
        return this;
    }

    public void addBox(float p_228301_1_, float p_228301_2_, float p_228301_3_, float p_228301_4_, float p_228301_5_, float p_228301_6_, float p_228301_7_) {
        this.addBox(this.textureOffsetX, this.textureOffsetY, p_228301_1_, p_228301_2_, p_228301_3_, p_228301_4_, p_228301_5_, p_228301_6_, p_228301_7_, p_228301_7_, p_228301_7_, this.mirror, false);
    }

    public void addBox(float p_228302_1_, float p_228302_2_, float p_228302_3_, float p_228302_4_, float p_228302_5_, float p_228302_6_, float p_228302_7_, float p_228302_8_, float p_228302_9_) {
        this.addBox(this.textureOffsetX, this.textureOffsetY, p_228302_1_, p_228302_2_, p_228302_3_, p_228302_4_, p_228302_5_, p_228302_6_, p_228302_7_, p_228302_8_, p_228302_9_, this.mirror, false);
    }

    public void addBox(float p_228303_1_, float p_228303_2_, float p_228303_3_, float p_228303_4_, float p_228303_5_, float p_228303_6_, float p_228303_7_, boolean p_228303_8_) {
        this.addBox(this.textureOffsetX, this.textureOffsetY, p_228303_1_, p_228303_2_, p_228303_3_, p_228303_4_, p_228303_5_, p_228303_6_, p_228303_7_, p_228303_7_, p_228303_7_, p_228303_8_, false);
    }

    private void addBox(int p_228305_1_, int p_228305_2_, float p_228305_3_, float p_228305_4_, float p_228305_5_, float p_228305_6_, float p_228305_7_, float p_228305_8_, float p_228305_9_, float p_228305_10_, float p_228305_11_, boolean p_228305_12_, boolean p_228305_13_) {
        this.cubeList.add(new CNTabulaModelRenderUtils.ModelBox(p_228305_1_, p_228305_2_, p_228305_3_, p_228305_4_, p_228305_5_, p_228305_6_, p_228305_7_, p_228305_8_, p_228305_9_, p_228305_10_, p_228305_11_, p_228305_12_, this.textureWidth, this.textureHeight));
    }

    public void setScale(float scaleX, float scaleY, float scaleZ) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.scaleZ = scaleZ;
    }

    public void updateDefaultPose() {
        this.defaultRotationX = this.rotateAngleX;
        this.defaultRotationY = this.rotateAngleY;
        this.defaultRotationZ = this.rotateAngleZ;
        this.defaultPositionX = this.rotationPointX;
        this.defaultPositionY = this.rotationPointY;
        this.defaultPositionZ = this.rotationPointZ;
    }

    public void resetToDefaultPose() {
        this.rotateAngleX = this.defaultRotationX;
        this.rotateAngleY = this.defaultRotationY;
        this.rotateAngleZ = this.defaultRotationZ;
        this.rotationPointX = this.defaultPositionX;
        this.rotationPointY = this.defaultPositionY;
        this.rotationPointZ = this.defaultPositionZ;
    }

    public void addChild(CNBasicModelPart child) {
        super.addChild(child);
        this.childModels.add(child);
        if (child instanceof CNAdvancedModelBox advancedChild) {
            advancedChild.setParent(this);
        }
    }

    public CNAdvancedModelBox getParent() {
        return this.parent;
    }

    public void setParent(CNAdvancedModelBox parent) {
        this.parent = parent;
    }

    public void translateAndRotate(PoseStack matrixStackIn) {
        matrixStackIn.translate(this.rotationPointX / 16.0F, this.rotationPointY / 16.0F, (double)(this.rotationPointZ / 16.0F));
        if (this.rotateAngleZ != 0.0F) {
            matrixStackIn.mulPose(Axis.ZP.rotation(this.rotateAngleZ));
        }

        if (this.rotateAngleY != 0.0F) {
            matrixStackIn.mulPose(Axis.YP.rotation(this.rotateAngleY));
        }

        if (this.rotateAngleX != 0.0F) {
            matrixStackIn.mulPose(Axis.XP.rotation(this.rotateAngleX));
        }

        matrixStackIn.scale(this.scaleX, this.scaleY, this.scaleZ);
    }

    public void render(PoseStack p_228309_1_, VertexConsumer p_228309_2_, int p_228309_3_, int p_228309_4_, float p_228309_5_, float p_228309_6_, float p_228309_7_, float p_228309_8_) {
        if (this.showModel && (!this.cubeList.isEmpty() || !this.childModels.isEmpty())) {
            p_228309_1_.pushPose();
            this.translateAndRotate(p_228309_1_);
            this.doRender(p_228309_1_.last(), p_228309_2_, p_228309_3_, p_228309_4_, p_228309_5_, p_228309_6_, p_228309_7_, p_228309_8_);
            ObjectListIterator var9 = this.childModels.iterator();
            if (!this.scaleChildren) {
                p_228309_1_.scale(1.0F / Math.max(this.scaleX, 1.0E-4F), 1.0F / Math.max(this.scaleY, 1.0E-4F), 1.0F / Math.max(this.scaleZ, 1.0E-4F));
            }

            while(var9.hasNext()) {
                CNBasicModelPart lvt_10_1_ = (CNBasicModelPart)var9.next();
                lvt_10_1_.render(p_228309_1_, p_228309_2_, p_228309_3_, p_228309_4_, p_228309_5_, p_228309_6_, p_228309_7_, p_228309_8_);
            }

            p_228309_1_.popPose();
        }

    }

    private void doRender(PoseStack.Pose p_228306_1_, VertexConsumer p_228306_2_, int p_228306_3_, int p_228306_4_, float p_228306_5_, float p_228306_6_, float p_228306_7_, float p_228306_8_) {
        Matrix4f lvt_9_1_ = p_228306_1_.pose();
        Matrix3f lvt_10_1_ = p_228306_1_.normal();
        ObjectListIterator var11 = this.cubeList.iterator();

        while(var11.hasNext()) {
            CNTabulaModelRenderUtils.ModelBox lvt_12_1_ = (CNTabulaModelRenderUtils.ModelBox)var11.next();

            for(CNTabulaModelRenderUtils.TexturedQuad lvt_16_1_ : lvt_12_1_.quads) {
                Vector3f lvt_17_1_ = new Vector3f(lvt_16_1_.normal);
                lvt_17_1_.mul(lvt_10_1_);
                float lvt_18_1_ = lvt_17_1_.x();
                float lvt_19_1_ = lvt_17_1_.y();
                float lvt_20_1_ = lvt_17_1_.z();

                for(int lvt_21_1_ = 0; lvt_21_1_ < 4; ++lvt_21_1_) {
                    CNTabulaModelRenderUtils.PositionTextureVertex lvt_22_1_ = lvt_16_1_.vertexPositions[lvt_21_1_];
                    float lvt_23_1_ = lvt_22_1_.position.x() / 16.0F;
                    float lvt_24_1_ = lvt_22_1_.position.y() / 16.0F;
                    float lvt_25_1_ = lvt_22_1_.position.z() / 16.0F;
                    Vector4f lvt_26_1_ = new Vector4f(lvt_23_1_, lvt_24_1_, lvt_25_1_, 1.0F);
                    lvt_26_1_.mul(lvt_9_1_);
                    p_228306_2_.vertex(lvt_26_1_.x(), lvt_26_1_.y(), lvt_26_1_.z(), p_228306_5_, p_228306_6_, p_228306_7_, p_228306_8_, lvt_22_1_.textureU, lvt_22_1_.textureV, p_228306_4_, p_228306_3_, lvt_18_1_, lvt_19_1_, lvt_20_1_);
                }
            }
        }

    }

    public CNAdvancedEntityModel getModel() {
        return this.model;
    }

    public CNAdvancedModelBox setTextureOffset(int textureOffsetX, int textureOffsetY) {
        this.textureOffsetX = textureOffsetX;
        this.textureOffsetY = textureOffsetY;
        return this;
    }
}
