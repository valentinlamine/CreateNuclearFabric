package net.nuclearteam.createnuclear.content.multiblock.controller.service;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import net.nuclearteam.createnuclear.infrastructure.config.CNConfigs;

public interface IExplosionService {
    void triggerExplosion(ServerLevel level, BlockPos controllerPos, int reactorSize, int countFuelRod,
                          int notifyRadius, boolean notifyWarnAll);

    default void triggerExplosion(ServerLevel level, BlockPos controllerPos, int reactorSize, int countFuelRod) {
        triggerExplosion(level, controllerPos, reactorSize,
                            countFuelRod, CNConfigs.server().notify.warningDistance.get(),
                            CNConfigs.server().notify.warnAllPlayers.get()
        );
    }
}
