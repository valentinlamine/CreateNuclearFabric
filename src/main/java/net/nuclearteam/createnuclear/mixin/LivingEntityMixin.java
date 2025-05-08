package net.nuclearteam.createnuclear.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.nuclearteam.createnuclear.accessor.IrradiatedLivingEntityAccessor;
import net.nuclearteam.createnuclear.tags.CNTag;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements IrradiatedLivingEntityAccessor {

    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public boolean radiationImmune() {
        return this.getType().is(CNTag.EntityTypeTags.IRRADIATED_IMMUNE.tag);
    }

}
