package net.ynov.createnuclear.mobentity;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.ynov.createnuclear.CreateNuclear;
import net.ynov.createnuclear.entity.IrradiatedChicken;

public class CNMobEntityType {
    private static <T extends Entity> EntityType<T> register(String key, EntityType.Builder<T> builder) {
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, key, builder.build(key));
    }

    public static final EntityType<IrradiatedChicken> IRRADIATED_CHICKEN =
            CNMobEntityType.register("irradiated_chicken", EntityType.Builder.of(IrradiatedChicken::new, MobCategory.CREATURE));

    public static void registerCNMod() {
        CreateNuclear.LOGGER.info("Registering ModEntity for " + CreateNuclear.MOD_ID);

        //ItemGroupEvents.modifyEntriesEvent(CNGroup.MAIN_KEY).register(CNBlocks::addBlockToCreateNuclearItemGroup);
    }

}
