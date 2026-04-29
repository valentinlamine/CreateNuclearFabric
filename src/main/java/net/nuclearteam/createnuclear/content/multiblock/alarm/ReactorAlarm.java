package net.nuclearteam.createnuclear.content.multiblock.alarm;

import com.simibubi.create.foundation.block.IBE;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.util.RandomSource;
import net.nuclearteam.createnuclear.CNBlockEntityTypes;
import net.nuclearteam.createnuclear.CNSoundEvents;

import java.util.HashMap;
import java.util.Map;

public class ReactorAlarm extends Block implements IBE<ReactorAlarmEntity> {
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    // Map pour suivre quelle alarme joue quel son (Côté Client uniquement)
    public static final Map<BlockPos, ReactorAlarmInstance> ACTIVE_SOUNDS = new HashMap<>();

    public ReactorAlarm(Properties props) {
        super(props);
        this.registerDefaultState(this.stateDefinition.any().setValue(POWERED, false));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(POWERED, ctx.getLevel().hasNeighborSignal(ctx.getClickedPos()));
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        if (level.isClientSide) return;
        boolean poweredNow = level.hasNeighborSignal(pos);
        if (state.getValue(POWERED) != poweredNow) {
            level.setBlock(pos, state.setValue(POWERED, poweredNow), 3);
        }
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (state.getValue(POWERED)) {
            // .immutable() crée une copie propre qui ne bougera plus
            BlockPos immutablePos = pos.immutable();
            if (!ACTIVE_SOUNDS.containsKey(immutablePos) || ACTIVE_SOUNDS.get(immutablePos).isStopped()) {
                startSound(level, immutablePos);
            }
        }
    }

    private void startSound(Level level, BlockPos pos) {
        ReactorAlarmInstance sound = new ReactorAlarmInstance(level, pos, CNSoundEvents.REACTOR_ALARM.getMainEvent());
        ACTIVE_SOUNDS.put(pos, sound);
        Minecraft.getInstance().getSoundManager().play(sound);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            if (level.isClientSide) {
                stopSound(pos);
            }
            super.onRemove(state, level, pos, newState, isMoving);
        }
    }

    public static void stopSound(BlockPos pos) {
        ReactorAlarmInstance sound = ACTIVE_SOUNDS.get(pos);
        if (sound != null) {
            net.minecraft.client.Minecraft.getInstance().getSoundManager().stop(sound);
            ACTIVE_SOUNDS.remove(pos);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWERED);
    }

    // Indispensable pour l'interface IBE de Create
    @Override
    public Class<ReactorAlarmEntity> getBlockEntityClass() {
        return ReactorAlarmEntity.class;
    }

    @Override
    public BlockEntityType<? extends ReactorAlarmEntity> getBlockEntityType() {
        return CNBlockEntityTypes.REACTOR_ALARM.get(); // Assure-toi que c'est bien enregistré ici
    }
}