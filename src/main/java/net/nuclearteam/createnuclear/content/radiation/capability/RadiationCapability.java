package net.nuclearteam.createnuclear.content.radiation.capability;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.nuclearteam.createnuclear.CNAttributes;
import net.nuclearteam.createnuclear.CNEffects;
import net.nuclearteam.createnuclear.CNTags;
import net.nuclearteam.createnuclear.api.radiation.IRadiationSource;
import net.nuclearteam.createnuclear.api.radiation.RadiationRegistry;
import net.nuclearteam.createnuclear.foundation.utility.ConfigValueResolver;
import net.nuclearteam.createnuclear.foundation.utility.InventoryHashUtil;
import net.nuclearteam.createnuclear.infrastructure.config.CNConfigs;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/** Loader-neutral radiation behavior backed by the LivingEntity mixin state. */
public final class RadiationCapability {
    private RadiationCapability() {
    }

    private static IRadiationCapability state(LivingEntity entity) {
        if (entity instanceof IRadiationCapability capability) {
            return capability;
        }
        throw new IllegalStateException("LivingEntity radiation mixin was not applied to " + entity.getType());
    }

    public static void applyContagion(LivingEntity entity, double doseValue, int durationTicks) {
        IRadiationCapability capability = state(entity);
        capability.setContagionDose(doseValue);
        capability.setContagionTicks(durationTicks);
    }

    public static void tickRadiation(LivingEntity entity) {
        Level world = entity.level();
        if (world.isClientSide) return;

        IRadiationCapability capability = state(entity);
        if (entity instanceof Player player) {
            long newHash = InventoryHashUtil.compute(player);
            if (newHash != capability.getInventoryHash()) {
                capability.setInventoryHash(newHash);
                capability.setRadiation(Math.max(0, computeItemRadiation(player)));
            }
        } else {
            capability.setRadiation(Math.max(0, computeItemRadiation(entity)));
        }

        ResourceKey<Biome> biomeKey = world.getBiome(entity.blockPosition()).getKey().orElse(null);
        ResourceLocation biomeLocation = biomeKey != null ? biomeKey.getValue() : null;
        if (!Objects.equals(biomeLocation, capability.getLastBiomeLocation())) {
            capability.setLastBiomeLocation(biomeLocation);
        }

        if (!canBeIrradiated(entity)) return;

        if (capability.getContagionTicks() > 0) {
            capability.setContagionTicks(capability.getContagionTicks() - 1);
        }

        double contagionDose = capability.getContagionTicks() > 0 ? capability.getContagionDose() : 0;
        double totalRaw = capability.getRadiation() + getRawBiomeRadiation(biomeKey) + contagionDose;
        double resistance = getRadiationResistance(entity);
        applyEffects(entity, totalRaw * (1.0 - resistance));
    }

    private static double computeItemRadiation(Player player) {
        double radiation = 0;
        for (ItemStack stack : player.getInventory().main) {
            if (stack.getItem() instanceof IRadiationSource source)
                radiation += source.getRadiation(stack, player);
            radiation += RadiationRegistry.getRadiation(stack, player);
        }
        for (ItemStack stack : player.getInventory().offHand) {
            if (stack.getItem() instanceof IRadiationSource source)
                radiation += source.getRadiation(stack, player);
            radiation += RadiationRegistry.getRadiation(stack, player);
        }
        return radiation;
    }

    private static double getStackRadiation(ItemStack stack, LivingEntity entity) {
        double radiation = 0;
        if (stack.getItem() instanceof IRadiationSource source)
            radiation += source.getRadiation(stack, entity);
        return radiation + RadiationRegistry.getRadiation(stack, entity);
    }

    private static double computeItemRadiation(LivingEntity entity) {
        double radiation = 0;
        for (ItemStack stack : entity.getArmorItems()) {
            radiation += getStackRadiation(stack, entity);
        }
        radiation += getStackRadiation(entity.getMainHandStack(), entity);
        radiation += getStackRadiation(entity.getOffHandStack(), entity);
        return radiation;
    }

    private static double getRawBiomeRadiation(ResourceKey<Biome> biomeKey) {
        return biomeKey == null ? 0 : RadiationRegistry.get(biomeKey);
    }

    public static boolean canBeIrradiated(LivingEntity entity) {
        if (entity.isSpectator()) return false;
        if (entity.getType().is(CNTags.CNEntityTypeTags.IRRADIATED_IMMUNE.tag)) return false;
        if (!CNConfigs.server().radiation.enabledItemRadiation.get()) return false;
        if (getEntityBlacklist().contains(entity.getType())) return false;
        return getRadiationResistance(entity) < 1.0;
    }

    private static List<? extends String> cachedBlacklistSource;
    private static Set<EntityType<?>> cachedBlacklist = Set.of();

    private static Set<EntityType<?>> getEntityBlacklist() {
        List<? extends String> source = CNConfigs.server().radiation.configuredLists.getEntityBlackList();
        if (source != cachedBlacklistSource) {
            Set<EntityType<?>> resolved = new HashSet<>();
            ConfigValueResolver.loadValuesInSet(source, resolved,
                entry -> BuiltInRegistries.ENTITY_TYPE.get(ResourceLocation.tryParse(entry)));
            cachedBlacklist = resolved;
            cachedBlacklistSource = source;
        }
        return cachedBlacklist;
    }

    public static double getRadiationResistance(LivingEntity entity) {
        double resistance = 0d;
        AttributeInstance attribute = entity.getAttribute(CNAttributes.IRRADIATED_RESISTANCE.get());
        if (attribute != null) resistance += attribute.getValue();
        return Mth.clamp(resistance, 0.0, 1.0);
    }

    private static void applyEffects(LivingEntity entity, double radiation) {
        final double radiationDesactive = 0;
        MobEffect radiationEffect = CNEffects.RADIATION.get();
        if (radiation <= radiationDesactive) return;

        int amp;
        if (radiation < CNConfigs.server().radiation.radiationLevel1.get())
            amp = CNConfigs.server().radiation.amplifierLevel0.get();
        else if (radiation < CNConfigs.server().radiation.radiationLevel2.get())
            amp = CNConfigs.server().radiation.amplifierLevel1.get();
        else if (radiation < CNConfigs.server().radiation.radiationLevel3.get())
            amp = CNConfigs.server().radiation.amplifierLevel2.get();
        else
            amp = CNConfigs.server().radiation.amplifierLevel2.get();

        MobEffectInstance current = entity.getStatusEffect(radiationEffect);
        if (current != null && current.getAmplifier() != amp) {
            entity.removeStatusEffect(radiationEffect);
            current = null;
        }
        if (current == null || current.getDuration() <= 40) {
            entity.addEffect(new MobEffectInstance(radiationEffect, 100, amp, true, true));
        }
    }
}
