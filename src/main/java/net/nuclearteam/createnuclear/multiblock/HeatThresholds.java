package net.nuclearteam.createnuclear.multiblock;

// dans HeatThresholds.java (ou en nested static record dans HeatLevel.java)
public record HeatThresholds(int safety, int caution, int maxHeat) {
    /**
     * Construit les seuils dynamiques à partir de maxHeat configuré.
     * - safety  = 50% de maxHeat
     * - caution = 80% de maxHeat
     */
    public static HeatThresholds of(int maxHeat) {
        int tSafety  = 500  * maxHeat / 1000;  // ancien 500 → 50%
        int tCaution = 800  * maxHeat / 1000;  // ancien 800 → 80%
        return new HeatThresholds(tSafety, tCaution, maxHeat);
    }
}
