package net.nuclearteam.createnuclear.foundation.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.nuclearteam.createnuclear.CNEffects;
import net.nuclearteam.createnuclear.CreateNuclear;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

// On cible Gui.class car c'est là que se trouve renderHeart
@Mixin(Gui.class)
public class RadiationHeartMixin {

    private static final ResourceLocation RADIATION_ICONS = CreateNuclear.asResource("textures/gui/icons.png");
    private static final ResourceLocation VANILLA_ICONS = new ResourceLocation("minecraft", "textures/gui/icons.png");

    @ModifyArg(
            method = "renderHeart", // La méthode singulière qui dessine VRAIMENT le coeur
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V"
            ),
            index = 0
    )
    private ResourceLocation createnuclear$changeHeartTexture(ResourceLocation originalTexture) {
        if (originalTexture.equals(VANILLA_ICONS)) {
            Player player = Minecraft.getInstance().player;

            // Si le joueur est irradié, on balance votre image !
            if (player != null && player.hasEffect(CNEffects.RADIATION.get())) {
                return RADIATION_ICONS;
            }
        }
        return originalTexture;
    }
}