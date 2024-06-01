package net.ynov.createnuclear.multiblock.controller;

import com.simibubi.create.foundation.gui.menu.AbstractSimiContainerScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.ynov.createnuclear.CreateNuclear;
import net.ynov.createnuclear.gui.CNGuiTextures;
import net.ynov.createnuclear.gui.CNIconButton;
import net.ynov.createnuclear.gui.CNIcons;
import net.ynov.createnuclear.item.CNItems;

import static net.ynov.createnuclear.multiblock.controller.ConfigureReactorControllerPacket.CNOption;
import static net.ynov.createnuclear.packets.CNPackets.getChannel;

import java.util.ArrayList;
import java.util.List;

public class ReactorControllerScreen extends AbstractSimiContainerScreen<ReactorControllerMenu> {
    protected static final CNGuiTextures BG = CNGuiTextures.REACTOR_CONTROLLER;
    protected static final CNGuiTextures PROGRESS_BAR = CNGuiTextures.REACTOR_CONTROLLER_PROGRESS;
    private CNIconButton powerButton;
    private float progress;


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
        ReactorControllerBlockEntity be = menu.contentHolder;

        powerButton = be.isPowered()
                ? new CNIconButton(leftPos +  BG.width - 25, topPos + 7, CNIcons.ON_NORMAL)
                : new CNIconButton(leftPos +  BG.width - 25, topPos + 7, CNIcons.OFF_NORMAL);

        powerButton.withCallback(() -> {// Quand le button est appuyé il fait ca
            Boolean powered = be.isPowered();
            if (powered != null && !powered) {
                be.setPowered(true);
                powerButton.setIcon(CNIcons.ON_NORMAL);
                sendOptionUpdate(CNOption.PLAY);
            } else if (powered != null) {
                be.setPowered(false);
                powerButton.setIcon(CNIcons.OFF_NORMAL);
                sendOptionUpdate(CNOption.STOP);
            }
        });
        addRenderableWidget(powerButton);
        be.reactorPower = be.lastReactorPower = heatManager();
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        ReactorControllerBlockEntity be = menu.contentHolder;

        int x = leftPos + imageWidth - BG.width;
        int y = topPos;
        int heightProgress;

        BG.render(graphics, x, y);
        graphics.drawString(font, title, x + 15, y + 4, 0x592424, false);

        int width = PROGRESS_BAR.width;

        heightProgress = (int) (PROGRESS_BAR.height * Mth.lerp(partialTicks, be.lastReactorPower / 100, be.lastReactorPower / 100));

