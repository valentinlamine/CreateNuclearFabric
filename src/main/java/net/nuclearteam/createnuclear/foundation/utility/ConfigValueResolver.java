package net.nuclearteam.createnuclear.foundation.utility;

import net.nuclearteam.createnuclear.CreateNuclear;

import java.util.Collection;
import java.util.Set;
import java.util.function.Function;

/**
 * Utility class for resolving and loading configuration values into a target set.
 * <p>
 * This class provides a static method to map a collection of input values to a set of resolved values,
 * using a provided resolver function. It handles errors and logs unknown or invalid values.
 * </p>
 */
public final class ConfigValueResolver {
    private ConfigValueResolver() {}

    /**
     * Loads and resolves values from a collection into a target set using the provided resolver function.
     * <p>
     * The target set is cleared before loading. For each value in the input collection, the resolver function is applied.
     * If the resolved value is not null, it is added to the target set. If the value is unknown or an exception occurs,
     * an error is logged.
     * </p>
     *
     * <b>Usage example:</b>
     * <pre>
     * Set<MyType> mySet = new HashSet<>();
     * List<String> configValues = Arrays.asList("A", "B", "C");
     * ConfigValueResolver.loadValuesInSet(configValues, mySet, MyType::fromString);
     * </pre>
     *
     * @param values    the collection of input values to resolve
     * @param targetSet the set to populate with resolved values (will be cleared first)
     * @param resolver  the function to resolve each input value to the target type
     * @param <I>       the input value type
     * @param <T>       the target value type
     */
    public static <I, T> void loadValuesInSet(Collection<? extends I> values, Set<T> targetSet, Function<I, T> resolver) {
        targetSet.clear();

        for (I valueId : values) {
            try {
                T value = resolver.apply(valueId);

                if (value != null) {
                    targetSet.add(value);
                } else {
                    CreateNuclear.LOGGER.error("Unknown value in config: {}", valueId);
                }
            } catch (Exception e) {
                CreateNuclear.LOGGER.error("Unknown value in config: {} - {}", valueId, e.getMessage());
            }
        }
    }
}
