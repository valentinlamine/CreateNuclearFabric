package net.nuclearteam.createnuclear.content.kinetics.fan.processing;

import com.simibubi.create.api.registry.CreateBuiltInRegistries;
import com.simibubi.create.content.kinetics.fan.processing.FanProcessingType;
import com.simibubi.create.foundation.recipe.RecipeApplier;
import it.unimi.dsi.fastutil.objects.Object2ReferenceOpenHashMap;
import net.createmod.catnip.math.VecHelper;
import net.createmod.catnip.theme.Color;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.nuclearteam.createnuclear.*;
import net.nuclearteam.createnuclear.content.enriching.campfire.EnrichingCampfireBlock;
import net.nuclearteam.createnuclear.content.kinetics.fan.processing.EnrichedRecipe.EnrichedWrapper;
import net.nuclearteam.createnuclear.content.kinetics.fan.processing.SnowPowderRecipe.SnowPowderWrapper;

import org.jetbrains.annotations.Nullable;


import java.util.List;
import java.util.Map;
import java.util.Optional;

@SuppressWarnings("unused")
public class CNFanProcessingTypes {
    public static final EnrichedType ENRICHED = register("enriched", new EnrichedType());
    public static final SnowPowderType SNOW_POWDER = register("snow_powder", new SnowPowderType());

    private static final Map<String, FanProcessingType> LEGACY_NAME_MAP;

    static {
        Object2ReferenceOpenHashMap<String, FanProcessingType> map = new Object2ReferenceOpenHashMap<>();
        map.put("ENRICHED", ENRICHED);
        map.put("SNOW_POWDER", SNOW_POWDER);
        map.trim();
        LEGACY_NAME_MAP = map;
    }

    private static <T extends FanProcessingType> T register(String id, T type) {
        return Registry.register(CreateBuiltInRegistries.FAN_PROCESSING_TYPE, CreateNuclear.asResource(id), type);
    }

    @Nullable
    public static FanProcessingType ofLegacyName(String name) {
        return LEGACY_NAME_MAP.get(name);
    }

    public static void register() {}

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
            BlockState state = level.getBlockState(pos);
            if (CNTags.CNBlockTags.FAN_PROCESSING_CATALYSTS_ENRICHED.matches(state)) {
                return !state.is(CNBlocks.ENRICHING_CAMPFIRE.get()) || !state.contains(EnrichingCampfireBlock.LIT) || state.getValue(EnrichingCampfireBlock.LIT);
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
            Optional<EnrichedRecipe> recipe = CNRecipeTypes.ENRICHED.find(ENRICHED_WRAPPER, level);
            return recipe.isPresent();
        }

        @Nullable
        @Override
        public List<ItemStack> process(ItemStack stack, Level level) {
            ENRICHED_WRAPPER.setItem(0, stack);
            Optional<EnrichedRecipe> recipe = CNRecipeTypes.ENRICHED.find(ENRICHED_WRAPPER, level);
            return recipe.map(enrichedRecipe -> RecipeApplier.applyRecipeOn(level, stack, enrichedRecipe, true)).orElse(null);
        }

        @Override
        public void spawnProcessingParticles(Level level, Vec3 pos) {
            if (level.random.nextInt(8) != 0) return;
            pos = pos.add(VecHelper.offsetRandomly(Vec3.ZERO, level.random, 1)
                    .multiply(1, 0.5f, 1)
                    .normalize()
                    .multiply(0.15f)
            );
            level.addParticle(ParticleTypes.ANGRY_VILLAGER, pos.x, pos.y + .45f, pos.z, 0.0, 0.0, 0.0);
            if (level.random.nextInt(2) != 0) level.addParticle(ParticleTypes.FIREWORK, pos.x, pos.y + .25f, pos.z, 0.0, 0.0, 0.0);
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

    public static class SnowPowderType implements FanProcessingType {
        private static final SnowPowderWrapper SNOW_POWDER_WRAPPER = new SnowPowderWrapper();

        @Override
        public boolean isValidAt(Level level, BlockPos pos) {
            return CNTags.CNBlockTags.FAN_PROCESSING_CATALYSTS_SNOW_POWDER.matches(level.getBlockState(pos));
        }

        @Override
        public int getPriority() {
            return 301;
        }

        @Override
        public boolean canProcess(ItemStack stack, Level level) {
            SNOW_POWDER_WRAPPER.setItem(0, stack);
            return CNRecipeTypes.SNOW_POWDER.find(SNOW_POWDER_WRAPPER, level).isPresent();
        }

        @Nullable
        @Override
        public List<ItemStack> process(ItemStack stack, Level level) {
            SNOW_POWDER_WRAPPER.setItem(0, stack);
            Optional<SnowPowderRecipe> recipe = CNRecipeTypes.SNOW_POWDER.find(SNOW_POWDER_WRAPPER, level);
            return recipe.map(value -> RecipeApplier.applyRecipeOn(level, stack, value, true)).orElse(null);
        }

        @Override
        public void spawnProcessingParticles(Level level, Vec3 pos) {
            if (level.random.nextInt(8) != 0) return;
            pos = pos.add(VecHelper.offsetRandomly(Vec3.ZERO, level.random, 1)
                    .multiply(1, 0.5f, 1)
                    .normalize()
                    .multiply(0.15f)
            );
            level.addParticle(ParticleTypes.SNOWFLAKE, pos.x, pos.y + .45f, pos.z, 0.0, 0.0, 0.0);
            if (level.random.nextInt(2) != 0)
                level.addParticle(ParticleTypes.SNEEZE, pos.x, pos.y + .25f, pos.z, 0.0, 0.0, 0.0);
        }

        @Override
        public void morphAirFlow(AirFlowParticleAccess particleAccess, RandomSource random) {
            particleAccess.setColor(Color.mixColors(0x0, 0x49FFFB, random.nextFloat()));
            particleAccess.setAlpha(1f);
            if (random.nextFloat() < 1 / 128f) particleAccess.spawnExtraParticle(ParticleTypes.SNOWFLAKE, .125f);
            if (random.nextFloat() < 1 / 32f) particleAccess.spawnExtraParticle(ParticleTypes.SNEEZE, .125f);
        }

        @Override
        public void affectEntity(Entity entity, Level level) {
            if (entity instanceof LivingEntity livingEntity) {
                livingEntity.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 10, 0, true, true));
            }
        }
    }
}
