package net.nuclearteam.createnuclear.tools;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.nuclearteam.createnuclear.CreateNuclear;
import net.nuclearteam.createnuclear.multiblock.controller.EventTriggerPacket;
import net.nuclearteam.createnuclear.packets.CNPackets;

public class EventTriggerBlock extends Block {
    public EventTriggerBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos,
                                 Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide) {
            // Send packet to all clients around this block within 16 blocks
            EventTriggerPacket packet = new EventTriggerPacket(100); // display for 100 ticks
            CreateNuclear.LOGGER.warn("hum EventTriggerBlock ? {}", packet);
            CNPackets.getChannel().sendToClientsAround(packet, (ServerLevel) level, new Vec3(pos.getX(), pos.getY(), pos.getZ()), 16);
        }
        return InteractionResult.SUCCESS;
    }
}
