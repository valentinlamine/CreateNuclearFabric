package net.nuclearteam.createnuclear.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.nuclearteam.createnuclear.CreateNuclear;
import net.nuclearteam.createnuclear.item.armor.AntiRadiationArmorItem;
import net.nuclearteam.createnuclear.tags.CNTag;

/**
 * Represents a harmful radiation status effect applied to living entities.
 * This effect reduces movement speed, attack damage, and attack speed,
 * and periodically inflicts magic damage unless the entity is immune
 * or wearing anti-radiation armor.
 */
public class RadiationEffect extends MobEffect {

    /**
     * Constructs the RadiationEffect with harmful category and color.
     * Also applies attribute modifiers to reduce speed, attack damage, and attack speed.
     */
    public RadiationEffect() {
        super(MobEffectCategory.HARMFUL, 15453236); // Custom color (hex value)

        // Reduces movement speed by 20%
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED,
                "91AEAA56-376B-4498-935B-2F7F68070635", -0.2D, AttributeModifier.Operation.MULTIPLY_TOTAL);

        // Reduces attack damage by 20%
        this.addAttributeModifier(Attributes.ATTACK_DAMAGE,
                "648D7064-6A60-4F59-8ABE-C2C23A6DD7A9", -0.2D, AttributeModifier.Operation.MULTIPLY_TOTAL);

        // Reduces attack speed by 20%
        this.addAttributeModifier(Attributes.ATTACK_SPEED,
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
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    /**
     * Applies the radiation effect to the entity.
     * - Does nothing if the entity is immune via tag.
     * - Skips damage if the entity wears any anti-radiation armor.
     * - Otherwise, applies magic damage based on the amplifier.
     *
     * @param livingEntity The affected living entity.
     * @param amplifier    The strength (level) of the effect.
     */
    @Override
    public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
        // If the entity is immune to radiation, remove the effect
        if (livingEntity.getType().is(CNTag.EntityTypeTags.IRRADIATED_IMMUNE.tag)) {
            livingEntity.removeEffect(this);
            return;
        }

        // Check if the entity is wearing any anti-radiation armor
        boolean isWearingAntiRadiationArmor = false;
        for (ItemStack armor : livingEntity.getArmorSlots()) {
            if (AntiRadiationArmorItem.Armor.isArmored(armor)) {
                isWearingAntiRadiationArmor = true;
                break;
            }
        }

        // If protected by armor, do not apply damage
        if (isWearingAntiRadiationArmor) {
            return;
        }

        // Apply radiation damage (magic type), scaled by amplifier
        int damage = 1 << amplifier;
        livingEntity.hurt(livingEntity.damageSources().magic(), damage);
    }
}
