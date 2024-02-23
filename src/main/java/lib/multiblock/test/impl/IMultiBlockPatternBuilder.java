package lib.multiblock.test.impl;

import lib.multiblock.test.SimpleMultiBlockPattern;

public interface IMultiBlockPatternBuilder {
    < T extends IMultiBlockPattern> T build(IPatternBuilder<T> builder);

    default <T extends IMultiBlockPattern> T build() {
        return (T) build(SimpleMultiBlockPattern::new);
    }
}
