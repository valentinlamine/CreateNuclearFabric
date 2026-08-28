package net.nuclearteam.createnuclear.content.multiblock;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.nuclearteam.createnuclear.CreateNuclear;
import net.nuclearteam.createnuclear.content.multiblock.controller.ReactorControllerBlockEntity;
import net.nuclearteam.createnuclear.content.multiblock.pattern.ReactorPattern;
import net.nuclearteam.createnuclear.foundation.advancement.CNAdvancement;
import net.nuclearteam.createnuclear.foundation.advancement.CNAdvancementBehaviour;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.BiConsumer;

public class MultiblockHelpers {
    private static final ReactorPattern pattern = new ReactorPattern();

    /**
     * Utility helpers for handling multiblock placement and removal events.
     * These helpers locate the controller for a given part position and
     * invoke provided callbacks to register or remove parts.
     */
    public static void handleOnPlace(BlockPos pos, Level level, BiConsumer<ReactorControllerBlockEntity, BlockPos> register) {
        BlockPos controllerPos = pattern.findControllerPos(pos, level, true);
        if (controllerPos != null) {
            ReactorControllerBlockEntity controllerBlockEntity = (ReactorControllerBlockEntity) level.getBlockEntity(controllerPos);
            if (controllerBlockEntity != null) {
                register.accept(controllerBlockEntity, pos);
            }
        }
    }

    public static void handleRemoval(BlockPos pos, Level level, BiConsumer<ReactorControllerBlockEntity, BlockPos> remover) {
        BlockPos controllerPos = pattern.findControllerPos(pos, level, false);
        if (controllerPos != null) {
            ReactorControllerBlockEntity controllerBlockEntity = (ReactorControllerBlockEntity) level.getBlockEntity(controllerPos);
            if (controllerBlockEntity != null) {
                remover.accept(controllerBlockEntity, pos);
            }
        }
    }

    public static ReactorControllerBlockEntity getControllerForPart(Level level, BlockPos pos) {
        /**
         * Returns the controller entity for the given part position, or null
         * if no valid controller is found.
         */
        BlockPos controllerPos = pattern.findControllerPos(pos, level);
        if (controllerPos != null) {
            return  (ReactorControllerBlockEntity) level.getBlockEntity(controllerPos);
        }

        return null;
    }

    public static void handleAdvancedPlacedBy(BlockPos pos, Level level, @Nullable LivingEntity entity) {
        if (entity == null) return;

        ReactorControllerBlockEntity controller = getControllerForPart(level, pos);

        if (controller != null) {
            CNAdvancementBehaviour.setPlacedBy(level, controller.getBlockPos(), entity);
        }
    }
}
