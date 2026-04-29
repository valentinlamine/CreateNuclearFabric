package net.nuclearteam.createnuclear.content.test;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.core.Direction;
import net.nuclearteam.createnuclear.CreateNuclear;
import net.nuclearteam.createnuclear.CNBlocks;
import net.nuclearteam.createnuclear.foundation.block.HorizontalDirectionalReactorBlock;

import java.util.*;

public class TestPropa {
    // Map levels to a set of ReactorController positions
    private static final Map<Level, Set<BlockPos>> controllers = new HashMap<>();

    public void onLoadWorld(LevelAccessor world) {
        if (world instanceof Level lvl) {
            controllers.putIfAbsent(lvl, new HashSet<>());
            CreateNuclear.LOGGER.warn("TestPropa: Loading World {}", lvl.dimension().location());
        }
    }

    public void onUnloadWorld(LevelAccessor world) {
        if (!(world instanceof Level lvl)) return;

        CreateNuclear.LOGGER.warn("TestPropa: Unloading World {} - will process {} controllers", lvl.dimension().location(), controllers.getOrDefault(lvl, Collections.emptySet()).size());

        Set<BlockPos> set = controllers.remove(lvl);
        if (set == null || set.isEmpty()) return;

        for (BlockPos pos : set) {
            try {
                if (!lvl.isAreaLoaded(pos, 0)) continue;
                BlockState state = lvl.getBlockState(pos);
                if (state.is(CNBlocks.REACTOR_CONTROLLER.get())) {
                    DirectionProperty facingProp = HorizontalDirectionalReactorBlock.FACING;
                    Direction current = state.getValue(facingProp);
                    Direction newDir = current.getOpposite(); // flip facing on unload
                    lvl.setBlock(pos, state.setValue(facingProp, newDir), 3);
                    CreateNuclear.LOGGER.warn("TestPropa: Changed facing at {} from {} to {}", pos, current, newDir);
                }
            } catch (Exception e) {
                CreateNuclear.LOGGER.warn("TestPropa: Error changing facing at {}: {}", pos, e.getMessage());
            }
        }
    }

    // Called by ReactorControllerBlock when it's placed/created
    public void registerController(Level level, BlockPos pos) {
        controllers.computeIfAbsent(level, l -> new HashSet<>()).add(pos);
        CreateNuclear.LOGGER.warn("TestPropa: Registered controller at {} in {}", pos, level.dimension().location());
    }

    // Called when controller is removed/destroyed
    public void unregisterController(Level level, BlockPos pos) {
        Set<BlockPos> set = controllers.get(level);
        if (set != null) {
            set.remove(pos);
            if (set.isEmpty()) controllers.remove(level);
            CreateNuclear.LOGGER.warn("TestPropa: Unregistered controller at {} in {}", pos, level.dimension().location());
        }
    }

    public Map<Long, String> getMap(SmartBlockEntity be) {
        Long id = 12L;
        Map<Long, String> getMap = new HashMap<>();
        getMap.put(id, "C'est un test");
        return getMap;
    }

    public Map<BlockState, BlockPos> getOrPos(SmartBlockEntity be) {
        BlockPos pos = be.getBlockPos();
        BlockState state = be.getBlockState();
        Map<BlockState, BlockPos> map = new HashMap<>();
        map.put(state, pos);
        return map;
    }
}
