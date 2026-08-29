package net.nuclearteam.createnuclear.content.explosion;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.core.Direction;
import com.mojang.math.Axis;
import net.minecraft.util.RandomSource;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class CNBasicModelPart {
    public float textureWidth;
    public float textureHeight;
    public int textureOffsetX;
    public int textureOffsetY;
    public float rotationPointX;
    public float rotationPointY;
    public float rotationPointZ;
    public float rotateAngleX;
    public float rotateAngleY;
    public float rotateAngleZ;
    public boolean mirror;
    public boolean showModel;
    private final ObjectList<CNBasicModelPart.ModelBox> cubeList;
    private final ObjectList<CNBasicModelPart> childModels;

    public CNBasicModelPart(CNBasicEntityModel model) {
        this.textureWidth = 64.0F;
        this.textureHeight = 32.0F;
        this.showModel = true;
        this.cubeList = new ObjectArrayList();
        this.childModels = new ObjectArrayList();
        this.setTextureSize(model.textureWidth, model.textureHeight);
    }

    public CNBasicModelPart(CNBasicEntityModel model, int texOffX, int texOffY) {
        this(model.textureWidth, model.textureHeight, texOffX, texOffY);
    }

    public CNBasicModelPart(int textureWidthIn, int textureHeightIn, int textureOffsetXIn, int textureOffsetYIn) {
        this.textureWidth = 64.0F;
        this.textureHeight = 32.0F;
        this.showModel = true;
        this.cubeList = new ObjectArrayList();
        this.childModels = new ObjectArrayList();
        this.setTextureSize(textureWidthIn, textureHeightIn);
        this.setTextureOffset(textureOffsetXIn, textureOffsetYIn);
    }

    private CNBasicModelPart() {
        this.textureWidth = 64.0F;
        this.textureHeight = 32.0F;
        this.showModel = true;
        this.cubeList = new ObjectArrayList();
        this.childModels = new ObjectArrayList();
    }

    public CNBasicModelPart getModelAngleCopy() {
        CNBasicModelPart CNBasicModelPart = new CNBasicModelPart();
        CNBasicModelPart.copyModelAngles(this);
        return CNBasicModelPart;
    }

    public void copyModelAngles(CNBasicModelPart CNBasicModelPartIn) {
        this.rotateAngleX = CNBasicModelPartIn.rotateAngleX;
        this.rotateAngleY = CNBasicModelPartIn.rotateAngleY;
        this.rotateAngleZ = CNBasicModelPartIn.rotateAngleZ;
        this.rotationPointX = CNBasicModelPartIn.rotationPointX;
        this.rotationPointY = CNBasicModelPartIn.rotationPointY;
        this.rotationPointZ = CNBasicModelPartIn.rotationPointZ;
    }

    public void addChild(CNBasicModelPart renderer) {
        this.childModels.add(renderer);
    }

    public CNBasicModelPart setTextureOffset(int x, int y) {
        this.textureOffsetX = x;
        this.textureOffsetY = y;
        return this;
    }

    public CNBasicModelPart addBox(String partName, float x, float y, float z, int width, int height, int depth, float delta, int texX, int texY) {
        this.setTextureOffset(texX, texY);
        this.addBox(this.textureOffsetX, this.textureOffsetY, x, y, z, (float)width, (float)height, (float)depth, delta, delta, delta, this.mirror, false);
        return this;
    }

    public CNBasicModelPart addBox(float x, float y, float z, float width, float height, float depth) {
        this.addBox(this.textureOffsetX, this.textureOffsetY, x, y, z, width, height, depth, 0.0F, 0.0F, 0.0F, this.mirror, false);
        return this;
    }

    public CNBasicModelPart addBox(float x, float y, float z, float width, float height, float depth, boolean mirrorIn) {
        this.addBox(this.textureOffsetX, this.textureOffsetY, x, y, z, width, height, depth, 0.0F, 0.0F, 0.0F, mirrorIn, false);
        return this;
    }

    public void addBox(float x, float y, float z, float width, float height, float depth, float delta) {
        this.addBox(this.textureOffsetX, this.textureOffsetY, x, y, z, width, height, depth, delta, delta, delta, this.mirror, false);
    }

    public void addBox(float x, float y, float z, float width, float height, float depth, float deltaX, float deltaY, float deltaZ) {
        this.addBox(this.textureOffsetX, this.textureOffsetY, x, y, z, width, height, depth, deltaX, deltaY, deltaZ, this.mirror, false);
    }

    public void addBox(float x, float y, float z, float width, float height, float depth, float delta, boolean mirrorIn) {
        this.addBox(this.textureOffsetX, this.textureOffsetY, x, y, z, width, height, depth, delta, delta, delta, mirrorIn, false);
    }

    private void addBox(int texOffX, int texOffY, float x, float y, float z, float width, float height, float depth, float deltaX, float deltaY, float deltaZ, boolean mirorIn, boolean p_228305_13_) {
        this.cubeList.add(new CNBasicModelPart.ModelBox(texOffX, texOffY, x, y, z, width, height, depth, deltaX, deltaY, deltaZ, mirorIn, this.textureWidth, this.textureHeight));
    }

    public void setRotationPoint(float rotationPointXIn, float rotationPointYIn, float rotationPointZIn) {
        this.rotationPointX = rotationPointXIn;
        this.rotationPointY = rotationPointYIn;
        this.rotationPointZ = rotationPointZIn;
    }

    public void render(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn) {
        this.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
    }

    public void render(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if (this.showModel && (!this.cubeList.isEmpty() || !this.childModels.isEmpty())) {
            matrixStackIn.pushPose();
            this.translateRotate(matrixStackIn);
            this.doRender(matrixStackIn.last(), bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
            ObjectListIterator var9 = this.childModels.iterator();

            while(var9.hasNext()) {
                CNBasicModelPart CNBasicModelPart = (CNBasicModelPart)var9.next();
                CNBasicModelPart.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
            }

            matrixStackIn.popPose();
        }

    }

    public void translateRotate(PoseStack matrixStackIn) {
        matrixStackIn.translate((double)(this.rotationPointX / 16.0F), (double)(this.rotationPointY / 16.0F), (double)(this.rotationPointZ / 16.0F));
        if (this.rotateAngleZ != 0.0F) {
            matrixStackIn.mulPose(Axis.ZP.rotation(this.rotateAngleZ));
        }

        if (this.rotateAngleY != 0.0F) {
            matrixStackIn.mulPose(Axis.YP.rotation(this.rotateAngleY));
        }

        if (this.rotateAngleX != 0.0F) {
            matrixStackIn.mulPose(Axis.XP.rotation(this.rotateAngleX));
        }

    }

    private void doRender(PoseStack.Pose matrixEntryIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        Matrix4f matrix4f = matrixEntryIn.pose();
        Matrix3f matrix3f = matrixEntryIn.normal();
        ObjectListIterator var11 = this.cubeList.iterator();

        while(var11.hasNext()) {
            CNBasicModelPart.ModelBox CNBasicModelPart$modelbox = (CNBasicModelPart.ModelBox)var11.next();

            for(CNBasicModelPart.TexturedQuad CNBasicModelPart$texturedquad : CNBasicModelPart$modelbox.quads) {
                Vector3f vector3f = new Vector3f(CNBasicModelPart$texturedquad.normal);
                vector3f.mul(matrix3f);
                float f = vector3f.x();
                float f1 = vector3f.y();
                float f2 = vector3f.z();

                for(int i = 0; i < 4; ++i) {
                    CNBasicModelPart.PositionTextureVertex CNBasicModelPart$positiontexturevertex = CNBasicModelPart$texturedquad.vertexPositions[i];
                    float f3 = CNBasicModelPart$positiontexturevertex.position.x() / 16.0F;
                    float f4 = CNBasicModelPart$positiontexturevertex.position.y() / 16.0F;
                    float f5 = CNBasicModelPart$positiontexturevertex.position.z() / 16.0F;
                    Vector4f vector4f = new Vector4f(f3, f4, f5, 1.0F);
                    vector4f.mul(matrix4f);
                    bufferIn.vertex(vector4f.x(), vector4f.y(), vector4f.z(), red, green, blue, alpha, CNBasicModelPart$positiontexturevertex.textureU, CNBasicModelPart$positiontexturevertex.textureV, packedOverlayIn, packedLightIn, f, f1, f2);
                }
            }
        }

    }

    public CNBasicModelPart setTextureSize(int textureWidthIn, int textureHeightIn) {
        this.textureWidth = (float)textureWidthIn;
        this.textureHeight = (float)textureHeightIn;
        return this;
    }

    public CNBasicModelPart.ModelBox getRandomCube(RandomSource randomIn) {
        return this.cubeList.size() > 0 ? (CNBasicModelPart.ModelBox)this.cubeList.get(randomIn.nextInt(this.cubeList.size())) : null;
    }

    public static class ModelBox {
        private final CNBasicModelPart.TexturedQuad[] quads;
        public final float posX1;
        public final float posY1;
        public final float posZ1;
        public final float posX2;
        public final float posY2;
        public final float posZ2;

        public ModelBox(int texOffX, int texOffY, float x, float y, float z, float width, float height, float depth, float deltaX, float deltaY, float deltaZ, boolean mirorIn, float texWidth, float texHeight) {
            this.posX1 = x;
            this.posY1 = y;
            this.posZ1 = z;
            this.posX2 = x + width;
            this.posY2 = y + height;
            this.posZ2 = z + depth;
            this.quads = new CNBasicModelPart.TexturedQuad[6];
            float f = x + width;
            float f1 = y + height;
            float f2 = z + depth;
            x -= deltaX;
            y -= deltaY;
            z -= deltaZ;
            f += deltaX;
            f1 += deltaY;
            f2 += deltaZ;
            if (mirorIn) {
                float f3 = f;
                f = x;
                x = f3;
            }

            CNBasicModelPart.PositionTextureVertex CNBasicModelPart$positiontexturevertex7 = new CNBasicModelPart.PositionTextureVertex(x, y, z, 0.0F, 0.0F);
            CNBasicModelPart.PositionTextureVertex CNBasicModelPart$positiontexturevertex = new CNBasicModelPart.PositionTextureVertex(f, y, z, 0.0F, 8.0F);
            CNBasicModelPart.PositionTextureVertex CNBasicModelPart$positiontexturevertex1 = new CNBasicModelPart.PositionTextureVertex(f, f1, z, 8.0F, 8.0F);
            CNBasicModelPart.PositionTextureVertex CNBasicModelPart$positiontexturevertex2 = new CNBasicModelPart.PositionTextureVertex(x, f1, z, 8.0F, 0.0F);
            CNBasicModelPart.PositionTextureVertex CNBasicModelPart$positiontexturevertex3 = new CNBasicModelPart.PositionTextureVertex(x, y, f2, 0.0F, 0.0F);
            CNBasicModelPart.PositionTextureVertex CNBasicModelPart$positiontexturevertex4 = new CNBasicModelPart.PositionTextureVertex(f, y, f2, 0.0F, 8.0F);
            CNBasicModelPart.PositionTextureVertex CNBasicModelPart$positiontexturevertex5 = new CNBasicModelPart.PositionTextureVertex(f, f1, f2, 8.0F, 8.0F);
            CNBasicModelPart.PositionTextureVertex CNBasicModelPart$positiontexturevertex6 = new CNBasicModelPart.PositionTextureVertex(x, f1, f2, 8.0F, 0.0F);
            float f4 = (float)texOffX;
            float f5 = (float)texOffX + depth;
            float f6 = (float)texOffX + depth + width;
            float f7 = (float)texOffX + depth + width + width;
            float f8 = (float)texOffX + depth + width + depth;
            float f9 = (float)texOffX + depth + width + depth + width;
            float f10 = (float)texOffY;
            float f11 = (float)texOffY + depth;
            float f12 = (float)texOffY + depth + height;
            this.quads[2] = new CNBasicModelPart.TexturedQuad(new CNBasicModelPart.PositionTextureVertex[]{CNBasicModelPart$positiontexturevertex4, CNBasicModelPart$positiontexturevertex3, CNBasicModelPart$positiontexturevertex7, CNBasicModelPart$positiontexturevertex}, f5, f10, f6, f11, texWidth, texHeight, mirorIn, Direction.DOWN);
            this.quads[3] = new CNBasicModelPart.TexturedQuad(new CNBasicModelPart.PositionTextureVertex[]{CNBasicModelPart$positiontexturevertex1, CNBasicModelPart$positiontexturevertex2, CNBasicModelPart$positiontexturevertex6, CNBasicModelPart$positiontexturevertex5}, f6, f11, f7, f10, texWidth, texHeight, mirorIn, Direction.UP);
            this.quads[1] = new CNBasicModelPart.TexturedQuad(new CNBasicModelPart.PositionTextureVertex[]{CNBasicModelPart$positiontexturevertex7, CNBasicModelPart$positiontexturevertex3, CNBasicModelPart$positiontexturevertex6, CNBasicModelPart$positiontexturevertex2}, f4, f11, f5, f12, texWidth, texHeight, mirorIn, Direction.WEST);
            this.quads[4] = new CNBasicModelPart.TexturedQuad(new CNBasicModelPart.PositionTextureVertex[]{CNBasicModelPart$positiontexturevertex, CNBasicModelPart$positiontexturevertex7, CNBasicModelPart$positiontexturevertex2, CNBasicModelPart$positiontexturevertex1}, f5, f11, f6, f12, texWidth, texHeight, mirorIn, Direction.NORTH);
            this.quads[0] = new CNBasicModelPart.TexturedQuad(new CNBasicModelPart.PositionTextureVertex[]{CNBasicModelPart$positiontexturevertex4, CNBasicModelPart$positiontexturevertex, CNBasicModelPart$positiontexturevertex1, CNBasicModelPart$positiontexturevertex5}, f6, f11, f8, f12, texWidth, texHeight, mirorIn, Direction.EAST);
            this.quads[5] = new CNBasicModelPart.TexturedQuad(new CNBasicModelPart.PositionTextureVertex[]{CNBasicModelPart$positiontexturevertex3, CNBasicModelPart$positiontexturevertex4, CNBasicModelPart$positiontexturevertex5, CNBasicModelPart$positiontexturevertex6}, f8, f11, f9, f12, texWidth, texHeight, mirorIn, Direction.SOUTH);
        }
    }

    static class PositionTextureVertex {
        public final Vector3f position;
        public final float textureU;
        public final float textureV;

        public PositionTextureVertex(float x, float y, float z, float texU, float texV) {
            this(new Vector3f(x, y, z), texU, texV);
        }

        public CNBasicModelPart.PositionTextureVertex setTextureUV(float texU, float texV) {
            return new CNBasicModelPart.PositionTextureVertex(this.position, texU, texV);
        }

        public PositionTextureVertex(Vector3f posIn, float texU, float texV) {
            this.position = posIn;
            this.textureU = texU;
            this.textureV = texV;
        }
    }

    static class TexturedQuad {
        public final CNBasicModelPart.PositionTextureVertex[] vertexPositions;
        public final Vector3f normal;

        public TexturedQuad(CNBasicModelPart.PositionTextureVertex[] positionsIn, float u1, float v1, float u2, float v2, float texWidth, float texHeight, boolean mirrorIn, Direction directionIn) {
            this.vertexPositions = positionsIn;
            float f = 0.0F / texWidth;
            float f1 = 0.0F / texHeight;
            positionsIn[0] = positionsIn[0].setTextureUV(u2 / texWidth - f, v1 / texHeight + f1);
            positionsIn[1] = positionsIn[1].setTextureUV(u1 / texWidth + f, v1 / texHeight + f1);
            positionsIn[2] = positionsIn[2].setTextureUV(u1 / texWidth + f, v2 / texHeight - f1);
            positionsIn[3] = positionsIn[3].setTextureUV(u2 / texWidth - f, v2 / texHeight - f1);
            if (mirrorIn) {
                int i = positionsIn.length;

                for(int j = 0; j < i / 2; ++j) {
                    CNBasicModelPart.PositionTextureVertex CNBasicModelPart$positiontexturevertex = positionsIn[j];
                    positionsIn[j] = positionsIn[i - 1 - j];
                    positionsIn[i - 1 - j] = CNBasicModelPart$positiontexturevertex;
                }
            }

            this.normal = directionIn.step();
            if (mirrorIn) {
                this.normal.mul(-1.0F, 1.0F, 1.0F);
            }

        }
    }
}
