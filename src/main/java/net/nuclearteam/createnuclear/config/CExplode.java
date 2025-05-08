package net.nuclearteam.createnuclear.config;

public class CExplode extends CNConfigBase {
    public final ConfigFloat size = f(10, 0, "Size of the reactor explosion");
    public final ConfigInt type = i(1, 0, 2, "Type of explosion", Comments.type);
    public final ConfigInt time = i(600, 100, 1200, "Duration before exploration", Comments.explosionTime, Comments.hintExplosion);

    @Override
    public String getName() {
        return "Explosion Reactor";
    }

    private static class Comments {
        static String explosionTime = "Create Nuclear Explosion Time";
        static String hintExplosion = "300 ticks = 15 seconds";
        static String type = "Explanation: 0 = no explosion, 1 = current explosion, 2 = new explosion.";
    }
}
