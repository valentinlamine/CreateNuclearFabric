package net.nuclearteam.createnuclear.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.nuclearteam.createnuclear.item.armor.AntiRadiationArmorItem;
import net.nuclearteam.createnuclear.tags.CNTag;

/**
 * Represents the Radiation Effect applied to a LivingEntity.
 * This effect harms entities by modifying attributes like movement speed, attack damage, and attack speed,
 * and applies damage over time unless mitigated by anti-radiation armor or immunity.
 */
public class RadiationEffect extends MobEffect {

    /**
     * Constructor for the RadiationEffect.
     * Initializes the effect with a harmful category and a specific color.
     * It also adds attribute modifiers to reduce movement speed, attack damage, and attack speed.
     */
    public RadiationEffect() {
        super(MobEffectCategory.HARMFUL, 15453236);  // The color code is arbitrary for this effect's visual representation
        // Attribute modifiers reduce movement speed, attack damage, and attack speed by 20% each
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, "91AEAA56-376B-4498-935B-2F7F68070635", -0.2D, AttributeModifier.Operation.MULTIPLY_TOTAL);
        this.addAttributeModifier(Attributes.ATTACK_DAMAGE, "648D7064-6A60-4F59-8ABE-C2C23A6DD7A9", -0.2D, AttributeModifier.Operation.MULTIPLY_TOTAL);
        this.addAttributeModifier(Attributes.ATTACK_SPEED, "55FCED67-E92A-486E-9800-B47F202C4386", -0.2D, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    /**
     * Determines whether the effect should be applied every tick.
     * In this case, the effect is applied every tick as long as the duration is greater than 0.
     *
     * @param duration The remaining duration of the effect.
     * @param amplifier The amplifier level of the effect.
     * @return True, meaning this effect will trigger every tick.
     */
    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    /**
     * Applies the radiation effect to a LivingEntity.
     * It checks if the entity has anti-radiation armor, immunity to radiation, or applies damage otherwise.
     * <p>
     * The entity will not take damage if they have anti-radiation armor,
     * but will receive magic damage over time otherwise. The damage is amplified based on the effect level.
     *
     * @param livingEntity The entity affected by the radiation.
     * @param amplifier The amplifier level of the effect, determining the damage.
     */
    @Override
    public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
        // Loop through the armor slots to check if the entity is wearing anti-radiation armor
        livingEntity.getArmorSlots().forEach(armorItem -> {
            // Check if the entity has the radiation effect and is wearing anti-radiation armor
            if (livingEntity.hasEffect(CNEffects.RADIATION.get()) && AntiRadiationArmorItem.Armor.isArmored(armorItem)) {
                // If armor protects the player, they take no damage
                livingEntity.hurt(livingEntity.damageSources().magic(), 0.0F);
                // Remove any attribute modifiers (e.g., speed or attack damage) applied by the radiation effect
                this.removeAttributeModifiers(livingEntity, livingEntity.getAttributes(), 0);
            }
            // Check if the entity is immune to radiation based on its type
            else if (livingEntity.getType().is(CNTag.EntityTypeTags.IRRADIATED_IMMUNE.tag)) {
                // Remove the radiation effect if the entity is immune
                livingEntity.removeEffect(this);
            }
            // If the entity is neither protected nor immune, apply damage based on the amplifier
            else {
                livingEntity.hurt(livingEntity.damageSources().magic(), 1 << amplifier);  // Magic damage with power scaled by amplifier
            }
        });
    }
}