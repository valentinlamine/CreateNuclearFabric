package net.ynov.createnuclear.entity.irradiatedcat;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FurnaceBlock;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;

public class CatSitOnBlockGoal extends MoveToBlockGoal {
    private final IrradiatedCat cat;

    public CatSitOnBlockGoal(IrradiatedCat cat, double speedModifier) {
        super(cat, speedModifier, 8);
        this.cat = cat;
    }

    public boolean canUse() {
        return this.cat.isTame() && !this.cat.isOrderedToSit() && super.canUse();
    }

    public void start() {
        super.start();
        this.cat.setInSittingPose(false);
    }

    public void stop() {
        super.stop();
        this.cat.setInSittingPose(false);
    }

    public void tick() {
        super.tick();
        this.cat.setInSittingPose(this.isReachedTarget());
    }

    protected boolean isValidTarget(LevelReader level, BlockPos pos) {
        if (!level.isEmptyBlock(pos.above())) {
            return false;
        } else {
            BlockState blockState = level.getBlockState(pos);
            if (blockState.is(Blocks.CHEST)) {
                return ChestBlockEntity.getOpenCount(level, pos) < 1;
            } else {
                return blockState.is(Blocks.FURNACE) && (Boolean) blockState.getValue(FurnaceBlock.LIT) || blockState.is(BlockTags.BEDS, (blockStatex) -> {
                    return (Boolean) blockStatex.getOptionalValue(BedBlock.PART).map((bedPart) -> {
                        return bedPart != BedPart.HEAD;
                    }).orElse(true);
                });
            }
        }
    }
}