package net.nuclearteam.createnuclear.multiblock.core;

import lib.multiblock.test.SimpleMultiBlockAislePatternBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.nuclearteam.createnuclear.CreateNuclear;
import net.nuclearteam.createnuclear.block.CNBlocks;
import net.nuclearteam.createnuclear.config.CNConfigs;
import net.nuclearteam.createnuclear.effects.CNEffects;
import net.nuclearteam.createnuclear.multiblock.IHeat;
import net.nuclearteam.createnuclear.multiblock.controller.ReactorControllerBlockEntity;
import net.nuclearteam.createnuclear.multiblock.casing.ReactorCasingBlockEntity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static net.nuclearteam.createnuclear.CNMultiblock.*;

public class ReactorCoreBlockEntity extends ReactorCasingBlockEntity {
    private int countdownTicks = 0;


    public ReactorCoreBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void tick() {
        super.tick();

        if (level.isClientSide)
            return;

        BlockPos controllerPos = getBlockPosForReactor();
        if (level.getBlockEntity(controllerPos) instanceof ReactorControllerBlockEntity reactorController) {
            int heat = (int) reactorController.configuredPattern.getOrCreateTag().getDouble("heat");
            if (IHeat.HeatLevel.of(heat) == IHeat.HeatLevel.DANGER) {
                if (countdownTicks >= CNConfigs.common().rods.time.get()) { // 300 ticks = 15 secondes
                    float explosionRadius = calculateExplosionRadius(reactorController.countUraniumRod);
                    explodeReactorCore(level, getBlockPos(), explosionRadius);
                } else {
                    countdownTicks++;
                }
            } else {
                countdownTicks = 0; // Réinitialise le compte à rebours si la chaleur n'est pas en danger
            }
        }
    }

    private float calculateExplosionRadius(int countUraniumRod) {
        return 10F + countUraniumRod; // Ajuste selon tes besoins
    }

    private void applyRadiationEffect(Level level, BlockPos center, float radius) {
        AABB explosionArea = new AABB(
                center.getX() - radius, center.getY() - radius, center.getZ() - radius,
                center.getX() + radius, center.getY() + radius, center.getZ() + radius
        );

        List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, explosionArea);

        for (LivingEntity entity : entities) {
            double distance = entity.distanceToSqr(center.getX() + 0.5, center.getY() + 0.5, center.getZ() + 0.5);
            if (distance <= radius * radius) {
                entity.addEffect(new MobEffectInstance(CNEffects.RADIATION.get(), 20 * 20, 1)); // 20s, Slowness II
            }
        }
    }


    private final Set<BlockPos> radiationZone = new HashSet<>();

    private void explodeReactorCore(Level level, BlockPos center, float radius) {
        int r = (int) Math.ceil(radius);

        for (int x = -r; x <= r; x++) {
            for (int y = -r; y <= r; y++) {
                for (int z = -r; z <= r; z++) {
                    BlockPos currentPos = center.offset(x, y, z);
                    double distanceSquared = x * x + y * y + z * z;
                    double distance = Math.sqrt(distanceSquared);

                    if (distance <= radius) {
                        BlockState blockState = level.getBlockState(currentPos);
                        if (!blockState.isAir() && !blockState.is(Blocks.BEDROCK)) {
                            double probability = distance / radius;
                            double noise = level.random.nextDouble();

                            if (distance > radius * 0.7 && noise < 0.4) {
                                continue;
                            }

                            level.destroyBlock(currentPos, false);
                        }

                        // Ajoute cette position à la zone contaminée
                        radiationZone.add(currentPos.immutable());
                    }
                }
            }
        }
    }

    private static BlockPos FindController(char character) {
        return SimpleMultiBlockAislePatternBuilder.start()
                .aisle(AAAAA, AAAAA, AAAAA, AAAAA, AAAAA)
                .aisle(AABAA, ADADA, BACAB, ADADA, AABAA)
                .aisle(AABAA, ADADA, BACAB, ADADA, AABAA)
                .aisle(AAIAA, ADADA, BACAB, ADADA, AAAA)
                .aisle(AABAA, ADADA, BACAB, ADADA, AABAA)
                .aisle(AABAA, ADADA, BACAB, ADADA, AABAA)
                .aisle(AAAAA, AAAAA, AAAAA, AAAAA, AAOAA)
                .where('A', a -> a.getState().is(CNBlocks.REACTOR_CASING.get()))
                .where('B', a -> a.getState().is(CNBlocks.REACTOR_FRAME.get()))
                .where('C', a -> a.getState().is(CNBlocks.REACTOR_CORE.get()))
                .where('D', a -> a.getState().is(CNBlocks.REACTOR_COOLER.get()))
                .where('*', a -> a.getState().is(CNBlocks.REACTOR_CONTROLLER.get()))
                .where('O', a -> a.getState().is(CNBlocks.REACTOR_OUTPUT.get()))
                .where('I', a -> a.getState().is(CNBlocks.REACTOR_INPUT.get()))
                .getDistanceController(character);
    }

    private BlockPos getBlockPosForReactor() {
        BlockPos posController = getBlockPos();
        BlockPos posInput = new BlockPos(posController.getX(), posController.getY(), posController.getZ());

        int[][][] directions = {
                {{0, 2, 2}, {0, 1, 2}, {0, 0, 2}, {0, -1, 2}, {0, -2, 2}}, // NORTH
                {{0, 2, -2}, {0, 1, -2}, {0, 0, -2}, {0, -1, -2}, {0, -2, -2}}, // SOUTH

                {{2, 2, 0}, {2, 1, 0}, {2, 0, 0}, {2, -1, 0}, {2, -2, 0}}, // EAST
                {{-2, 2, 0}, {-2, 1, 0}, {-2, 0, 0}, {-2, -1, 0}, {-2, -2, 0}} // WEST
        };


        for (int[][] direction : directions) {
            for (int[] dir : direction) {
                BlockPos newPos = posController.offset(dir[0], dir[1], dir[2]);
                if (level.getBlockState(newPos).is(CNBlocks.REACTOR_CONTROLLER.get())) {
                    posInput = newPos;
                    break;
                }
            }
        }

        return posInput;
    }


}
