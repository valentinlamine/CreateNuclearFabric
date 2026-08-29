package net.nuclearteam.createnuclear.content.multiblock.bluePrintItem;

import com.simibubi.create.foundation.gui.menu.AbstractSimiContainerScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.nuclearteam.createnuclear.CNPackets;
import net.nuclearteam.createnuclear.foundation.gui.CNGuiTextures;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.simibubi.create.foundation.gui.AllGuiTextures.PLAYER_INVENTORY;

@ParametersAreNonnullByDefault
@SuppressWarnings({"unused"})
public class ReactorBluePrintItemScreen extends AbstractSimiContainerScreen<ReactorBluePrintMenu> {
    protected static final CNGuiTextures BG = CNGuiTextures.CONFIGURED_PATTERN_GUI;

    public ReactorBluePrintItemScreen(ReactorBluePrintMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
    }

    @Override
    protected void init() {
        setWindowSize(BG.width, BG.height + PLAYER_INVENTORY.getHeight());
        setWindowOffset(0, 0);
        super.init();
        clearWidgets();
    }


    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int x = leftPos;
        int y = topPos+38;

        BG.render(guiGraphics, x+23, y-19);
        renderPlayerInventory(guiGraphics, x+23, y+175);

        guiGraphics.drawString(font, title, x+26, y-12, 0x592424, false); // renders the screen title
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        if (!ItemStack.matches(menu.player.getMainHandItem(), menu.contentHolder)) {
            menu.player.closeContainer();
        }

        CompoundTag tag = menu.contentHolder.getOrCreateTag();
        if (tag.contains("pattern")) {
            sendValueUpdate(tag, tag.getFloat("heat"),
                    tag.getInt("coolerTime"),
                    tag.getInt("fuelTime"),
                    tag.getInt("countCoolerRod"),
                    tag.getInt("countFuelRod")
            );
        }
    }


    private static void sendValueUpdate(CompoundTag tag, float heat, int coolerTime, int fuelTime, int countCoolerRod, int countFuelRod) {
        CNPackets.getChannel().sendToServer(new ReactorBluePrintItemPacket(tag, heat, coolerTime, fuelTime, countCoolerRod, countFuelRod));
    }
}
