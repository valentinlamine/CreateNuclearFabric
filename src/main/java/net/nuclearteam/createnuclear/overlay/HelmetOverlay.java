package net.nuclearteam.createnuclear.overlay;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.nuclearteam.createnuclear.CreateNuclear;
import net.nuclearteam.createnuclear.tags.CNTag;
import net.nuclearteam.createnuclear.utils.RenderHelper;

/**
 * HUD overlay for displaying helmet condition based on durability.
 */
public class HelmetOverlay  implements HudOverlay {
    private static final ResourceLocation[] HELMET_TEXTURES = {
            CreateNuclear.asResource("textures/misc/helmet_new.png"),
            CreateNuclear.asResource("textures/misc/helmet_minor_damage.png"),
            CreateNuclear.asResource("textures/misc/helmet_crack1.png"),
            CreateNuclear.asResource("textures/misc/helmet_crack2.png"),
            CreateNuclear.asResource("textures/misc/helmet_almost_broken.png")
    };
    private static final float[] COVERAGE_FACTORS = {.5f, 1f, 1.05f, 1.45f, 1.98f};
    private static final int BASE_PRIORITY = 50;

    @Override
    public boolean isActive() {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return false;
        ItemStack helmet = player.getInventory().getArmor(EquipmentSlot.HEAD.getIndex());
        return !helmet.isEmpty() && helmet.is(CNTag.ItemTags.ANTI_RADIATION_HELMET_FULL_DYE.tag);
    }

    @Override
    public void render(GuiGraphics graphics, float partialTicks) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;
        ItemStack helmet = player.getInventory().getArmor(EquipmentSlot.HEAD.getIndex());
        if (helmet.isEmpty()) return;

        // Calculate durability ratio
        float durabilityRatio = (helmet.getMaxDamage() - helmet.getDamageValue())
                / (float) helmet.getMaxDamage();
        // Determine texture index based on thresholds
        int index = durabilityRatio >= 0.95f ? 0
                : durabilityRatio >= 0.80f ? 1
                : durabilityRatio >= 0.60f ? 2
                : durabilityRatio >= 0.25f ? 3
                : 4;

        // Update radiation coverage based on helmet condition
        RadiationOverlay.setCoverage(COVERAGE_FACTORS[index]);

        // Render helmet overlay texture
        RenderHelper.renderFirstPersonOverlay(graphics, HELMET_TEXTURES[index], 1f, 1f);
        // Render the hotbar behind the helmet overlay
        Minecraft.getInstance().gui.renderHotbar(12f, graphics);
    }

    @Override
    public int getPriority() {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return BASE_PRIORITY;
        ItemStack helmet = player.getInventory().getArmor(EquipmentSlot.HEAD.getIndex());
        if (helmet.isEmpty()) return BASE_PRIORITY;

        float durabilityRatio = (helmet.getMaxDamage() - helmet.getDamageValue())
                / (float) helmet.getMaxDamage();
        return BASE_PRIORITY + (int) ((1f - durabilityRatio) * 100);
    }
}
