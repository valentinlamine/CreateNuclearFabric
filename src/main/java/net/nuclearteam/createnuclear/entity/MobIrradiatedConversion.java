package net.nuclearteam.createnuclear.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;

import java.util.HashMap;
import java.util.Map;

public enum MobIrradiatedConversion {
    CAT(EntityType.CAT, CNMobEntityType.IRRADIATED_CAT.get()),
    CHICKEN(EntityType.CHICKEN, CNMobEntityType.IRRADIATED_CHICKEN.get()), // pas beacoups de vie
    WOLF(EntityType.WOLF, CNMobEntityType.IRRADIATED_WOLF.get()),
    PIG(EntityType.PIG, CNMobEntityType.IRRADIATED_PIG.get()),
    BEE(EntityType.BEE, CNMobEntityType.IRRADIATED_BEE.get()),
    ;

    private static final Map<EntityType<?>, EntityType<?>> IRRADIATED_MAP = new HashMap<>();

    static {
        for (MobIrradiatedConversion conversion : values()) {
            IRRADIATED_MAP.put(conversion.origineMob, conversion.irradiatedMobType);
        }
    }

    private final EntityType<?> origineMob;
    private final EntityType<?> irradiatedMobType;

    MobIrradiatedConversion(EntityType<? extends Mob> origineMob, EntityType<? extends Mob> irradiatedMobType) {
        this.origineMob = origineMob;
        this.irradiatedMobType = irradiatedMobType;
    }

    public EntityType<?> getOrigineMob() {
        return origineMob;
    }

    public EntityType<?> getIrradiatedMobType() {
        return irradiatedMobType;
    }

    public static EntityType<?> getByIrradiatedType(EntityType<?> origineMob) {
        return IRRADIATED_MAP.get(origineMob);
    }

    public static boolean isPresent(EntityType<?> entityType) {
        return IRRADIATED_MAP.containsKey(entityType);
    }


}
