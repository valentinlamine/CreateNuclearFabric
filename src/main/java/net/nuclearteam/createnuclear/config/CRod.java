package net.nuclearteam.createnuclear.config;

public class CRod extends CNConfigBase {
    public final ConfigInt time = i(600, 100, 1200, "Duration before exploration", Comments.explosionTime, Comments.hintExplosion);
    public final ConfigInt uraniumRodLifetime = i(3600, 100, 5000, "Uranium rod lifespan", Comments.UraniumRodLifetime, Comments.hintTick);
    public final ConfigInt graphiteRodLifetime = i(3600, 100, 5000, "Graphite rod lifespan", Comments.GraphiteRodLifetime, Comments.hintTick);
    public final ConfigInt maxHeat = i(1000, 200, 1000, "Maximum reactor heat", Comments.maxHeat, Comments.hintHeat);

    @Override
    public String getName() {
        return "Rods";
    }

    private static class Comments {
        static String explosionTime = "Create Nuclear Explosion Time";
        static String hintExplosion = "300 ticks = 15 seconds";
        static String hintTick = "20 ticks = 1 second";
        static String UraniumRodLifetime = "Uranium rod lifespan in reactor";
        static String GraphiteRodLifetime = "Graphite rod lifespan in reactor";
        static String maxHeat = "Maximum heat a reactor block can handle";
        static String hintHeat = "Avoids reactor failure due to excessive heat";
    }
}
