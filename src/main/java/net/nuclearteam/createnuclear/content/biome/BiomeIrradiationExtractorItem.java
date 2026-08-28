package net.nuclearteam.createnuclear.content.biome;

import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.nuclearteam.createnuclear.foundation.utility.CreateNuclearLang;
import net.nuclearteam.createnuclear.infrastructure.config.CNConfigs;
import net.nuclearteam.createnuclear.infrastructure.worldgen.biome.BiomeIrradiationService;

import java.util.List;

public class BiomeIrradiationExtractorItem extends Item {
    public static final String TAG = "biome_restore";
    private static final int CHARGE_PER_CLICK = 1;

    public BiomeIrradiationExtractorItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack stack = player.getItemInHand(interactionHand);

        if (level.isClientSide || !(level instanceof ServerLevel serverLevel)) {
            return InteractionResultHolder.success(stack);
        }

        if (getCharge(stack) >= getMaxCharge()) return InteractionResultHolder.pass(stack);

        boolean restored = BiomeIrradiationService.restoreArea(serverLevel, player.blockPosition());
        if (!restored) return InteractionResultHolder.pass(stack);

        ItemStack charged = stack.copyWithCount(1);
        addCharge(charged, CHARGE_PER_CLICK);

        stack.decrement(1);
        if (stack.isEmpty()) {
            return InteractionResultHolder.success(charged);
        }

        if (!player.getInventory().insertStack(charged)) {
            player.dropItem(charged, false);
        }
        return InteractionResultHolder.success(stack);
    }

    @Override
    public void appendTooltip(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag tooltipFlag) {
        super.appendTooltip(stack, level, tooltip, tooltipFlag);
        if (tooltipFlag.isAdvanced() && getCharge(stack) > 0) {
            tooltip
                .add(CreateNuclearLang.translateDirect("tooltip.biome_irradiation_extractor." + TAG,  getCharge(stack), getMaxCharge())
                .withStyle(ChatFormatting.GRAY));
        }
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        return Math.round(13.0f * getCharge(stack) / getMaxCharge());
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        return 0x4A90D9;
    }

    public static int getCharge(ItemStack stack) {
        return getChargeTag(stack, 0);
    }

    public static int getMaxCharge() {
        return CNConfigs.server().biomeRestore.maxCharge.get();
    }

    public static void addCharge(ItemStack stack, int amount) {
        int current = getCharge(stack);
        int next = Mth.clamp(current + amount, 0, getMaxCharge());
        stack.getOrCreateTag().putInt(TAG, next);
    }

    public static int getChargeTag(ItemStack stack, int defaultValue) {
        if (stack == null) return defaultValue;
        CompoundTag tag = stack.getTag();
        return (tag != null && tag.contains(TAG)) ? tag.getInt(TAG) : defaultValue;
    }
}
