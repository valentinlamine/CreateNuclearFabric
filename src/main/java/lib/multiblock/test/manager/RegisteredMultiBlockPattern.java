package lib.multiblock.test.manager;

import lib.multiblock.test.impl.IMultiBlockPattern;

public record RegisteredMultiBlockPattern<T> (String ID, T data, IMultiBlockPattern pattern) {
}
