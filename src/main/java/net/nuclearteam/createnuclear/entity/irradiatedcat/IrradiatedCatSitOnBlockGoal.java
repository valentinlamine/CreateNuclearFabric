package net.nuclearteam.createnuclear.entity.irradiatedcat;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FurnaceBlock;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;

public class IrradiatedCatSitOnBlockGoal extends MoveToBlockGoal {
    private final IrradiatedCat irradiatedCat;

    public IrradiatedCatSitOnBlockGoal(IrradiatedCat irradiatedCat, double speedModifier) {
        super(irradiatedCat, speedModifier, 8);
        this.irradiatedCat = irradiatedCat;
    }

    public boolean canUse() {
        return this.irradiatedCat.isTame() && !this.irradiatedCat.isOrderedToSit() && super.canUse();
    }

    public void start() {
        super.start();
        this.irradiatedCat.setInSittingPose(false);
    }

    public void stop() {
        super.stop();
        this.irradiatedCat.setInSittingPose(false);
    }

    public void tick() {
        super.tick();
        this.irradiatedCat.setInSittingPose(this.isReachedTarget());
    }

    protected boolean isValidTarget(LevelReader level, BlockPos pos) {
        if (!level.isEmptyBlock(pos.above())) {
            return false;
        } else {
            BlockState blockState = level.getBlockState(pos);
            if (blockState.is(Blocks.CHEST)) {
                return ChestBlockEntity.getOpenCount(level, pos) < 1;
            } else {
                return blockState.is(Blocks.FURNACE) && blockState.getValue(FurnaceBlock.LIT) || blockState.is(BlockTags.BEDS, (blockStates) ->
                        blockStates.getOptionalValue(BedBlock.PART)
                                .map((bedPart) -> bedPart != BedPart.HEAD)
                                .orElse(true));
            }
        }
    }
}