package net.nuclearteam.createnuclear.infrastructure.config;

import net.createmod.catnip.config.ConfigBase;

public class CReactorHeat extends ConfigBase {
    public final ConfigInt size5Danger = i(256, 0, 8192, "size5Danger", "Heat threshold for DANGER level (5x5 reactor)");
    public final ConfigInt size7Danger = i(1024, 0, 8192, "size7Danger", "Heat threshold for DANGER level (7x7 reactor)");
    public final ConfigInt size9Danger = i(4096, 0, 8192, "size9Danger", "Heat threshold for DANGER level (9x9 reactor)");

    @Override
    public String getName() {
        return "ReactorHeat";
    }
}
