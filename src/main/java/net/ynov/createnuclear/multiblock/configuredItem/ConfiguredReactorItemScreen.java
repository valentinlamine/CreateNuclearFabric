package net.ynov.createnuclear.multiblock.configuredItem;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.gui.menu.AbstractSimiContainerScreen;
import io.github.fabricators_of_create.porting_lib.util.PlayerEntityHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.ynov.createnuclear.CreateNuclear;
import net.ynov.createnuclear.gui.CNGuiTextures;
import net.ynov.createnuclear.gui.CNIconButton;
import net.ynov.createnuclear.gui.CNIcons;
import net.ynov.createnuclear.item.CNItems;

import java.util.*;

import static com.simibubi.create.foundation.gui.AllGuiTextures.PLAYER_INVENTORY;

public class ConfiguredReactorItemScreen  extends AbstractSimiContainerScreen<ConfiguredReactorItemMenu> {
    protected static final CNGuiTextures BG = CNGuiTextures.CONFIGURED_PATTERN_GUI;
    protected static final CNGuiTextures PROGRESS_BAR = CNGuiTextures.REACTOR_CONTROLLER_PROGRESS;
    private final Map<Integer, CNIconButton> switchButtons = new HashMap<>();
    private final Map<Integer, Integer> patternButtons = new HashMap<>();

    private Map<String, Object> inventory = new HashMap<>();
    private int grahiteTime = 3600;
    private int uraniumTime = 5000;


    public ConfiguredReactorItemScreen(ConfiguredReactorItemMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
    }

    @Override
    protected void init() {
        setWindowSize(BG.width, BG.height + PLAYER_INVENTORY.height);
        setWindowOffset(0, -2);
        super.init();
        clearWidgets();


        List<Map<String, Object>> items = new ArrayList<>();


        CompoundTag tag = menu.contentHolder.getOrCreateTag();
        ListTag slots = tag.getCompound("pattern").getList("Items", Tag.TAG_COMPOUND);

        for (int i = 0; i < tag.getInt("Size"); i++) {
            Map<String, Object> item = new HashMap<>();
            CompoundTag slotCompound = slots.getCompound(i);

            item.put("Count", slotCompound.isEmpty() ? (byte) 1 : slotCompound.getByte("Count"));
            item.put("Slot", i);
            item.put("Item", slotCompound.isEmpty() ? ItemStack.EMPTY.getItem().toString() : slotCompound.get("id"));

            items.add(item);
        }

        inventory.put("Size", tag.getInt("Size"));
        inventory.put("Pattern", items);


        CreateNuclear.LOGGER.warn("contentHolder: " + "  " + Arrays.toString(inventory.values().toArray()));

        //placeSwitchItem();
    }


    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int x = leftPos;
        int y = topPos+38;

        BG.render(guiGraphics, x, y);
        renderPlayerInventory(guiGraphics, x+23, y+175);

        guiGraphics.drawString(font, title, x + 2, y + 4, 0x592424, false);

        int width = PROGRESS_BAR.width;

        float heightProgress = (PROGRESS_BAR.height * Mth.lerp(partialTick, 12/100, 13/100));

        /*guiGraphics.blit(PROGRESS_BAR.location, x + 179, (int) (y + 40 + (PROGRESS_BAR.height - heightProgress)),
                PROGRESS_BAR.startX, (int) (176 - heightProgress), width, (int) heightProgress);*/
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void containerTick() {
        super.containerTick();
        if (!ItemStack.matches(menu.player.getMainHandItem(), menu.contentHolder)) {
            PlayerEntityHelper.closeScreen(menu.player);
        }
        InitInventory();
        float coef = 0.1F;
        CreateNuclear.LOGGER.warn("ddddf: " + inventory.get("pattern"));
        if (inventory.get("pattern") == null) {
            super.containerTick();
            return;
        }

        List<Map<String, Object>> pattern = (List<Map<String, Object>>) inventory.get("pattern");

        CreateNuclear.LOGGER.warn("pattern: " + pattern.get(0));


    }

    private void InitInventory() {
        List<Map<String, Object>> items = new ArrayList<>();

        CompoundTag tag = menu.contentHolder.getOrCreateTag();
        ListTag slots = tag.getCompound("pattern").getList("Items", Tag.TAG_COMPOUND);

        for (int i = 0; i < tag.getInt("Size"); i++) {
            Map<String, Object> item = new HashMap<>();
            CompoundTag slotCompound = slots.getCompound(i);

            item.put("Count", slotCompound.isEmpty() ? (byte) 1 : slotCompound.getByte("Count"));
            item.put("Slot", i);
            item.put("Item", slotCompound.isEmpty() ? ItemStack.EMPTY.getItem().toString() : slotCompound.get("id"));

            items.add(item);
        }

        inventory.put("Size", tag.getInt("Size"));
        inventory.put("Pattern", items);
    }

}
