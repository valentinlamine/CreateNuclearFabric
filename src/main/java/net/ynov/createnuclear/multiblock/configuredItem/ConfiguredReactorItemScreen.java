package net.ynov.createnuclear.multiblock.configuredItem;

import com.simibubi.create.AllItems;
import com.simibubi.create.foundation.gui.element.GuiGameElement;
import com.simibubi.create.foundation.gui.menu.AbstractSimiContainerScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.ynov.createnuclear.gui.CNGuiTextures;
import net.ynov.createnuclear.gui.CNIconButton;
import net.ynov.createnuclear.gui.CNIcons;
import net.ynov.createnuclear.item.CNItems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfiguredReactorItemScreen  extends AbstractSimiContainerScreen<ConfiguredReactorItemMenu> {
    protected static final CNGuiTextures BG = CNGuiTextures.REACTOR_CONTROLLER;
    //protected static final CNGuiTextures BG = CNGuiTextures.REACTOR_SLOT_INVENTOR;
    protected static final CNGuiTextures PROGRESS_BAR = CNGuiTextures.REACTOR_CONTROLLER_PROGRESS;
    private final Map<Integer, CNIconButton> switchButtons = new HashMap<>();
    private final Map<Integer, Integer> patternButtons = new HashMap<>();


    public ConfiguredReactorItemScreen(ConfiguredReactorItemMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
    }

    @Override
    protected void init() {
        setWindowSize(BG.width, BG.height);
        setWindowOffset(0, 0);
        super.init();
        clearWidgets();

        //placeSwitchItem();
    }


    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int x = leftPos;
        int y = topPos;

        BG.render(guiGraphics, x, y);
        guiGraphics.drawString(font, title, x + 15, y + 4, 0x592424, false);

        int width = PROGRESS_BAR.width;

        float heightProgress = (PROGRESS_BAR.height * Mth.lerp(partialTick, 12/100, 13/100));

        guiGraphics.blit(PROGRESS_BAR.location, x + 179, (int) (y + 40 + (PROGRESS_BAR.height - heightProgress)),
                PROGRESS_BAR.startX, (int) (176 - heightProgress), width, (int) heightProgress);

        /*GuiGameElement.of(menu.contentHolder).<GuiGameElement
                        .GuiRenderBuilder>at(x + BG.width + 8, y + BG.height - 52, -200)
                .scale(4)
                .render(guiGraphics);*/
    }

    private void placeSwitchItem() {
        int startWidth = 7;
        int startHeight = 39;
        int incr = 18;
        int i = 0;
        int[][] positions = {
                {3, 0}, {4, 0}, {5, 0},
                {2, 1}, {3, 1}, {4, 1}, {5, 1}, {6, 1},
                {1, 2}, {2, 2}, {3, 2}, {4, 2}, {5, 2}, {6, 2}, {7, 2},
                {0, 3}, {1, 3}, {2, 3}, {3, 3}, {4, 3}, {5, 3}, {6, 3}, {7, 3}, {8, 3},
                {0, 4}, {1, 4}, {2, 4}, {3, 4}, {4, 4}, {5, 4}, {6, 4}, {7, 4}, {8, 4},
                {0, 5}, {1, 5}, {2, 5}, {3, 5}, {4, 5}, {5, 5}, {6, 5}, {7, 5}, {8, 5},
                {1, 6}, {2, 6}, {3, 6}, {4, 6}, {5, 6}, {6, 6}, {7, 6},
                {2, 7}, {3, 7}, {4, 7}, {5, 7}, {6, 7},
                {3, 8}, {4, 8}, {5, 8}
        };

        if (patternButtons.isEmpty()) {
            for (int[] pos : positions) {// up and down not middle
                switchButtons.put(i, new CNIconButton(leftPos + startWidth + incr * pos[0], topPos + startHeight + incr * pos[1], CNIcons.EMPTY_ICON));
                patternButtons.put(i, conversionIconInt(CNIcons.EMPTY_ICON));
                i++;
            }
        }
        else {
            for (int[] pos : positions) {// up and down not middle
                switchButtons.put(i, new CNIconButton(leftPos + startWidth + incr * pos[0], topPos + startHeight + incr * pos[1], conversionIntIcon(patternButtons.get(i))));
                i++;
            }
        }

        for (int j = 0; j < switchButtons.size(); j++) {
            int finalJ = j;
            switchButtons.get(j).withCallback(() -> handleButtonClick(switchButtons, finalJ));
        }

        addRenderableWidgets(switchButtons.values());
    }

    private void handleButtonClick(Map<Integer, CNIconButton> button, int index) {
        if (button.get(index).getIcon() == CNIcons.EMPTY_ICON) {
            button.get(index).setIcon(CNIcons.URANIUM_ROD_ICON);
            patternButtons.replace(index, conversionIconInt(CNIcons.EMPTY_ICON), conversionIconInt(CNIcons.URANIUM_ROD_ICON));
        }
        else if (button.get(index).getIcon() == CNIcons.URANIUM_ROD_ICON) {
            button.get(index).setIcon(CNIcons.GRAPHITE_ROD_ICON);
            patternButtons.replace(index, conversionIconInt(CNIcons.URANIUM_ROD_ICON), conversionIconInt(CNIcons.GRAPHITE_ROD_ICON));
        }
        else {
            button.get(index).setIcon(CNIcons.EMPTY_ICON);
            patternButtons.replace(index, conversionIconInt(CNIcons.GRAPHITE_ROD_ICON), conversionIconInt(CNIcons.EMPTY_ICON));
        }
    }

    private int conversionIconInt(CNIcons icons) {
        return icons == CNIcons.URANIUM_ROD_ICON
                ? -1
                : icons == CNIcons.GRAPHITE_ROD_ICON
                    ? 1
                    : icons == CNIcons.EMPTY_ICON ? 0 : 999;
    }

    private CNIcons conversionIntIcon(int index) {
        return switch (index) {
            case 1 -> CNIcons.GRAPHITE_ROD_ICON;
            case -1 -> CNIcons.URANIUM_ROD_ICON;
            default -> CNIcons.EMPTY_ICON;
        };
    }


}
