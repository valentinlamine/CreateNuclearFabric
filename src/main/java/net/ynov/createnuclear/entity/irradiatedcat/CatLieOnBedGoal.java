package net.ynov.createnuclear.entity.irradiatedcat;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.LevelReader;

import java.util.EnumSet;

public class CatLieOnBedGoal extends MoveToBlockGoal {
    private final IrradiatedCat cat;

    public CatLieOnBedGoal(IrradiatedCat cat, double speedModifier, int searchRange) {
        super(cat, speedModifier, searchRange, 6);
        this.cat = cat;
        this.verticalSearchStart = -2;
        this.setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE));
    }

    public boolean canUse() {
        return this.cat.isTame() && !this.cat.isOrderedToSit() && !this.cat.isLying() && super.canUse();
    }

    public void start() {
        super.start();
        this.cat.setInSittingPose(false);
    }

    protected int nextStartTick(PathfinderMob creature) {
        return 40;
    }

    public void stop() {
        super.stop();
        this.cat.setLying(false);
    }

    public void tick() {
        super.tick();
        this.cat.setInSittingPose(false);
        if (!this.isReachedTarget()) {
            this.cat.setLying(false);
        } else if (!this.cat.isLying()) {
            this.cat.setLying(true);
        }

    }

    protected boolean isValidTarget(LevelReader level, BlockPos pos) {
        return level.isEmptyBlock(pos.above()) && level.getBlockState(pos).is(BlockTags.BEDS);
    }
}
