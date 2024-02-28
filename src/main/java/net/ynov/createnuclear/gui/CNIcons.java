package net.ynov.createnuclear.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.gui.element.DelegatedStencilElement;
import com.simibubi.create.foundation.gui.element.ScreenElement;
import com.simibubi.create.foundation.utility.Color;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.ynov.createnuclear.CreateNuclear;
import org.joml.Matrix4f;

public class CNIcons implements ScreenElement {

    public static final ResourceLocation REACTOR_CONTROLLER_COMPONENTS = new ResourceLocation(CreateNuclear.MOD_ID, "textures/gui/reactor-controller-components.png");

    private static int x = 0, y = -1;
    private int iconX;
    private int iconY;

    public static final CNIcons
            ON_NORMAL = new CNIcons(84,27),
            OFF_NORMAL = new CNIcons(112,27),
            ON_WARNING = new CNIcons(84,54),

            OFF_WARNING = new CNIcons(112,52);

    public CNIcons(int x, int y) {
        iconX = x;
        iconY = y;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void render(GuiGraphics graphics, int x, int y) {
        graphics.blit(REACTOR_CONTROLLER_COMPONENTS, x, y, 0, iconX, iconY, 16, 16, 256, 256);
    }

}
