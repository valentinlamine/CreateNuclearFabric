package net.ynov.createnuclear.block;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.ynov.createnuclear.CNMultiblock;
import net.ynov.createnuclear.CreateNuclear;
import net.ynov.createnuclear.energy.ReactorOutput;
import net.ynov.createnuclear.energy.ReactorOutputEntity;

import java.util.List;
import java.util.Objects;

public class ReactorController extends Block {
    public boolean destroyed = false;
    public boolean created = false;
    public int speed = 16; // This is the result speed of the reactor, change this to change the total capacity
    //public ReactorController controller;

    public ReactorController(Properties properties) {
        super(properties);
    }

    /*@Override
    protected void read(CompoundTag compound, boolean clientPacket) { //Permet de stocker les item 1/2

        super.read(compound, clientPacket);
    }

    @Override
    protected void write(CompoundTag compound, boolean clientPacket) { //Permet de stocker les item 2/2
        compound.put("Inventory", inventory.serializeNBT());
        super.write(compound, clientPacket);
    }*/

    // this is the Function that verifies if the pattern is correct (as a test, we added the energy output)
    public void Verify(BlockPos pos, Level level, List<? extends Player> players, boolean create){
            boolean structureFound = false;

            var result = CNMultiblock.REGISTRATE_MULTIBLOCK.findStructure(level, pos); // control the pattern
            if (result != null) { // the pattern is correct
                CreateNuclear.LOGGER.info("structure verified, SUCCESS to create multiblock");

                for (Player player : players) {
                    if (create && !created)
                    {
                        player.sendSystemMessage(Component.literal("WARNING : Reactor Assembled"));
                        created = true;
                        destroyed = false;
                    }
                }
                structureFound = true;
                ReactorOutputEntity.structure = true;
            }

            if (!structureFound) { // the pattern is incorrect
                CreateNuclear.LOGGER.info("structure not verified, FAILED to create multiblock");
                for (Player player : players) {
                    if (!create && !destroyed)
                    {
                        player.sendSystemMessage(Component.literal("CRITICAL : Reactor Destroyed"));
                        destroyed = true;
                        created = false;
                    }
                }
                ReactorOutputEntity.structure = false;
            }
            Rotate(pos.below(3), level, 0);
    }
    public void Rotate(BlockPos pos, Level level, int rotation) {
        if (level.getBlockState(pos).is(CNBlocks.REACTOR_OUTPUT.get())) {
            if (ReactorOutputEntity.structure) { // Starting the energy
                ReactorOutput block = (ReactorOutput) level.getBlockState(pos).getBlock();
                Objects.requireNonNull(block.getBlockEntityType().getBlockEntity(level, pos)).speed = rotation;
                Objects.requireNonNull(block.getBlockEntityType().getBlockEntity(level, pos)).updateSpeed = true;
                Objects.requireNonNull(block.getBlockEntityType().getBlockEntity(level, pos)).updateGeneratedRotation();
            } else { // stopping the energy
                ReactorOutput block = (ReactorOutput) level.getBlockState(pos).getBlock();
                Objects.requireNonNull(block.getBlockEntityType().getBlockEntity(level, pos)).speed = 0;
                Objects.requireNonNull(block.getBlockEntityType().getBlockEntity(level, pos)).updateSpeed = true;
                Objects.requireNonNull(block.getBlockEntityType().getBlockEntity(level, pos)).updateGeneratedRotation();
            }

            ReactorOutput block = (ReactorOutput) level.getBlockState(pos).getBlock();

            //CompoundTag compoundtag =
        }
    }
}
