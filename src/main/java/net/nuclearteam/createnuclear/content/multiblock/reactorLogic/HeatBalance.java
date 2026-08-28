package net.nuclearteam.createnuclear.content.multiblock.reactorLogic;

/**
 * Thermal balance of a pattern: heat points (fuel) and cooling points
 * (cooler), each weighted by {@link net.nuclearteam.createnuclear.api.multiblock.rods.RodType#ratio()}.
 * <p>
 * Since {@code graphiteHeatRatio} switched to a positive value (1),
 * {@code heatPoints} and {@code coolingPoints} are both raw positive sums
 * (e.g. uranium=2/rod, thorium=1/rod, graphite=1/rod) — the 6:1 equilibrium
 * ratio (wiki reference) is therefore no longer implicit in the config and
 * must be compared explicitly via {@link #TARGET_RATIO}.
 */
public record HeatBalance(int heatPoints, int coolingPoints) {
    /** Reference fuel:cooler ratio (wiki) at equilibrium. */
    public static final int TARGET_RATIO = 6;

    /**
     * Resolves the equilibrium state from this balance.
     *
     * @return {@link EquilibriumState#OVERHEATING} if no cooler is present
     *         while there is fuel, otherwise the state corresponding to the
     *         comparison of {@code heatPoints / coolingPoints} against
     *         {@link #TARGET_RATIO}
     */
    public EquilibriumState resolve() {
        if (coolingPoints == 0) return heatPoints() > 0 ? EquilibriumState.OVERHEATING : EquilibriumState.BALANCED;

        double ration = (double) heatPoints() / coolingPoints();
        if (ration > TARGET_RATIO) return EquilibriumState.OVERHEATING;
        if (ration < TARGET_RATIO) return EquilibriumState.OVERCOOLING;

        return EquilibriumState.BALANCED;
    }
}
