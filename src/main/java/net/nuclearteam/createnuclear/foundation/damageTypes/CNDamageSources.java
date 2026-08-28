package net.nuclearteam.createnuclear.foundation.damageTypes;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.nuclearteam.createnuclear.CNDamageTypes;

import javax.annotation.Nullable;

public class CNDamageSources {
    public static DamageSource radiation(Level level) {
        return source(CNDamageTypes.RADIATION, level);
    }
    public static DamageSource fanRadiation(Level level) {
        return source(CNDamageTypes.FAN_RADIATION, level);
    }

    private static DamageSource source(ResourceKey<DamageType> key, LevelReader level) {
        Registry<DamageType> registry = level.registryAccess().getValue(Registries.DAMAGE_TYPE);
        return new DamageSource(registry.entryOf(key));
    }

    private static DamageSource source(ResourceKey<DamageType> key, LevelReader level, @Nullable Entity entity) {
        Registry<DamageType> registry = level.registryAccess().getValue(Registries.DAMAGE_TYPE);
        return new DamageSource(registry.entryOf(key), entity);
    }

    private static DamageSource source(ResourceKey<DamageType> key, LevelReader level, @Nullable Entity causingEntity, @Nullable Entity directEntity) {
        Registry<DamageType> registry = level.registryAccess().getValue(Registries.DAMAGE_TYPE);
        return new DamageSource(registry.entryOf(key), causingEntity, directEntity);
    }
}