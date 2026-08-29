package net.nuclearteam.createnuclear.foundation.mixin.radiation;

import net.minecraft.world.item.ItemStack;
import net.nuclearteam.createnuclear.content.biome.BiomeIrradiationExtractorItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMaxCountMixin {
    @Inject(method = "getMaxStackSize", at = @At("HEAD"), cancellable = true)
    private void createnuclear$chargedExtractorIsUnstackable(CallbackInfoReturnable<Integer> cir) {
        ItemStack self = (ItemStack) (Object) this;
        if (self.getItem() instanceof BiomeIrradiationExtractorItem
            && BiomeIrradiationExtractorItem.getCharge(self) > 0) {
            cir.setReturnValue(1);
        }
    }
}
