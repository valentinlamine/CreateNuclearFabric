package net.nuclearteam.createnuclear.foundation.mixin.client;

import net.minecraft.client.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Camera.class)
public interface CameraAccessor {
    // Exposes the private Camera#move so it can be called from outside the class
    @Invoker("move")
    void callMove(double distanceOffset, double verticalOffset, double horizontalOffset);
}
