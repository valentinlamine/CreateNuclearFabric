package net.nuclearteam.createnuclear.content.multiblock.input.item;

import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.gui.menu.AbstractSimiContainerScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.network.chat.Component;
import net.nuclearteam.createnuclear.foundation.gui.CNGuiTextures;

import static com.simibubi.create.foundation.gui.AllGuiTextures.PLAYER_INVENTORY;

public class ReactorRodInputScreen extends AbstractSimiContainerScreen<ReactorRodInputMenu> {

    protected static final CNGuiTextures background = CNGuiTextures.REACTOR_SLOT_INVENTOR;

    public ReactorRodInputScreen(ReactorRodInputMenu container, Inventory inv, Component title) {
        super(container, inv, title);
    }

    @Override
    protected void init() {
        setWindowSize(background.width, background.height+ 4 + AllGuiTextures.PLAYER_INVENTORY.getHeight());
        setWindowOffset(0,0);
        super.init();
    }

    @Override
    protected void drawBackground(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int invX = getLeftOfCentered(PLAYER_INVENTORY.getWidth());
        int invY = y + background.height + 4;
        renderPlayerInventory(guiGraphics, invX, invY);

        int x = this.x;
        int y = this.y;

        background.render(guiGraphics, x, y);

    }
}
