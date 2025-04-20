package net.nuclearteam.createnuclear.multiblock.controller;

import com.mojang.authlib.minecraft.client.ObjectMapper;
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
import net.fabricmc.fabric.api.transfer.v1.storage.base.SidedStorageBlockEntity;
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
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.nuclearteam.createnuclear.CreateNuclear;
import net.nuclearteam.createnuclear.block.CNBlocks;
import net.nuclearteam.createnuclear.config.CNConfigs;
import net.nuclearteam.createnuclear.gui.CNIconButton;
import net.nuclearteam.createnuclear.item.CNItems;
import net.nuclearteam.createnuclear.multiblock.IHeat;
import net.nuclearteam.createnuclear.multiblock.output.ReactorOutput;
import net.nuclearteam.createnuclear.multiblock.output.ReactorOutputEntity;
import net.nuclearteam.createnuclear.multiblock.input.ReactorInputEntity;
import static net.nuclearteam.createnuclear.multiblock.blueprint.ReactorBluePrint.getItemStorage;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.nuclearteam.createnuclear.CNMultiblock.*;
import static net.nuclearteam.createnuclear.multiblock.controller.ReactorControllerBlock.ASSEMBLED;

public class ReactorControllerBlockEntity extends SmartBlockEntity implements IInteractionChecker, SidedStorageBlockEntity, IHaveGoggleInformation {
    public boolean destroyed = false;
    public boolean created = false;
    public boolean test = true;
    //public int speed = 16; // This is the result speed of the reactor, change this to change the total capacity

    public boolean sendUpdate;

    public ReactorControllerBlock controller;

    public ReactorControllerInventory inventory;

    //private boolean powered;
    public State powered = State.OFF;
    public float reactorPower;
    public float lastReactorPower;
    public int countUraniumRod;
    public int countGraphiteRod;
    int overFlowHeatTimer = 0;
    int overFlowLimiter = 30;
    double overHeat = 0;
    public int baseUraniumHeat = CNConfigs.common().rods.baseValueUranium.get();
    public int baseGraphiteHeat = CNConfigs.common().rods.baseValueGraphite.get();
    public int proximityUraniumHeat = CNConfigs.common().rods.BoProxiUranium.get();
    public int proximityGraphiteHeat = -CNConfigs.common().rods.MaProxigraphite.get();
    public int maxUraniumPerGraphite = CNConfigs.common().rods.uraMaxGraph.get();
    public int graphiteTimer = CNConfigs.common().rods.graphiteRodLifetime.get();
    public int uraniumTimer = CNConfigs.common().rods.uraniumRodLifetime.get();
    public int heat;
    public double total;
    public CompoundTag screen_pattern = new CompoundTag();
    private List<CNIconButton> switchButtons;
    public ItemStack configuredPattern;

    private ItemStack fuelItem;
    private ItemStack coolerItem;

    private final int[][] formattedPattern = new int[][]{
            {99,99,99,0,1,2,99,99,99},
            {99,99,3,4,5,6,7,99,99},
            {99,8,9,10,11,12,13,14,99},
            {15,16,17,18,19,20,21,22,23},
            {24,25,26,27,28,29,30,31,32},
            {33,34,35,36,37,38,39,40,41},
            {99,42,43,44,45,46,47,48,99},
            {99,99,49,50,51,52,53,99,99},
            {99,99,99,54,55,56,99,99,99}
    };
    private final int[][] offsets = { {1, 0}, {-1, 0}, {0, 1}, {0, -1} };



    public ReactorControllerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        inventory = new ReactorControllerInventory(this);
        configuredPattern = ItemStack.EMPTY;
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {

    }

