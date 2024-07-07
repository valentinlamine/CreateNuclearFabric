package net.ynov.createnuclear.multiblock.controller;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllEntityTypes;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.IInteractionChecker;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.LangBuilder;
import lib.multiblock.test.SimpleMultiBlockAislePatternBuilder;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SidedStorageBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.ynov.createnuclear.CreateNuclear;
import net.ynov.createnuclear.block.CNBlocks;
import net.ynov.createnuclear.gui.CNIconButton;
import net.ynov.createnuclear.multiblock.IHeat;
import net.ynov.createnuclear.multiblock.energy.ReactorOutput;
import net.ynov.createnuclear.multiblock.energy.ReactorOutputEntity;

import java.util.List;

import static net.ynov.createnuclear.CNMultiblock.*;
import static net.ynov.createnuclear.multiblock.controller.ReactorControllerBlock.ASSEMBLED;
import static net.ynov.createnuclear.packets.CNPackets.getChannel;

public class ReactorControllerBlockEntity extends SmartBlockEntity implements /*MenuProvider, */IInteractionChecker/*, SidedStorageBlockEntity*/, IHaveGoggleInformation {
    public boolean destroyed = false;
    public boolean created = false;
    public int speed = 16; // This is the result speed of the reactor, change this to change the total capacity

    public boolean sendUpdate;

    public ReactorControllerBlock controller;

    public ReactorControllerInventory inventory;

    //private boolean powered;
    public State powered = State.OFF;
    public float reactorPower;
    public float lastReactorPower;
    public int countUraniumRod;
    public int countGraphiteRod;
    public int graphiteTimer = 3600;
    public int uraniumTimer = 6000;
    public int heat;
    public CompoundTag screen_pattern = new CompoundTag();
    private List<CNIconButton> switchButtons;



    public ReactorControllerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        inventory = new ReactorControllerInventory(this);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {

    }

    public boolean getAssembled() { // permet de savoir si le réacteur est formé ou pas.
        BlockState state = getBlockState();
        return Boolean.TRUE.equals(state.getValue(ASSEMBLED));
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        tooltip.add(componentSpacing.plainCopy().append(Lang.translateDirect("gui.gauge.info_header")));

        IHeat.HeatLevel.getName("reactor_controller").style(ChatFormatting.GRAY).forGoggles(tooltip);

        Lang.builder(CreateNuclear.MOD_ID).translate("")
                .style(ChatFormatting.BLUE)
                .translate("uranium.rod")
                .add(Lang.number(Math.abs(inventory.getSlot(0).getAmount())))
                .newLine()
                .translate("graphene.rod")
                .add(Lang.number(Math.abs(inventory.getSlot(1).getAmount())))
                .forGoggles(tooltip);

        IHeat.HeatLevel.getFormattedHeatText(heat).forGoggles(tooltip);
        return true;
    }


    /*@Override
    public Component getDisplayName() {
        return Components.translatable("gui.createnuclear.reactor_controller.title");
    }*/
    //(Si les methode read et write ne sont pas implémenté alors lorsque l'on relance le monde minecraft les items dans le composant auront disparu !)
    @Override
    protected void read(CompoundTag compound, boolean clientPacket) { //Permet de stocker les item 1/2
        /*if (!clientPacket) {
            inventory.deserializeNBT(compound.getCompound("Inventory"));
        }

        String stateString = compound.getString("state");
        powered = stateString.isEmpty() ? State.OFF : State.valueOf(compound.getString("state"));
        countGraphiteRod = compound.getInt("countGraphiteRod");
        countUraniumRod = compound.getInt("countUraniumRod");
        graphiteTimer = compound.getInt("graphiteTimer");
        uraniumTimer = compound.getInt("uraniumTimer");
        heat = compound.getInt("heat");
        screen_pattern = compound.getCompound("screen_pattern");
*/
        super.read(compound, clientPacket);
    }

    @Override
    protected void write(CompoundTag compound, boolean clientPacket) { //Permet de stocker les item 2/2
        /*if (!clientPacket) {
            compound.put("Inventory", inventory.serializeNBT());
            compound.putBoolean("powered", isPowered());

        }

        compound.putInt("countGraphiteRod", countGraphiteRod);
        compound.putInt("countUraniumRod", countUraniumRod);
        compound.putInt("graphiteTimer", graphiteTimer);
        compound.putInt("uraniumTimer", uraniumTimer);
        compound.putInt("heat", heat);
        compound.putString("state", powered.name());
        compound.put("screen_pattern", screen_pattern);

*/
        super.write(compound, clientPacket);
    }

