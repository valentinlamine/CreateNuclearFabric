package net.nuclearteam.createnuclear.infrastructure.config;

import net.createmod.catnip.config.ConfigBase;

public class CWorldGen extends ConfigBase {
    public final ConfigBool enable = b(true, "enable_world_gen", Comments.enable);

    @Override
    public String getName() {
        return "worldGen";
    }

    private static class Comments {
        static String enable = "When true, CreateNuclear world generation features are active. Set to false to disable all added ores/structures.";
    }
}
