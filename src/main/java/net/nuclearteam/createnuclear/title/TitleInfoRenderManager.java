package net.nuclearteam.createnuclear.title;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class TitleInfoRenderManager {
    public final TitleInfoRender infoTitleRenderer = new TitleInfoRender(TitleInfoConfig.WARNING, 20, 20);

    public void clientTick() {
        if (!Minecraft.getInstance().isPaused()) {
            infoTitleRenderer.tick();
        }
    }

    public void renderTitle(GuiGraphics gui, float partialTicks) {
        if (!Minecraft.getInstance().options.renderDebug) {
            infoTitleRenderer.renderText(partialTicks, gui);
        }
    }

    public void playerTick(Player player) {
        if (player instanceof LocalPlayer && player.level().isLoaded(player.blockPosition())) {
            BlockPos playerPos = player.blockPosition();
            Level world = player.level();

            
        }
    }

}
