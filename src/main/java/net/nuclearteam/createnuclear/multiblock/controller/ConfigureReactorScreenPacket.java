package net.nuclearteam.createnuclear.multiblock.controller;

import com.simibubi.create.foundation.networking.BlockEntityConfigurationPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.ynov.createnuclear.CreateNuclear;


public class ConfigureReactorScreenPacket extends /*SimplePacketBase*/ BlockEntityConfigurationPacket<ReactorControllerBlockEntity> {

    private CNOption option;
    private CompoundTag nbt;

    public ConfigureReactorScreenPacket(BlockPos pos, CompoundTag instructions, CNOption option) {
        super(pos);
        CreateNuclear.LOGGER.warn("Send To Clients 2");
        this.option = option;
        this.nbt = instructions;
    }

    public ConfigureReactorScreenPacket(FriendlyByteBuf buffer) {
        super(buffer);
        CreateNuclear.LOGGER.warn("Send To Clients 3 ");

    }

    @Override
    protected void readSettings(FriendlyByteBuf buffer) {
        nbt = buffer.readNbt();
        option = buffer.readEnum(CNOption.class);
        CreateNuclear.LOGGER.warn("Test ?1");

    }


    @Override
    protected void writeSettings(FriendlyByteBuf buffer) {
        buffer.writeNbt(nbt);
        buffer.writeEnum(option);
        CreateNuclear.LOGGER.warn("Test ?2");

    }

    @Override
    protected void applySettings(ReactorControllerBlockEntity controllerBlockEntity) {
        if (!controllerBlockEntity.created) return;
CreateNuclear.LOGGER.warn("Test ?3");
        controllerBlockEntity.screen_pattern = nbt;
        controllerBlockEntity.sendData();
    }



    /*@Override
    public void write(FriendlyByteBuf buffer) {
        CreateNuclear.LOGGER.warn("Send To Clients 4 ");
        buffer.writeEnum(option);
        buffer.writeNbt(nbt);
        CreateNuclear.LOGGER.warn("Send To Clients 4.5 ");
    }

    //@Environment(EnvType.CLIENT)
    @Override
    public boolean handle(Context context) {
        CreateNuclear.LOGGER.warn("Send To Clients 5 ");
        return true;
    }

    public void handleClient() {
        CreateNuclear.LOGGER.warn("rgfnfdbnflnfkjfkljlhgjffhjklmlkjhgfhfggghgjklm45654545465465465465465456456465465465465456456465456465456465");
        Minecraft mc = Minecraft.getInstance();
        BlockPos pos = new BlockPos(62, 100, 29);
        if (!mc.player.blockPosition().closerThan(pos, 100))
            return;
        SuperGlueItem.spawnParticles(mc.level, pos, Direction.SOUTH, true);
        /*LocalPlayer player = Minecraft.getInstance().player;
        if (player != null || !(player.containerMenu instanceof ReactorControllerMenu)) return;
        player.sendSystemMessage(Component.literal("test"));
        CreateNuclear.LOGGER.warn("d4 screen " + nbt);
        ReactorControllerBlockEntity be = ((ReactorControllerMenu) player.containerMenu).contentHolder;

        switch (option) {
            case SCREEN_PATTERN:
                be.screen_pattern = this.nbt;
                break;
            default:
                break;
        }
        be.sendUpdate = true;
    }*/
}
