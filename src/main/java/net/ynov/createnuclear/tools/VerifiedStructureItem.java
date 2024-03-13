package net.ynov.createnuclear.tools;

import lib.multiblock.test.misc.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.ynov.createnuclear.CNMultiblock;
import net.ynov.createnuclear.CreateNuclear;
import net.ynov.createnuclear.block.CNBlocks;
import net.ynov.createnuclear.blockentity.ReinforcedGlassBlock;
import net.ynov.createnuclear.energy.ReactorOutput;
import net.ynov.createnuclear.energy.ReactorOutputEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

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
                    if(level.getBlockState(pos.below(3)).is(CNBlocks.REACTOR_OUTPUT.get()))
                        player.sendSystemMessage(Component.literal("ok"));
                    else player.sendSystemMessage(Component.literal("pas ok" + level.getBlockState(pos.below(3))));
                    ReactorOutput block = (ReactorOutput) level.getBlockState(pos.below(3)).getBlock();
                    player.sendSystemMessage(Component.literal("Analyse multiBlock"));
                    boolean structureFound = false;

                    var result = CNMultiblock.REGISTRATE_MULTIBLOCK.findStructure(context.getLevel(), pos);
                    if (result != null) {
                        String id = result.id().replace("createnuclear:", "");
                        MutableComponent translatedID = Component.translatable("jei_pattern." + id);
                        player.sendSystemMessage(Component.literal("MultiBlock assemblé: " + translatedID.getString()).withStyle(ChatFormatting.BLUE));
                        structureFound = true;
                        ReactorOutputEntity.structure = true;
                        Objects.requireNonNull(block.getBlockEntityType().getBlockEntity(level, pos.below(3))).speed = 16;
                        Objects.requireNonNull(block.getBlockEntityType().getBlockEntity(level, pos.below(3))).updateSpeed = true;
                        Objects.requireNonNull(block.getBlockEntityType().getBlockEntity(level, pos.below(3))).updateGeneratedRotation();
                    }

                    if (!structureFound) {
                        ReactorOutputEntity.structure = false;
                        player.sendSystemMessage(Component.literal("Erreur dans l'assemblage du multiBlock2").withStyle(ChatFormatting.RED));
                        Objects.requireNonNull(block.getBlockEntityType().getBlockEntity(level, pos.below(3))).speed = 0;
                        Objects.requireNonNull(block.getBlockEntityType().getBlockEntity(level, pos.below(3))).updateSpeed = true;
                        Objects.requireNonNull(block.getBlockEntityType().getBlockEntity(level, pos.below(3))).updateGeneratedRotation();
                    }
                }
            } else if (level.getBlockState(pos).is(CNBlocks.REACTOR_OUTPUT.get()) && ReactorOutputEntity.structure) {
                ReactorOutput blocky = (ReactorOutput)level.getBlockState(pos).getBlock();
                Objects.requireNonNull(blocky.getBlockEntityType().getBlockEntity(level, pos)).speed = -Objects.requireNonNull(blocky.getBlockEntityType().getBlockEntity(level, pos)).speed;
                Objects.requireNonNull(blocky.getBlockEntityType().getBlockEntity(level, pos)).updateSpeed = true;
                Objects.requireNonNull(blocky.getBlockEntityType().getBlockEntity(level, pos)).updateGeneratedRotation();
            } else {
                assert player != null;
                player.sendSystemMessage(Component.literal("Click droit sur le REACTOR_CONTROLLER"));
            }
        }
        return super.useOn(context);
    }
}
