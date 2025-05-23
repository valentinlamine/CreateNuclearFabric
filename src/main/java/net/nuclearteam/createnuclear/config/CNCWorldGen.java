package net.nuclearteam.createnuclear.config;

public class CNCWorldGen extends CNConfigBase {
    public final ConfigBool disable = b(false, "disableWorldGen", Comments.disable);

    @Override
    public String getName() {
        return "worldgen";
    }

    private static class Comments {
        static String disable = "Prevents all worldgen added by CreateNuclear from taking effect";
    }
}
