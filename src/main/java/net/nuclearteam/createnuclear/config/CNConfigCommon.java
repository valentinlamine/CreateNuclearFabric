package net.nuclearteam.createnuclear.config;


public class CNConfigCommon extends CNConfigBase {

    public final CNCWorldGen worldGen = nested(0, CNCWorldGen::new, Comments.worldGen);
    public final CRod rods = nested(0, CRod::new, Comments.rods);
    public final CExplose explose = nested(0, CExplose::new, Comments.explode);


    @Override
    public String getName() {
        return "Common";
    }

    private static class Comments {
        static String worldGen = "Modify CreateNuclear's impact on your terrain";
        static String rods = "Modify rods time and config";
        static String explode = "Explose: pas d'idée";
    }
}
