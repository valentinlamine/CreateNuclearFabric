package net.nuclearteam.createnuclear.infrastructure.config;

import net.createmod.catnip.config.ConfigBase;

public class CNotify extends ConfigBase {
    public final ConfigBool warnAllPlayers = b(false, "warn_all_players", Comments.warning);
    public final ConfigInt warningDistance = i(100, 30, 5000, "warning_distance_blocks", Comments.rangeOfWarning);

    @Override
    public String getName() {
        return "Notify";
    }

    private static class Comments {
        static String warning = "When true, broadcast a warning to nearby players before a reactor explosion.";
        static String rangeOfWarning = "Distance (in blocks) to search for players to warn about impending explosion.";
    }
}
