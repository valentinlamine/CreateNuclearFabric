package net.nuclearteam.createnuclear;


import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;
import net.nuclearteam.createnuclear.foundation.events.CNClientEvent;

import static net.nuclearteam.createnuclear.CNPackets.getChannel;

@Environment(EnvType.CLIENT)
public class CreateNuclearClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
       BlockRenderLayerMap.INSTANCE.putBlock(CNBlocks.REINFORCED_GLASS.get(), RenderType.translucent());
       BlockRenderLayerMap.INSTANCE.putBlock(CNBlocks.ENRICHING_FIRE.get(), RenderType.cutout());

       CNEntityType.register();
       CNEntityType.registerModelLayer();


       getChannel().initClientListener();
       CNClientEvent.register();

    }
}
