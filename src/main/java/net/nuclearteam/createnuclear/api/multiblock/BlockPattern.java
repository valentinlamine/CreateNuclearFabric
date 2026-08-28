package net.nuclearteam.createnuclear.api.multiblock;

import lib.multiblock.impl.IMultiBlockPattern;

public record BlockPattern<T>(String id, T data, IMultiBlockPattern structure) {}
