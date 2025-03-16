package net.nuclearteam.createnuclear.mixin;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.nuclearteam.createnuclear.CreateNuclear;
import net.nuclearteam.createnuclear.effects.CNEffects;
import net.nuclearteam.createnuclear.entity.MobIrradiatedConversion;
import net.nuclearteam.createnuclear.tags.CNTag;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mob.class)
public abstract class IrradiatedMob extends LivingEntity implements Targeting {

    @Shadow @Nullable public abstract <T extends Mob> T convertTo(EntityType<?> entityType, boolean transferInventory);

    @Unique
    private static final EntityDataAccessor<Boolean> DATA_IRRADIATED_CONVERSION_ID = SynchedEntityData.defineId(IrradiatedMob.class, EntityDataSerializers.BOOLEAN);
    @Unique
    private int conversionIrradiatedTime;

    protected IrradiatedMob(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(at = @At("TAIL"), method = "defineSynchedData")
    protected void irradiatedDefineSynchedData(CallbackInfo ci) {
        this.entityData.define(DATA_IRRADIATED_CONVERSION_ID, false);
    }

    @Unique
    public boolean isIrradiatedConversion() {
        return this.entityData.hasItem(DATA_IRRADIATED_CONVERSION_ID) && this.entityData.get(DATA_IRRADIATED_CONVERSION_ID);
    }

    @Unique
    private void startIrradiatedConversion(int conversionTime) {
        this.conversionIrradiatedTime = conversionTime;
        this.entityData.set(DATA_IRRADIATED_CONVERSION_ID, true);
    }

    @Inject(at = @At("TAIL"), method = "tick")
    public void irradiatedTick(CallbackInfo ci) {
        if (!this.level().isClientSide && this.isAlive()) {
            if (this.isIrradiatedConversion()) {
                --this.conversionIrradiatedTime;
                if (this.conversionIrradiatedTime < 0) {
                    this.doConvertIrradiatedEntityEffect();
                }
            } else if (this.convertsInIrradiatedPotion()) {
                if (this.hasEffect(CNEffects.RADIATION.get())) {
                    if (this.getHealth() < (this.getMaxHealth() * 15) / 100) {
                        this.startIrradiatedConversion(3);
                    }
                }
            }

        }
    }

    @Unique
    protected void doConvertIrradiatedEntityEffect() {
        CreateNuclear.LOGGER.warn("getType: {}, irradiatedType: {}", this.getType(), MobIrradiatedConversion.getByIrradiatedType(this.getType()));
        Mob mob = this.convertTo(MobIrradiatedConversion.getByIrradiatedType(this.getType()), true);
        if (!this.isSilent()) {
            this.level().levelEvent(null, 1040, this.blockPosition(), 0);
        }
    }

    @Unique
    protected boolean convertsInIrradiatedPotion() {
        return !CNTag.EntityTypeTags.IRRADIATED_IMMUNE.matches(this.getType());
    }


}
