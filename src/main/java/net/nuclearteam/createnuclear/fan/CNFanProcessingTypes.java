package net.nuclearteam.createnuclear.fan;

import com.simibubi.create.content.kinetics.fan.processing.AllFanProcessingTypes;
import com.simibubi.create.content.kinetics.fan.processing.FanProcessingType;
import com.simibubi.create.content.kinetics.fan.processing.FanProcessingTypeRegistry;
import com.simibubi.create.foundation.recipe.RecipeApplier;
import com.simibubi.create.foundation.utility.Color;
import com.simibubi.create.foundation.utility.VecHelper;
import it.unimi.dsi.fastutil.objects.Object2ReferenceOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.nuclearteam.createnuclear.CreateNuclear;
import net.nuclearteam.createnuclear.block.CNBlocks;
import net.nuclearteam.createnuclear.effects.CNEffects;
import net.nuclearteam.createnuclear.fan.EnrichedRecipe.EnrichedWrapper;
import net.nuclearteam.createnuclear.particle.IrradiatedParticlesData;
import net.nuclearteam.createnuclear.tools.EnrichingCampfireBlock;

import org.jetbrains.annotations.Nullable;

import static net.nuclearteam.createnuclear.tags.CNTag.BlockTags.FAN_PROCESSING_CATALYSTS_ENRICHED;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CNFanProcessingTypes extends AllFanProcessingTypes {
    public static final EnrichedType ENRICHED = register("enriched", new EnrichedType());
    private static final Map<String, FanProcessingType> LEGACY_NAME_MAP;

    static {
        Object2ReferenceOpenHashMap<String, FanProcessingType> map = new Object2ReferenceOpenHashMap<>();
        map.put("ENRICHED", ENRICHED);
        map.trim();
        LEGACY_NAME_MAP = map;
    }

    private static <T extends FanProcessingType> T register(String id, T type) {
        FanProcessingTypeRegistry.register(CreateNuclear.asResource(id), type);
        return type;
    }

    @Nullable
    public static FanProcessingType ofLegacyName(String name) {
        return LEGACY_NAME_MAP.get(name);
    }

    public static void register() {
    }

   public static FanProcessingType parseLegacy(String str) {
        FanProcessingType type = ofLegacyName(str);
        if (type != null) {
            return type;
        }
        return FanProcessingType.parse(str);
   }

    public static class EnrichedType implements FanProcessingType {
        private static final EnrichedWrapper ENRICHED_WRAPPER = new EnrichedWrapper();

        @Override
        public boolean isValidAt(Level level, BlockPos pos) {
            BlockState blockState = level.getBlockState(pos);
            if (FAN_PROCESSING_CATALYSTS_ENRICHED.matches(blockState)) {
                if (blockState.is(CNBlocks.ENRICHING_CAMPFIRE.get()) && blockState.hasProperty(EnrichingCampfireBlock.LIT) && ! blockState.getValue(EnrichingCampfireBlock.LIT)) {
                    return false;
                }
                return true;
            }
            return false;
        }
        @Override
        public int getPriority() {
            return 301;
        }

        @Override
        public boolean canProcess(ItemStack stack, Level level) {
            ENRICHED_WRAPPER.setItem(0, stack);
            Optional<EnrichedRecipe> recipe =  CNRecipeTypes.ENRICHED.find(ENRICHED_WRAPPER, level);
            return recipe.isPresent();
        }
        @Override
        @Nullable
        public List<ItemStack> process(ItemStack stack, Level level) {
            ENRICHED_WRAPPER.setItem(0, stack);
            Optional<EnrichedRecipe> recipe = CNRecipeTypes.ENRICHED.find(ENRICHED_WRAPPER, level);
            return recipe.map(enrichedRecipe -> RecipeApplier.applyRecipeOn(level, stack, enrichedRecipe)).orElse(null);
        }

        @Override
        public void spawnProcessingParticles(Level level, Vec3 pos) {
            if (level.random.nextInt(8) != 0) return;
            pos = pos.add(VecHelper.offsetRandomly(Vec3.ZERO, level.random, 1)
                    .multiply(1, 0.05f, 1)
                    .normalize()
                    .scale(0.15f));
            level.addParticle(new IrradiatedParticlesData(), pos.x, pos.y + .45f, pos.z, 0, 0, 0);
            if (level.random.nextInt(2) == 0) level.addParticle(ParticleTypes.FIREWORK, pos.x, pos.y + .25f, pos.z, 0, 0, 0);
        }

        @Override
        public void morphAirFlow(AirFlowParticleAccess particleAccess, RandomSource random) {
            particleAccess.setColor(Color.mixColors(0x0, 0x126568, random.nextFloat()));
            particleAccess.setAlpha(1f);
            if (random.nextFloat() < 1 / 128f) particleAccess.spawnExtraParticle(ParticleTypes.ASH, .125f);
            if (random.nextFloat() < 1 / 32f) particleAccess.spawnExtraParticle(ParticleTypes.DOLPHIN, .125f);
        }

        @Override
        public void affectEntity(Entity entity, Level level) {
            if (entity instanceof LivingEntity livingEntity) {
                livingEntity.addEffect(new MobEffectInstance(CNEffects.RADIATION.get(), 10, 0, true, true));
            }
        }
    }
}
