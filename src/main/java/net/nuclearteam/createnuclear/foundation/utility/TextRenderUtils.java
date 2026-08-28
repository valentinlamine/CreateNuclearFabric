package net.nuclearteam.createnuclear.foundation.utility;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import org.joml.Matrix4f;

/**
 * Client-only half of the original mixed-side {@link TextUtils} utility.
 */
public final class TextRenderUtils {
    private TextRenderUtils() {
    }

    public static void renderMultilineDebugText(PoseStack poseStack, MultiBufferSource buffer, int packedLight,
                                                double baseY, boolean transparent, String... lines) {
        double y = baseY + (lines.length / 4.0D);
        for (String line : lines) {
            renderDebugText(poseStack, buffer, packedLight, y, transparent, line);
            y -= 0.25D;
        }
    }

    public static void renderDebugText(PoseStack poseStack, MultiBufferSource buffer, int packedLight,
                                       double y, boolean transparent, String text) {
        poseStack.push();
        poseStack.translate(0.0D, y, 0.0D);
        poseStack.multiply(Minecraft.getInstance().getBlockEntityRenderDispatcher().camera.getRotation());
        poseStack.scale(-0.025F, -0.025F, 0.025F);
        Matrix4f matrix = poseStack.peek().getPositionMatrix();
        float backgroundOpacity = Minecraft.getInstance().options.getTextBackgroundOpacity(0.25F);
        int backgroundColor = (int) (backgroundOpacity * 255.0F) << 24;
        Font font = Minecraft.getInstance().textRenderer;
        float x = (float) (-font.getWidth(text) / 2);
        Font.TextLayerType layer = transparent
            ? Font.TextLayerType.SEE_THROUGH
            : Font.TextLayerType.NORMAL;
        font.draw(text, x, 0, 553648127, false, matrix, buffer, layer, backgroundColor, packedLight);

        if (transparent) {
            font.draw(text, x, 0, -1, false, matrix, buffer, Font.TextLayerType.NORMAL, 0, packedLight);
        }

        poseStack.pop();
    }
}
