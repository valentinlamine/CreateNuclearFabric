package com.createnuclear;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderLayer;
import com.createnuclear.block.ModBlocks;


public class CreateNuclearClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.ENRICHING_FLAME, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.ENRICHING_CAMPFIRE, RenderLayer.getCutout());
	}
}