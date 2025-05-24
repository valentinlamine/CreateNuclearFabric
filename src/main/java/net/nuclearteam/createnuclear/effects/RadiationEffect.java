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

public class RadiationEffect extends MobEffect {
    public RadiationEffect() {
        super(MobEffectCategory.HARMFUL, 15453236);
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, "91AEAA56-376B-4498-935B-2F7F68070635", -0.2D, AttributeModifier.Operation.MULTIPLY_TOTAL);
        this.addAttributeModifier(Attributes.ATTACK_DAMAGE, "648D7064-6A60-4F59-8ABE-C2C23A6DD7A9", -0.2D, AttributeModifier.Operation.MULTIPLY_TOTAL);
        this.addAttributeModifier(Attributes.ATTACK_SPEED, "55FCED67-E92A-486E-9800-B47F202C4386", -0.2D, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if (livingEntity.getType().is(CNTag.EntityTypeTags.IRRADIATED_IMMUNE.tag)) {
            livingEntity.removeEffect(this);
            return;
        }

        boolean isWearingAntiRadiationArmor = false;
        for (ItemStack armor : livingEntity.getArmorSlots()) {
            if (AntiRadiationArmorItem.Armor.isArmored(armor)) {
                isWearingAntiRadiationArmor = true;
                break;
            }
        }

        if (isWearingAntiRadiationArmor) {
            return;
        }

        int damage = 1 << amplifier;
        livingEntity.hurt(livingEntity.damageSources().magic(), damage);
    }
}
