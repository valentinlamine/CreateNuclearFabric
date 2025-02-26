package net.nuclearteam.createnuclear.config;


public class CNConfigCommon extends CNConfigBase {

    public final CNCWorldGen worldGen = nested(0, CNCWorldGen::new, Comments.worldGen);
    public final ConfigInt time = i(600, 100, 1200, "Durée avant l'explotion", Comments.explotionTime, Comments.indice);


    @Override
    public String getName() {
        return "Common";
    }

    private static class Comments {
        static String worldGen = "Modify CreateNuclear's impact on your terrain";
        static String explotionTime = "Create Nuclear Explotion Time";
        static String indice = "300 ticks = 15 secondes";
    }
}
