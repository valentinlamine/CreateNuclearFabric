package net.ynov.createnuclear.multiblock;

import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.LangBuilder;
import net.minecraft.ChatFormatting;
import net.ynov.createnuclear.CreateNuclear;

public interface IHeat extends IWrenchable {

    enum HeatLevel {
        NONE(ChatFormatting.DARK_GRAY, 0x000000),
        SAFETY(ChatFormatting.GREEN, 0x68CC03),
        CAUTION(ChatFormatting.YELLOW, 0xC9CC03),
        WARNING(0xFF6A00, 0xC9CC03),
        DANGER(ChatFormatting.RED, 0xFF6A00),
        ;

        private final ChatFormatting color;
        private final Integer intColor;
        private final int colorCode;

        HeatLevel(ChatFormatting textColor, int colorCode) {
            this.color = textColor;
            this.intColor = null;
            this.colorCode = colorCode;
        }

        HeatLevel(int intColor, int colorCode) {
            this.color = null;
            this.intColor = intColor;
            this.colorCode = colorCode;
        }

        public ChatFormatting getTextColor() {
            return color;
        }

        public int getColorCode() {
            return colorCode;
        }

        public int getHeatValue() {
            return switch (this) {
                case SAFETY -> 0;
                case CAUTION -> 1;
                case WARNING -> 2;
                case DANGER -> 3;
                default -> 0;
            };
        }

        public  static HeatLevel of(int heat) {
            heat = Math.abs(heat);

            if (heat > 0 && heat < 78) return SAFETY;
            if (heat >= 79/*126*/ && heat <= 83/*134*/) return CAUTION;
            if (heat >= 100) return DANGER;

            return NONE;
        }

        public static LangBuilder getFormattedHeatText(int heat) {
            HeatLevel heatLevel = of(heat);
            LangBuilder builder = Lang.builder(CreateNuclear.MOD_ID).text(TooltipHelper.makeProgressBar(5, heatLevel.ordinal()));

            builder.translate("tooltip.heatLevel." + Lang.asId(heatLevel.name()))
                    .space()
                    .text("(")
                    .add(Lang.number(Math.abs(heat)))
                    .space()
                    .translate("generic.unit.heat")
                    .text(")")
                    .space();

            if (heatLevel == DANGER) builder.style(DANGER.getTextColor()).style(ChatFormatting.STRIKETHROUGH);
            else builder.style(heatLevel.getTextColor());

            return builder;
        }

        public static LangBuilder getName(String name) {
            LangBuilder builder = Lang.builder(CreateNuclear.MOD_ID);
            builder.translate("gui." + name + ".info_header.title");

            return builder;
        }
    }
}