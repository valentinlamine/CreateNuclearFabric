package net.nuclearteam.createnuclear.content.contraptions.irradiated;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.tags.BlockTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.LevelEvent;

import java.util.HashMap;
import java.util.Map;

public interface IrradiatedAnimal {
    Map<EntityType<?>, EntityType<? extends Animal>> VANILLA_TO_IRRADIATED = new HashMap<>();

    EntityType<? extends Animal> getNormalVariant();

    default void readFromVanilla(Animal animal) {}

    default void writeToVanilla(Animal animal) {}

    boolean isConverting();

    void setConverting();

    void setConversionTime(int conversionTime);

    int getConversionTime();

    default void startConverting(int conversionTime) {
        Animal animal = (Animal) this;

        setConversionTime(conversionTime);
        setConverting();
        animal.removeStatusEffect(MobEffects.WEAKNESS);
        animal.addEffect(new MobEffectInstance(MobEffects.STRENGTH, conversionTime, Math.min(animal.level().getDifficulty().getId() -1, 0)));
        animal.level().sendEntityStatus(animal, EntityEvent.PLAY_CURE_ZOMBIE_VILLAGER_SOUND);
    }

    default void finishConversion(ServerLevel level) {
        Animal irradifiedAnimal = (Animal) this;
        Animal vanillaAnimal = irradifiedAnimal.convertTo(getNormalVariant(), false);

        if (vanillaAnimal != null) {
            vanillaAnimal.initialize(level, level.getLocalDifficulty(vanillaAnimal.blockPosition()), MobSpawnType.CONVERSION, null, null);
            writeToVanilla(vanillaAnimal);
            vanillaAnimal.addEffect(new MobEffectInstance(MobEffects.NAUSEA, 200, 0));

            if (!irradifiedAnimal.isSilent()) {
                level.syncWorldEvent(null, LevelEvent.ZOMBIE_VILLAGER_CURED, irradifiedAnimal.blockPosition(), 0);
            }

            // Fabric API fires ServerLivingEntityEvents.MOB_CONVERSION from Entity#convertTo.
        }
    }

    default int getConversionProgress() {
        int progress = 1;
        Animal animal = (Animal) this;

        if (animal.getRandom().nextFloat() < 0.01F) {
            int buffCount = 0;
            BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

            for (int x = (int) animal.getX() - 4; x < animal.getX() + 4 && buffCount < 14; ++x) {
                for (int y = (int) animal.getY() - 4; y < animal.getY() + 4 && buffCount < 14; ++y) {
                    for (int z = (int) animal.getZ() - 4; z < animal.getZ() + 4 && buffCount < 14; ++z) {
                        BlockState state = animal.level().getBlockState(pos.set(x, y, z));

                        if (state.is(BlockTags.WOODEN_FENCES) || state.is(Blocks.HAY_BLOCK)) {
                            if (animal.getRandom().nextFloat() < 0.3F)
                                ++progress;

                            ++buffCount;
                        }
                    }
                }
            }
        }

        return progress;
    }
}
