package net.nuclearteam.createnuclear.content.contraptions.irradiated.cat;

import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FurnaceBlock;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.tags.BlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;

public class CatSitOnBlockGoal extends MoveToBlockGoal {
    private final IrradiatedCat cat;

    public CatSitOnBlockGoal(IrradiatedCat cat, double speedModifier) {
        super(cat, speedModifier, 8);
        this.cat = cat;
    }

    public boolean canUse() {
        return this.cat.isTame() && !this.cat.isInSittingPose() && super.canUse();
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
        this.cat.setInSittingPose(this.hasReached());
    }

    @SuppressWarnings("null")
    protected boolean isTargetPos(LevelReader level, BlockPos pos) {
        if (!level.isAir(pos.up())) {
            return false;
        } else {
            BlockState blockState = level.getBlockState(pos);
            if (blockState.is(Blocks.CHEST)) {
                return ChestBlockEntity.getPlayersLookingInChestCount(level, pos) < 1;
            } else {
                return blockState.is(Blocks.FURNACE) && blockState.getValue(FurnaceBlock.LIT) || blockState.is(BlockTags.BEDS, (blockStates) ->
                        blockStates.getOrEmpty(BedBlock.PART)
                                .map((bedPart) -> bedPart != BedPart.HEAD)
                                .orElse(true));
            }
        }
    }
}
