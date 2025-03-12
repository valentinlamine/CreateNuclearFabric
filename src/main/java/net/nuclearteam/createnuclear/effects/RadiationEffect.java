package net.nuclearteam.createnuclear.effects;

import io.github.fabricators_of_create.porting_lib.entity.client.MobEffectRenderer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.*;
import net.nuclearteam.createnuclear.CreateNuclear;
import net.nuclearteam.createnuclear.item.armor.AntiRadiationArmorItem;
import net.nuclearteam.createnuclear.tags.CNTag;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class RadiationEffect extends MobEffect {
    public RadiationEffect() {
        super(MobEffectCategory.HARMFUL, 15453236);

        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, "91AEAA56-376B-4498-935B-2F7F68070635", -0.3D, AttributeModifier.Operation.MULTIPLY_TOTAL);
        this.addAttributeModifier(Attributes.ATTACK_DAMAGE, "648D7064-6A60-4F59-8ABE-C2C23A6DD7A9", -0.3D, AttributeModifier.Operation.MULTIPLY_TOTAL);
        this.addAttributeModifier(Attributes.ATTACK_SPEED, "55FCED67-E92A-486E-9800-B47F202C4386", -0.1D, AttributeModifier.Operation.MULTIPLY_TOTAL);

    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
        //Si le joueur porte l'armure anti_radiation_suit alors il ne prend pas de dégâts
        livingEntity.getArmorSlots().forEach(e -> {
            if (livingEntity.hasEffect(CNEffects.RADIATION.get()) && AntiRadiationArmorItem.Armor.isArmored(e)) {
                livingEntity.hurt(livingEntity.damageSources().magic(), 0.0F);
                this.removeAttributeModifiers(livingEntity, livingEntity.getAttributes(), 15);

            }
            else if (livingEntity.getType().is(CNTag.EntityTypeTags.IRRADIATED_IMMUNE.tag)) {
                livingEntity.removeEffect(this);
            }
            else {
                livingEntity.hurt(livingEntity.damageSources().magic(), 1 << amplifier);
                this.addAttributeModifier(Attributes.MOVEMENT_SPEED, "7107DE5E-7CE8-4030-940E-514C1F160890", 15.0,  AttributeModifier.Operation.MULTIPLY_TOTAL);
            }
        });
    }
}
