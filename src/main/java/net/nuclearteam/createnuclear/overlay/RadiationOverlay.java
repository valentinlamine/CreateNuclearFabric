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
public class RadiationOverlay implements HudOverlay {
    private static final ResourceLocation RADIATION_TEXTURE =
            CreateNuclear.asResource("textures/misc/irradiated_vision.png");
    private static float coverage = 1f;
    private static float irradiatedVisionAlpha = 0f;

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
        return player != null && player.hasEffect(CNEffects.RADIATION.get());
    }

    @Override
    public void render(GuiGraphics graphics, float partialTicks) {
        if (isActive()) {
            irradiatedVisionAlpha = Math.min(1f, irradiatedVisionAlpha + 0.1f);
        } else {
            irradiatedVisionAlpha = Math.max(0f, irradiatedVisionAlpha - 0.1f);
        }

        if (irradiatedVisionAlpha > 0f) {
            RenderHelper.renderTextureOverlayTest(graphics, RADIATION_TEXTURE, irradiatedVisionAlpha);
        }
    }

    /*@Override
    protected void renderWithAlpha(GuiGraphics graphics, float partialTicks, float alpha) {
        // Render radiation overlay with dynamic coverage and alpha
        RenderHelper.renderTextureOverlay(graphics, RADIATION_TEXTURE, Math.round(alpha * coverage));
    }*/

    @Override
    public int getPriority() {
        return 100; // Fixed background priority for radiation effect
    }
}
