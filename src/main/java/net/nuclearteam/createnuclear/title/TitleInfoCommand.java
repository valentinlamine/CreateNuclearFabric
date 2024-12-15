package net.nuclearteam.createnuclear.title;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.Commands.CommandSelection;
import net.minecraft.commands.arguments.ResourceOrTagKeyArgument;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;

public class TitleInfoCommand {

    public static final DynamicCommandExceptionType INVALID_BIOME_EXCEPTION = new DynamicCommandExceptionType(
            (formatArgs) -> Component.translatable("createnuclear.title.info.invalid", formatArgs));

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context, CommandSelection environment) {
        dispatcher.register(Commands.literal("titleinfo")
                .requires(source -> source.hasPermission(2))
                .then(Commands.argument("info", ResourceOrTagKeyArgument.resourceOrTagKey(Registries.CHAT_TYPE))
                        .executes(ctx -> displayTitle(ctx.getSource(), ResourceOrTagKeyArgument.getResourceOrTagKey(ctx, "info", Registries.CHAT_TYPE, INVALID_BIOME_EXCEPTION)))
                )
        );
    }
    //https://github.com/YUNG-GANG/Travelers-Titles/blob/1.20/Common/src/main/java/com/yungnickyoung/minecraft/travelerstitles/command/BiomeTitleCommand.java#L56

    private static int displayTitle(CommandSourceStack source, ResourceOrTagKeyArgument.Result<ChatType> chatResult) throws CommandSyntaxException {
        return 1;
    }
}
