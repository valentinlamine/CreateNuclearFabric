package net.ynov.createnuclear.tools;

import lib.multiblock.test.misc.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.core.Rotations;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Rotation;
import net.ynov.createnuclear.CNMultiblock;
import net.ynov.createnuclear.block.CNBlocks;
import net.ynov.createnuclear.blockentity.CNBlocksEntity;
import net.ynov.createnuclear.blockentity.ReinforcedGlassEntity;
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

            if (level.getBlockState(pos).is(CNBlocks.REINFORCED_GLASS.get())) {
                if (player != null) {
                    boolean structureFound = false;


                    Direction direction = level.getBlockState(pos).getValue(ReinforcedGlassEntity.FACING);
                    Rotation rotations = Util.DirectionToRotation(direction);

                    var result = CNMultiblock.REGISTRATE_MULTIBLOCK.findStructure(context.getLevel(), pos, rotations);
                    if (result != null) {
                        String id = result.id().replace("createnuclear:", "");
                        MutableComponent translatedID = Component.translatable("jei_pattern." + id);
                        player.sendSystemMessage(Component.literal("Structure trouvé: " + translatedID.getString()).withStyle(ChatFormatting.BLUE));
                        structureFound = true;
                    }

                    if (!structureFound) {
                        player.sendSystemMessage(Component.literal("Structure non trouver").withStyle(ChatFormatting.RED));
                    }
                }
            }else {
                assert player != null;
                player.sendSystemMessage(Component.literal("Click droit sur le Reinforced Glass (pour l'instant"));

            }
        }
        return super.useOn(context);
    }
}
