package net.nuclearteam.createnuclear.content.multiblock.controller;

import net.minecraft.world.entity.player.Player;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.nuclearteam.createnuclear.content.multiblock.controller.manager.*;
import net.nuclearteam.createnuclear.foundation.utility.CreateNuclearLang;

import java.util.Collection;

public class ReactorDebugDiagnostics {
    private static final String TITLE = "reactor.debug.title";
    private static final String SUMMARY = "reactor.debug.summary";
    private static final String REGISTERED_POSITION = "reactor.debug.registered_position";
    private static final String VALID_POSITION = "reactor.debug.valid_position";

    private static final String INPUT = "reactor.debug.label.input";
    private static final String INPUT_FLUID = "reactor.debug.label.fluid_input";
    private static final String OUTPUT = "reactor.debug.label.output";
    private static final String ALARM = "reactor.debug.label.alarm";

    private ReactorDebugDiagnostics() {}

    public static void sendReactorConnectionsTo(Player player, Level level,
            ReactorInputManagerI inputManager, ReactorInputFluidManagerI inputFluidManager,
            ReactorOutputManagerI outputManager,
            ReactorAlarmManagerI alarmManager
    ) {
        player.displayClientMessage(CreateNuclearLang.translate(TITLE).style(ChatFormatting.GOLD).component(), false);

        sendManagerSummary(player, CreateNuclearLang.translate(INPUT).component(),
                inputManager.size(), inputManager.getBlocksPosition(), inputManager.getBlocksPosition(level));

        sendManagerSummary(player, CreateNuclearLang.translate(INPUT_FLUID).component(),
                inputFluidManager.size(), inputFluidManager.getBlocksPosition(), inputFluidManager.getBlocksPosition(level));

        sendManagerSummary(player, CreateNuclearLang.translate(OUTPUT).component(),
                outputManager.size(), outputManager.getBlocksPosition(), outputManager.getBlocksPosition(level));

        sendManagerSummary(player, CreateNuclearLang.translate(ALARM).component(),
                alarmManager.size(), alarmManager.getBlocksPosition(), alarmManager.getBlocksPosition(level));
    }

    private static void sendManagerSummary(Player player, Component label, int registeredCount,
                                           Collection<BlockPos> registered, Collection<BlockPos> valid) {
        player.displayClientMessage(CreateNuclearLang.translate(SUMMARY, label, registeredCount, valid.size()).style(ChatFormatting.YELLOW).component(), false);

        for (BlockPos pos : registered) {
            player.displayClientMessage(CreateNuclearLang.translate(REGISTERED_POSITION, pos.toShortString()).component(), false);
        }
        for (BlockPos pos : valid) {
            player.displayClientMessage(CreateNuclearLang.translate(VALID_POSITION, pos.toShortString()).component(), false);
        }
    }
}
