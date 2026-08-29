package net.nuclearteam.createnuclear.content.multiblock.input.item;

import org.jetbrains.annotations.NotNull;

public record VirtualReactorInputsItem(int fuel, int cooler) {
    public VirtualReactorInputsItem() {
        this(0,0);
    }

    @Override
    public @NotNull String toString() {
        return "fuel: " + fuel + " cooler: " + cooler;
    }
}