        //CreateNuclear.LOGGER.info("count Graphite : " + countGraphiteRod());
        //CreateNuclear.LOGGER.info("count Uranium : " + countUraniumRod());
        //CreateNuclear.LOGGER.info("current heat : " + heatManager());
        graphics.blit(PROGRESS_BAR.location, x + 179, y + 40 + (PROGRESS_BAR.height - heightProgress), PROGRESS_BAR.startX,
                (176 - heightProgress), width, heightProgress);
    }


    @Override
    protected void containerTick() {
        super.containerTick();

        ReactorControllerBlockEntity be = menu.contentHolder;

        float coef = 0.1F;

        if (!be.isPowered()) {
            be.heat = 0;
            be.lastReactorPower = 0;
            super.containerTick();
            return;
        }

        if (menu.getSlot(1).getItem().getCount() >= 0) {
            be.graphiteTimer -= countGraphiteRod();
            //CreateNuclear.LOGGER.info(String.valueOf(be.graphiteTimer));
            //CreateNuclear.LOGGER.info(String.valueOf(menu.getItems()));
        }

        if (menu.getSlot(0).getItem().getCount() > 0) {
            be.uraniumTimer -= countUraniumRod();
            //CreateNuclear.LOGGER.info(String.valueOf(be.uraniumTimer));
        }

        updateTimers();


        be.reactorPower = be.isPowered() ? heatManager() : 0;

        boolean hasUranium = menu.getSlot(0).getItem().is(CNItems.URANIUM_ROD.get());
        boolean hasGraphite = menu.getSlot(1).getItem().is(CNItems.GRAPHITE_ROD.get());

        if (hasUranium && hasGraphite) {
            adjustReactorPower(coef);
        } else {
            be.lastReactorPower = 0;
        }

        sendValueUpdate(CNOption.HEAT, heatManager());
    }

    private void updateTimers() {
        ReactorControllerBlockEntity be = menu.contentHolder;

        if (be.uraniumTimer <= 0) {
            CreateNuclear.LOGGER.info("Uranium Rod removed " + menu.getSlot(0).getItem());
            menu.getSlot(0).getItem().shrink(1);
            be.inventory.setStackInSlot(0, menu.getSlot(0).getItem());
            be.uraniumTimer = 5000;
            menu.sendAllDataToRemote();
        }
        if (be.graphiteTimer <= 0) {
            CreateNuclear.LOGGER.info("Graphite Rod removed");
            menu.getSlot(1).getItem().shrink(1);
            be.graphiteTimer = 3600;
            menu.sendAllDataToRemote();
            //menu.getItems();
        }
    }

    private void adjustReactorPower(float coef) {
        ReactorControllerBlockEntity be = menu.contentHolder;

        if (be.reactorPower < be.lastReactorPower - coef || be.reactorPower > be.lastReactorPower + coef) {
            if (be.reactorPower < be.lastReactorPower) {
                be.lastReactorPower -= 0.2F;
            } else {
                be.lastReactorPower += 0.2F;
            }
        } else {
            be.lastReactorPower = be.reactorPower;
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

        int[][] positions = {
                {3, 0}, {4, 0}, {5, 0},
                {2, 1}, {3, 1}, {4, 1}, {5, 1}, {6, 1},
                {1, 2}, {2, 2}, {3, 2}, {4, 2}, {5, 2}, {6, 2}, {7, 2},
                {3, 8}, {4, 8}, {5, 8},
                {2, 7}, {3, 7}, {4, 7}, {5, 7}, {6, 7},
                {1, 6}, {2, 6}, {3, 6}, {4, 6}, {5, 6}, {6, 6}, {7, 6}
        };

        for (int[] pos : positions) {// up and down not middle
            switchButtons.add(new CNIconButton(leftPos + startWidth + incr * pos[0], topPos + startHeight + incr * pos[1], CNIcons.EMPTY_ICON));
        }

        // Loop for positions (3,3) to (3,5), (4,3) to (4,5), ..., (11,3) to (11,5)
        for (int i = 3; i <= 5; i++) {
            for (int j = 0; j <= 8; j++) {
                switchButtons.add(new CNIconButton(leftPos + startWidth + incr * j, topPos + startHeight + incr * i, CNIcons.EMPTY_ICON));
            }
        }


        for (CNIconButton button : switchButtons) {
            button.withCallback(() -> handleButtonClick(button)); // Quand le button est appuyé il fait ca
        }

        menu.contentHolder.setSwitchButtons(switchButtons);
        addRenderableWidgets(switchButtons);
    }

    private void handleButtonClick(CNIconButton button) {
        if (button.getIcon().equals(CNIcons.EMPTY_ICON)) button.setIcon(CNIcons.URANIUM_ROD_ICON);
        else if (button.getIcon().equals(CNIcons.URANIUM_ROD_ICON)) button.setIcon(CNIcons.GRAPHITE_ROD_ICON);
        else if (button.getIcon().equals(CNIcons.GRAPHITE_ROD_ICON)) button.setIcon(CNIcons.EMPTY_ICON);
    }


    public int countGraphiteRod() {
        return (int) menu.contentHolder.getSwitchButtons().stream().filter(e -> e.getIcon().equals(CNIcons.GRAPHITE_ROD_ICON)).count();
    }

    public int countUraniumRod() {
        return (int) menu.contentHolder.getSwitchButtons().stream().filter(e -> e.getIcon().equals(CNIcons.URANIUM_ROD_ICON)).count();
    }

    public int heatManager() {
        ReactorControllerBlockEntity be = menu.contentHolder;

        be.countGraphiteRod = countGraphiteRod();
        be.countUraniumRod = countUraniumRod();
        be.heat = be.countUraniumRod * 10 - be.countGraphiteRod * 5;

        int [][] list = new int[][] {
                {99,99,99,0,1,2,99,99,99},
                {99,99,3,4,5,6,7,99,99},
                {99,8,9,10,11,12,13,14,99},
                {15,16,17,18,19,20,21,22,23},
                {24,25,26,27,28,29,30,31,32},
                {33,34,35,36,37,38,39,40,41},
                {99,42,43,44,45,46,47,48,99},
                {99,99,49,50,51,52,53,99,99},
                {99,99,99,54,55,56,99,99,99}
        };

        for (int j = 0; j < list.length; j++) {
            for (int k = 0; k < list[j].length; k++) {
                if (list[j][k] != 99) {
                    int colUp = getValue(list, j - 1, k);
                    int colDown = getValue(list, j + 1, k);
                    int colLeft = getValue(list, j, k - 1);
                    int colRight = getValue(list, j, k + 1);

                    be.heat += ProximityManager(list[j][k], colUp, colDown, colLeft, colRight);
                }
            }
        }

        if (be.heat >= 100) {
            CreateNuclear.LOGGER.info("oulala il est chaud un peu la, attenti... BOOOOM");
            return 0;
        }
        return be.heat;
    }

    private int getValue(int[][] array, int row, int col) {
        if (row < 0 || row >= array.length || col < 0 || col >= array[row].length) {
            return 99;
        }
        return array[row][col];
    }



    public int ProximityManager(int current, int up, int down, int left, int right) {
        List<CNIconButton> switchButtons = menu.contentHolder.getSwitchButtons();
        CNIconButton button = switchButtons.get(current);

        if (button.getIcon().equals(CNIcons.EMPTY_ICON)) {
            return 0;
        }

        int isUranium = button.getIcon().equals(CNIcons.GRAPHITE_ROD_ICON) ? 1 : 0;

        int tmpHeat = 0;

        tmpHeat += calculateHeat(up, switchButtons, isUranium);
        tmpHeat += calculateHeat(down, switchButtons, isUranium);
        tmpHeat += calculateHeat(left, switchButtons, isUranium);
        tmpHeat += calculateHeat(right, switchButtons, isUranium);

        // CreateNuclear.LOGGER.info("Heat at index: " + current + " is " + tmpHeat);
        return tmpHeat;
    }

    private int calculateHeat(int index, List<CNIconButton> switchButtons, int isUranium) {
        if (index == 99) return 0;
        CNIconButton button = switchButtons.get(index);
        return addTmpHeat(button, isUranium);
    }

    private int addTmpHeat(CNIconButton button, int isUranium) {

        if (button.getIcon().equals(CNIcons.GRAPHITE_ROD_ICON)) return 0;
        if (button.getIcon().equals(CNIcons.URANIUM_ROD_ICON)) return isUranium == 0 ? 4 : -5;
        return 0;
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

    protected void sendOptionUpdate(ConfigureReactorControllerPacket.CNOption option) {
        getChannel().sendToServer(new ConfigureReactorControllerPacket(option));
    }

    protected void sendValueUpdate(CNOption option, int value) {
        getChannel().sendToServer(new ConfigureReactorControllerPacket(option, value));
    }
}