    public boolean getAssembled() { // permet de savoir si le réacteur est assemblé ou pas.
        BlockState state = getBlockState();
        return Boolean.TRUE.equals(state.getValue(ASSEMBLED));
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        if(!configuredPattern.getOrCreateTag().isEmpty()) {
            tooltip.add(componentSpacing.plainCopy().append(Lang.translateDirect("gui.gauge.info_header")));
            IHeat.HeatLevel.getName("reactor_controller").style(ChatFormatting.GRAY).forGoggles(tooltip);

            IHeat.HeatLevel.getFormattedHeatText(configuredPattern.getOrCreateTag().getInt("heat")).forGoggles(tooltip);

            if (fuelItem.isEmpty()) {
                // if rod empty we initialize it at 1 (and display it as 0) to avoid having air item displayed instead of the rod
                IHeat.HeatLevel.getFormattedItemText(new ItemStack(CNItems.URANIUM_ROD.asItem(), 1), true).forGoggles(tooltip);
            } else {
                IHeat.HeatLevel.getFormattedItemText(fuelItem, false).forGoggles(tooltip);
            }

            if (fuelItem.isEmpty()) {
                // if rod empty we initialize it at 1 (and display it as 0) to avoid having air item displayed instead of the rod
                IHeat.HeatLevel.getFormattedItemText(new ItemStack(CNItems.GRAPHITE_ROD.asItem(), 1), true).forGoggles(tooltip);
            } else {
                IHeat.HeatLevel.getFormattedItemText(coolerItem, false).forGoggles(tooltip);
            }
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
        if (ItemStack.of(compound.getCompound("cooler")) != null || ItemStack.of(compound.getCompound("fuel")) != null) {
            coolerItem = ItemStack.of(compound.getCompound("cooler"));
            fuelItem = ItemStack.of(compound.getCompound("fuel"));

        }
        total = compound.getDouble("total");
        super.read(compound, clientPacket);
    }

    @Override
    protected void write(CompoundTag compound, boolean clientPacket) { //Permet de stocker les item 2/2
        if (!clientPacket) {
            compound.put("pattern", inventory.serializeNBT());
            //compound.putBoolean("powered", isPowered());
        }
        compound.put("items", configuredPattern.serializeNBT());

        if (coolerItem != null || fuelItem != null) {
            compound.put("cooler", coolerItem.serializeNBT());
            compound.put("fuel", fuelItem.serializeNBT());
        }

        compound.putDouble("total", calculateProgress());
        super.write(compound, clientPacket);
    }

    public enum State {
        ON, OFF;
    }

    private void explodeReactorCore(Level level, BlockPos pos) {
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    BlockPos currentPos = pos.offset(x, y, z);
                    //le problème viens de la il ne rentre pas dans le if
                    if (level.getBlockState(currentPos).is(CNBlocks.REACTOR_CORE.get())) {
                        // Create and execute the explosion
                        Explosion explosion = new Explosion(level, null, currentPos.getX(), currentPos.getY(), currentPos.getZ(), 4.0F, false, Explosion.BlockInteraction.DESTROY);
                        explosion.explode();
                        explosion.finalizeExplosion(true);
                    }
                }
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (level.isClientSide)
            return;


        if (isEmptyConfiguredPattern()) {

            StorageProvider<ItemVariant> storage = StorageProvider.createForItems(level, getBlockPosForReactor('I'));

            if (storage.findBlockEntity() instanceof ReactorInputEntity be) {
                CompoundTag tag = be.serializeNBT();
                ListTag inventoryTag = tag.getCompound("Inventory").getList("Items", Tag.TAG_COMPOUND);
                fuelItem = ItemStack.of(inventoryTag.getCompound(0));
                coolerItem = ItemStack.of(inventoryTag.getCompound(1));
                if (fuelItem.getCount() > 0 && coolerItem.getCount() > 0) {
                    configuredPattern.getOrCreateTag().putDouble("heat", calculateHeat(tag));
                    if (updateTimers()) {
                        TransferUtil.extract(be.inventory, ItemVariant.of(fuelItem), 1);
                        TransferUtil.extract(be.inventory, ItemVariant.of(coolerItem), 1);
                        total = calculateProgress();

                        int heat = (int) configuredPattern.getOrCreateTag().getDouble("heat");

                        if (IHeat.HeatLevel.of(heat) == IHeat.HeatLevel.SAFETY || IHeat.HeatLevel.of(heat) == IHeat.HeatLevel.CAUTION || IHeat.HeatLevel.of(heat) == IHeat.HeatLevel.WARNING) {
                            //j'ai divisé la chaleur par 4, car maintenant on a mis la chaleur sur 1000 et non plus sur 200 en ayant rajouté 1/5 de bonus
                            this.rotate(getBlockState(), new BlockPos(getBlockPos().getX(), getBlockPos().getY() + FindController('O').getY(), getBlockPos().getZ()), getLevel(), heat/4);
                        } else {
                            this.rotate(getBlockState(), new BlockPos(getBlockPos().getX(), getBlockPos().getY() + FindController('O').getY(), getBlockPos().getZ()), getLevel(), 0);
                        }
                        return;
                    }
                } else {
                    this.rotate(getBlockState(), new BlockPos(getBlockPos().getX(), getBlockPos().getY() + FindController('O').getY(), getBlockPos().getZ()), getLevel(), 0);
                }

                this.notifyUpdate();
            }

        }
    }

