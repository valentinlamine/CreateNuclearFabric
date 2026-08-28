package net.nuclearteam.createnuclear.infrastructure.worldgen;

import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.nuclearteam.createnuclear.CreateNuclear;

public final class CNPlacementModifiers {
    private static final LazyRegistrar<PlacementModifierType<?>> REGISTER =
        LazyRegistrar.create(Registries.PLACEMENT_MODIFIER_TYPE, CreateNuclear.MOD_ID);

    public static final RegistryObject<PlacementModifierType<ConfigPlacementFilter>> CONFIG_FILTER =
        REGISTER.register("config_filter", () -> () -> ConfigPlacementFilter.CODEC);

    private CNPlacementModifiers() {
    }

    public static void register() {
        REGISTER.register();
    }
}
