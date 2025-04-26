package net.nuclearteam.createnuclear.world;

import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.nuclearteam.createnuclear.CreateNuclear;

public class CNPlacementModifiers {
    private static final LazyRegistrar<PlacementModifierType<?>> REGISTER = LazyRegistrar.create(Registries.PLACEMENT_MODIFIER_TYPE, CreateNuclear.MOD_ID);

    public static final RegistryObject<PlacementModifierType<CNConfigPlacementFilter>> CONFIG_FILTER = REGISTER.register("config_filter", () -> () -> CNConfigPlacementFilter.CODEC);

    public static void register() {
        REGISTER.register();
    }
}
