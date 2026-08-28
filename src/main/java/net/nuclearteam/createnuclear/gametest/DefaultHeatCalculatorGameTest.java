package net.nuclearteam.createnuclear.gametest;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.core.BlockPos;
import io.github.fabricators_of_create.porting_lib.transfer.item.ItemStackHandler;
import net.nuclearteam.createnuclear.CNBlocks;
import net.nuclearteam.createnuclear.CNItems;
import net.nuclearteam.createnuclear.content.multiblock.controller.ReactorControllerBlockEntity;
import net.nuclearteam.createnuclear.content.multiblock.controller.ReactorControllerInventory;
import net.nuclearteam.createnuclear.content.multiblock.controller.display.ReactorDisplayState;
import net.nuclearteam.createnuclear.content.multiblock.reactorLogic.DefaultHeatCalculator;
import net.nuclearteam.createnuclear.infrastructure.config.CNConfigs;

import java.util.List;
import java.util.Map;

/**
 * GameTest coverage for {@link DefaultHeatCalculator#computeHeat}, run against a real
 * {@code ServerLevel}, a real {@link ReactorControllerBlockEntity} and real registered
 * rod items (no mocks) — plain JUnit cannot exercise this method at all: {@code Item},
 * {@code ItemStack} and {@code RodType} resolution all depend on Minecraft registries
 * that only exist once the game has bootstrapped (see AUDIT_ACTUEL.md §3).
 * <p>
 * All tests share the "empty_platform" structure and place a single
 * {@code CNBlocks.REACTOR_CONTROLLER} to obtain a real {@link ReactorControllerInventory}.
 * The reactor "pattern" (57-slot {@link ItemStackHandler}, slots per
 * {@code DefaultHeatCalculator.formattedPattern}) is written directly into the blueprint
 * item's {@code pattern} NBT tag and set into inventory slot 0, exactly as
 * {@code ReactorBluePrintItem.getItemStorage} reads it back.
 * <p>
 * Expected heat values are computed from the live {@code CNConfigs.server().rods} values
 * rather than hardcoded, so these tests stay correct if the mod's default balance changes.
 */
public class DefaultHeatCalculatorGameTest implements FabricGameTest {

    private static final String STRUCTURE = "createnuclear:empty_platform";
    private static final double DELTA = 1e-9;

    // ---------- test fixtures ----------

    private static ReactorControllerInventory placeController(GameTestHelper helper, BlockPos rel) {
        helper.setBlock(rel, CNBlocks.REACTOR_CONTROLLER.get().defaultBlockState());
        ReactorControllerBlockEntity be = (ReactorControllerBlockEntity) helper.getBlockEntity(rel);
        return be.getInventoryObject();
    }

    /** Writes {@code rodsBySlot} into the blueprint item's "pattern" NBT and loads it into slot 0. */
    private static void loadPattern(ReactorControllerInventory inventory, Map<Integer, Item> rodsBySlot) {
        ItemStackHandler patternHandler = new ItemStackHandler(57);
        rodsBySlot.forEach((slot, item) -> patternHandler.setStackInSlot(slot, new ItemStack(item)));

        ItemStack blueprint = new ItemStack(CNItems.REACTOR_BLUEPRINT.get());
        blueprint.getOrCreateTag().put("pattern", patternHandler.serializeNBT());
        inventory.setStackInSlot(0, blueprint);
    }

    private static final DefaultHeatCalculator CALCULATOR = new DefaultHeatCalculator();

    // ================================================================
    // 1. single fuel rod, no neighbors
    // ================================================================

    @GameTest(template = STRUCTURE)
    public void singleFuelRod_noNeighbors_addsOnlyItsOwnBaseRodHeat(GameTestHelper helper) {
        Item thorium = CNItems.THORIUM_ROD.get();
        int baseRodHeat = CNConfigs.server().rods.baseValueThorium.get();

        ReactorControllerInventory inventory = placeController(helper, new BlockPos(1, 1, 1));
        // slot 18: middle row, no adjacent slot populated -> no neighbor bonus possible
        loadPattern(inventory, Map.of(18, thorium));

        ReactorDisplayState displayState = new ReactorDisplayState(Map.of(thorium, 1), List.of(), 0);
        double overHeat = 10.0;

        double heat = CALCULATOR.computeHeat(null, null, inventory, overHeat, displayState, helper.getLevel());

        helper.assertTrue(Math.abs(heat - (baseRodHeat + overHeat)) < DELTA,
                "an isolated fuel rod should only contribute its own baseRodHeat, plus overHeat: expected "
                        + (baseRodHeat + overHeat) + ", got " + heat);
        helper.succeed();
    }

