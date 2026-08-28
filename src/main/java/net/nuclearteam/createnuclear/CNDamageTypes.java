package net.nuclearteam.createnuclear;

import com.simibubi.create.foundation.damageTypes.DamageTypeBuilder;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;

@SuppressWarnings("unused")
public class CNDamageTypes {
    public static final ResourceKey<DamageType>
        RADIATION = key("radiation"),
        FAN_RADIATION = key("fan_radiation")
    ;

    private static ResourceKey<DamageType> key(String name) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, CreateNuclear.asResource(name));
    }

    public static void bootstrap(BootstapContext<DamageType> ctx) {
        new DamageTypeBuilder(RADIATION).register(ctx);
        new DamageTypeBuilder(FAN_RADIATION).register(ctx);
    }
}
