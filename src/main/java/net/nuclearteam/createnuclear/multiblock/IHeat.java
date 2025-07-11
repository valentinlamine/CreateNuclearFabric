package net.nuclearteam.createnuclear.multiblock;

import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.LangBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.world.item.ItemStack;
import net.nuclearteam.createnuclear.CreateNuclear;
import net.nuclearteam.createnuclear.config.CNConfigs;
import net.nuclearteam.createnuclear.tags.CNTag;

@SuppressWarnings("unused")
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
            return ofInit(heat);
        }

        public static HeatLevel ofInit(int heat) {
            if (heat < 0) return NONE;

            heat = Math.abs(heat);

            if (heat > 0 && heat < 500) return SAFETY;
            if (heat >= 501 && heat <= 800) return CAUTION;
            if (heat >= 801 && heat <= 1000) return WARNING;
            if (heat >= 1001) return DANGER;

            return NONE;
        }

        public static HeatLevel ofTest1(int heat) {
            if (heat < 0) return NONE;

            heat = Math.abs(heat);

            HeatThresholds thresholds = HeatThresholds.of(CNConfigs.common().rods.maxHeat.get());
            int h = Math.min(heat, thresholds.maxHeat());

            return h < thresholds.safety() ? SAFETY
                 : h < thresholds.caution() ? CAUTION
                 : h < thresholds.maxHeat() ? WARNING
                 : DANGER;

        }

        public static HeatLevel ofTest2(int heat) {
            if (heat < 0) return NONE;

            heat = Math.abs(heat);

            HeatThresholds thresholds = HeatThresholds.of(CNConfigs.common().rods.maxHeat.get());

            if (heat < thresholds.safety()) {
                return SAFETY;
            }

            if (heat < thresholds.caution()) {
                return CAUTION;
            }

            if (heat < thresholds.maxHeat()) {
                return WARNING;
            }

            return DANGER;
        }

        public static HeatLevel ofTest3(int heat) {
            if (heat < 0) return NONE;

            heat = Math.abs(heat);

            int maxHeat = CNConfigs.common().rods.maxHeat.get();

            int safetyThreshold = (int) (0.5 * maxHeat);  // 50%
            int cautionThreshold = (int) (0.8 * maxHeat); // 80%

            HeatLevelMapper mapper = HeatLevelMapper.newBuilder()
                    .level(HeatLevel.SAFETY).ifHeatBelow(safetyThreshold)
                    .level(HeatLevel.CAUTION).ifHeatBetween(safetyThreshold, cautionThreshold)
                    .level(HeatLevel.WARNING).ifHeatBetween(cautionThreshold + 1, maxHeat)
                    .otherwise(HeatLevel.DANGER)
                    .build();

            return mapper.map(heat);
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

        public static LangBuilder getFormattedItemText(ItemStack itemRod, Boolean IsEmpty) {
            LangBuilder builder = Lang.builder(CreateNuclear.MOD_ID);

            String tooltip = "unknown";
            
            if (itemRod.is(CNTag.ItemTags.FUEL.tag)) {
                tooltip = "uranium";
            }

            if (itemRod.is(CNTag.ItemTags.COOLER.tag)) {
                tooltip = "graphene";
            }

            builder.translate("tooltip.item." + tooltip + ".rod")
                    // when it's empty, we show the number minus one to display zero because we fake the item count as 1
                    .add(Lang.number(Math.abs((IsEmpty ? itemRod.getCount() - 1 : itemRod.getCount()))))
                    .style(ChatFormatting.BLUE)
            ;

            return builder;
        }

        public static LangBuilder getName(String name) {
            LangBuilder builder = Lang.builder(CreateNuclear.MOD_ID);
            builder.translate("gui." + name + ".info_header.title");

            return builder;
        }
    }
}
