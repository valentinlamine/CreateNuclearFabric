package net.nuclearteam.createnuclear.config;


public class CNConfigCommon extends CNConfigBase {

    public final CNCWorldGen worldGen = nested(0, CNCWorldGen::new, Comments.worldGen);


    @Override
    public String getName() {
        return "Common";
    }

    private static class Comments {
        static String worldGen = "Modify Create's impact on your terrain";
    }
}
