package net.nuclearteam.createnuclear.entity;

import com.tterrag.registrate.util.entry.EntityEntry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.storage.loot.LootTable;
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
import net.nuclearteam.createnuclear.tags.CNTag;

public class CNMobEntityType {

    public static final EntityEntry<IrradiatedCat> IRRADIATED_CAT = CreateNuclear.REGISTRATE
        .entity("irradiated_cat", IrradiatedCat::new, MobCategory.CREATURE)
        .loot((tb, e) -> tb.add(e, LootTable.lootTable()))
        .tag(CNTag.EntityTypeTags.IRRADIATED_IMMUNE.tag, EntityTypeTags.FALL_DAMAGE_IMMUNE)
        .properties(b -> b.dimensions(EntityDimensions.scalable(0.6f, 0.7f)))
        .lang("Irradiated Cat")
        .renderer(() -> IrradiatedCatRenderer::new)
        .attributes(IrradiatedCat::createAttributes)
        .register();

    public static final EntityEntry<IrradiatedChicken> IRRADIATED_CHICKEN = CreateNuclear.REGISTRATE
        .entity("irradiated_chicken", IrradiatedChicken::new, MobCategory.CREATURE)
        .loot((tb, e) -> tb.add(e, LootTable.lootTable()))
        .tag(CNTag.EntityTypeTags.IRRADIATED_IMMUNE.tag, EntityTypeTags.FALL_DAMAGE_IMMUNE)
        .properties(b -> b.dimensions(EntityDimensions.scalable(0.4f, 0.7f)))
        .lang("Irradiated Chicken")
        .renderer(() -> IrradiatedChickenRenderer::new)
        .attributes(IrradiatedChicken::createAttributes)
        .register();

    public static final EntityEntry<IrradiatedWolf> IRRADIATED_WOLF = CreateNuclear.REGISTRATE
        .entity("irradiated_wolf", IrradiatedWolf::new, MobCategory.CREATURE)
        .loot((tb, e) -> tb.add(e, LootTable.lootTable()))
        .tag(CNTag.EntityTypeTags.IRRADIATED_IMMUNE.tag)
        .properties(b -> b.dimensions(EntityDimensions.scalable(0.6f, 0.85f)))
        .lang("Irradiated Wolf")
        .renderer(() -> IrradiatedWolfRenderer::new)
        .attributes(IrradiatedWolf::createAttributes)
        .register();


    @Environment(EnvType.CLIENT)
    public static void registerModelLayer() {
        EntityModelLayerRegistry.registerModelLayer(CNModelLayers.IRRADIATED_CAT, IrradiatedCatModel::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(CNModelLayers.IRRADIATED_CHICKEN, IrradiatedChickenModel::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(CNModelLayers.IRRADIATED_WOLF, IrradiatedWolfModel::getTexturedModelData);
    }

    public static void registerCNMod() {
        CreateNuclear.LOGGER.info("Registering ModEntity for {}", CreateNuclear.MOD_ID);
    }

}
