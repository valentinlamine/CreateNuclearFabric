package net.nuclearteam.createnuclear.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.simibubi.create.foundation.gui.UIRenderHelper;
import com.simibubi.create.foundation.gui.element.ScreenElement;
import com.simibubi.create.foundation.utility.Color;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.nuclearteam.createnuclear.CreateNuclear;

@SuppressWarnings( "unused")
public enum CNGuiTextures implements ScreenElement {
    REACTOR_CONTROLLER("reactor-controller", 222, 207),
    REACTOR_CONTROLLER_PROGRESS("reactor-controller-components", 24, 13, 20, 162),
    REACTOR_SLOT_INVENTOR("storage-slot", 97, 75),
    CONFIGURED_PATTERN_GUI("configured-pattern-gui", 222, 194),
    ;

    public static final int FONT_COLOR = 0x575F7A;

    public final ResourceLocation location;
    public final int width;
    public final int height;
    public final int startX;
    public final int startY;

    CNGuiTextures(String location, int width, int height) {
        this(location, 0, 0, width, height);
    }

    CNGuiTextures(int startX, int startY) {
        this("icons", startX * 16, startY * 16, 16, 16);
    }

    CNGuiTextures(String location, int startX, int startY, int width, int height) {
        this(CreateNuclear.MOD_ID, location, startX, startY, width, height);
    }

    CNGuiTextures(String namespace, String location, int startX, int startY, int width, int height) {
        this.location = new ResourceLocation(namespace, "textures/gui/" + location + ".png");
        this.width = width;
        this.height = height;
        this.startX = startX;
        this.startY = startY;
    }

    @Environment(EnvType.CLIENT)
    public void bind() {
        RenderSystem.setShaderTexture(0, location);
    }

    @Environment(EnvType.CLIENT)
    public void render(GuiGraphics graphics, int x, int y) {
        graphics.blit(location, x, y, startX, startY, width, height);
    }

    @Environment(EnvType.CLIENT)
    public void render(GuiGraphics graphics, int x, int y, Color c) {
        bind();
        UIRenderHelper.drawColoredTexture(graphics, c, x, y, startX, startY, width, height);
    }
}
