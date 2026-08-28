package net.nuclearteam.createnuclear.content.radiation;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.effect.MobEffectCategory;
import net.nuclearteam.createnuclear.CNEffects;
import net.nuclearteam.createnuclear.content.effects.VicinityEffect;
import net.nuclearteam.createnuclear.content.radiation.capability.RadiationCapability;
import net.nuclearteam.createnuclear.foundation.damageTypes.CNDamageSources;

public class RadiationEffect extends VicinityEffect {
    private static final int CONTAGION_DURATION_TICKS = 300;

    /**
     * Constructs the RadiationEffect with harmful category and color.
     * Also applies attribute modifiers to reduce speed, attack damage, and attack speed.
     */
    public RadiationEffect() {
        super(MobEffectCategory.HARMFUL, 15453236,
                amplifier -> 10,
                RadiationCapability::canBeIrradiated,
                timer -> {}); // Custom color (hex value)

        // Reduces movement speed by 20%
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED,
                "91AEAA56-376B-4498-935B-2F7F68070635", -0.2D, AttributeModifier.Operation.MULTIPLY_TOTAL);

        // Reduces attack damage by 20%
        this.addAttributeModifier(Attributes.ATTACK_DAMAGE,
                "648D7064-6A60-4F59-8ABE-C2C23A6DD7A9", -0.2D, AttributeModifier.Operation.MULTIPLY_TOTAL);

        // Reduces attack speed by 20%
        this.addAttributeModifier(Attributes.GENERIC_ATTACK_SPEED,
                "55FCED67-E92A-486E-9800-B47F202C4386", -0.2D, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    /**
     * Determines if the effect should be applied this tick.
     * Returning true causes the effect to apply every tick.
     *
     * @param duration  The remaining duration of the effect.
     * @param amplifier The strength (level) of the effect.
     * @return true if the effect should apply on this tick.
     */
    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    @Override
    protected void onContaminate(LivingEntity nearby) {
        RadiationCapability.applyContagion(nearby, 15D, CONTAGION_DURATION_TICKS);
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        super.applyUpdateEffect(entity, amplifier);

        double resistance = RadiationCapability.getRadiationResistance(entity);

        float damage = (float) ((1 << amplifier) * (1.0 - resistance));

        if (damage <= 0.0f) {
            return;
        }

        entity.damage(CNDamageSources.radiation(entity.level()), damage);
    }
}
