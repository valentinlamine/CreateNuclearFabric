package net.ynov.createnuclear.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.ynov.createnuclear.item.CNItems;

public class RadiationEffect extends MobEffect {
    public RadiationEffect() {
        super(MobEffectCategory.HARMFUL, 15453236);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
        //Si le joueur porte l'armure anti_radiation_suit alors il ne prend pas de dégâts
        livingEntity.getArmorSlots().forEach(e -> {
            if (e.getItem() == CNItems.ANTI_RADIATION_BOOTS.get()
                    || e.getItem() == CNItems.ANTI_RADIATION_CHESTPLATE.get()
                    || e.getItem() == CNItems.ANTI_RADIATION_HELMET.get()
                    || e.getItem() == CNItems.ANTI_RADIATION_LEGGINGS.get()) {
                livingEntity.hurt(livingEntity.damageSources().magic(), 0.0F);

            } else {
                livingEntity.hurt(livingEntity.damageSources().magic(), 1.0F);
            }
        });


//        if (this == CNEffects.RADIATION.get() && !armored) {
//            CreateNuclear.LOGGER.warn("RadiationEffect: ArmorSlots is null   " + livingEntity.getArmorSlots());
//
//            livingEntity.hurt(livingEntity.damageSources().magic(), 1.0F);
//        }
    }
}
