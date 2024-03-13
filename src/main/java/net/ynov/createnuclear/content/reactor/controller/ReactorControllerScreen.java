package net.ynov.createnuclear.content.reactor.controller;

import com.simibubi.create.foundation.gui.menu.AbstractSimiContainerScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.ynov.createnuclear.CreateNuclear;
import net.ynov.createnuclear.gui.CNGuiTextures;
import net.ynov.createnuclear.gui.CNIconButton;
import net.ynov.createnuclear.gui.CNIcons;

import java.util.ArrayList;
import java.util.List;

public class ReactorControllerScreen extends AbstractSimiContainerScreen<ReactorControllerMenu> {
    protected static final CNGuiTextures BG = CNGuiTextures.REACTOR_CONTROLLER;
    protected static final CNGuiTextures PROGRESS_BAR = CNGuiTextures.REACTOR_CONTROLLER_PROGRESS;
    private CNIconButton powerButton;
    private float progress;
    private float chasingProgress;
    private float lastChasingProgress;

    public ReactorControllerScreen(ReactorControllerMenu container, Inventory inv, Component title) {
        super(container, inv, title);
    }

    @Override
    protected void init() {
        setWindowSize(BG.width, BG.height);
        setWindowOffset(0, 0);
        super.init();
        clearWidgets();

        //S'initialise a chaque fois que l'on click sur le block


        placeSwitchItem();


        powerButton = menu.contentHolder.isPowered() ? new CNIconButton(leftPos +  BG.width - 25, topPos + 7, CNIcons.OFF_NORMAL) : new CNIconButton(leftPos +  BG.width - 25, topPos + 7, CNIcons.ON_NORMAL);
        powerButton.withCallback(() -> {// Quand le button est appuyé il fait ca
            Boolean powered = menu.contentHolder.isPowered();
            if (powered!= null && !powered) {
                menu.contentHolder.setPowered(true);
                powerButton.setIcon(CNIcons.OFF_NORMAL);
            } else if (powered != null) {
                menu.contentHolder.setPowered(false);
                powerButton.setIcon(CNIcons.ON_NORMAL);
            }
        });
        addRenderableWidget(powerButton);
    }
    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        int x = leftPos + imageWidth - BG.width;
        int y = topPos;

        BG.render(graphics, x, y);
        graphics.drawString(font, title, x + 15, y + 4, 0x592424, false);

