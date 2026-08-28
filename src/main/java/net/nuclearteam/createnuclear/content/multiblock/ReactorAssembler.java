package net.nuclearteam.createnuclear.content.multiblock;

import net.createmod.catnip.lang.LangBuilder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.sounds.SoundSource;
import net.minecraft.ChatFormatting;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.nuclearteam.createnuclear.CNBlocks;
import net.nuclearteam.createnuclear.CNSoundEvents;
import net.nuclearteam.createnuclear.api.multiblock.BlockPattern;
import net.nuclearteam.createnuclear.api.multiblock.TypeMultiblock;
import net.nuclearteam.createnuclear.content.multiblock.controller.ReactorControllerBlockEntity;
import net.nuclearteam.createnuclear.content.multiblock.frame.ReactorFrameEntity;
import net.nuclearteam.createnuclear.content.multiblock.input.fluid.ReactorFluidInputEntity;
import net.nuclearteam.createnuclear.foundation.advancement.CNAdvancement;
import net.nuclearteam.createnuclear.foundation.utility.CreateNuclearLang;
import net.nuclearteam.createnuclear.foundation.utility.NotifyUtil;
import net.nuclearteam.createnuclear.infrastructure.config.CNConfigs;

import java.util.ArrayList;
import java.util.List;

public final class ReactorAssembler {

    public static final int configRadius = CNConfigs.server().notify.warningDistance.get();
    public static final boolean configWarnAll = CNConfigs.server().notify.warnAllPlayers.get();

    private ReactorAssembler() {}

    public static void assemble(BlockPos pos, Level level) {
        if (level.isClientSide) return;

        ReactorControllerBlockEntity entity = getBlockEntity(level, pos);
        if (entity == null) return;

        BlockPattern<TypeMultiblock> result = CNMultiblock.REGISTRATE_MULTIBLOCK.findStructure(level, pos, entity);
        if (result == null) return;

        // Captured before setAssembled(true) below: assemble() is also reached from
        // ReactorPattern.findControllerPos on an already-assembled reactor, and only the
        // actual not-assembled -> assembled transition should notify and play the cue.
        boolean wasAssembled = entity.isAssembled();

        sendMessageToPlayer(level, pos, CreateNuclearLang.translate("notification.reactor.assembled"), !wasAssembled);

        if (!wasAssembled) {
            // Server-side (null player) so the one-shot broadcasts to nearby clients.
            level.playSound(null, pos, CNSoundEvents.MOTOR_ASSEMBLE.getMainEvent(), SoundSource.BLOCKS, 1.0f, 1.0f);
        }

        switch (result.data()) {
            case REACTOR_T1 -> entity.getAdvancement().awardPlayer(CNAdvancement.T1_REACTOR);
            case REACTOR_T2 -> entity.getAdvancement().awardPlayer(CNAdvancement.T2_REACTOR);
            case REACTOR_T3 -> entity.getAdvancement().awardPlayer(CNAdvancement.T3_REACTOR);
        }

        entity.setMultiblockSize(result.data().getSize());
        entity.setAssembled(true);
        entity.setMultiblockStructure(ReactorAssembler.getStructureBound(pos, entity.getMultiblockSize(), entity.getMultiblockFacing()));

        findAndRegisterSpecialBlocks(entity.getMultiblockPos(), entity, level);
    }

    public static void disassemble(BlockPos pos, Level level) {
        if (level.isClientSide) return;

        ReactorControllerBlockEntity entity = getBlockEntity(level, pos);
        if (entity == null || !entity.isAssembled()) return;

        BlockPattern<TypeMultiblock> result = CNMultiblock.REGISTRATE_MULTIBLOCK.findStructure(level, pos, entity);
        if (result != null) return;

        sendMessageToPlayer(level, pos, CreateNuclearLang.translate("notification.reactor.disassembled"), true);

        // Both early returns above already guarantee this is a real assembled -> broken
        // transition, so no extra guard is needed here.
        level.playSound(null, pos, CNSoundEvents.MOTOR_DISASSEMBLE.getMainEvent(), SoundSource.BLOCKS, 1.0f, 1.0f);

        entity.setAssembled(false);
        entity.removeIOAll();
    }

