package net.ynov.createnuclear.entity;

import com.simibubi.create.foundation.utility.Lang;
import com.tterrag.registrate.util.entry.EntityEntry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.ynov.createnuclear.CreateNuclear;
import net.ynov.createnuclear.entity.irradiatedcat.IrradiatedCat;
import net.ynov.createnuclear.entity.irradiatedchicken.IrradiatedChicken;
import net.ynov.createnuclear.entity.irradiatedwolf.IrradiatedWolf;

public class CNMobEntityType {

    public static final EntityType<IrradiatedChicken> IRRADIATED_CHICKEN =
            CNMobEntityType.register("irradiated_chicken", EntityType.Builder.of(IrradiatedChicken::new, MobCategory.CREATURE));

    public static final EntityType<IrradiatedWolf> IRRADIATED_WOLF =
            CNMobEntityType.register("irradiated_wolf", EntityType.Builder.of(IrradiatedWolf::new, MobCategory.CREATURE));

    public static final EntityType<IrradiatedCat> IRRADIATED_CAT =
            CNMobEntityType.register("irradiated_cat", EntityType.Builder.of(IrradiatedCat::new, MobCategory.CREATURE));

    private static <T extends Entity> EntityType<T> register(String key, EntityType.Builder<T> builder) {
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, key, builder.build(key));
    }

    public static void registerCNMod() {
        CreateNuclear.LOGGER.info("Registering ModEntity for " + CreateNuclear.MOD_ID);
    }

}
