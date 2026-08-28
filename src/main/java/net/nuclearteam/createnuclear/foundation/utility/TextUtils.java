package net.nuclearteam.createnuclear.foundation.utility;


import org.apache.commons.lang3.StringUtils;
import java.util.Optional;
import java.util.stream.Stream;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.Component;

@SuppressWarnings("unused")
public class TextUtils {
    public static String titleCaseConversion(String inputString)
    {
        if (StringUtils.isBlank(inputString)) {
            return "";
        }

        if (StringUtils.length(inputString) == 1) {
            return inputString.toUpperCase();
        }

        inputString = inputString.replaceAll("_", " ");

        StringBuffer resultPlaceHolder = new StringBuffer(inputString.length());

        Stream.of(inputString.split(" ")).forEach(stringPart ->
        {
            if (stringPart.length() > 1)
                resultPlaceHolder.append(stringPart.substring(0, 1)
                                .toUpperCase())
                        .append(stringPart.substring(1)
                                .toLowerCase());
            else
                resultPlaceHolder.append(stringPart.toUpperCase());

            resultPlaceHolder.append(" ");
        });
        return StringUtils.trim(resultPlaceHolder.toString());
    }

    public static Component translateWithFormatting(String key, Object... args) {
        MutableComponent base = Component.translatable(key, args);
        StringBuilder partsStringBuilder = new StringBuilder();
        base.visit((style, part) -> {
            partsStringBuilder.append(part);
            return Optional.empty();
        }, Style.EMPTY);
        return Component.literal(partsStringBuilder.toString());
    }

    public static String formatInt(int num) {
        return formatInt(num, ",");
    }

    public static String formatInt(int num, String separator) {
        String raw = String.valueOf(num);
        if (raw.length() <= 3)
            return raw;

        int start = raw.length() % 3;
        StringBuilder out = new StringBuilder(raw.length() + (raw.length()/3) * separator.length());
        out.append(raw, 0, start);
        for (int i = 0; i < raw.length() / 3; i++) {
            if (i != 0 || start != 0)
                out.append(separator);
            out.append(raw, i*3 + start, i*3 + start + 3);
        }
        return out.toString();
    }

    public static String leftPad(String s, char c, int width) {
        if (s.length() >= width) return s;
        return String.valueOf(c).repeat(width - s.length()) + s;
    }
}