    // ================================================================
    // 2. single cooler rod, no neighbors
    // ================================================================

    @GameTest(template = STRUCTURE)
    public void singleCoolerRod_noNeighbors_addsOnlyItsOwnBaseRodHeat(GameTestHelper helper) {
        Item graphite = CNItems.GRAPHITE_ROD.get();
        int baseRodHeat = CNConfigs.server().rods.graphiteBaseValue.get();

        ReactorControllerInventory inventory = placeController(helper, new BlockPos(1, 1, 1));
        loadPattern(inventory, Map.of(18, graphite));

        ReactorDisplayState displayState = new ReactorDisplayState(Map.of(graphite, 1), List.of(), 0);

        // computeHeat clamps its result to a minimum of 0 (Math.max(0, heat+overHeat)); an isolated
        // cooler's own baseRodHeat is negative by default, so a large overHeat is added here to keep
        // the total positive and actually observe baseRodHeat's contribution instead of it being
        // swallowed by the floor.
        double overHeat = 50.0;

        double heat = CALCULATOR.computeHeat(null, null, inventory, overHeat, displayState, helper.getLevel());

        helper.assertTrue(Math.abs(heat - (baseRodHeat + overHeat)) < DELTA,
                "a cooler contributes its own baseRodHeat exactly like a fuel rod would, when isolated: expected "
                        + (baseRodHeat + overHeat) + ", got " + heat);
        helper.succeed();
    }

    // ================================================================
    // 3. fuel/cooler mix adjacency: cooler scans its own fuel neighbors symmetrically
    //    to a fuel rod (AUDIT_ACTUEL.md §2.2, fixed 2026-07-26 to match the wiki
    //    calculator / external spec: "Graphite extra: -1/4Q of the heating rod")
    // ================================================================

    /**
     * Layout (row 3 of {@code formattedPattern}, three consecutive slots):
     * uranium A (18) - thorium B (19) - graphite C (20).
     * <p>
     * Expected total = sum of each rod's own baseRodHeat, PLUS proximity contributions from
     * both fuel-fuel and cooler-fuel adjacency (cooler-cooler and fuel-cooler-from-the-fuel-side
     * contribute nothing):
     * <ul>
     *   <li>A's scan (fuel, neighbor B is fuel) -&gt; + A.proximityRodHeat()</li>
     *   <li>B's scan (fuel, neighbor A is fuel) -&gt; + B.proximityRodHeat()</li>
     *   <li>B's scan (fuel, neighbor C is cooler) -&gt; contributes nothing (fuel only scores against fuel neighbors)</li>
     *   <li>C's scan (cooler, neighbor B is fuel) -&gt; + B.baseRodHeat() * C.proximityRodHeat()</li>
     * </ul>
     */
    @GameTest(template = STRUCTURE)
    public void fuelCoolerMix_coolerScansItsOwnFuelNeighborsSymmetrically(GameTestHelper helper) {
        Item uranium = CNItems.URANIUM_ROD.get();
        Item thorium = CNItems.THORIUM_ROD.get();
        Item graphite = CNItems.GRAPHITE_ROD.get();

        int uraniumBase = CNConfigs.server().rods.uraniumBaseValue.get();
        float uraniumProxy = CNConfigs.server().rods.uraniumProximityBonus.get();
        int thoriumBase = CNConfigs.server().rods.baseValueThorium.get();
        float thoriumProxy = CNConfigs.server().rods.thoriumProxyBonus.get();
        int graphiteBase = CNConfigs.server().rods.graphiteBaseValue.get();
        float graphiteProxy = CNConfigs.server().rods.graphiteProximityMalus.getF();

        ReactorControllerInventory inventory = placeController(helper, new BlockPos(1, 1, 1));
        loadPattern(inventory, Map.of(18, uranium, 19, thorium, 20, graphite));

        ReactorDisplayState displayState = new ReactorDisplayState(
                Map.of(uranium, 1, thorium, 1, graphite, 1), List.of(), 0);

        double heat = CALCULATOR.computeHeat(null, null, inventory, /*overHeat*/ 0.0, displayState, helper.getLevel());

        double expected = (uraniumBase + thoriumBase + graphiteBase)  // each rod's own baseRodHeat
                + uraniumProxy              // A's scan: fuel neighbor B (thorium) -> addition
                + thoriumProxy              // B's scan: fuel neighbor A (uranium) -> addition
                + (thoriumBase * graphiteProxy);
                // C's scan: fuel neighbor B (thorium) -> multiplication (neighbor's base * cooler's own proximity)
                // B's scan towards C contributes 0: fuel only scores against fuel neighbors

        helper.assertTrue(Math.abs(heat - expected) < DELTA,
                "cooler must score its own malus against its fuel neighbor: expected "
                        + expected + ", got " + heat);
        helper.succeed();
    }

