package net.nuclearteam.createnuclear.config;

public class CRod extends CNConfigBase {
    public final ConfigInt uraniumRodLifetime = i(3600, 100, 5000, "Uranium rod lifespan", Comments.UraniumRodLifetime, Comments.hintTick);
    public final ConfigInt graphiteRodLifetime = i(3600, 100, 5000, "Graphite rod lifespan", Comments.GraphiteRodLifetime, Comments.hintTick);
    public final ConfigInt maxHeat = i(1000, 200, 1000, "Maximum reactor heat", Comments.maxHeat, Comments.hintHeat);

    //public final ConfigGroup rodInfo = group(1, "Uranium Rod infos", Comments.warning);
    public final ConfigInt uraMaxGraph = i(3, 0, 20, "Uranium max for graphite", Comments.warning, Comments.maxUraniumPerGraphite);
    public final ConfigInt BoProxiUranium = i(5, -20, 20, "Uranium proxy bonus", Comments.warning, Comments.uraniumProximityBonus);
    public final ConfigInt MaProxigraphite = i(-5, -20, 20, "Graphite proxy Malus", Comments.warning, Comments.graphiteProximityMalus);
    public final ConfigInt baseValueUranium = i(25, -50, 50, "Base value Uranium", Comments.warning, Comments.uraniumBaseValue);
    public final ConfigInt baseValueGraphite = i(-10, -50, 50, "Base value Graphite", Comments.warning, Comments.graphiteBaseValue);

    @Override
    public String getName() {
        return "Rods";
    }

    private static class Comments {
        static String hintTick = "20 ticks = 1 second";
        static String UraniumRodLifetime = "Uranium rod lifespan in reactor";
        static String GraphiteRodLifetime = "Graphite rod lifespan in reactor";
        static String maxHeat = "Maximum heat a reactor block can handle";
        static String hintHeat = "Avoids reactor failure due to excessive heat";

        static String warning = " Modifying these values may cause imbalances in the reactor";
        static String maxUraniumPerGraphite = "Maximum uranium rods per graphite rod";
        static String uraniumProximityBonus = "Bonus applied when uranium rods are near each other";
        static String graphiteProximityMalus = "Malus applied when graphite rods are near each other";
        static String uraniumBaseValue = "Base value of uranium before modifiers";
        static String graphiteBaseValue = "Base value of graphite before modifiers";
    }
}
