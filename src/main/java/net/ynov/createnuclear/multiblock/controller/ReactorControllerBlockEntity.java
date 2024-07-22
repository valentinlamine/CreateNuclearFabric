package net.ynov.createnuclear.multiblock.controller;

import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.IInteractionChecker;
import com.simibubi.create.foundation.utility.Lang;
import io.github.fabricators_of_create.porting_lib.transfer.TransferUtil;
import io.github.fabricators_of_create.porting_lib.util.StorageProvider;
import lib.multiblock.test.SimpleMultiBlockAislePatternBuilder;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.ResourceAmount;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SidedStorageBlockEntity;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.ynov.createnuclear.CreateNuclear;
import net.ynov.createnuclear.block.CNBlocks;
import net.ynov.createnuclear.gui.CNIconButton;
import net.ynov.createnuclear.item.CNItems;
import net.ynov.createnuclear.multiblock.IHeat;
import net.ynov.createnuclear.multiblock.energy.ReactorOutput;
import net.ynov.createnuclear.multiblock.energy.ReactorOutputEntity;
import net.ynov.createnuclear.multiblock.input.ReactorInputEntity;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static net.ynov.createnuclear.CNMultiblock.*;
import static net.ynov.createnuclear.multiblock.controller.ReactorControllerBlock.ASSEMBLED;

import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.simibubi.create.content.schematics.requirement.ItemRequirement.ItemUseType;

public class ReactorControllerBlockEntity extends SmartBlockEntity implements /*MenuProvider, */IInteractionChecker, SidedStorageBlockEntity, IHaveGoggleInformation {
    public boolean destroyed = false;
    public boolean created = false;
    public boolean test = true;
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
    public ItemStack configuredPattern;



    public ReactorControllerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        inventory = new ReactorControllerInventory(this);
        configuredPattern = ItemStack.EMPTY;
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {

    }

    public boolean getAssembled() { // permet de savoir si le réacteur est formé ou pas.
        BlockState state = getBlockState();
        return Boolean.TRUE.equals(state.getValue(ASSEMBLED));
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        if(!configuredPattern.getOrCreateTag().isEmpty()) {
            tooltip.add(componentSpacing.plainCopy().append(Lang.translateDirect("gui.gauge.info_header")));
            IHeat.HeatLevel.getName("reactor_controller").style(ChatFormatting.GRAY).forGoggles(tooltip);
            IHeat.HeatLevel.getFormattedHeatText(20).forGoggles(tooltip);

            BlockPos pos = FindController('I');
            BlockPos posController = getBlockPos();
            BlockPos posInput = null;
            if(level.getBlockState(new BlockPos(posController.getX(), posController.getY(), posController.getZ() + pos.getX())).is(CNBlocks.REACTOR_INPUT.get())) { // NORTH
                posInput = new BlockPos(posController.getX(), posController.getY(), posController.getZ() + pos.getX());
            }
            else if(level.getBlockState(new BlockPos(posController.getX(), posController.getY(), posController.getZ() - pos.getX())).is(CNBlocks.REACTOR_INPUT.get())) { // SOUTH
                posInput = new BlockPos(posController.getX(), posController.getY(), posController.getZ() - pos.getX());
            }
            else if(level.getBlockState(new BlockPos(posController.getX() - pos.getX(), posController.getY(), posController.getZ())).is(CNBlocks.REACTOR_INPUT.get())) { // EST
                posInput = new BlockPos(posController.getX() - pos.getX(), posController.getY(), posController.getZ());
            }
            else if(level.getBlockState(new BlockPos(posController.getX() + pos.getX(), posController.getY(), posController.getZ())).is(CNBlocks.REACTOR_INPUT.get())) { // WEST
                posInput = new BlockPos(posController.getX() + pos.getX(), posController.getY(), posController.getZ());
            }
            else {
                posInput = new BlockPos(posController.getX(), posController.getY(), posController.getZ());
            }

            StorageProvider<ItemVariant> storage = StorageProvider.createForItems(level, posInput);

            if (storage.findBlockEntity() instanceof ReactorInputEntity be) {
                CompoundTag tag = be.serializeNBT();
                ListTag inventoryTag = tag.getCompound("Inventory").getList("Items", Tag.TAG_COMPOUND);

                CreateNuclear.LOGGER.warn("Storage be: " + inventoryTag.getCompound(0).get("id") + " " +
                        (CNItems.URANIUM_ROD.get().toString().equals(inventoryTag.getCompound(0).get("id"))));

                IHeat.HeatLevel.getFormattedItemText(new ItemStack(CNItems.URANIUM_ROD.get(), 12)).forGoggles(tooltip);
                IHeat.HeatLevel.getFormattedItemText(new ItemStack(CNItems.GRAPHITE_ROD, 32)).forGoggles(tooltip);
            }
            else {
                IHeat.HeatLevel.getFormattedItemText(new ItemStack(Items.AIR, 0)).forGoggles(tooltip);
            }


            //IHeat.HeatLevel.getFormattedHeatText(heat).forGoggles(tooltip);
        }

        return true;
    }


    //(Si les methode read et write ne sont pas implémenté alors lorsque l'on relance le monde minecraft les items dans le composant auront disparu !)
    @Override
    protected void read(CompoundTag compound, boolean clientPacket) { //Permet de stocker les item 1/2
        if (!clientPacket) {
            inventory.deserializeNBT(compound.getCompound("pattern"));
        }
        configuredPattern = ItemStack.of(compound.getCompound("items"));
        /*String stateString = compound.getString("state");
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
        if (!clientPacket) {
            compound.put("pattern", inventory.serializeNBT());
            //compound.putBoolean("powered", isPowered());
        }
        compound.put("items", configuredPattern.serializeNBT());
        /*compound.putInt("countGraphiteRod", countGraphiteRod);
        compound.putInt("countUraniumRod", countUraniumRod);
        compound.putInt("graphiteTimer", graphiteTimer);
        compound.putInt("uraniumTimer", uraniumTimer);
        compound.putInt("heat", heat);
        compound.putString("state", powered.name());
        compound.put("screen_pattern", screen_pattern);

*/
        super.write(compound, clientPacket);
    }

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

    @Override
    public Storage<ItemVariant> getItemStorage(@Nullable Direction face) {
        return inventory;
    }

    public InteractionResult onClick(Player player, InteractionHand hand) {
        ItemStack heldItem = player.getItemInHand(hand);
        if (heldItem.is(CNItems.CONFIGURED_REACTOR_ITEM.get()) && !heldItem.isEmpty()) {
            if (configuredPattern.isEmpty()) {
                inventory.setStackInSlot(0, heldItem);
                configuredPattern = heldItem;
                //player.setItemInHand(hand, ItemStack.EMPTY);
            }
            CreateNuclear.LOGGER.warn(""+inventory.getStackInSlot(0).getOrCreateTag());
            notifyUpdate();
            return InteractionResult.SUCCESS;
        }
        else if (heldItem.isEmpty()) {
            if (configuredPattern.isEmpty()) {
                if (!level.isClientSide) {
                    if (player.addItem(configuredPattern)){
                        configuredPattern = ItemStack.EMPTY;
                        notifyUpdate();
                        return InteractionResult.CONSUME;
                    }
                    else {
                        CreateNuclear.LOGGER.warn("dddddddd");
                        player.setItemInHand(hand, configuredPattern);
                        inventory.setStackInSlot(0, ItemStack.EMPTY);
                        notifyUpdate();
                        return InteractionResult.CONSUME;
                    }
                    //return InteractionResult.FAIL;
                }
                else return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }
}