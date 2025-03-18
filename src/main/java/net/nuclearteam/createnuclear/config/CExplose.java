package net.nuclearteam.createnuclear.config;

public class CExplose extends CNConfigBase {
    public final ConfigInt size = i(10, "Size of the reactor explosion.");
    public final ConfigInt type = i(1, 0, 2, "Type of explosion.", Comments.type);

    @Override
    public String getName() {
        return "Explosion Reactor";
    }

    private static class Comments {
        static String type = "Explanation: 0 = no explosion, 1 = current explosion, 2 = new explosion.";
    }
}
