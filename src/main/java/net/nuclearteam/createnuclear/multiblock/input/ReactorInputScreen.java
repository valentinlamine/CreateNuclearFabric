package net.nuclearteam.createnuclear.multiblock.input;

import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.gui.menu.AbstractSimiContainerScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.nuclearteam.createnuclear.gui.CNGuiTextures;

import static com.simibubi.create.foundation.gui.AllGuiTextures.PLAYER_INVENTORY;

public class ReactorInputScreen extends AbstractSimiContainerScreen<ReactorInputMenu> {

    protected static final CNGuiTextures background = CNGuiTextures.REACTOR_SLOT_INVENTOR;

    public ReactorInputScreen(ReactorInputMenu container, Inventory inv, Component title) {
        super(container, inv, title);
    }

    @Override
    protected void init() {
        setWindowSize(background.width, background.height+ 4 + AllGuiTextures.PLAYER_INVENTORY.height);
        setWindowOffset(0,0);
        super.init();
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int invX = getLeftOfCentered(PLAYER_INVENTORY.width);
        int invY = topPos + background.height + 4;
        renderPlayerInventory(guiGraphics, invX, invY);

        int x = leftPos;
        int y = topPos;

        background.render(guiGraphics, x, y);

    }
}