    public static void findAndRegisterSpecialBlocks(BoundingBox reactorPos, ReactorControllerBlockEntity entity, Level level) {
        int xMin = reactorPos.getMinX(), xMax = reactorPos.getMaxX();
        int yMin = reactorPos.getMinY(), yMax = reactorPos.getMaxY();
        int zMin = reactorPos.getMinZ(), zMax = reactorPos.getMaxZ();

        final Block reactorOutputBlock = CNBlocks.REACTOR_OUTPUT.get();
        final Block reactorRodInputBlock = CNBlocks.REACTOR_ROD_INPUT.get();
        final Block reactorInputFluidBlock = CNBlocks.REACTOR_FLUID_INPUT.get();
        final Block reactorAlarmBlock = CNBlocks.REACTOR_ALARM.get();
        final Block reactorFrameBlock = CNBlocks.REACTOR_FRAME.get();

        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

        int frameMinY = Integer.MAX_VALUE;
        int frameMaxY = Integer.MIN_VALUE;

        // Collected here and assigned their capacity after the scan: the reactor-size capacity is a
        // single TOTAL shared across every fluid input, so we split it once we know how many there are.
        List<ReactorFluidInputEntity> fluidInputs = new ArrayList<>();

        for (int y = yMin; y <= yMax; y++) {
            boolean isYBoundary = (y == yMin || y == yMax);
            for (int x = xMin; x <= xMax; x++) {
                boolean isXBoundary = (x == xMin || x == xMax);
                for (int z = zMin; z <= zMax; z++) {
                    boolean isZBoundary = (z == zMin || z == zMax);

                    if (!(isYBoundary || isXBoundary || isZBoundary)) continue;

                    mutablePos.set(x, y, z);
                    BlockState blockState = level.getBlockState(mutablePos);

                    if (blockState.is(reactorOutputBlock)) {
                        entity.addOutput(mutablePos.immutable());
                    } else if (blockState.is(reactorRodInputBlock)) {
                        entity.addInput(mutablePos.immutable());
                    } else if (blockState.is(reactorInputFluidBlock)) {
                        entity.addInputFluid(mutablePos.immutable());
                        if (level.getBlockEntity(mutablePos) instanceof ReactorFluidInputEntity fluidInput) {
                            fluidInputs.add(fluidInput);
                        }
                    } else if (blockState.is(reactorAlarmBlock)) {
                        entity.addAlarm(mutablePos.immutable());
                    } else if (blockState.is(reactorFrameBlock)) {
                        if (level.getBlockEntity(mutablePos) instanceof ReactorFrameEntity frame) {
                            frame.setController(entity.position());
                        }
                        frameMinY = Math.min(frameMinY, y);
                        frameMaxY = Math.max(frameMaxY, y);
                    }
                }
            }
        }

        if (frameMinY != Integer.MAX_VALUE) {
            entity.getFrameDisplayManager().setFrameColumn(frameMinY, frameMaxY, entity::notifyUpdate);
        }

        distributeFluidInputCapacity(fluidInputs, entity.getMultiblockSize());
    }

    /**
     * Splits the reactor-size total fluid capacity evenly across all fluid inputs so the combined
     * max capacity always equals the configured value, regardless of how many inputs were placed.
     * The droplet remainder is spread one unit at a time over the first inputs so the sum stays exact.
     */
    private static void distributeFluidInputCapacity(List<ReactorFluidInputEntity> fluidInputs, int reactorSize) {
        int count = fluidInputs.size();
        if (count == 0) return;

        long total = ReactorFluidInputEntity.getCapacityForReactorSize(reactorSize);
        long base = total / count;
        long remainder = total % count;

        for (int i = 0; i < count; i++) {
            fluidInputs.get(i).applyCapacity(base + (i < remainder ? 1 : 0));
        }
    }

    private static ReactorControllerBlockEntity getBlockEntity(Level level, BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof ReactorControllerBlockEntity entity) {
            return entity;
        }
        return null;
    }

    private static void sendMessageToPlayer(Level level, BlockPos pos, LangBuilder component, boolean condition) {
        if (!condition) return;

        NotifyUtil.sendActionBar(level, pos,
                component,
                ChatFormatting.GOLD, configRadius, configWarnAll
        );
    }

    public static BoundingBox getStructureBound(BlockPos center, int size, Direction facing) {
        int radius = (size - 1) / 2;
        int height = radius + 1;
        int depth = size - 1;
        Direction into = facing.getOpposite();

        int axisX = into.getOffsetX() * depth;
        int axisZ = into.getOffsetZ() * depth;

        int minX = center.getX() + Math.min(0, axisX) - (axisX == 0 ? radius : 0);
        int maxX = center.getX() + Math.max(0, axisX) + (axisX == 0 ? radius : 0);
        int minZ = center.getZ() + Math.min(0, axisZ) - (axisZ == 0 ? radius : 0);
        int maxZ = center.getZ() + Math.max(0, axisZ) + (axisZ == 0 ? radius : 0);

        return new BoundingBox(minX, center.getY() - height, minZ,
                maxX, center.getY() + height, maxZ);
    }
}
