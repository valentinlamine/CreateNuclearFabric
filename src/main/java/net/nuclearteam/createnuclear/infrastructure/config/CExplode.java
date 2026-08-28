package net.nuclearteam.createnuclear.infrastructure.config;

import net.createmod.catnip.config.ConfigBase;

public class CExplode extends ConfigBase {
    public final ConfigInt size = i(10, "Explosion size (radius in blocks)");
    public final ConfigInt type = i(1, 0, 2, "Type of explosion", Comments.type);
    public final ConfigInt time = i(600, 100, 1200, "Delay before explosion (ticks)", Comments.explosionTime, Comments.hintExplosion);

    @Override
    public String getName() {
        return "Explosion Reactor";
    }

    private static class Comments {
        static String explosionTime = "Delay before the nuclear explosion (in ticks).";
        static String hintExplosion = "300 ticks = 15 seconds.";
        static String type = "0 = none, 1 = current explosion behavior, 2 = alternative explosion behavior.";
    }
}
