package net.nuclearteam.createnuclear.fluid;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.FluidState;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Central manager for registering and applying fluid interaction rules.
 */
public class FluidInteractionManager {
    /**
     * Context passed to interaction callbacks, encapsulating
     * world, position, source flag, and the fluid instance.
     */
    public record InteractionContext(LevelAccessor world, BlockPos pos, boolean isSource, FlowingFluid fluid) {}


    /**
     * Defines how a source fluid interacts with an adjacent target fluid.
     *
     * @param priority     evaluation order (lower values are processed first)
     * @param checkFluid   predicate to test the adjacent FluidState (e.g., water or lava)
     * @param resultState  function that returns the replacement BlockState
     *                     based on whether the source block is a full source (true) or flowing (false)
     * @param onInteract   callback to execute after the default fizz effect
     *                     (for additional particles, sounds, etc.)
     * @param returnValue  the boolean value to return from shouldSpreadLiquid
     *                     (false to cancel vanilla propagation, true to allow it)
     */
    public record FluidInteractionRule(int priority, Predicate<FluidState> checkFluid, Boolean excludeSelf, Function<Boolean, BlockState> resultState,
                                       Consumer<InteractionContext> onInteract, boolean returnValue) {}

    /** Map of source fluid → list of interaction rules. */
    private static final Map<FlowingFluid, List<FluidInteractionRule>> RULES = new HashMap<>();

    /** Directions in which to check adjacent fluids. */
    private static final ImmutableList<Direction> DIRS = LiquidBlock.POSSIBLE_FLOW_DIRECTIONS;

    /**
     * Registers a new interaction rule for the given source fluid.
     */
    public static void addRule(FlowingFluid source, FluidInteractionRule rule) {
        RULES.computeIfAbsent(source, f -> new ArrayList<>()).add(rule);
    }

    /**
     * Applies the first matching rule for the given source fluid at the position.
     * Executes a default fizz(), then any custom onInteract(), and returns
     * Optional.of(returnValue). If no rule matches, returns Optional.empty().
     */
    public static Optional<Boolean> applyRules(FlowingFluid fluid,
                                               LevelAccessor world,
                                               BlockPos pos) {
        List<FluidInteractionRule> list = RULES.get(fluid);
        if (list == null || list.isEmpty()) {
            return Optional.empty();
        }

        boolean isSource = world.getFluidState(pos).isSource();
        InteractionContext ctx = new InteractionContext(world, pos, isSource, fluid);

        List<FluidInteractionRule> sorted = list.stream()
                .sorted(Comparator.comparingInt(r -> r.priority))
                .toList();

        for (FluidInteractionRule rule : sorted) {
            boolean adjacentMatch = DIRS.stream()
                    .map(d -> world.getFluidState(pos.relative(d)))
                    .anyMatch(fs -> rule.checkFluid().test(fs) && (!rule.excludeSelf() || !fs.getType().isSame(fluid)));;
            if (!adjacentMatch) continue;

            // replace block
            if (world instanceof Level level) {
                level.setBlockAndUpdate(pos, rule.resultState.apply(isSource));
            }
            // default fizz effect
            world.levelEvent(1501, pos, 0);
            // custom effects
            rule.onInteract.accept(ctx);
            // return configured value
            return Optional.of(rule.returnValue);
        }

        return Optional.empty();
    }
}
