package net.nuclearteam.createnuclear.foundation.mixin.client;


import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.EquipmentSlot;
import com.mojang.blaze3d.vertex.PoseStack;
import net.nuclearteam.createnuclear.content.equipment.armor.AntiRadiationArmorItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerRenderer.class)
public abstract class PlayerEntityRendererMixin {
    @Unique private boolean createnuclear$hatVisible;
    @Unique private boolean createnuclear$jacketVisible;
    @Unique private boolean createnuclear$rightSleeveVisible;
    @Unique private boolean createnuclear$leftSleeveVisible;
    @Unique private boolean createnuclear$rightPantsVisible;
    @Unique private boolean createnuclear$leftPantsVisible;

    @Inject(
        method = "render(Lnet/minecraft/client/player/AbstractClientPlayer;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
        at = @At("HEAD")
    )
    private void createnuclear$hideOuterPlayerLayers(
        AbstractClientPlayer player,
        float entityYaw,
        float tickDelta,
        PoseStack matrices,
        MultiBufferSource vertexConsumers,
        int light,
        CallbackInfo ci
    ) {
        PlayerModel<AbstractClientPlayer> model =
            ((PlayerRenderer) (Object) this).getModel();

        createnuclear$hatVisible = model.hat.visible;
        createnuclear$jacketVisible = model.jacket.visible;
        createnuclear$rightSleeveVisible = model.rightSleeve.visible;
        createnuclear$leftSleeveVisible = model.leftSleeve.visible;
        createnuclear$rightPantsVisible = model.rightPants.visible;
        createnuclear$leftPantsVisible = model.leftPants.visible;

        if (player.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof AntiRadiationArmorItem) {
            model.hat.visible = false;
        }
        if (player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof AntiRadiationArmorItem) {
            model.jacket.visible = false;
            model.rightSleeve.visible = false;
            model.leftSleeve.visible = false;
        }
        if (player.getItemBySlot(EquipmentSlot.LEGS).getItem() instanceof AntiRadiationArmorItem) {
            model.rightPants.visible = false;
            model.leftPants.visible = false;
        }
    }

    @Inject(
        method = "render(Lnet/minecraft/client/player/AbstractClientPlayer;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
        at = @At("RETURN")
    )
    private void createnuclear$restoreOuterPlayerLayers(
        AbstractClientPlayer player,
        float entityYaw,
        float tickDelta,
        PoseStack matrices,
        MultiBufferSource vertexConsumers,
        int light,
        CallbackInfo ci
    ) {
        PlayerModel<AbstractClientPlayer> model =
            ((PlayerRenderer) (Object) this).getModel();
        model.hat.visible = createnuclear$hatVisible;
        model.jacket.visible = createnuclear$jacketVisible;
        model.rightSleeve.visible = createnuclear$rightSleeveVisible;
        model.leftSleeve.visible = createnuclear$leftSleeveVisible;
        model.rightPants.visible = createnuclear$rightPantsVisible;
        model.leftPants.visible = createnuclear$leftPantsVisible;
    }
}
