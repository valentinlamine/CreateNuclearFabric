package net.nuclearteam.createnuclear.content.redstone.displayLink.source;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.network.chat.MutableComponent;

/**
 * Typed, immutable snapshot of a reactor controller's summary stats (status, size,
 * fuel, cooler, fluid, heat), one named {@link ReactorSummaryRow} per stat.
 * <p>
 * Replaces the previous {@code List<List<MutableComponent>>} representation, which
 * relied on a size-based sentinel (1 element = no controller, 6 elements = full
 * summary) and on fragile positional access (e.g. {@code components.get(2).get(1)}
 * for the fuel value) that silently broke if the row order ever changed.
 * <p>
 * Callers should build instances via {@link Builder}, and only call {@link #toRows()}
 * at the point where Create's {@code DisplaySource} API actually requires the
 * positional list format.
 */
public record ReactorSummary(ReactorSummaryRow status,
                             ReactorSummaryRow size,
                             ReactorSummaryRow fuel,
                             ReactorSummaryRow cooler,
                             ReactorSummaryRow fluid,
                             ReactorSummaryRow heat) {

    /** A single display row: a label component and its associated value component. */
    public record ReactorSummaryRow(MutableComponent label, MutableComponent value) {
        List<MutableComponent> toPair() {
            return List.of(label(), value());
        }
    }

    /**
     * Converts this summary to the positional {@code List<List<MutableComponent>>}
     * format required by Create's {@code DisplaySource} API. This is the single place
     * where row order matters; every other consumer should access fields by name
     * (e.g. {@link #fuel()}) instead of relying on this order.
     */
    public List<List<MutableComponent>> toRows() {
        return List.of(
                status().toPair(), size().toPair(), fuel().toPair(),
                cooler().toPair(), fluid().toPair(), heat().toPair()
        );
    }

    /**
     * Fluent builder for {@link ReactorSummary}. All six rows are required;
     * {@link #build()} throws {@link IllegalStateException} naming any missing row
     * instead of allowing a partially-populated summary to be constructed silently.
     */
    public static class Builder {
        private ReactorSummaryRow status;
        private ReactorSummaryRow size;
        private ReactorSummaryRow fuel;
        private ReactorSummaryRow cooler;
        private ReactorSummaryRow fluid;
        private ReactorSummaryRow heat;

        public Builder status(MutableComponent label, MutableComponent value) {
            this.status = new ReactorSummaryRow(label, value);
            return this;
        }

        public Builder size(MutableComponent label, MutableComponent value) {
            this.size = new ReactorSummaryRow(label, value);
            return this;
        }

        public Builder fuel(MutableComponent label, MutableComponent value) {
            this.fuel = new ReactorSummaryRow(label, value);
            return this;
        }

        public Builder cooler(MutableComponent label, MutableComponent value) {
            this.cooler = new ReactorSummaryRow(label, value);
            return this;
        }

        public Builder fluid(MutableComponent label, MutableComponent value) {
            this.fluid = new ReactorSummaryRow(label, value);
            return this;
        }

        public Builder heat(MutableComponent label, MutableComponent value) {
            this.heat = new ReactorSummaryRow(label, value);
            return this;
        }

        public ReactorSummary build() {
            List<String> missing = new ArrayList<>();
            if (status == null) missing.add("status");
            if (size == null) missing.add("size");
            if (fuel == null) missing.add("fuel");
            if (cooler == null) missing.add("cooler");
            if (fluid == null) missing.add("fluid");
            if (heat == null) missing.add("heat");

            if (!missing.isEmpty())
                throw new IllegalStateException("Missing required ReactorSummary fields: " + String.join(", ", missing));

            return new ReactorSummary(status, size, fuel, cooler, fluid, heat);
        }
    }
}
