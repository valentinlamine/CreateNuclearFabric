package net.nuclearteam.createnuclear.foundation.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.createmod.catnip.gui.UIRenderHelper;
import net.createmod.catnip.gui.element.ScreenElement;
import net.createmod.catnip.theme.Color;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.nuclearteam.createnuclear.CreateNuclear;

public enum CNGuiTextures implements ScreenElement {
    //    REACTOR_CONTROLLER("toolbox", 188, 171),
    REACTOR_CONTROLLER("reactor-controller", 222, 207),
    REACTOR_CONTROLLER_PROGRESS("reactor-controller-components", 24, 13, 20, 162),
    REACTOR_SLOT_INVENTOR("storage-slot", 97, 75),
    CONFIGURED_PATTERN_GUI("configured-pattern-gui", 222, 194),
    ;

    public static final int FONT_COLOR = 0x575F7A;

    public final ResourceLocation location;
    public int width, height;
    public int startX, startY;

    private CNGuiTextures(String location, int width, int height) {
        this(location, 0, 0, width, height);
    }

    private CNGuiTextures(int startX, int startY) {
        this("icons", startX * 16, startY * 16, 16, 16);
    }

    private CNGuiTextures(String location, int startX, int startY, int width, int height) {
        this(CreateNuclear.MOD_ID, location, startX, startY, width, height);
    }

    private CNGuiTextures(String namespace, String location, int startX, int startY, int width, int height) {
        this.location = new ResourceLocation(namespace, "textures/gui/" + location + ".png");
        this.width = width;
        this.height = height;
        this.startX = startX;
        this.startY = startY;
    }

    public void bind() {
        RenderSystem.setShaderTexture(0, location);
    }

    public void render(GuiGraphics graphics, int x, int y) {
        graphics.drawTexture(location, x, y, startX, startY, width, height);
    }

    public void render(GuiGraphics graphics, int x, int y, Color c) {
        bind();
        UIRenderHelper.drawColoredTexture(graphics, c, x, y, startX, startY, width, height);
    }
}
