package net.nuclearteam.createnuclear.multiblock;

import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.LangBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.nuclearteam.createnuclear.CreateNuclear;
import net.nuclearteam.createnuclear.tags.CNTag;

public interface IHeat extends IWrenchable {

    enum HeatLevel {
        NONE(ChatFormatting.DARK_GRAY, 0x000000),
        SAFETY(ChatFormatting.GREEN, 0x68CC03),
        CAUTION(ChatFormatting.YELLOW, 0xC9CC03),
        WARNING(ChatFormatting.GOLD, 0xFF6A00),
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
                case CAUTION -> 1;
                case WARNING -> 2;
                case DANGER -> 3;
                default -> 0;
            };
        }

        public static HeatLevel of(int heat) {
            if (heat < 0) return NONE;

            heat = Math.abs(heat);

            if (heat > 0 && heat < 500) return SAFETY;
            if (heat >= 501 && heat <= 800) return CAUTION;
            if (heat >= 801 && heat <= 1000) return WARNING;
            if (heat >= 1001) return DANGER;

            return NONE;
        }

        public static LangBuilder getFormattedHeatText(int heat) {
            HeatLevel heatLevel = of(heat);
            LangBuilder builder = Lang.builder(CreateNuclear.MOD_ID).text(TooltipHelper.makeProgressBar(5, heatLevel.ordinal()+1));

            builder.translate("tooltip.heatLevel." + Lang.asId(heatLevel.name()))
                    .space()
                    .text("(")
                    .add(Lang.number(heat))
                    .space()
                    .translate("generic.unit.heat")
                    .text(")")
                    .space();

            if (heatLevel == DANGER) builder.style(DANGER.getTextColor()).style(ChatFormatting.STRIKETHROUGH);
            else builder.style(heatLevel.getTextColor());

            return builder;
        }

        public static LangBuilder getFormattedItemText(ItemStack itemRod) {
            LangBuilder builder = Lang.builder(CreateNuclear.MOD_ID);

            String tooltip = itemRod.is(CNTag.ItemTags.FUEL.tag)
                    ? "uranium"
                    : itemRod.is(CNTag.ItemTags.COOLER.tag)
                        ? "graphene"
                        : "unknown";

            builder.translate("tooltip.item." + tooltip + ".rod")
                    .add(Lang.number(Math.abs(itemRod.getCount())));

            if (tooltip.contains("unknown")) builder.style(ChatFormatting.GRAY).style(ChatFormatting.ITALIC);
            else builder.style(ChatFormatting.BLUE);

            return builder;
        }

        public static LangBuilder getName(String name) {
            LangBuilder builder = Lang.builder(CreateNuclear.MOD_ID);
            builder.translate("gui." + name + ".info_header.title");

            return builder;
        }
    }
}
