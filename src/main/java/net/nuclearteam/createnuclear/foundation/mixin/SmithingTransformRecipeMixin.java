package net.nuclearteam.createnuclear.foundation.mixin;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SmithingTransformRecipe;
import net.minecraft.core.RegistryAccess;
import net.nuclearteam.createnuclear.CNTags;
import net.nuclearteam.createnuclear.foundation.utility.ClothTagHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SmithingTransformRecipe.class)
public class SmithingTransformRecipeMixin {

    @Shadow
    @Final
    ItemStack result;

    @Inject(
            at = @At("HEAD"),
            method = "craft(Lnet/minecraft/inventory/Container;Lnet/minecraft/registry/RegistryAccess;)Lnet/minecraft/item/ItemStack;",
            cancellable = true
    )
    public void CN$craft(Container pContainer, RegistryAccess pRegistryAccess, CallbackInfoReturnable<ItemStack> cir) {
        if (pContainer.getItem(1).is(CNTags.CNItemTags.ANTI_RADIATION_ARMOR.tag)) {
            ItemStack resultItem = this.result.copy();
            ItemStack baseItem = pContainer.getItem(1);
            ItemStack additionItem = pContainer.getItem(2);

            ClothTagHelper.initClothTagsForResult(resultItem, baseItem, additionItem);

            cir.setReturnValue(resultItem);
        }
    }
}
