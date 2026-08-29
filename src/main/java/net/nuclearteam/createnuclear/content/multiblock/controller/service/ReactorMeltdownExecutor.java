package net.nuclearteam.createnuclear.content.multiblock.controller.service;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.GameRules;
import net.nuclearteam.createnuclear.CNEntityType;
import net.nuclearteam.createnuclear.content.explosion.NuclearExplosionEntity;
import net.nuclearteam.createnuclear.foundation.utility.CreateNuclearLang;
import net.nuclearteam.createnuclear.foundation.utility.NotifyUtil;
import net.nuclearteam.createnuclear.infrastructure.worldgen.biome.BiomeIrradiationService;
import net.nuclearteam.createnuclear.infrastructure.worldgen.biome.CNBiomes;

public class ReactorMeltdownExecutor implements IExplosionService {
    @Override
    public void triggerExplosion(ServerLevel level, BlockPos controllerPos, int reactorSize, int countFuelRod, int notifyRadius, boolean notifyWarnAll) {
        BlockPos explosionPos = controllerPos.above(5);

        NotifyUtil.sendTitle(level, controllerPos,
                CreateNuclearLang.translate("notification.reactor.destroyed"),
                CreateNuclearLang.translate("notification.reactor.meltdown_finished"),
                ChatFormatting.DARK_RED, notifyRadius, notifyWarnAll, 10, 60, 20
        );

        float size = computeExplosionSize(reactorSize, countFuelRod);

        NuclearExplosionEntity explosion = new NuclearExplosionEntity(CNEntityType.NUCLEAR_EXPLOSION.get(), level);
        explosion.setPos(explosionPos.getX() + 0.5D, explosionPos.getY() + 10.0D, explosionPos.getZ() - 2.0D);
        explosion.setSize(size);
        explosion.setNoGriefing(!level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING));
        level.addFreshEntity(explosion);

        level.destroyBlock(controllerPos, false);

        BiomeIrradiationService.circularArea(level, explosionPos, CNBiomes.Irradiated.PLAIN, (int) (size * 30));

    }

    private static float computeExplosionSize(int reactorSize, int countFuelRod) {
        // Scales with reactor footprint: 5x5 -> 1.0, 7x7 -> 1.4, 9x9 -> 1.8
        float structureFactor = reactorSize / 5f;

        // Square root gives diminishing returns: each extra uranium rod adds less
        // to the radius than the previous one.
        float fuelImpact = Mth.sqrt(countFuelRod) * .3f;

        // Final size: constant base + (fuel impact scaled by structure factor)
        return Mth.clamp(1.5f + (fuelImpact * structureFactor), 1f, 10f);
    }
}
