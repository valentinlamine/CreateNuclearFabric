package net.nuclearteam.createnuclear.foundation.events;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.nuclearteam.createnuclear.CNEffects;
import net.nuclearteam.createnuclear.CNTags;

public class CNCommonEvents {
    public static void handleFluidEffect(ServerLevel world) {
        world.players().forEach(player -> {
            if (player.isAlive() && !player.isSpectator()) {
                if (player.tickCount % 20 != 0) return;
                if (player.updateFluidHeightAndDoFluidPushing(CNTags.CNFluidTags.URANIUM.tag, 0.014) || player.updateFluidHeightAndDoFluidPushing(CNTags.CNFluidTags.URANIUM.tag, 0.014)) {
                    player.addEffect(new MobEffectInstance(CNEffects.RADIATION.get(), 100, 0));
                }
            }
        });
    }

    public static void register() {
        ServerTickEvents.START_WORLD_TICK.register(CNCommonEvents::handleFluidEffect);
    }
}
