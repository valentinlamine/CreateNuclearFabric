package net.nuclearteam.createnuclear.attributes;

import com.tterrag.registrate.util.entry.RegistryEntry;
import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.nuclearteam.createnuclear.CreateNuclear;

public class CNAttributes {
    public static final LazyRegistrar<Attribute> ATTRIBUTE = LazyRegistrar.create(BuiltInRegistries.ATTRIBUTE, CreateNuclear.MOD_ID);
    public static final RegistryObject<RangedAttribute> IRRADIATED_RESISTANCE = ATTRIBUTE.register("generic.irradiated_resistance", () -> new RangedAttribute("attribute.name.createnuclear.generic.irradiated_resistance", 0, 0, 6));

    public static void register() {
    }
}
