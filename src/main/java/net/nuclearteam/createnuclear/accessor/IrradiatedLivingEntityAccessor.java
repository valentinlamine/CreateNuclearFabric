package net.nuclearteam.createnuclear.accessor;

public interface IrradiatedLivingEntityAccessor {
    default boolean radiationImmune() {
        throw new RuntimeException("This should be overridden by mixin!");
    }
}
