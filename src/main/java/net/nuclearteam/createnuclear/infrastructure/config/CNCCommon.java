package net.nuclearteam.createnuclear.infrastructure.config;

import net.createmod.catnip.config.ConfigBase;

public class CNCCommon extends ConfigBase {
    public final CWorldGen worldGen = nested(0, CWorldGen::new, Comments.worldGen);

    @Override
    public String getName() {
        return "Common";
    }

    private static class Comments {
        static String worldGen = "Modify how CreateNuclear affects world generation.";
    }
}