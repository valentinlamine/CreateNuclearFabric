package net.nuclearteam.createnuclear.entity;

import com.simibubi.create.foundation.utility.Lang;
import com.tterrag.registrate.util.entry.EntityEntry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.nuclearteam.createnuclear.CreateNuclear;
import net.nuclearteam.createnuclear.entity.irradiatedcat.IrradiatedCat;
import net.nuclearteam.createnuclear.entity.irradiatedcat.IrradiatedCatModel;
import net.nuclearteam.createnuclear.entity.irradiatedcat.IrradiatedCatRenderer;
import net.nuclearteam.createnuclear.entity.irradiatedchicken.IrradiatedChicken;
import net.nuclearteam.createnuclear.entity.irradiatedchicken.IrradiatedChickenModel;
import net.nuclearteam.createnuclear.entity.irradiatedchicken.IrradiatedChickenRenderer;
import net.nuclearteam.createnuclear.entity.irradiatedwolf.IrradiatedWolf;
import net.nuclearteam.createnuclear.entity.irradiatedwolf.IrradiatedWolfModel;
import net.nuclearteam.createnuclear.entity.irradiatedwolf.IrradiatedWolfRenderer;

public class CNMobEntityType {

    public static final EntityType<IrradiatedChicken> IRRADIATED_CHICKEN =
            CNMobEntityType.register("irradiated_chicken", EntityType.Builder.of(IrradiatedChicken::new, MobCategory.CREATURE).sized(0.4f, 0.7f));

    public static final EntityType<IrradiatedWolf> IRRADIATED_WOLF =
            CNMobEntityType.register("irradiated_wolf", EntityType.Builder.of(IrradiatedWolf::new, MobCategory.CREATURE).sized(0.6f, 0.85f));

    public static final EntityType<IrradiatedCat> IRRADIATED_CAT =
            CNMobEntityType.register("irradiated_cat", EntityType.Builder.of(IrradiatedCat::new, MobCategory.CREATURE).sized(0.6f, 0.7f));

    private static <T extends Entity> EntityType<T> register(String key, EntityType.Builder<T> builder) {
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, key, builder.build(key));
    }

    @Environment(EnvType.CLIENT)
    public static void EntityModelLayerAndEntityRendererRegistry() {
        EntityModelLayerRegistry.registerModelLayer(CNModelLayers.IRRADIATED_CHICKEN, IrradiatedChickenModel::createBodyLayer);
        EntityRendererRegistry.register(CNMobEntityType.IRRADIATED_CHICKEN, IrradiatedChickenRenderer::new);

        EntityModelLayerRegistry.registerModelLayer(CNModelLayers.IRRADIATED_WOLF, IrradiatedWolfModel::getTexturedModelData);
        EntityRendererRegistry.register(CNMobEntityType.IRRADIATED_WOLF, IrradiatedWolfRenderer::new);

        EntityModelLayerRegistry.registerModelLayer(CNModelLayers.IRRADIATED_CAT, IrradiatedCatModel::createBodyLayer);
        EntityRendererRegistry.register(CNMobEntityType.IRRADIATED_CAT, IrradiatedCatRenderer::new);
    }

    public static void registerCNMod() {
        CreateNuclear.LOGGER.info("Registering ModEntity for " + CreateNuclear.MOD_ID);
    }

}
