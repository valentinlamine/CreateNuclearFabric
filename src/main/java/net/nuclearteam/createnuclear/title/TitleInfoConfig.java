package net.nuclearteam.createnuclear.title;

public enum TitleInfoConfig {
    DEFAULT(true, 0, 0, 0, 0, "FFFFFF", 0, false, true),
    WARNING(true, 10, 50, 10, 80, "FFFFFF", 2.1, true, true),
    ;

    private final boolean enabled;
    private final int textFadeInTime;
    private final int TextDisplayTime;
    private final int textFadeOutTime;
    private final int textCooldownTime;
    private final String textColor;
    private final double textSize;
    private final boolean renderShadow;
    private final boolean centerText;


    TitleInfoConfig(boolean enabled, int textFadeInTime, int TextDisplayTime, int textFadeOutTime, int textCooldownTime, String textColor, double textSize, boolean renderShadow, boolean centerText) {
        this.enabled = enabled;
        this.textFadeInTime = textFadeInTime;
        this.TextDisplayTime = TextDisplayTime;
        this.textFadeOutTime = textFadeOutTime;
        this.textCooldownTime = textCooldownTime;
        this.textColor = textColor;
        this.textSize = textSize;
        this.renderShadow = renderShadow;
        this.centerText = centerText;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isCenterText() {
        return centerText;
    }

    public boolean isRenderShadow() {
        return renderShadow;
    }

    public double getTextSize() {
        return textSize;
    }

    public int getTextCooldownTime() {
        return textCooldownTime;
    }

    public int getTextDisplayTime() {
        return TextDisplayTime;
    }

    public int getTextFadeInTime() {
        return textFadeInTime;
    }

    public int getTextFadeOutTime() {
        return textFadeOutTime;
    }

    public String getTextColor() {
        return textColor;
    }
}
