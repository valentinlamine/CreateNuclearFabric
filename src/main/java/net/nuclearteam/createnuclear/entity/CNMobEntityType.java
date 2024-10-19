package net.nuclearteam.createnuclear.entity;

import com.simibubi.create.foundation.utility.Lang;
import com.tterrag.registrate.util.entry.EntityEntry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.nuclearteam.createnuclear.CreateNuclear;
import net.nuclearteam.createnuclear.entity.irradiatedcat.IrradiatedCat;
import net.nuclearteam.createnuclear.entity.irradiatedchicken.IrradiatedChicken;
import net.nuclearteam.createnuclear.entity.irradiatedwolf.IrradiatedWolf;

public class CNMobEntityType {

    public static final EntityType<IrradiatedChicken> IRRADIATED_CHICKEN =
            CNMobEntityType.register("irradiated_chicken", EntityType.Builder.of(IrradiatedChicken::new, MobCategory.CREATURE).sized(0.4f, 0.7f));

    public static final EntityType<IrradiatedWolf> IRRADIATED_WOLF =
            CNMobEntityType.register("irradiated_wolf", EntityType.Builder.of(IrradiatedWolf::new, MobCategory.CREATURE).sized(0.6f, 0.85f));

    public static final EntityType<IrradiatedCat> IRRADIATED_CAT =
            CNMobEntityType.register("irradiated_cat", EntityType.Builder.of(IrradiatedCat::new, MobCategory.CREATURE).sized(0.6f, 0.7f));

    private static <T extends Entity> EntityType<T> register(String key, EntityType.Builder<T> builder) {
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, CreateNuclear.asResource(key), builder.build(key));
    }

    public static void registerCNMod() {
        CreateNuclear.LOGGER.info("Registering ModEntity for " + CreateNuclear.MOD_ID);
    }

}
