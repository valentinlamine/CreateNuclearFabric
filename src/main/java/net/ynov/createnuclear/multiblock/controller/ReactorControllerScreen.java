package net.ynov.createnuclear.multiblock.controller;

import com.simibubi.create.foundation.gui.menu.AbstractSimiContainerScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.ynov.createnuclear.CreateNuclear;
import net.ynov.createnuclear.gui.CNGuiTextures;
import net.ynov.createnuclear.gui.CNIconButton;
import net.ynov.createnuclear.gui.CNIcons;
import net.ynov.createnuclear.utils.Logger;

import java.util.ArrayList;
import java.util.List;

public class ReactorControllerScreen extends AbstractSimiContainerScreen<ReactorControllerMenu> {
    protected static final CNGuiTextures BG = CNGuiTextures.REACTOR_CONTROLLER;
    protected static final CNGuiTextures PROGRESS_BAR = CNGuiTextures.REACTOR_CONTROLLER_PROGRESS;
    private CNIconButton powerButton;
    private float progress;
    private float reactorPower;

    public static BlockPos pos;
    public static BlockState state;
    public static Level level;

    private float lastReactorPower;

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
        reactorPower = lastReactorPower = heatManager();
    }
    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        int x = leftPos + imageWidth - BG.width;
        int y = topPos;
        int heightProgress;

        BG.render(graphics, x, y);
        graphics.drawString(font, title, x + 15, y + 4, 0x592424, false);

        int width = PROGRESS_BAR.width;

        heightProgress = (int) (PROGRESS_BAR.height * Mth.lerp(partialTicks, lastReactorPower / 100, lastReactorPower / 100));

        //System.out.println("count Graphite : " + countGraphiteRod());
        //System.out.println("count Uranium : " + countUraniumRod());
        //System.out.println("current heat : " + heatManager());
        graphics.blit(PROGRESS_BAR.location, x + 179, y + 40 + (PROGRESS_BAR.height - heightProgress), PROGRESS_BAR.startX,
                (176 - heightProgress), width, heightProgress);
    }


    @Override
    protected void containerTick() {
        float coef = 0.1F;
        super.containerTick();
        reactorPower = heatManager();
        boolean hasUranium = menu.getSlot(0).hasItem();
        boolean hasGraphite = menu.getSlot(1).hasItem();
        if (menu.contentHolder.isPowered() && hasUranium && hasGraphite) {
            ReactorControllerBlock controller = (ReactorControllerBlock) state.getBlock();
            CreateNuclear.LOGGER.info(controller.toString() + "_________________________________________________");
            controller.Rotate(state, pos.below(3), level, heatManager());
            if (reactorPower < lastReactorPower - coef || reactorPower > lastReactorPower + coef) {
                if (reactorPower < lastReactorPower) {
                    lastReactorPower -= 0.2F;
                }else {
                    lastReactorPower += 0.2F;
                }
            }else {
                lastReactorPower = reactorPower;
            }

        } else {
            lastReactorPower = 0;
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

        List<CNIconButton> switchButtons = new ArrayList<>();

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


            //CreateNuclear.LOGGER.info(String.valueOf(heatManager()));
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

    public int heatManager() {
        int colDown;
        int colUp;
        int colLeft;
        int colRight;
        int heat = countUraniumRod() * 10 - countGraphiteRod() * 5;

        int [][] list = new int[][] {{99,99,99,0,1,2,99,99,99}, {99,99,3,4,5,6,7,99,99}, {99,8,9,10,11,12,13,14,99}, {15,16,17,18,19,20,21,22,23}, {24,25,26,27,28,29,30,31,32}, {33,34,35,36,37,38,39,40,41}, {99,42,43,44,45,46,47,48,99}, {99,99,49,50,51,52,53,99,99}, {99,99,99,54,55,56,99,99,99}};
        for (int j = 0; j != list.length; j++) {
            for (int k = 0; k != list[j].length; k++) {
                if (j == 0) {
                    colUp = 99;
                    colDown = list[j+1][k];
                } else if (j == list.length-1) {
                    colUp = list[j-1][k];
                    colDown = 99;
                } else {
                    colUp = list[j-1][k];
                    colDown = list[j+1][k];
                }
                if (k == 0) {
                    colLeft = 99;
                    colRight = list[j][k+1];
                } else if (k == list[j].length-1) {
                    colLeft = list[j][k-1];
                    colRight = 99;
                } else {
                    colLeft = list[j][k-1];
                    colRight = list[j][k+1];
                }
                if (list[j][k] != 99) {
                    heat += ProximityManager(list[j][k], colUp, colDown, colLeft, colRight);
                }
            }
        }

        if (heat >= 100) {
            CreateNuclear.LOGGER.info("oulala il est chaud un peu la, attenti... BOOOOM");
        }
        return heat;
    }

    private int addTmpHeat(CNIconButton button, int isUranium) {
        if (isUranium == 0) {
            if (button.getIcon().equals(CNIcons.GRAPHITE_ROD_ICON)) {
                return 0;
            } else if (button.getIcon().equals(CNIcons.URANIUM_ROD_ICON)) {
                return 4;
            }
        } else if (isUranium == 1) {
            if (button.getIcon().equals(CNIcons.GRAPHITE_ROD_ICON)) {
                return 0;
            } else if (button.getIcon().equals(CNIcons.URANIUM_ROD_ICON)) {
                return -5;
            }
        }
        return 0;
    }
    public int ProximityManager(int current, int up, int down, int left, int right) {
        List<CNIconButton> switchButtons = menu.contentHolder.getSwitchButtons();
        CNIconButton button = switchButtons.get(current);
        CNIconButton buttonDown;
        CNIconButton buttonUp;
        CNIconButton buttonLeft;
        CNIconButton buttonRight;
        int isUranium = 2;

        int tmpHeat = 0;

        if (button.getIcon().equals(CNIcons.EMPTY_ICON)) {
            return 0;
        } else if (button.getIcon().equals(CNIcons.GRAPHITE_ROD_ICON)) {
            isUranium = 1;
        } else if (button.getIcon().equals(CNIcons.URANIUM_ROD_ICON)) {
            isUranium = 0;
        }
        if (up != 99) {
            buttonUp = switchButtons.get(up);
            tmpHeat = addTmpHeat(buttonUp, isUranium);
        }
        if (down != 99) {
            buttonDown = switchButtons.get(down);
            tmpHeat = addTmpHeat(buttonDown, isUranium);
        }
        if (left != 99) {
            buttonLeft = switchButtons.get(left);
            tmpHeat = addTmpHeat(buttonLeft, isUranium);
        }
        if (right != 99) {
            buttonRight = switchButtons.get(right);
            tmpHeat = addTmpHeat(buttonRight, isUranium);
        }
        //CreateNuclear.LOGGER.info("Heat at index: " + current + " is " + tmpHeat);
        return tmpHeat;
    }

    public void showAllItemsInTab() {
        List<CNIconButton> switchButtons = menu.contentHolder.getSwitchButtons();
        for (int i = 0; i < switchButtons.size(); i++) {
            CNIconButton button = switchButtons.get(i);
            if (button.getIcon().equals(CNIcons.GRAPHITE_ROD_ICON)) {
                CreateNuclear.LOGGER.info("Graphite Rod at index: " + i);
            } else if (button.getIcon().equals(CNIcons.URANIUM_ROD_ICON)) {
                CreateNuclear.LOGGER.info("Uranium Rod at index: " + i);
            } else {
                CreateNuclear.LOGGER.info("Empty at index: " + i);
            }
        }
    }
}
