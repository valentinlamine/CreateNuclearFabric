package net.nuclearteam.createnuclear.effects.damageTypes;

import com.simibubi.create.foundation.damageTypes.DamageTypeBuilder;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.nuclearteam.createnuclear.CreateNuclear;

public enum CNDamageTypes {
    IRRADIATED
    ;
    private String name;
    private final ResourceKey<DamageType> damageType;

    CNDamageTypes(String name) {
        this.damageType = ResourceKey.create(Registries.DAMAGE_TYPE, CreateNuclear.asResource(name));
    }

    CNDamageTypes() {
        this.damageType = ResourceKey.create(Registries.DAMAGE_TYPE, CreateNuclear.asResource(Lang.asId(name().toLowerCase())));
    }

    public ResourceKey<DamageType> key() {
        return damageType;
    }

    public static void bootstrap(BootstapContext<DamageType> ctx) {
        new DamageTypeBuilder(CNDamageTypes.IRRADIATED.key()).register(ctx);
    }
}
