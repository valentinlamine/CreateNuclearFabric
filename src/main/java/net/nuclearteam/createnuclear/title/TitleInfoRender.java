package net.nuclearteam.createnuclear.title;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.nuclearteam.createnuclear.CreateNuclear;
import org.jetbrains.annotations.Nullable;

public class TitleInfoRender {

    public Component displayedTitle = null;
    public Component displayedSubTitle = null;
    public int titleTimer = 0;
    public int cooldownTimer = 0;

    private boolean enabled;
    private int titleFadeInTicks;
    private int titleDisplayTime;
    private int titleFadeOutTicks;
    private int textCooldownTime;
    private String titleDefaultTextColor ;
    private float titleTextSize;
    private boolean showTextShadow;
    private boolean isTextCentered;
    public int titleTextcolor;

    public int titleXOffset;
    public int titleYOffset;

    public TitleInfoRender(
            boolean enabled, int fadeInTicks, int displayTicks,
            int fadeOutTicks, int textCooldownTime, String textColor,
            double textSize, int xOffset, int yOffset,
            boolean renderShadow, boolean centerText
    ) {
        this.enabled = enabled;
        this.titleFadeInTicks = fadeInTicks;
        this.titleDisplayTime = displayTicks;
        this.titleFadeOutTicks = fadeOutTicks;
        this.setColor(textColor);
        this.titleDefaultTextColor = textColor;
        this.showTextShadow = renderShadow;
        this.titleTextSize = (float)textSize;
        this.titleXOffset = xOffset;
        this.titleYOffset = yOffset;
        this.isTextCentered = centerText;
    }

    public TitleInfoRender(
            TitleInfoConfig config, int xOffset, int yOffset
    ) {
        this.enabled = config.isEnabled();
        this.titleFadeInTicks = config.getTextFadeInTime();
        this.titleDisplayTime = config.getTextDisplayTime();
        this.titleFadeOutTicks = config.getTextFadeOutTime();
        this.setColor(config.getTextColor());
        this.titleDefaultTextColor = config.getTextColor();
        this.showTextShadow = config.isRenderShadow();
        this.titleTextSize = (float)config.getTextSize();
        this.titleXOffset = xOffset;
        this.titleYOffset = yOffset;
        this.isTextCentered = config.isCenterText();
    }

    public void renderText(float partialTicks, GuiGraphics guiGraphics) {
        if (displayedTitle != null && titleTimer > 0) {
            float age = (float) titleTimer - partialTicks;
            int opacity = 255;
            if (titleTimer > titleFadeOutTicks + titleDisplayTime) {
                float r = (float) (titleFadeInTicks + titleDisplayTime + titleFadeOutTicks) - age;
                opacity = (int) (r * 255.0F / (float) titleFadeInTicks);
            }

            if (titleTimer <= titleFadeOutTicks) {
                opacity = (int) (age * 255.0F / (float) titleFadeOutTicks);
            }

            opacity = Mth.clamp(opacity, 0, 255);
            if (opacity > 8) {
                // Set up render system
                guiGraphics.pose().pushPose();
                if (this.isTextCentered) {
                    guiGraphics.pose().translate(Minecraft.getInstance().getWindow().getGuiScaledWidth() / 2D, (Minecraft.getInstance().getWindow().getGuiScaledHeight() / 2D), 0);
                }
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();

                // Render title
                guiGraphics.pose().pushPose();
                guiGraphics.pose().scale(titleTextSize, titleTextSize, titleTextSize);
                int alpha = opacity << 24 & 0xFF000000;
                Font fontRenderer = Minecraft.getInstance().font;
                int titleWidth = fontRenderer.width(displayedTitle);

                // Currently does nothing?
                drawBackdrop(guiGraphics, -10, titleWidth, titleTextcolor | alpha);

                // Determine x offset
                int xOffset = this.isTextCentered
                        ? this.titleXOffset + (-titleWidth / 2)
                        : this.titleXOffset;

                // Render title
                guiGraphics.drawString(fontRenderer, displayedTitle, xOffset, titleYOffset, titleTextcolor | alpha, showTextShadow);
                guiGraphics.pose().popPose();

                // Subtitle render. Currently unused
                if (displayedSubTitle != null) {
                    guiGraphics.pose().pushPose();
                    guiGraphics.pose().scale(1.3F, 1.3F, 1.3F);
                    int subtitleWidth = fontRenderer.width(displayedSubTitle);
                    drawBackdrop(guiGraphics, 5, subtitleWidth, 0xFFFFFF | alpha);
                    guiGraphics.drawString(fontRenderer, displayedSubTitle, -subtitleWidth / 2, -35, 0xFFFFFF | alpha, showTextShadow);
                    guiGraphics.pose().popPose();
                }

                RenderSystem.disableBlend();
                guiGraphics.pose().popPose();
            }
        }
    }

    public void tick() {
        if (titleTimer > 0) {
            --titleTimer;
            if (titleTimer <= 0) {
                clearTimer();
            }
        }
        if (cooldownTimer > 0) {
            --cooldownTimer;
        }
    }

    public void displayTitle(Component titleText, @Nullable Component subtitleText) {
        displayedTitle = titleText;
        titleTimer = titleFadeInTicks + titleDisplayTime + titleFadeOutTicks;
        if (subtitleText != null)
            displayedSubTitle = subtitleText;
    }

    public void clearTimer() {
        titleTimer = 0;
    }

    public void setColor(String textColor) {
        try {
            this.titleTextcolor = (int) Long.parseLong(textColor, 16);
        } catch (Exception e) {
            CreateNuclear.LOGGER.error("Text color {} is not a valid RGB color. Defaulting to white...", textColor);
            CreateNuclear.LOGGER.error(e.toString());
            this.titleTextcolor = 0xFFFFFF;
        }
    }

    protected void drawBackdrop(GuiGraphics guiGraphics, int yOffset, int width, int color) {
        int textBackgroundColor = Minecraft.getInstance().options.getBackgroundColor(0.0F);
        if (textBackgroundColor != 0) {
            int xOffset = -width / 2;
            guiGraphics.fill(xOffset - 2, yOffset - 2, xOffset + width + 2, yOffset + 9 + 2, FastColor.ARGB32.multiply(textBackgroundColor, color));
        }
    }
}
