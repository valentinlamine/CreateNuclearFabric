package net.nuclearteam.createnuclear.infrastructure.config;

import net.createmod.catnip.config.ConfigBase;

public class CNCServer extends ConfigBase {
    public final CRods rods = nested(0, CRods::new, Comments.rods);
    public final CNotify notify = nested(0, CNotify::new, Comments.notify);
    public final CRadiation radiation = nested(0, CRadiation::new, Comments.radiation);
    public final CReactorHeat reactorHeat = nested(0, CReactorHeat::new, Comments.reactorHeat);
    public final CBiomeRestore biomeRestore = nested(0, CBiomeRestore::new, Comments.biomeRestore);


    @Override
    public String getName() {
        return "Server";
    }

    private static class Comments {
        static String rods = "Modify rod durations and related parameters.";
        static String radiation = "Enable or disable radiation effects emitted by mod items. ";
        static String notify = "Notification settings for reactor warnings.";
        static String reactorHeat = "Heat thresholds for different reactor sizes";
        static String biomeRestore = "";

    }
}