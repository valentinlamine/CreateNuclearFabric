package net.nuclearteam.createnuclear.content.effects;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.effect.MobEffectInstance;
import net.nuclearteam.createnuclear.CNEffects;

public class RadiationItem extends Item {

    public RadiationItem(Item.Properties settings) {
        super(settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
        if (world.isClientSide) return;

        if (entity instanceof Player player) {
            int total = 0;
            for (ItemStack s : player.getInventory().items) {
                if (s.getItem() == this) total += s.getCount();
            }
            for (ItemStack s : player.getInventory().offhand) {
                if (s.getItem() == this) total += s.getCount();
            }


            if (total <= 0) {
                return;
            }

            int amp;

            if (total < 9) { // Voir equilibrage
                amp = 0;
            }
            else if (total < 18) { // Voir equilibrage
                amp = 1;
            }
            else {
                amp = 2;
            }

            MobEffectInstance effect = new MobEffectInstance(
                    CNEffects.RADIATION.get(),
                    40,
                    amp,
                    true,
                    true
            );

            player.addEffect(effect);
        }
    }
}