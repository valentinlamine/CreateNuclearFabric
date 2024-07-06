package net.ynov.createnuclear.multiblock.configuredItem;

import com.simibubi.create.foundation.gui.element.GuiGameElement;
import com.simibubi.create.foundation.gui.menu.AbstractSimiContainerScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.ynov.createnuclear.gui.CNGuiTextures;

import static com.simibubi.create.foundation.gui.AllGuiTextures.PLAYER_INVENTORY;

public class ConfiguredReactorSlotItemScreen extends AbstractSimiContainerScreen<ConfiguredReactorSlotItemMenu> {
    protected static final CNGuiTextures BG = CNGuiTextures.REACTOR_SLOT_INVENTOR;


    public ConfiguredReactorSlotItemScreen(ConfiguredReactorSlotItemMenu container, Inventory inv, Component title) {
        super(container, inv, title);
    }

    @Override
    protected void init() {
        setWindowSize(BG.width, BG.height + 4 + PLAYER_INVENTORY.height);
        setWindowOffset(0, 0);
        super.init();
        //clearWidgets();
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int x = leftPos;
        int y = topPos;

        BG.render(guiGraphics, x, y);

        int invX = getLeftOfCentered(PLAYER_INVENTORY.width);
        int invY = topPos + BG.height + 7;
        renderPlayerInventory(guiGraphics, invX, invY);
        //renderPlayerInventory(guiGraphics, x, y);
    }
}
