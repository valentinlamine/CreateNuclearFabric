package net.nuclearteam.createnuclear;

import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.core.registries.Registries;
import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;

public class CNAttributes {
    public static final LazyRegistrar<Attribute> ATTRIBUTES =
        LazyRegistrar.create(Registries.ATTRIBUTE, CreateNuclear.MOD_ID);

    private static final Attribute IRRADIATED_RESISTANCE_ATTRIBUTE =
        new RangedAttribute("attribute.name.createnuclear.generic.irradiated_resistance", 0, 0, 1)
            .setSyncable(true);

    public static final RegistryObject<Attribute> IRRADIATED_RESISTANCE = ATTRIBUTES.register(
        "generic.irradiated_resistance", () -> IRRADIATED_RESISTANCE_ATTRIBUTE);

    /**
     * Used while vanilla builds its default LivingEntity attribute containers, before
     * Fabric invokes the mod initializer and registers the attribute itself.
     */
    public static Attribute irradiatedResistanceAttribute() {
        return IRRADIATED_RESISTANCE_ATTRIBUTE;
    }

    public static void register() {
        ATTRIBUTES.register();
    }
}
