package net.nuclearteam.createnuclear.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.nuclearteam.createnuclear.item.armor.AntiRadiationArmorItem;
import net.nuclearteam.createnuclear.tags.CNTag;

import java.util.stream.StreamSupport;

public class RadiationEffect extends MobEffect {

    public RadiationEffect() {
        super(MobEffectCategory.HARMFUL, 0xEBB13C);
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED,
                "91AEAA56-376B-4498-935B-2F7F68070635", -0.2D, AttributeModifier.Operation.MULTIPLY_TOTAL);
        this.addAttributeModifier(Attributes.ATTACK_DAMAGE,
                "648D7064-6A60-4F59-8ABE-C2C23A6DD7A9", -0.2D, AttributeModifier.Operation.MULTIPLY_TOTAL);
        this.addAttributeModifier(Attributes.ATTACK_SPEED,
                "55FCED67-E92A-486E-9800-B47F202C4386", -0.2D, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.getType().is(CNTag.EntityTypeTags.IRRADIATED_IMMUNE.tag)) {
            entity.removeEffect(this);
            return;
        }

        boolean isProtected = StreamSupport.stream(entity.getArmorSlots().spliterator(), false)
                .allMatch(AntiRadiationArmorItem.Armor::isArmored);

        if (isProtected) {
            entity.hurt(entity.damageSources().magic(), 0.0F);
            this.removeAttributeModifiers(entity, entity.getAttributes(), amplifier);
        } else {
            entity.hurt(entity.damageSources().magic(), 1 << amplifier);
        }
    }
}
