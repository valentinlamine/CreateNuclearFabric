package net.nuclearteam.createnuclear.overlay;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.nuclearteam.createnuclear.CreateNuclear;
import net.nuclearteam.createnuclear.effects.CNEffects;
import net.nuclearteam.createnuclear.utils.RenderHelper;

/**
 * HUD overlay for radiation effect when the player is irradiated.
 */
public class RadiationOverlay extends EasingHudOverlay {
    private static final ResourceLocation RADIATION_TEXTURE =
            CreateNuclear.asResource("textures/misc/irradiated_vision.png");
    private static float coverage = 1f;

    /**
     * Updates the coverage scale for the radiation effect.
     * @param newCoverage scale factor (1.0 = normal size)
     */
    public static void setCoverage(float newCoverage) {
        coverage = newCoverage;
    }

    @Override
    public boolean isActive() {
        LocalPlayer player = Minecraft.getInstance().player;
        return player != null && player.getEffect(CNEffects.RADIATION.get()) != null;
    }

    @Override
    protected void renderWithAlpha(GuiGraphics graphics, float partialTicks, float alpha) {
        // Render radiation overlay with dynamic coverage and alpha
        RenderHelper.renderTextureOverlay(graphics, RADIATION_TEXTURE, alpha, coverage);
    }

    @Override
    public int getPriority() {
        return 100; // Fixed background priority for radiation effect
    }
}
