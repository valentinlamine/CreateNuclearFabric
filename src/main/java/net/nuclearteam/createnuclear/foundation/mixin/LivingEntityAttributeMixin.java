package net.nuclearteam.createnuclear.foundation.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.nuclearteam.createnuclear.CNAttributes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityAttributeMixin {
    @Inject(
        method = "createLivingAttributes()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;",
        at = @At("RETURN")
    )
    private static void createnuclear$addRadiationResistance(
        CallbackInfoReturnable<AttributeSupplier.Builder> cir
    ) {
        cir.getReturnValue().add(CNAttributes.irradiatedResistanceAttribute());
    }
}
