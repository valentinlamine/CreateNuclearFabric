package net.nuclearteam.createnuclear.foundation.events;

import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.BuiltInRegistries;
import net.nuclearteam.createnuclear.CreateNuclear;
import net.nuclearteam.createnuclear.foundation.item.RodsStats;

public class RodsTooltipHandler {
    public static void register() {
        ItemTooltipCallback.EVENT.register((stack, context, tooltip) -> {
        var player = Minecraft.getInstance().player;
        if (player == null) return;

        var id = BuiltInRegistries.ITEM.getKey(stack.getItem());
        // Mod items already get their rod tooltip via Registrate's setTooltipModifierFactory
        // (CreateNuclear.java:48). This handler only serves EXTERNAL items (other mods or
        // datapack-defined RodTypes resolved at runtime via RodType.resolveRodType), which
        // setTooltipModifierFactory cannot cover. Do NOT remove/invert: doing so double-tooltips mod rods.
        if (CreateNuclear.MOD_ID.equals(id.getNamespace())) return;

        RodsStats.create(stack.getItem()).modify(stack, player, context, tooltip);
        });
    }
}