    private boolean isEmptyConfiguredPattern() {
        return !configuredPattern.isEmpty() || !configuredPattern.getOrCreateTag().isEmpty();
    }

    private boolean updateTimers() {

        double constTotal = calculateProgress();

        total -= 10;
        return total <= 0;//(total/constTotal) <= 0;
    }

    private double calculateProgress() {
        countGraphiteRod = configuredPattern.getOrCreateTag().getInt("countGraphiteRod");
        countUraniumRod = configuredPattern.getOrCreateTag().getInt("countUraniumRod");
        graphiteTimer = configuredPattern.getOrCreateTag().getInt("graphiteTime");
        uraniumTimer = configuredPattern.getOrCreateTag().getInt("uraniumTime");

        double totalGraphiteRodLife = (double) graphiteTimer / countGraphiteRod;
        double totalUraniumRodLife = (double) uraniumTimer / countUraniumRod;

        return totalGraphiteRodLife + totalUraniumRodLife;
    }

    private double calculateHeat(CompoundTag tag) {
        countGraphiteRod = configuredPattern.getOrCreateTag().getInt("countGraphiteRod");
        countUraniumRod = configuredPattern.getOrCreateTag().getInt("countUraniumRod");
        heat = 0;

        // if more than maxUraniumPerGraphite of the rods are uranium, the reactor will overheat
        if (countUraniumRod > countGraphiteRod * maxUraniumPerGraphite) {
            overFlowHeatTimer++;
            if (overFlowHeatTimer >= overFlowLimiter) {
                overHeat+=1;
                overFlowHeatTimer= 0;
                if (overFlowLimiter > 1) {
                    overFlowLimiter -= 1;
                }
            }
        } else {
            overFlowHeatTimer = 0;
            overFlowLimiter = 30;
            if (overHeat > 0) {
                overHeat -= 2;
            } else {
                overHeat = 0;
            }
        }
        // the offsets for the four directions (down, up, right, left) is int[][] offsets = { {1, 0}, {-1, 0}, {0, 1}, {0, -1} }; (defined at the top of the class)
        String currentRod = "";
        ListTag list = inventory.getStackInSlot(0).getOrCreateTag().getCompound("pattern").getList("Items", Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            if (list.getCompound(i).getString("id").equals("createnuclear:uranium_rod")) {
                heat += baseUraniumHeat;
                currentRod = "u";
            } else if (list.getCompound(i).getString("id").equals("createnuclear:graphite_rod")) {
                heat += baseGraphiteHeat;
                currentRod = "g";
            }
            for (int j = 0; j < formattedPattern.length; j++) {
                for (int k = 0; k < formattedPattern[j].length; k++) {
                    // Skip if the current pattern value is 99
                    if (formattedPattern[j][k] == 99) continue;

                    // Check if the current slot matches the pattern
                    if (list.getCompound(i).getInt("Slot") != formattedPattern[j][k]) continue;

                    // For each neighbor (up, down, right, left)
                    for (int[] offset : offsets) {
                        int nj = j + offset[0];
                        int nk = k + offset[1];

                        // Check if the indices are within the array boundaries
                        if (nj < 0 || nj >= formattedPattern.length || nk < 0 || nk >= formattedPattern[j].length)
                            continue;

                        int neighborSlot = formattedPattern[nj][nk];

                        // Loop through the list to find the neighbor slot
                        for (int l = 0; l < list.size(); l++) {
                            if (list.getCompound(l).getInt("Slot") == neighborSlot) {
                                // If currentRod equals "u", apply the corresponding heat
                                if (currentRod.equals("u")) {
                                    String id = list.getCompound(l).getString("id");
                                    if (id.equals("createnuclear:uranium_rod")) {
                                        heat += proximityUraniumHeat;
                                    } else if (id.equals("createnuclear:graphite_rod")) {
                                        heat += proximityGraphiteHeat;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return heat + overHeat;
    }


    private BlockPos getBlockPosForReactor(char character) {
        BlockPos pos = FindController(character);
        BlockPos posController = getBlockPos();
        BlockPos posInput = new BlockPos(posController.getX(), posController.getY(), posController.getZ());

        int[][] directions = {
                {0,0, pos.getX()}, // NORTH
                {0,0, -pos.getX()}, // SOUTH
                {-pos.getX(),0,0}, // EAST
                {pos.getX(),0,0} // WEST
        };


        for (int[] direction : directions) {
            BlockPos newPos = posController.offset(direction[0], direction[1], direction[2]);
            if (level.getBlockState(newPos).is(CNBlocks.REACTOR_INPUT.get())) {
                posInput = newPos;
                break;
            }
        }

        return posInput;
    }

    private CompoundTag convertePattern(CompoundTag compoundTag) {
        ListTag pattern = compoundTag.getList("Items", Tag.TAG_COMPOUND);

        return null;
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
                .where('B', a -> a.getState().is(CNBlocks.REACTOR_FRAME.get()))
                .where('C', a -> a.getState().is(CNBlocks.REACTOR_CORE.get()))
                .where('D', a -> a.getState().is(CNBlocks.REACTOR_COOLER.get()))
                .where('*', a -> a.getState().is(CNBlocks.REACTOR_CONTROLLER.get()))
                .where('O', a -> a.getState().is(CNBlocks.REACTOR_OUTPUT.get()))
                .where('I', a -> a.getState().is(CNBlocks.REACTOR_INPUT.get()))
                .getDistanceController(character);
    }

    public void rotate(BlockState state, BlockPos pos, Level level, int rotation) {
        if (level.getBlockState(pos).is(CNBlocks.REACTOR_OUTPUT.get()) && rotation > 0) {
            if (level.getBlockState(pos).getBlock() instanceof ReactorOutput) {
                ReactorOutput block = (ReactorOutput) level.getBlockState(pos).getBlock();
                ReactorOutputEntity entity = block.getBlockEntityType().getBlockEntity(level, pos);
                if (state.getValue(ASSEMBLED)) { // Starting the energy
                    entity.speed = rotation;
                    entity.heat = rotation;
                    entity.updateSpeed = true;
                    entity.updateGeneratedRotation();
                } else { // stopping the energy
                    entity.speed = 0;
                    entity.heat = 0;
                    entity.updateSpeed = true;
                    entity.updateGeneratedRotation();
                }
                entity.setSpeed(rotation);

            }
        }
        else {
            if (level.getBlockState(pos).getBlock() instanceof ReactorOutput) {
                ReactorOutput block = (ReactorOutput) level.getBlockState(pos).getBlock();
                ReactorOutputEntity entity = block.getBlockEntityType().getBlockEntity(level, pos);
                entity.setSpeed(0);
                entity.heat = 0;
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
        if (heldItem.is(CNItems.REACTOR_BLUEPRINT.get()) && !heldItem.isEmpty()) {
            if (configuredPattern.isEmpty()) {
                inventory.setStackInSlot(0, heldItem);
                configuredPattern = heldItem;
                //player.setItemInHand(hand, ItemStack.EMPTY);
            }
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