    // ================================================================
    // 4. 3x3 diamond of rods: cross-checked against the community wiki calculator
    // ================================================================

    /**
     * Layout (rows 3-5, columns 3-5 of {@code formattedPattern} — a fully interior 3x3 block,
     * every position outside it stays empty so neighbor counts match an isolated 3x3 grid):
     * <pre>
     * graphite(18) thorium(19) graphite(20)
     * thorium(27)  uranium(28) thorium(29)
     * graphite(36) thorium(37) graphite(38)
     * </pre>
     * With the mod's default balance values this totals {@code 128}, matching the value produced
     * by the community wiki calculator for the same pattern (verified by hand during the JS/Java
     * comparison audit, see AUDIT_ACTUEL.md §0/§2.2) — but the assertion below is derived from the
     * live config, not hardcoded, so it stays correct if the default balance changes:
     * <ul>
     *   <li>4 corner graphites, each with 2 thorium neighbors -&gt;
     *       {@code graphiteBase + 2 * (thoriumBase * graphiteProxy)} each</li>
     *   <li>4 edge thoriums, each with 2 graphite neighbors (ignored, cooler) + 1 uranium neighbor (fuel) -&gt;
     *       {@code thoriumBase + thoriumProxy} each</li>
     *   <li>1 center uranium, with 4 thorium neighbors (all fuel) -&gt;
     *       {@code uraniumBase + 4 * uraniumProxy}</li>
     * </ul>
     */
    @GameTest(template = STRUCTURE)
    public void threeByThreeDiamond_matchesWikiCalculatorReferenceValue(GameTestHelper helper) {
        Item uranium = CNItems.URANIUM_ROD.get();
        Item thorium = CNItems.THORIUM_ROD.get();
        Item graphite = CNItems.GRAPHITE_ROD.get();

        int uraniumBase = CNConfigs.server().rods.uraniumBaseValue.get();
        float uraniumProxy = CNConfigs.server().rods.uraniumProximityBonus.get();
        int thoriumBase = CNConfigs.server().rods.baseValueThorium.get();
        float thoriumProxy = CNConfigs.server().rods.thoriumProxyBonus.get();
        int graphiteBase = CNConfigs.server().rods.graphiteBaseValue.get();
        float graphiteProxy = CNConfigs.server().rods.graphiteProximityMalus.getF();

        ReactorControllerInventory inventory = placeController(helper, new BlockPos(1, 1, 1));
        loadPattern(inventory, Map.ofEntries(
                Map.entry(18, graphite), Map.entry(19, thorium), Map.entry(20, graphite),
                Map.entry(27, thorium), Map.entry(28, uranium), Map.entry(29, thorium),
                Map.entry(36, graphite), Map.entry(37, thorium), Map.entry(38, graphite)
        ));

        ReactorDisplayState displayState = new ReactorDisplayState(
                Map.of(uranium, 1, thorium, 4, graphite, 4), List.of(), 0);

        double heat = CALCULATOR.computeHeat(null, null, inventory, /*overHeat*/ 0.0, displayState, helper.getLevel());

        double cornerGraphite = graphiteBase + 2 * (thoriumBase * graphiteProxy);
        double edgeThorium = thoriumBase + thoriumProxy;
        double centerUranium = uraniumBase + 4 * uraniumProxy;
        double expected = 4 * cornerGraphite + 4 * edgeThorium + centerUranium;

        helper.assertTrue(Math.abs(heat - expected) < DELTA,
                "3x3 diamond should match the wiki calculator's reference value for this pattern: expected "
                        + expected + " (128 with default balance), got " + heat);
        helper.succeed();
    }
}
