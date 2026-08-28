package net.nuclearteam.createnuclear.content.radiation;

import com.simibubi.create.api.effect.OpenPipeEffectHandler;
import io.github.fabricators_of_create.porting_lib.fluids.FluidStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.Level;
import net.nuclearteam.createnuclear.content.radiation.capability.RadiationCapability;

import java.util.List;

public class RadiationEffectHandler implements OpenPipeEffectHandler {
    private static final double PIPE_LEAK_DOSE = 40.0D;

    @Override
    public void apply(Level level, AABB area, FluidStack fluid) {
        if (level.getTime() % 5 != 0) return;

        List<LivingEntity> entities = level.getEntitiesByClass(LivingEntity.class, area, LivingEntity::isAffectedBySplashPotions);
        for (LivingEntity entity : entities) {
            if (!RadiationCapability.canBeIrradiated(entity)) continue;
            RadiationCapability.applyContagion(entity, PIPE_LEAK_DOSE, 20);
        }
    }
}