    /*@Nullable
    @Override
    public Storage<ItemVariant> getItemStorage(@Nullable Direction face) {
        return inventory;
    }*/

    /*@Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        return ReactorControllerMenu.create(id, inv, this);
    }*/

    public Boolean isPowered() {
        return powered == State.ON;
    }

    public void setPowered(boolean power) {
        powered = power ? State.ON : State.OFF;
    }

    /*public List<CNIconButton> getSwitchButtons() {
        return switchButtons;
    }

    public void setSwitchButtons(List<CNIconButton> switchButtons) {
        this.switchButtons = switchButtons;
    }*/

    public enum State {
        ON, OFF;
    }

    @Override
    public void tick() {
        super.tick();

        if (level.isClientSide)
            return;

        /*if (level.getBlockState(getBlockPos().below(3)).getBlock() == CNBlocks.REACTOR_OUTPUT.get() && powered == State.ON){
            // En attendant l'explosion on arrete simplement la rotation quand la chaleur depasse 100
            Rotate(getBlockState(), getBlockPos().below(3), getLevel(), (heat >= 100 ? 0 : heat));
        }*/
        //if (heat >= 100 || heat <= 0) Rotate(getBlockState(), getBlockPos().below(3), getLevel(), 0);

        // Update Client block entity
        /*if (sendUpdate) {
            sendUpdate = false;
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 6);
        }*/
    }

    private static BlockPos FindController(char character) {
        return SimpleMultiBlockAislePatternBuilder.start()
                .aisle(AAAAA, AAAAA, AAAAA, AAAAA, AAAAA)
                .aisle(AABAA, ADADA, BACAB, ADADA, AABAA)
                .aisle(AABAA, ADADA, BACAB, ADADA, AABAA)
                .aisle(AAIAA, ADADA, BACAB, ADADA, AAAA)
                .aisle(AABAA, ADADA, BACAB, ADADA, AABAA)
                .aisle(AABAA, ADADA, BACAB, ADADA, AABAA)
                .aisle(AAAAA, AAAAA, AAAAA, AAAAA, AAOAA)
                .where('A', a -> a.getState().is(CNBlocks.REACTOR_CASING.get()))
                .where('B', a -> a.getState().is(CNBlocks.REACTOR_MAIN_FRAME.get()))
                .where('C', a -> a.getState().is(CNBlocks.REACTOR_CORE.get()))
                .where('D', a -> a.getState().is(CNBlocks.REACTOR_COOLING_FRAME.get()))
                .where('*', a -> a.getState().is(CNBlocks.REACTOR_CONTROLLER.get()))
                .where('O', a -> a.getState().is(CNBlocks.REACTOR_OUTPUT.get()))
                .where('I', a -> a.getState().is(CNBlocks.REACTOR_INPUT.get()))
                .getDistanceController(character);
    }

    public void Rotate(BlockState state, BlockPos pos, Level level, int rotation) {
        if (level.getBlockState(pos).is(CNBlocks.REACTOR_OUTPUT.get()) && rotation > 0) {
            if (level.getBlockState(pos).getBlock() instanceof ReactorOutput) {
                ReactorOutput block = (ReactorOutput) level.getBlockState(pos).getBlock();
                ReactorOutputEntity entity = block.getBlockEntityType().getBlockEntity(level, pos);

                if (state.getValue(ASSEMBLED) && rotation != 0) { // Starting the energy
                    //CreateNuclear.LOGGER.info("Change " + pos);
                    if (entity.getDir() == 1) rotation = -rotation;
                    entity.speed = rotation;
                    entity.updateSpeed = true;
                    entity.updateGeneratedRotation();
                } else { // stopping the energy
                    entity.speed = 0;
                    entity.updateSpeed = true;
                    entity.updateGeneratedRotation();
                }
                if (rotation < 0) rotation = -rotation;
                entity.setSpeed(rotation);

            }
        }
        else {
            if (level.getBlockState(pos).getBlock() instanceof ReactorOutput) {
                ReactorOutput block = (ReactorOutput) level.getBlockState(pos).getBlock();
                ReactorOutputEntity entity = block.getBlockEntityType().getBlockEntity(level, pos);
                entity.setSpeed(0);
                entity.updateSpeed = true;
                entity.updateGeneratedRotation();
            }
        }
    }
}