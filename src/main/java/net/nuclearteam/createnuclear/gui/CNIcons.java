package net.nuclearteam.createnuclear.gui;

import com.simibubi.create.foundation.gui.element.ScreenElement;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.nuclearteam.createnuclear.CreateNuclear;

@SuppressWarnings("unused")
public class CNIcons implements ScreenElement {

    //public static final ResourceLocation REACTOR_CONTROLLER_COMPONENTS = new ResourceLocation(CreateNuclear.MOD_ID, "textures/gui/reactor-controller-components.png");


    private static int x = 0, y = -1;
    private int iconX;
    private int iconY;

    public static final CNIcons
        ON_NORMAL = new CNIcons(84,27),
        OFF_NORMAL = new CNIcons(112,27),
        ON_WARNING = new CNIcons(84,54),
        GRAPHITE_ROD_ICON = new CNIcons(0,178),
        URANIUM_ROD_ICON = new CNIcons(18,178),
        EMPTY_ICON = new CNIcons(0,0),
        OFF_WARNING = new CNIcons(112,52);

    public CNIcons(int x, int y) {
        iconX = x;
        iconY = y;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void render(GuiGraphics graphics, int x, int y) {

    }
}
