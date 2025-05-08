package net.nuclearteam.createnuclear.compact.archEx;

import com.tterrag.registrate.providers.RegistrateLangProvider;

import java.util.List;
import java.util.function.BiConsumer;

public record CNLangProvider(String modId, List<CNArchExGroup> groups, BiConsumer<String, String> langConsumer) {
    public static final String LANG_PREFIX = "architecture_extensions.grouped_block.";

    public void run() {
        groups.forEach(group -> {
            String key = LANG_PREFIX + modId + '.' + group.name();
            String translated = RegistrateLangProvider.toEnglishName(group.name());
            langConsumer.accept(key, translated);
        });
    }
}
