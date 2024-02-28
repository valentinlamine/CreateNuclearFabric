package net.ynov.createnuclear.content.reactor.controller;

import com.google.common.collect.ImmutableList;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllPackets;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.equipment.toolbox.ToolboxDisposeAllPacket;
import com.simibubi.create.content.equipment.toolbox.ToolboxInventory;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.gui.element.GuiGameElement;
import com.simibubi.create.foundation.gui.menu.AbstractSimiContainerScreen;
import com.simibubi.create.foundation.gui.widget.IconButton;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.ynov.createnuclear.gui.CNGuiTextures;
import net.ynov.createnuclear.gui.CNIcons;

import java.util.Collections;
import java.util.List;

import static com.simibubi.create.foundation.gui.AllGuiTextures.SCHEMATIC_TABLE_PROGRESS;

public class ReactorControllerScreen extends AbstractSimiContainerScreen<ReactorControllerMenu> {
    protected static final CNGuiTextures BG = CNGuiTextures.REACTOR_CONTROLLER;
    protected static final CNGuiTextures PROGRESS_BAR = CNGuiTextures.REACTOR_CONTROLLER_PROGRESS;
    private IconButton powerButton;
//    protected static final AllGuiTextures PLAYER = AllGuiTextures.PLAYER_INVENTORY;
    private float progress;
    private float chasingProgress;
    private float lastChasingProgress;

    public ReactorControllerScreen(ReactorControllerMenu container, Inventory inv, Component title) {
        super(container, inv, title);
    }

    @Override
    protected void init() {
        setWindowSize(BG.width, BG.height );
        setWindowOffset(0, 0);
        super.init();
        clearWidgets();

        //S'initialise a chaque fois que l'on click sur le block
        powerButton = menu.contentHolder.isPowered() ? new IconButton(leftPos +  BG.width - 25, topPos + 7, CNIcons.OFF_NORMAL) : new IconButton(leftPos +  BG.width - 25, topPos + 7, CNIcons.ON_NORMAL);
        powerButton.withCallback(() -> {// Quand le button est appuyé il fait ca
            Boolean powered = menu.contentHolder.isPowered();
            if (powered!= null && !powered) {
                menu.contentHolder.setPowered(true);
                powerButton.setIcon(CNIcons.OFF_NORMAL);
            } else if (powered != null) {
                menu.contentHolder.setPowered(false);
                powerButton.setIcon(CNIcons.ON_NORMAL);
            }
//            minecraft.player.closeContainer();
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
        int heightStart = (int) (PROGRESS_BAR.height
                * Mth.lerp(partialTicks, lastChasingProgress, chasingProgress));
        System.out.println("PartialTicks : " + partialTicks);
        System.out.println("lastChasingProgress : " + lastChasingProgress);
        System.out.println("chaisingProgress : " + chasingProgress);
        System.out.println("HEIGHT START = " + heightStart);
        graphics.blit(PROGRESS_BAR.location, x + 179, y + 39 + (PROGRESS_BAR.height - heightStart), PROGRESS_BAR.startX,
                (160 - heightStart), width, heightStart);
    }


    @Override
    protected void containerTick() {
        super.containerTick();

        boolean hasUranium = menu.getSlot(0).hasItem();
        boolean hasGraphene = menu.getSlot(1).hasItem();

        if ((menu.contentHolder.isPowered() && hasUranium && hasGraphene) || progress <= 1) {
            lastChasingProgress = chasingProgress;
            progress += 0.1F;
            chasingProgress += (progress - chasingProgress) * .5f;

        } else {
            progress = chasingProgress = lastChasingProgress = 0;
        }
    }

}
