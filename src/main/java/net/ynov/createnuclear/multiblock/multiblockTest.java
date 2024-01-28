package net.ynov.createnuclear.multiblock;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.ynov.createnuclear.block.CNBlocks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
/*
public class multiblockTest {

    private List<BlockPos> queue = new ArrayList<>();
    public boolean build;

    private int delay = 5;

    public multiblockTest(){

    }
    public void read(CompoundTag nbt) {
        this.build = nbt.getBoolean("build");
        if(!this.build) {
            this.queue = readPosList(nbt, "queue_pos", new ArrayList<>());
        }
    }

    public void write(CompoundTag nbt) {
        nbt.putBoolean("build", this.build);
        if (!this.build) {
            writePosList(nbt, this.queue, "queue_pos");
        }
    }

    public boolean isDone(Level world){
        if (this.build) return true;
        else if(!this.queue.isEmpty()) {
            if (this.delay-- <= 0) {
                Iterator<BlockPos> itr = this.queue.iterator();
                while (itr.hasNext()) {
                    BlockPos pos = itr.next();
                    BlockState state = CNBlocks.REINFORCED_GLASS.getDefaultState();
                    if (!world.getBlockState(pos).canBeReplaced()) {
                        this.demolish(world);
                        return false;
                    }
                    world.setBlock(pos, state.setValue(CNBlocks.DEEPSLATE_URANIUM_ORE))
                }
            }
        }
    }


    private static List<BlockPos> readPosList(CompoundTag nbt, String key, ArrayList<BlockPos> list) {
        ListTag listNBT = nbt.getList(key, Tag.TAG_COMPOUND);
        for (int i = 0; listNBT.size(); i++){
            CompoundTag compound = listNBT.getCompound(i);
            list.add(readPos(compound, "Pos"));
        }
        return list;
    }

    private static void writePosList(CompoundTag nbt, ArrayList<BlockPos> list, String key) {
        ListTag listNBT = new ListTag();
        list.forEach(pos -> {
            CompoundTag compound = new CompoundTag();
            writePos(compound, pos, "Pos");
            listNBT.add(compound);
        });
        nbt.put(key, listNBT);
    }

    private static BlockPos readPos(CompoundTag nbt, String key) {
        return NbtUtils.readBlockPos(nbt.getCompound(key));
    }

    private static void writePos(CompoundTag nbt, BlockPos pos, String key) {
        nbt.put(key, NbtUtils.writeBlockPos(pos));
    }
}*/
