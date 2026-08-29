package net.nuclearteam.createnuclear.foundation.mixin.radiation;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.nuclearteam.createnuclear.CreateNuclear;
import net.nuclearteam.createnuclear.content.radiation.capability.IRadiationCapability;
import net.nuclearteam.createnuclear.content.radiation.capability.RadiationCapability;
import net.nuclearteam.createnuclear.CNFluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityRadiationMixin implements IRadiationCapability {
    @Unique private static final String CREATENUCLEAR_STATE_KEY = CreateNuclear.asResource("irradiated_resistance").toString();
    @Unique private static final String FORGE_CAPS_KEY = "ForgeCaps";

    @Unique private double createnuclear$radiation;
    @Unique private long createnuclear$inventoryHash;
    @Unique private ResourceLocation createnuclear$lastBiomeLocation;
    @Unique private double createnuclear$contagionDose;
    @Unique private int createnuclear$contagionTicks;

    @Inject(method = "tick", at = @At("TAIL"))
    private void createnuclear$tickRadiation(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        CNFluids.handleFluidEffect(entity);
        RadiationCapability.tickRadiation(entity);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void createnuclear$writeRadiation(CompoundTag entityNbt, CallbackInfo ci) {
        CompoundTag radiationNbt = new CompoundTag();
        radiationNbt.putDouble("radiation", createnuclear$radiation);
        radiationNbt.putLong("hash", createnuclear$inventoryHash);
        if (createnuclear$lastBiomeLocation != null) {
            radiationNbt.putString("lastBiome", createnuclear$lastBiomeLocation.toString());
        }
        entityNbt.put(CREATENUCLEAR_STATE_KEY, radiationNbt);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void createnuclear$readRadiation(CompoundTag entityNbt, CallbackInfo ci) {
        CompoundTag radiationNbt = null;
        if (entityNbt.contains(CREATENUCLEAR_STATE_KEY, Tag.TAG_COMPOUND)) {
            radiationNbt = entityNbt.getCompound(CREATENUCLEAR_STATE_KEY);
        } else if (entityNbt.contains(FORGE_CAPS_KEY, Tag.TAG_COMPOUND)) {
            CompoundTag forgeCaps = entityNbt.getCompound(FORGE_CAPS_KEY);
            if (forgeCaps.contains(CREATENUCLEAR_STATE_KEY, Tag.TAG_COMPOUND)) {
                radiationNbt = forgeCaps.getCompound(CREATENUCLEAR_STATE_KEY);
            }
        }

        if (radiationNbt == null) return;
        createnuclear$radiation = radiationNbt.getDouble("radiation");
        createnuclear$inventoryHash = radiationNbt.getLong("hash");
        createnuclear$lastBiomeLocation = radiationNbt.contains("lastBiome")
            ? ResourceLocation.tryParse(radiationNbt.getString("lastBiome"))
            : null;
    }

    @Override public double getRadiation() { return createnuclear$radiation; }
    @Override public void setRadiation(double value) { createnuclear$radiation = value; }
    @Override public long getInventoryHash() { return createnuclear$inventoryHash; }
    @Override public void setInventoryHash(long hash) { createnuclear$inventoryHash = hash; }
    @Override public ResourceLocation getLastBiomeLocation() { return createnuclear$lastBiomeLocation; }
    @Override public void setLastBiomeLocation(ResourceLocation location) { createnuclear$lastBiomeLocation = location; }
    @Override public double getContagionDose() { return createnuclear$contagionDose; }
    @Override public void setContagionDose(double dose) { createnuclear$contagionDose = dose; }
    @Override public int getContagionTicks() { return createnuclear$contagionTicks; }
    @Override public void setContagionTicks(int ticks) { createnuclear$contagionTicks = ticks; }
}
