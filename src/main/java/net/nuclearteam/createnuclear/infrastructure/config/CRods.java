package net.nuclearteam.createnuclear.infrastructure.config;

import net.createmod.catnip.config.ConfigBase;
import net.minecraft.MethodsReturnNonnullByDefault;

@MethodsReturnNonnullByDefault
public class CRods extends ConfigBase {
    public final ConfigInt uraniumRodLifetime = i(3600, 100, 5000, "uranium_rod_lifetime_ticks", Comments.uraniumRodLifetime, Comments.hintTick);
    public final ConfigInt uraniumProximityBonus = i(32, -70, 70, "uranium_proximity_bonus", Comments.warning, Comments.uraniumProximityBonus);
    public final ConfigInt uraniumBaseValue = i(64, -70, 70, "uranium_base_value", Comments.warning, Comments.uraniumBaseValue);
    public final ConfigInt uraniumHeatRatio = i(2, -10, 10, "Heat ratio for uranium rods", Comments.warning, Comments.uraniumHeatRatio);

    public final ConfigInt graphiteRodLifetime = i(3600, 100, 5000, "graphite_rod_lifetime_ticks", Comments.graphiteRodLifetime, Comments.hintTick);
    // the proximity value of coolant items are divisions so if the heat of the fuel is 64, the calcul will be "heat + 64/-4"
    public final ConfigFloat graphiteProximityMalus = f(-0.25f, -70, 70, "graphite_proximity_penalty", Comments.warning, Comments.graphiteProximityMalus);
    public final ConfigInt graphiteBaseValue = i(-32, -50, 50, "graphite_base_value", Comments.warning, Comments.graphiteBaseValue);
    public final ConfigInt graphiteHeatRatio = i(1, -10, 10, "Heat ratio for graphite rods", Comments.warning, Comments.graphiteHeatRatio);

    public final ConfigInt thoriumRodLifetime = i(3600, 100, 5000, "Thorium rod lifetime (in ticks)", Comments.thoriumRodLifetime, Comments.hintTick);
    public final ConfigInt thoriumProxyBonus = i(8, -70, 70, "Heat bonus from nearby thorium rods", Comments.warning, Comments.thoriumProximityBonus);
    public final ConfigInt baseValueThorium = i(16, -70, 70, "Base heat value for thorium rods", Comments.warning, Comments.thoriumBaseValue);
    public final ConfigInt thoriumHeatRatio = i(1, -10, 10, "Heat ratio for thorium rods", Comments.warning, Comments.thoriumHeatRatio);


    @Override
    public String getName() {
        return "rods";
    }

    private static class Comments {
        static String hintTick = "20 ticks = 1 second";
        static String warning = "Changing these values may unbalance reactor behavior.";
        static String maxFuelPerCooled = "Maximum fuel rods supported by a single cooled rod";

        static String uraniumRodLifetime = "Lifetime of uranium rods in ticks.";
        static String uraniumProximityBonus = "Heat bonus applied when uranium rods are adjacent.";
        static String uraniumBaseValue = "Base heat contribution of a uranium rod before modifiers.";
        static String uraniumHeatRatio = "Heat ratio modifier for a uranium rod";

        static String graphiteRodLifetime = "Lifetime of graphite (cooling) rods in ticks.";
        static String graphiteProximityMalus = "Heat penalty applied when graphite rods are adjacent.";
        static String graphiteBaseValue = "Base heat contribution of a graphite rod before modifiers.";
        static String graphiteHeatRatio = "Heat ratio modifier for a graphite rod";

        static String thoriumRodLifetime = "Lifetime of thorium rods in the reactor";
        static String thoriumProximityBonus = "Bonus heat applied when thorium rods are adjacent";
        static String thoriumBaseValue = "Base heat contribution of a thorium rod before modifiers";
        static String thoriumHeatRatio = "Heat ratio modifier for a thorium rod";

    }
}