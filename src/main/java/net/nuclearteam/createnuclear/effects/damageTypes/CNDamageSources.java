package net.nuclearteam.createnuclear.effects.damageTypes;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelReader;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum CNDamageSources {
    RADIANCE(CNDamageTypes.IRRADIATED.key())
    ;

    private final ResourceKey<DamageType> key;

    private static final Map<ResourceKey<DamageType>, Holder<DamageType>> HOLDER_CACHE =
            new ConcurrentHashMap<>();

    CNDamageSources(ResourceKey<DamageType> key) {
        this.key = key;
    }

    private static Holder<DamageType> getHolder(ResourceKey<DamageType> key, LevelReader lvl) {
        return HOLDER_CACHE.computeIfAbsent(key, k -> lvl
                .registryAccess()
                .registryOrThrow(Registries.DAMAGE_TYPE)
                .getHolderOrThrow(k)
        );
    }

    public DamageSource create(LevelReader lvl, @Nullable Entity... entities) {
        Holder<DamageType> holder = getHolder(key, lvl);
        return switch (entities == null ? 0 : entities.length) {
            case 0 -> new DamageSource(holder);
            case 1 -> new DamageSource(holder, entities[0]);
            default -> new DamageSource(holder, entities[0], entities[1]);
        };
    }
}
