package net.nuclearteam.createnuclear.content.multiblock.controller;

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
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.nuclearteam.createnuclear.CNBlocks;
import net.nuclearteam.createnuclear.CNItems;
import net.nuclearteam.createnuclear.CNTags;
import net.nuclearteam.createnuclear.content.multiblock.CNMultiblock;
import net.nuclearteam.createnuclear.content.multiblock.IHeat;
import net.nuclearteam.createnuclear.content.multiblock.input.ReactorInputEntity;
import net.nuclearteam.createnuclear.content.multiblock.output.ReactorOutput;
import net.nuclearteam.createnuclear.content.multiblock.output.ReactorOutputEntity;
import net.nuclearteam.createnuclear.infrastructure.config.CNConfigs;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static net.nuclearteam.createnuclear.content.multiblock.CNMultiblock.*;
import static net.nuclearteam.createnuclear.content.multiblock.controller.ReactorControllerBlock.ASSEMBLED;


@SuppressWarnings({"unused", "deprecation", "ConstantConditions"})
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
    public int heat;
    public double total;
    public CompoundTag screen_pattern = new CompoundTag();
    public ItemStack configuredPattern;

    private ItemStack fuelItem;
    private ItemStack coolerItem;

    private ReactorData reactorData;


    public ReactorControllerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        inventory = new ReactorControllerInventory(this);
        configuredPattern = ItemStack.EMPTY;
        fuelItem = ItemStack.EMPTY;
        coolerItem = ItemStack.EMPTY;

        this.reactorData = new ReactorData();
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {

    }

    public boolean getAssembled() {
        BlockState state = getBlockState();
        return state.getValue(ASSEMBLED);
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        if(!configuredPattern.getOrCreateTag().isEmpty()) {
            tooltip.add(componentSpacing.plainCopy().append(Lang.translateDirect("gui.gauge.info_header")));
            IHeat.HeatLevel.getName("reactor_controller").style(ChatFormatting.GRAY).forGoggles(tooltip);

            IHeat.HeatLevel.getFormattedHeatText(configuredPattern.getOrCreateTag().getInt("heat")).forGoggles(tooltip);

            if (fuelItem == null || fuelItem.isEmpty()) {
                // if rod empty we initialize it at 1 (and display it as 0) to avoid having air item displayed instead of the rod
                IHeat.HeatLevel.getFormattedItemText(new ItemStack(CNItems.URANIUM_ROD.asItem(), 1), true).forGoggles(tooltip);
            } else {
                IHeat.HeatLevel.getFormattedItemText(fuelItem, false).forGoggles(tooltip);
            }

            if (coolerItem == null || coolerItem.isEmpty()) {
                // if rod empty we initialize it at 1 (and display it as 0) to avoid having air item displayed instead of the rod
                IHeat.HeatLevel.getFormattedItemText(new ItemStack(CNItems.GRAPHITE_ROD.asItem(), 1), true).forGoggles(tooltip);
            } else {
                IHeat.HeatLevel.getFormattedItemText(coolerItem, false).forGoggles(tooltip);
            }
        }

        return true;
    }


    // (Si les methode read et write ne sont pas implémenté alors lorsque l'on relance le monde minecraft les items dans le composant auront disparu !)
    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        if (!clientPacket) {
            inventory.deserializeNBT(compound.getCompound("pattern"));
        }

        configuredPattern = ItemStack.of(compound.getCompound("items"));
        this.reactorData.read(compound);

        // Initialize with empty stacks if not present in compound
        coolerItem = compound.contains("cooler") ? ItemStack.of(compound.getCompound("cooler")) : ItemStack.EMPTY;
        fuelItem = compound.contains("fuel") ? ItemStack.of(compound.getCompound("fuel")) : ItemStack.EMPTY;

        total = compound.getDouble("total");

        super.read(compound, clientPacket);
    }

    @Override
    protected void write(CompoundTag compound, boolean clientPacket) {
        if (!clientPacket) {
            compound.put("pattern", inventory.serializeNBT());
        }

        compound.put("items", configuredPattern.serializeNBT());
        this.reactorData.write(compound);

        // Always write cooler and fuel items, even if they're empty
        compound.put("cooler", (coolerItem != null ? coolerItem : ItemStack.EMPTY).serializeNBT());
        compound.put("fuel", (fuelItem != null ? fuelItem : ItemStack.EMPTY).serializeNBT());

        compound.putDouble("total", this.reactorData.calculateProgress(configuredPattern.getOrCreateTag()));

        super.write(compound, clientPacket);
    }

    public enum State {
        ON, OFF
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
                if (fuelItem != null && coolerItem != null && fuelItem.getCount() > 0 && coolerItem.getCount() > 0) {
                    configuredPattern.getOrCreateTag().putDouble("heat", this.reactorData.calculateHeat(tag));
                    if (this.reactorData.updateTimers()) {
                        TransferUtil.extract(be.inventory, ItemVariant.of(fuelItem), 1);
                        TransferUtil.extract(be.inventory, ItemVariant.of(coolerItem), 1);
                        total = this.reactorData.calculateProgress(configuredPattern.getOrCreateTag());

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
        return CNMultiblock.getPatternBuilder().getDistanceController(character);
    }

    public void rotate(BlockState state, BlockPos pos, Level level, int rotation) {
        if (level.getBlockState(pos).is(CNBlocks.REACTOR_OUTPUT.get()) && rotation > 0) {
            if (level.getBlockState(pos).getBlock() instanceof ReactorOutput block) {
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
            if (level.getBlockState(pos).getBlock() instanceof ReactorOutput block) {
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