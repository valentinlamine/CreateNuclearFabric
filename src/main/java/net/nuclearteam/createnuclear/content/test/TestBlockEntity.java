package net.nuclearteam.createnuclear.content.test;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.nuclearteam.createnuclear.CreateNuclear;

import java.util.List;

public class TestBlockEntity extends SmartBlockEntity {
    public TestBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
    }

    @Override
    public void tick() {
        super.tick();

        CreateNuclear.LOGGER.info("\n TestBlockEntity.tick Map: {}, {} \n TestBlockEntity.tick Pos: {}"
                , CreateNuclear.TEST_PROPA.getMap(this), CreateNuclear.TEST_PROPA.getMap(this).get(12L)
                , CreateNuclear.TEST_PROPA.getOrPos(this));

    }
}
