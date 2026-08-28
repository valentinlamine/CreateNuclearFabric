package net.nuclearteam.createnuclear.content.multiblock.input.item;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.BlockPos;

public final class ReactorRodInputClient {

    private ReactorRodInputClient() {
    }

    public static void register() {
        ReactorRodInputMenu.setClientContentResolver(ReactorRodInputClient::resolve);
    }

    private static ReactorRodInputEntity resolve(BlockPos pos, CompoundTag data) {
        ClientLevel world = Minecraft.getInstance().level;
        if (world == null) {
            throw new IllegalStateException("Cannot open reactor rod input menu without a client world");
        }

        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (!(blockEntity instanceof ReactorRodInputEntity reactorRodInputEntity)) {
            throw new IllegalStateException("Missing reactor rod input block entity at " + pos);
        }

        if (data != null) {
            reactorRodInputEntity.readClient(data);
        }
        return reactorRodInputEntity;
    }
}
