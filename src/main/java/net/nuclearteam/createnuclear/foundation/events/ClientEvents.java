package net.nuclearteam.createnuclear.foundation.events;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.util.RandomSource;
import net.nuclearteam.createnuclear.CNClientProxy;
import net.nuclearteam.createnuclear.foundation.mixin.client.CameraAccessor;
import net.nuclearteam.createnuclear.infrastructure.config.CNConfigs;

public class ClientEvents {

    /**
     * Ticks down the nuke flash/darken timers, once per client tick.
     */
    public static void onClientTick(Minecraft client) {
            // Store the previous value for smooth interpolation of the flash
            CNClientProxy.prevNukeFlashAmount = CNClientProxy.nukeFlashAmount;

            // Count down the sky-darkening timer
            if (CNClientProxy.renderNukeSkyDarkFor > 0) {
                CNClientProxy.renderNukeSkyDarkFor--;
            }

            // Ramp the white flash up while active, then fade it back out
            if (CNClientProxy.renderNukeFlashFor > 0) {
                if (CNClientProxy.nukeFlashAmount < 1F) {
                    CNClientProxy.nukeFlashAmount = Math.min(CNClientProxy.nukeFlashAmount + 0.4F, 1F);
                }
                CNClientProxy.renderNukeFlashFor--;
            } else if (CNClientProxy.nukeFlashAmount > 0F) {
                CNClientProxy.nukeFlashAmount = Math.max(CNClientProxy.nukeFlashAmount - 0.05F, 0F);
            }
    }

    /**
     * Shakes the player's camera while a nuke explosion is active.
     */
    public static void computeCameraAngles(WorldRenderContext event) {
        Entity player = Minecraft.getInstance().getCameraEntity();

        // Shake at 1.5F while the sky is darkened (nuke active), otherwise no shake
        float tremorAmount = CNClientProxy.renderNukeSkyDarkFor > 0 ? 1.5F : 0F;

        if (player != null && CNConfigs.client().screenShaking.get()) {
            if (tremorAmount > 0) {
                // Generate random offsets for the shake, once per tick
                if (CNClientProxy.lastTremorTick != player.tickCount) {
                    RandomSource rng = player.level().random;
                    CNClientProxy.randomTremorOffsets[0] = rng.nextFloat();
                    CNClientProxy.randomTremorOffsets[1] = rng.nextFloat();
                    CNClientProxy.randomTremorOffsets[2] = rng.nextFloat();
                    CNClientProxy.lastTremorTick = player.tickCount;
                }

                // Scale by Minecraft's screen-effect accessibility setting
                double intensity = tremorAmount * Minecraft.getInstance().options.screenEffectScale().get();

                // Physically offset the camera
                ((CameraAccessor) event.camera()).callMove(
                        CNClientProxy.randomTremorOffsets[0] * 0.2F * intensity,
                        CNClientProxy.randomTremorOffsets[1] * 0.2F * intensity,
                        CNClientProxy.randomTremorOffsets[2] * 0.5F * intensity
                );
            }
        }
    }

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(ClientEvents::onClientTick);
        WorldRenderEvents.START.register(ClientEvents::computeCameraAngles);
    }
}
