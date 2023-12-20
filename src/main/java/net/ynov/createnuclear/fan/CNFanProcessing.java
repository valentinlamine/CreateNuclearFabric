package net.ynov.createnuclear.fan;
/*
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.kinetics.fan.FanProcessing;
import com.simibubi.create.foundation.utility.VecHelper;
import io.github.fabricators_of_create.porting_lib.transfer.item.ItemStackHandlerContainer;
import net.fabricmc.loader.impl.lib.sat4j.core.Vec;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.ynov.createnuclear.tools.EnrichedRecipe;

import java.util.Optional;

public class CNFanProcessing extends FanProcessing {
    private static final EnrichedWrapper ENRICHED_WRAPPER = new EnrichedWrapper();

    public static boolean isEnriched(ItemStack stack, Level world) {
        ENRICHED_WRAPPER.setItem(0, stack);
        Optional<EnrichedRecipe> recipe = AllRecipeTypes.ENRICHED.find(ENRICHED_WRAPPER, world);
        return  recipe.isPresent();
    }

    @Override
    public enum Type {
        ENRICHED {
            @Override
            public void spawnParticlesForProcessing(Level level, Vec3 pos) {
                if (level.random.nextInt(8) != 0) return;
                pos = pos.add(VecHelper.offsetRandomly(Vec3.ZERO, level.random, 1)
                        .multiply(1, 0.05f, 1)
                        .normalize()
                        .scale(0.15f));
                level.addParticle(ParticleTypes.BUBBLE_POP, pos.x, pos.y + .45f, pos.z, 0, 0, 0);
                if (level.random.nextInt(2) == 0) level.addParticle(ParticleTypes.SMOKE, pos.x, pos.y + .25f, pos.z, 0,0,0);
            }

            @Override
            public void affectEntity(Entity entity, Level level) {}

            @Override
            public boolean canProcess(ItemStack stack, Level level) {
                return isEnriched(stack, level);
            }
        };

        public abstract boolean canProcess(ItemStack stack, Level level);

        public abstract void spawnParticlesForProcessing(Level level, Vec3 pos);

        public abstract void affectEntity(Entity entity, Level level);


    }

    public static class EnrichedWrapper extends ItemStackHandlerContainer {
        public EnrichedWrapper() {
            super(1);
        }
    }
}*/
