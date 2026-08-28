package net.nuclearteam.createnuclear.content.effects;

import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.nuclearteam.createnuclear.CNAttributes;

import java.util.UUID;

public class IodineEffect extends MobEffect {
    public static final UUID IODINE_UUID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

    protected IodineEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);

        this.addAttributeModifier(CNAttributes.IRRADIATED_RESISTANCE.get(), IODINE_UUID.toString(), 1D, AttributeModifier.Operation.ADDITION);
    }

    public IodineEffect() {
        this(MobEffectCategory.BENEFICIAL, 7328217);
    }

    @Override
    public boolean canApplyUpdateEffect(int pDuration, int pAmplifier) {
        return true;
    }
}
