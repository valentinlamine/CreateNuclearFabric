package net.ynov.createnuclear.tools;

import lib.multiblock.test.misc.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Rotation;
import net.ynov.createnuclear.CNMultiblock;
import net.ynov.createnuclear.block.CNBlocks;
import net.ynov.createnuclear.blockentity.ReinforcedGlassBlock;
import org.jetbrains.annotations.NotNull;

public class VerifiedStructureItem extends Item {
    public VerifiedStructureItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        if (!context.getLevel().isClientSide){
            Level level = context.getLevel();
            var pos = context.getClickedPos();
            var player = context.getPlayer();

            if (level.getBlockState(pos).is(CNBlocks.REACTOR_CONTROLLER.get())) {
                if (player != null) {
                    player.sendSystemMessage(Component.literal("Analyse multiBlock"));
                    boolean structureFound = false;

                    var result = CNMultiblock.REGISTRATE_MULTIBLOCK.findStructure(context.getLevel(), pos);
                    if (result != null) {
                        String id = result.id().replace("createnuclear:", "");
                        MutableComponent translatedID = Component.translatable("jei_pattern." + id);
                        player.sendSystemMessage(Component.literal("MultiBlock assemblé: " + translatedID.getString()).withStyle(ChatFormatting.BLUE));
                        structureFound = true;
                    }

                    if (!structureFound) {
                        player.sendSystemMessage(Component.literal("Erreur dans l'assemblage du multiBlock").withStyle(ChatFormatting.RED));
                    }
                }
            }else {
                assert player != null;
                player.sendSystemMessage(Component.literal("Click droit sur le REACTOR_CONTROLLER"));

            }
        }
        return super.useOn(context);
    }
}
