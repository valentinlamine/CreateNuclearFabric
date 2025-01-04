package net.nuclearteam.createnuclear;

import static net.nuclearteam.createnuclear.packets.CNPackets.getChannel;

import net.fabricmc.api.ClientModInitializer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.renderer.RenderType;
import net.nuclearteam.createnuclear.block.CNBlocks;
import net.nuclearteam.createnuclear.entity.CNMobEntityType;
import net.nuclearteam.createnuclear.entity.CNModelLayers;
import net.nuclearteam.createnuclear.entity.irradiatedcat.IrradiatedCatModel;
import net.nuclearteam.createnuclear.entity.irradiatedcat.IrradiatedCatRenderer;
import net.nuclearteam.createnuclear.entity.irradiatedchicken.IrradiatedChickenModel;
import net.nuclearteam.createnuclear.entity.irradiatedchicken.IrradiatedChickenRenderer;
import net.nuclearteam.createnuclear.entity.irradiatedwolf.IrradiatedWolfModel;
import net.nuclearteam.createnuclear.entity.irradiatedwolf.IrradiatedWolfRenderer;
import net.nuclearteam.createnuclear.ponder.CNPonderIndex;
import net.nuclearteam.createnuclear.utils.CNClientEvent;

@Environment(EnvType.CLIENT)
public class CreateNuclearClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(CNBlocks.REINFORCED_GLASS.get(), RenderType.translucent());
        BlockRenderLayerMap.INSTANCE.putBlock(CNBlocks.ENRICHING_FIRE.get(), RenderType.cutout());
        CNPonderIndex.register();

        CNMobEntityType.registerCNMod();
        CNMobEntityType.registerModelLayer();


        getChannel().initClientListener();
        CNClientEvent.register();

    }
}
