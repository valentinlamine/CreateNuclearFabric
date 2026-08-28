package net.nuclearteam.createnuclear;


import net.createmod.ponder.foundation.PonderIndex;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.resources.ResourceLocation;
import net.nuclearteam.createnuclear.content.biome.BiomeIrradiationExtractorItem;
import net.nuclearteam.createnuclear.content.particles.IrradiatedParticles;
import net.nuclearteam.createnuclear.content.particles.IrradiatedParticlesData;
import net.nuclearteam.createnuclear.content.particles.NuclearMushroomCloudParticle;
import net.nuclearteam.createnuclear.content.particles.SmallNuclearExplosionParticle;
import net.nuclearteam.createnuclear.foundation.events.CNClientEvent;
import net.nuclearteam.createnuclear.foundation.events.ClientEvents;
import net.nuclearteam.createnuclear.foundation.events.RodsTooltipHandler;
import net.nuclearteam.createnuclear.foundation.ponder.CreateNuclearPonderPlugin;
import net.nuclearteam.createnuclear.foundation.utility.ClothTagHelper;
import net.nuclearteam.createnuclear.content.multiblock.alarm.ReactorAlarmClient;
import net.nuclearteam.createnuclear.content.multiblock.alarm.ReactorAlarmEntity;
import net.nuclearteam.createnuclear.content.multiblock.controller.ReactorControllerBlockEntity;
import net.nuclearteam.createnuclear.content.multiblock.controller.display.ReactorGoggleTooltipRenderer;
import net.nuclearteam.createnuclear.content.multiblock.input.item.ReactorRodInputClient;

import static net.nuclearteam.createnuclear.CNPackets.getChannel;

@Environment(EnvType.CLIENT)
public class CreateNuclearClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
       CNClientRegistrations.register();
       ReactorAlarmEntity.setClientTick(ReactorAlarmClient::tick);
       ReactorRodInputClient.register();
       ReactorControllerBlockEntity.setGoggleTooltipRenderer(ReactorGoggleTooltipRenderer::render);
       registerItemProperties();
       registerParticles();

        PonderIndex.addPlugin(new CreateNuclearPonderPlugin());

       getChannel().initClientListener();
       CNClientEvent.register();
       ClientEvents.register();
       RodsTooltipHandler.register();
    }

    private static void registerItemProperties() {
        ResourceLocation clothColor = CreateNuclear.asResource("cloth_color");
        ClampedItemPropertyFunction colorProvider = (stack, world, entity, seed) -> {
            DyeColor dye = DyeColor.byName(ClothTagHelper.getClothColor(stack, "default"), null);
            return dye == null ? 0f : (dye.getId() + 1) / 16f;
        };
        ItemProperties.register(CNItems.ANTI_RADIATION_HELMETS.get(), clothColor, colorProvider);
        ItemProperties.register(CNItems.ANTI_RADIATION_CHESTPLATES.get(), clothColor, colorProvider);
        ItemProperties.register(CNItems.ANTI_RADIATION_LEGGINGS.get(), clothColor, colorProvider);
        ItemProperties.register(CNItems.ANTI_RADIATION_BOOTS.get(), clothColor, colorProvider);

        ClampedItemPropertyFunction chargeProvider = (stack, world, entity, seed) -> {
            int maxCharge = BiomeIrradiationExtractorItem.getMaxCharge();
            return maxCharge <= 0 ? 0f : (float) BiomeIrradiationExtractorItem.getChargeTag(stack, 0) / maxCharge;
        };
        ItemProperties.register(CNItems.IRRADIATION_BIOME_EXTRACTOR.get(),
                CreateNuclear.asResource(BiomeIrradiationExtractorItem.TAG), chargeProvider);
    }

    @SuppressWarnings("unchecked")
    private static void registerParticles() {
        ParticleFactoryRegistry particles = ParticleFactoryRegistry.getInstance();
        particles.register(CNParticleRegistry.NUCLEAR_MUSHROOM_CLOUD.get(), new NuclearMushroomCloudParticle.Factory());
        particles.register(CNParticleRegistry.NUCLEAR_MUSHROOM_CLOUD_SMOKE.get(), SmallNuclearExplosionParticle.NukeFactory::new);
        particles.register(CNParticleRegistry.NUCLEAR_MUSHROOM_CLOUD_EXPLOSION.get(), SmallNuclearExplosionParticle.NukeFactory::new);
        particles.register((ParticleType<IrradiatedParticlesData>) CNParticleTypes.IRRADIATED_PARTICLES.get(), IrradiatedParticles.Provider::new);
    }
}