        int width = PROGRESS_BAR.width;
        int heightProgress = (int) (PROGRESS_BAR.height
                * Mth.lerp(partialTicks, lastChasingProgress, chasingProgress));
        System.out.println("count Graphite : " + countGraphiteRod());
        System.out.println("count Uranium : " + countUraniumRod());
        graphics.blit(PROGRESS_BAR.location, x + 179, y + 40 + (PROGRESS_BAR.height - heightProgress), PROGRESS_BAR.startX,
                (176 - heightProgress), width, heightProgress);
    }


    @Override
    protected void containerTick() {
        super.containerTick();

        boolean hasUranium = menu.getSlot(0).hasItem();
        boolean hasGraphite = menu.getSlot(1).hasItem();

        if (menu.contentHolder.isPowered() && hasUranium && hasGraphite && progress <= 1) {
            lastChasingProgress = chasingProgress;
            progress += 0.01F;
            chasingProgress += (progress - chasingProgress) * .5f;

        } else {
            progress = chasingProgress = lastChasingProgress = 0;
        }
    }

    private void placeSwitchItem() {
        int startWidth = 7;
        int startHeight = 39;
        int incr = 18;
        if (menu.contentHolder.getSwitchButtons() != null && !menu.contentHolder.getSwitchButtons().isEmpty()) {
            addRenderableWidgets(menu.contentHolder.getSwitchButtons());
            return;
        }

        List<CNIconButton> switchButtons = new ArrayList<>(); //57 icon sur l'interface

        switchButtons.add(new CNIconButton(leftPos + startWidth + incr*3, topPos + startHeight, CNIcons.EMPTY_ICON));
        switchButtons.add(new CNIconButton(leftPos + startWidth + incr*4, topPos + startHeight, CNIcons.EMPTY_ICON));
        switchButtons.add(new CNIconButton(leftPos + startWidth + incr*5, topPos + startHeight, CNIcons.EMPTY_ICON));

        switchButtons.add(new CNIconButton(leftPos + startWidth + incr*2, topPos + startHeight + incr, CNIcons.EMPTY_ICON));
        switchButtons.add(new CNIconButton(leftPos + startWidth + incr*3, topPos + startHeight + incr, CNIcons.EMPTY_ICON));
        switchButtons.add(new CNIconButton(leftPos + startWidth + incr*4, topPos + startHeight + incr, CNIcons.EMPTY_ICON));
        switchButtons.add(new CNIconButton(leftPos + startWidth + incr*5, topPos + startHeight + incr, CNIcons.EMPTY_ICON));
        switchButtons.add(new CNIconButton(leftPos + startWidth + incr*6, topPos + startHeight + incr, CNIcons.EMPTY_ICON));

        switchButtons.add(new CNIconButton(leftPos + startWidth + incr, topPos + startHeight + incr*2, CNIcons.EMPTY_ICON));
        switchButtons.add(new CNIconButton(leftPos + startWidth + incr*2, topPos + startHeight + incr*2, CNIcons.EMPTY_ICON));
        switchButtons.add(new CNIconButton(leftPos + startWidth + incr*3, topPos + startHeight + incr*2, CNIcons.EMPTY_ICON));
        switchButtons.add(new CNIconButton(leftPos + startWidth + incr*4, topPos + startHeight + incr*2, CNIcons.EMPTY_ICON));
        switchButtons.add(new CNIconButton(leftPos + startWidth + incr*5, topPos + startHeight + incr*2, CNIcons.EMPTY_ICON));
        switchButtons.add(new CNIconButton(leftPos + startWidth + incr*6, topPos + startHeight + incr*2, CNIcons.EMPTY_ICON));
        switchButtons.add(new CNIconButton(leftPos + startWidth + incr*7, topPos + startHeight + incr*2, CNIcons.EMPTY_ICON));

        for (int i= 3 ; i<6 ; i++) {
            for (int j = 0; j<9 ; j++) {
                switchButtons.add(new CNIconButton(leftPos + startWidth + incr*j, topPos + startHeight + incr*i, CNIcons.EMPTY_ICON));
            }
        }

        switchButtons.add(new CNIconButton(leftPos + startWidth + incr, topPos + startHeight + incr*6, CNIcons.EMPTY_ICON));
        switchButtons.add(new CNIconButton(leftPos + startWidth + incr*2, topPos + startHeight + incr*6, CNIcons.EMPTY_ICON));
        switchButtons.add(new CNIconButton(leftPos + startWidth + incr*3, topPos + startHeight + incr*6, CNIcons.EMPTY_ICON));
        switchButtons.add(new CNIconButton(leftPos + startWidth + incr*4, topPos + startHeight + incr*6, CNIcons.EMPTY_ICON));
        switchButtons.add(new CNIconButton(leftPos + startWidth + incr*5, topPos + startHeight + incr*6, CNIcons.EMPTY_ICON));
        switchButtons.add(new CNIconButton(leftPos + startWidth + incr*6, topPos + startHeight + incr*6, CNIcons.EMPTY_ICON));
        switchButtons.add(new CNIconButton(leftPos + startWidth + incr*7, topPos + startHeight + incr*6, CNIcons.EMPTY_ICON));

        switchButtons.add(new CNIconButton(leftPos + startWidth + incr*2, topPos + startHeight + incr*7, CNIcons.EMPTY_ICON));
        switchButtons.add(new CNIconButton(leftPos + startWidth + incr*3, topPos + startHeight + incr*7, CNIcons.EMPTY_ICON));
        switchButtons.add(new CNIconButton(leftPos + startWidth + incr*4, topPos + startHeight + incr*7, CNIcons.EMPTY_ICON));
        switchButtons.add(new CNIconButton(leftPos + startWidth + incr*5, topPos + startHeight + incr*7, CNIcons.EMPTY_ICON));
        switchButtons.add(new CNIconButton(leftPos + startWidth + incr*6, topPos + startHeight + incr*7, CNIcons.EMPTY_ICON));

        switchButtons.add(new CNIconButton(leftPos + startWidth + incr*3, topPos + startHeight + incr*8, CNIcons.EMPTY_ICON));
        switchButtons.add(new CNIconButton(leftPos + startWidth + incr*4, topPos + startHeight + incr*8, CNIcons.EMPTY_ICON));
        switchButtons.add(new CNIconButton(leftPos + startWidth + incr*5, topPos + startHeight + incr*8, CNIcons.EMPTY_ICON));


        for (CNIconButton button : switchButtons) {
            button.withCallback(() -> {// Quand le button est appuyé il fait ca
                if (button.getIcon().equals(CNIcons.EMPTY_ICON)){
                    button.setIcon(CNIcons.URANIUM_ROD_ICON);
                }else if (button.getIcon().equals(CNIcons.URANIUM_ROD_ICON)){
                    button.setIcon(CNIcons.GRAPHITE_ROD_ICON);
                }else if (button.getIcon().equals(CNIcons.GRAPHITE_ROD_ICON)){
                    button.setIcon(CNIcons.EMPTY_ICON);
                }
            });
        }
        menu.contentHolder.setSwitchButtons(switchButtons);
        addRenderableWidgets(switchButtons);
    }


    public int countGraphiteRod() {
        return (int) menu.contentHolder.getSwitchButtons().stream().filter(e -> e.getIcon().equals(CNIcons.GRAPHITE_ROD_ICON)).count();
    }
    public int countUraniumRod() {
        return (int) menu.contentHolder.getSwitchButtons().stream().filter(e -> e.getIcon().equals(CNIcons.URANIUM_ROD_ICON)).count();
    }
}
