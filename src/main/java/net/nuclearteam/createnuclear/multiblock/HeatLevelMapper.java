package net.nuclearteam.createnuclear.multiblock;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntPredicate;

import net.nuclearteam.createnuclear.multiblock.IHeat.HeatLevel;

@SuppressWarnings("unused")
public class HeatLevelMapper {
    private final List<LevelRule> rules = new ArrayList<>();
    private HeatLevel defaultLevel = HeatLevel.NONE;

    public static Builder newBuilder() {
        return new Builder();
    }

    public HeatLevel map(int heat) {
        for (LevelRule rule : rules) {
            if (rule.predicate().test(heat)) {
                return rule.level();
            }
        }
        return defaultLevel;
    }

    private record LevelRule(HeatLevel level, IntPredicate predicate) {}

    public static class Builder {
        private final HeatLevelMapper mapper = new HeatLevelMapper();

        public ConditionBuilder level(HeatLevel level) {
            return new ConditionBuilder(this, level);
        }

        public Builder otherwise(HeatLevel level) {
            mapper.defaultLevel = level;
            return this;
        }

        public HeatLevelMapper build() {
            return mapper;
        }

        public static class ConditionBuilder {
            private final Builder parent;
            private final HeatLevel level;

            public ConditionBuilder(Builder parent, HeatLevel level) {
                this.parent = parent;
                this.level = level;
            }

            public Builder ifHeatBelow(int threshold) {
                parent.mapper.rules.add(new LevelRule(level, h -> h < threshold));
                return parent;
            }

            public Builder ifHeatAbove(int threshold) {
                parent.mapper.rules.add(new LevelRule(level, h -> h > threshold));
                return parent;
            }

            public Builder ifHeatBetween(int min, int max) {
                parent.mapper.rules.add(new LevelRule(level, h -> h >= min && h <= max));
                return parent;
            }
        }
    }
}
