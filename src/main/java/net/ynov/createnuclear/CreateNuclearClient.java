package net.ynov.createnuclear;

import net.fabricmc.api.ClientModInitializer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.renderer.RenderType;
import net.ynov.createnuclear.block.CNBlocks;
import net.ynov.createnuclear.entity.CNMobEntityType;
import net.ynov.createnuclear.entity.CNModelLayers;
import net.ynov.createnuclear.entity.irradiatedchicken.IrradiatedChickenModel;
import net.ynov.createnuclear.entity.irradiatedwolf.IrradiatedWolfModel;
import net.ynov.createnuclear.entity.irradiatedwolf.IrradiatedWolfRenderer;
import net.ynov.createnuclear.ponder.CNPonderIndex;
import net.ynov.createnuclear.entity.irradiatedchicken.IrradiatedChickenRenderer;

@Environment(EnvType.CLIENT)
public class CreateNuclearClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(CNBlocks.REINFORCED_GLASS.get(), RenderType.translucent());
        BlockRenderLayerMap.INSTANCE.putBlock(CNBlocks.ENRICHING_FIRE.get(), RenderType.cutout());
        CNPonderIndex.register();


        EntityModelLayerRegistry.registerModelLayer(CNModelLayers.IRRADIATED_CHICKEN, IrradiatedChickenModel::createBodyLayer);
        EntityRendererRegistry.register(CNMobEntityType.IRRADIATED_CHICKEN, IrradiatedChickenRenderer::new);

        EntityModelLayerRegistry.registerModelLayer(CNModelLayers.IRRADIATED_WOLF, IrradiatedWolfModel::createBodyLayer);
        EntityRendererRegistry.register(CNMobEntityType.IRRADIATED_WOLF, IrradiatedWolfRenderer::new);
    }
}
