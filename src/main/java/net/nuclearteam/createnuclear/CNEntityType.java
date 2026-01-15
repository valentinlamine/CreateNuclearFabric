package net.nuclearteam.createnuclear;

import com.tterrag.registrate.util.entry.EntityEntry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.storage.loot.LootTable;
import net.nuclearteam.createnuclear.content.contraptions.irradiated.CNModelLayers;
import net.nuclearteam.createnuclear.content.contraptions.irradiated.cat.IrradiatedCat;
import net.nuclearteam.createnuclear.content.contraptions.irradiated.cat.IrradiatedCatModel;
import net.nuclearteam.createnuclear.content.contraptions.irradiated.cat.IrradiatedCatRenderer;
import net.nuclearteam.createnuclear.content.contraptions.irradiated.chicken.IrradiatedChicken;
import net.nuclearteam.createnuclear.content.contraptions.irradiated.chicken.IrradiatedChickenModel;
import net.nuclearteam.createnuclear.content.contraptions.irradiated.chicken.IrradiatedChickenRenderer;
import net.nuclearteam.createnuclear.content.contraptions.irradiated.cow.IrradiatedCow;
import net.nuclearteam.createnuclear.content.contraptions.irradiated.cow.IrradiatedCowModel;
import net.nuclearteam.createnuclear.content.contraptions.irradiated.cow.IrradiatedCowRenderer;
import net.nuclearteam.createnuclear.content.contraptions.irradiated.wolf.IrradiatedWolf;
import net.nuclearteam.createnuclear.content.contraptions.irradiated.wolf.IrradiatedWolfModel;
import net.nuclearteam.createnuclear.content.contraptions.irradiated.wolf.IrradiatedWolfRenderer;
import net.nuclearteam.createnuclear.content.contraptions.irradiated.zombie.IrradiatedZombie;
import net.nuclearteam.createnuclear.content.contraptions.irradiated.zombie.IrradiatedZombieModel;
import net.nuclearteam.createnuclear.content.contraptions.irradiated.zombie.IrradiatedZombieRenderer;

public class CNEntityType {

    public static final EntityEntry<IrradiatedCat> IRRADIATED_CAT = CreateNuclear.REGISTRATE
        .entity("irradiated_cat", IrradiatedCat::new, MobCategory.CREATURE)
        .loot((tb, e) -> tb.add(e, LootTable.lootTable()))
        .tag(CNTags.CNEntityTypeTags.IRRADIATED_IMMUNE.tag)
        .properties(b -> b.dimensions(EntityDimensions.scalable(0.6f, 0.7f)))
        .lang("Irradiated Cat")
        .renderer(() -> IrradiatedCatRenderer::new)
        .attributes(IrradiatedCat::createAttributes)
        .register();

    public static final EntityEntry<IrradiatedChicken> IRRADIATED_CHICKEN = CreateNuclear.REGISTRATE
        .entity("irradiated_chicken", IrradiatedChicken::new, MobCategory.CREATURE)
        .loot((tb, e) -> tb.add(e, LootTable.lootTable()))
        .tag(CNTags.CNEntityTypeTags.IRRADIATED_IMMUNE.tag)
        .properties(b -> b.dimensions(EntityDimensions.scalable(0.4f, 0.7f)))
        .lang("Irradiated Chicken")
        .renderer(() -> IrradiatedChickenRenderer::new)
        .attributes(IrradiatedChicken::createAttributes)
        .register();

    public static final EntityEntry<IrradiatedCow> IRRADIATED_COW = CreateNuclear.REGISTRATE
        .entity("irradiated_cow", IrradiatedCow::new, MobCategory.CREATURE)
        .loot((tb, e) -> tb.add(e, LootTable.lootTable()))
        .tag(CNTags.CNEntityTypeTags.IRRADIATED_IMMUNE.tag)
        .properties(b -> b.dimensions(EntityDimensions.scalable(0.4f, 0.7f)))
        .lang("Irradiated Cow")
        .renderer(() -> IrradiatedCowRenderer::new)
        .attributes(IrradiatedCow::createAttributes)
        .register();

    public static final EntityEntry<IrradiatedWolf> IRRADIATED_WOLF = CreateNuclear.REGISTRATE
        .entity("irradiated_wolf", IrradiatedWolf::new, MobCategory.CREATURE)
        .loot((tb, e) -> tb.add(e, LootTable.lootTable()))
        .tag(CNTags.CNEntityTypeTags.IRRADIATED_IMMUNE.tag)
        .properties(b -> b.dimensions(EntityDimensions.scalable(0.6f, 0.85f)))
        .lang("Irradiated Wolf")
        .renderer(() -> IrradiatedWolfRenderer::new)
        .attributes(IrradiatedWolf::createAttributes)
        .register();

    public static final EntityEntry<IrradiatedZombie> IRRADIATED_ZOMBIE = CreateNuclear.REGISTRATE
        .entity("irradiated_zombie", IrradiatedZombie::new, MobCategory.CREATURE)
        .loot((tb, e) -> tb.add(e, LootTable.lootTable()))
        .tag(CNTags.CNEntityTypeTags.IRRADIATED_IMMUNE.tag)
        .properties(b -> b.dimensions(EntityDimensions.scalable(0.6f, 0.85f)))
        .lang("Irradiated Zombie")
        .renderer(() -> IrradiatedZombieRenderer::new)
        .attributes(IrradiatedZombie::createAttributes)
        .register();


    @Environment(EnvType.CLIENT)
    public static void registerModelLayer() {
        EntityModelLayerRegistry.registerModelLayer(CNModelLayers.IRRADIATED_CAT, IrradiatedCatModel::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(CNModelLayers.IRRADIATED_CHICKEN, IrradiatedChickenModel::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(CNModelLayers.IRRADIATED_WOLF, IrradiatedWolfModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(CNModelLayers.IRRADIATED_COW, IrradiatedCowModel::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(CNModelLayers.IRRADIATED_ZOMBIE, IrradiatedZombieModel::createBodyLayer);
    }

    public static void registerCNMod() {
        CreateNuclear.LOGGER.info("Registering ModEntity for {}", CreateNuclear.MOD_ID);
    }

}
