package net.nuclearteam.createnuclear.infrastructure.config;

import net.createmod.catnip.config.ConfigBase;

public class CNCClient extends ConfigBase {
    public final ConfigBool nuclearBombFlash = b(true, "nuclear_bomb_flash", Comments.nuclearBombFlash);
    public final ConfigBool screenShaking = b(true, "screen_shake", Comments.screenShaking);


    @Override
    public String getName() { return "Client"; }

    private static class Comments {
        static String nuclearBombFlash = "Enable the bright flash at the start of a nuclear explosion.";
        static String screenShaking = "Enable screen shake effect during a nuclear explosion.";
    }
}