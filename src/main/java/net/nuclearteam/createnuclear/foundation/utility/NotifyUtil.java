package net.nuclearteam.createnuclear.foundation.utility;

import net.createmod.catnip.lang.LangBuilder;
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.Level;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class for sending player notifications server-side.
 * Supports Action Bar messages and full-screen title/subtitle overlays,
 * with radius-based or server-wide targeting.
 */
public class NotifyUtil {

    /**
     * Convenience overload that accepts a {@link LangBuilder} message,
     * converting it to a {@link MutableComponent} before sending.
     *
     * @see #sendActionBar(Level, BlockPos, MutableComponent, ChatFormatting, int, boolean)
     */
    public static void sendActionBar(Level level, BlockPos pos, LangBuilder message, ChatFormatting color, int radius, boolean warnAll) {
        sendActionBar(level, pos, message.component(), color, radius, warnAll);
    }

    /**
     * Sends a bold colored message to the Action Bar (above the hotbar) of nearby players.
     * No-ops on the client side.
     *
     * @param level    The server-side world used to resolve players
     * @param pos      Center position used to compute the notification radius
     * @param message  The component text to display; will be styled bold with {@code color}
     * @param color    {@link ChatFormatting} color applied to the message
     * @param radius   Block radius around {@code pos} within which players are notified;
     *                 ignored when {@code warnAll} is {@code true}
     * @param warnAll  If {@code true}, all online players receive the message regardless of position
     */
    public static void sendActionBar(Level level, BlockPos pos, MutableComponent message, ChatFormatting color, int radius, boolean warnAll) {
        if (level.isClientSide) return;

        // Build the text component in BOLD with the chosen color
        Component actionBarComp = message.withStyle(color, ChatFormatting.BOLD);

        List<ServerPlayer> targets = getTargetPlayers(level, pos, radius, warnAll);

        for (ServerPlayer player : targets) {
            // The 'true' parameter routes the message to the Action Bar
            player.displayClientMessage(actionBarComp, true);
        }
    }

    /**
     * Convenience overload that accepts {@link LangBuilder} title and subtitle,
     * converting them to {@link MutableComponent} before sending.
     *
     * @see #sendTitle(Level, BlockPos, MutableComponent, MutableComponent, ChatFormatting, int, boolean, int, int, int)
     */
    public static void sendTitle(Level level, BlockPos pos, LangBuilder title, LangBuilder subtitle, ChatFormatting color,
                                 int radius, boolean warnAll, int fadeIn, int stay, int fadeOut) {
        sendTitle(level, pos, title.component(), subtitle.component(), color, radius, warnAll, fadeIn, stay, fadeOut);
    }

    /**
     * Sends a full-screen title and subtitle overlay to nearby players.
     * The title is rendered bold with {@code color}; the subtitle is always white.
     * No-ops on the client side.
     *
     * @param level    The server-side world used to resolve players
     * @param pos      Center position used to compute the notification radius
     * @param title    Main title text displayed in large letters
     * @param subtitle Subtitle text displayed below the title
     * @param color    {@link ChatFormatting} color applied to the title
     * @param radius   Block radius around {@code pos} within which players are notified;
     *                 ignored when {@code warnAll} is {@code true}
     * @param warnAll  If {@code true}, all online players receive the message regardless of position
     * @param fadeIn   Ticks for the fade-in animation
     * @param stay     Ticks the title remains fully visible
     * @param fadeOut  Ticks for the fade-out animation
     */
    public static void sendTitle(Level level, BlockPos pos, MutableComponent title, MutableComponent subtitle, ChatFormatting color,
                                 int radius, boolean warnAll, int fadeIn, int stay, int fadeOut) {

        if (level.isClientSide) return;

        Component titleComp = title.withStyle(color, ChatFormatting.BOLD);
        Component subtitleComp = subtitle.withStyle(ChatFormatting.WHITE);

        List<ServerPlayer> targets = getTargetPlayers(level, pos, radius, warnAll);

        for (ServerPlayer serverPlayer : targets) {
            serverPlayer.connection.send(new ClientboundSetTitlesAnimationPacket(fadeIn, stay, fadeOut));
            serverPlayer.connection.send(new ClientboundSetSubtitleTextPacket(subtitleComp));
            serverPlayer.connection.send(new ClientboundSetTitleTextPacket(titleComp));
        }
    }

    /**
     * Convenience overload that accepts {@link LangBuilder} title and subtitle,
     * converting them to {@link MutableComponent} before sending.
     *
     * @see #quickAlert(Level, BlockPos, MutableComponent, MutableComponent, ChatFormatting, int, boolean)
     */
    public static void quickAlert(Level level, BlockPos pos, LangBuilder title, LangBuilder sub, ChatFormatting color, int radius, boolean warnAll) {
        quickAlert(level, pos, title.component(), sub.component(), color, radius, warnAll);
    }


    /**
     * Shortcut for {@link #sendTitle} using default animation timings (fadeIn=10, stay=50, fadeOut=10).
     *
     * @param level   The server-side world used to resolve players
     * @param pos     Center position used to compute the notification radius
     * @param title   Main title text
     * @param sub     Subtitle text
     * @param color   {@link ChatFormatting} color applied to the title
     * @param radius  Block radius around {@code pos}; ignored when {@code warnAll} is {@code true}
     * @param warnAll If {@code true}, all online players receive the message
     */
    public static void quickAlert(Level level, BlockPos pos, MutableComponent title, MutableComponent sub, ChatFormatting color, int radius, boolean warnAll) {
        sendTitle(level, pos, title, sub, color, radius, warnAll, 10, 50, 10);
    }

    /**
     * Resolves the list of players to notify.
     * Returns all online players if {@code warnAll} is {@code true},
     * otherwise returns only those within {@code radius} blocks of {@code pos}.
     *
     * @param level   The server-side world
     * @param pos     Center position for the radius check
     * @param radius  Block radius for proximity filtering
     * @param warnAll If {@code true}, bypasses the radius check
     * @return Mutable list of matching {@link ServerPlayer} instances; empty if the server is unavailable
     */
    private static List<ServerPlayer> getTargetPlayers(Level level, BlockPos pos, int radius, boolean warnAll) {
        if (level.getServer() == null) return List.of();

        List<ServerPlayer> allPlayers = level.getServer().getPlayerList().getPlayers();
        if (warnAll) return new ArrayList<>(allPlayers);

        AABB area = new AABB(pos).inflate(radius);
        return allPlayers.stream()
                .filter(p -> area.contains(p.getX(), p.getY(), p.getZ()))
                .collect(Collectors.toList());
    }
}