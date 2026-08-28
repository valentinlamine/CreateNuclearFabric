package net.nuclearteam.createnuclear.content.multiblock.reactorLogic;

/**
 * Reactor thermal equilibrium state, derived from the fuel:cooler ratio
 * (wiki reference: {@code heatPoints / coolingPoints} ratio compared to 6).
 * <p>
 * Only {@link #OVERHEATING} is currently consumed by
 * {@link DefaultOverheatController} (triggers the escalation toward
 * shutdown/explosion). {@link #BALANCED} and {@link #OVERCOOLING} are kept
 * for future versions (wiki-style status display, output bonus/malus) but
 * have no effect on overheating today: a reactor in overcooling is simply
 * suboptimal (less heat produced), not penalized.
 */
public enum EquilibriumState {
    /** ratio &gt; 6:1 — too much fuel for the cooling, leads to explosion. */
    OVERHEATING,
    /** ratio == 6:1 — exact wiki equilibrium. */
    BALANCED,
    /** ratio &lt; 6:1 — safe but suboptimal output, no malus. */
    OVERCOOLING
}
