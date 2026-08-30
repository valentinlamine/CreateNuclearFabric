package net.nuclearteam.createnuclear.foundation.mixin.client;

import net.minecraft.client.renderer.GameRenderer;
import net.nuclearteam.createnuclear.CNClientProxy;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

    @Shadow
    private float darkenWorldAmount;

    // Handles darkening the sky/world during a nuclear explosion
    @Inject(
            method = "tick()V",
            at = @At(value = "TAIL")
    )
    public void CN$tick(CallbackInfo ci) {
        if (CNClientProxy.renderNukeSkyDarkFor > 0 && darkenWorldAmount < 1.0F) {
            darkenWorldAmount = Math.min(darkenWorldAmount + 0.3F, 1.0F);
        }
    }

    // Triggers the white flash render (preScreenRender)
    @Inject(
            method = "render(FJZ)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/platform/Lighting;setupFor3DItems()V",
                    shift = At.Shift.AFTER
            )
    )
    public void CN$render(float partialTick, long nanos, boolean idk, CallbackInfo ci) {
        CNClientProxy.preScreenRender(partialTick);
    }
}
