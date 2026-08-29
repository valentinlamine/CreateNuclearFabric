package net.nuclearteam.createnuclear.content.multiblock.controller.service;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.nuclearteam.createnuclear.foundation.utility.CreateNuclearLang;
import net.nuclearteam.createnuclear.foundation.utility.NotifyUtil;
import net.nuclearteam.createnuclear.infrastructure.config.CNConfigs;

/**
 * Default {@link IReactorMeltdownMonitor}. Extracted from
 * {@code ReactorControllerBlockEntity.tick()}: the countdown logic and its notifications
 * are unchanged from the original implementation, only moved into their own class.
 */
public class ReactorMeltdownMonitor implements IReactorMeltdownMonitor {
    /** Ticks of continuous danger before the reactor explodes (15s at 20 TPS). */
    private static final int EXPLOSION_THRESHOLD_TICKS = 300;
    /** Last seconds of the countdown during which the flashing "meltdown in" message is shown. */
    private static final int CRITICAL_WINDOW_SECONDS = 10;

    private int explosionCountDown = 0;

    @Override
    public MeltdownState tick(Level level, BlockPos pos, boolean isDanger) {
        int configRadius = CNConfigs.server().notify.warningDistance.get();
        boolean configWarnAll = CNConfigs.server().notify.warnAllPlayers.get();

        if (!isDanger) {
            boolean wasCountingDown = explosionCountDown > 0;
            explosionCountDown = 0;
            if (wasCountingDown) {
                NotifyUtil.sendActionBar(level, pos,
                    CreateNuclearLang.translate("notification.reactor.stabilized"),
                    ChatFormatting.GREEN, configRadius, configWarnAll);
            }
            return MeltdownState.NONE;
        }

        explosionCountDown++;
        int secondsLeft = (EXPLOSION_THRESHOLD_TICKS - explosionCountDown) / 20;

        if (secondsLeft <= CRITICAL_WINDOW_SECONDS && secondsLeft > 0) {
            boolean isWhite = (level.getGameTime() / 5) % 2 == 0;
            ChatFormatting flashColor = isWhite ? ChatFormatting.WHITE : ChatFormatting.RED;

            NotifyUtil.sendActionBar(level, pos,
                    CreateNuclearLang.translate("notification.reactor.meltdown_in")
                            .add(CreateNuclearLang.number(secondsLeft))
                            .add(CreateNuclearLang.translate("generic.unit.seconds")),
                    flashColor, configRadius, configWarnAll);
        } else if (secondsLeft > CRITICAL_WINDOW_SECONDS && explosionCountDown % 20 == 0) {
            NotifyUtil.sendActionBar(level, pos,
                    CreateNuclearLang.translate("notification.reactor.overheating"),
                    ChatFormatting.DARK_RED, configRadius, configWarnAll);
        }

        if (explosionCountDown >= EXPLOSION_THRESHOLD_TICKS) {
            NotifyUtil.sendTitle(level, pos,
                    CreateNuclearLang.translate("notification.reactor.critical_failure"),
                    CreateNuclearLang.translate("notification.reactor.imminent_explosion"),
                    ChatFormatting.DARK_RED, configRadius, configWarnAll, 0, 40, 10);
            return MeltdownState.EXPLODE;
        }

        return MeltdownState.CRITICAL;
    }
}
