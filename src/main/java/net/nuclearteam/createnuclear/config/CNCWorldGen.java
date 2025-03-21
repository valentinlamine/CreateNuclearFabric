package net.nuclearteam.createnuclear.config;

import com.simibubi.create.foundation.config.ConfigBase;
import com.simibubi.create.infrastructure.config.CWorldGen;

public class CNCWorldGen extends CNConfigBase {
    public final ConfigBool disable = b(false, "disableWorldGen", Comments.disable);

    @Override
    public String getName() {
        return "worldgen";
    }

    private static class Comments {
        static String disable = "Prevents all worldgen added by Create from taking effect";
    }
}
