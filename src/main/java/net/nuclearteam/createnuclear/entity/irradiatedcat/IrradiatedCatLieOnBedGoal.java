package net.nuclearteam.createnuclear.entity.irradiatedcat;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.LevelReader;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.EnumSet;

@ParametersAreNonnullByDefault
public class IrradiatedCatLieOnBedGoal extends MoveToBlockGoal {
    private final IrradiatedCat irradiatedCat;

    public IrradiatedCatLieOnBedGoal(IrradiatedCat irradiatedCat, double speedModifier, int searchRange) {
        super(irradiatedCat, speedModifier, searchRange, 6);
        this.irradiatedCat = irradiatedCat;
        this.verticalSearchStart = -2;
        this.setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE));
    }

    public boolean canUse() {
        return this.irradiatedCat.isTame() && !this.irradiatedCat.isOrderedToSit() && !this.irradiatedCat.isLying() && super.canUse();
    }

    public void start() {
        super.start();
        this.irradiatedCat.setInSittingPose(false);
    }

    protected int nextStartTick(PathfinderMob creature) {
        return 40;
    }

    public void stop() {
        super.stop();
        this.irradiatedCat.setLying(false);
    }

    public void tick() {
        super.tick();
        this.irradiatedCat.setInSittingPose(false);
        if (!this.isReachedTarget()) {
            this.irradiatedCat.setLying(false);
        } else if (!this.irradiatedCat.isLying()) {
            this.irradiatedCat.setLying(true);
        }

    }

    protected boolean isValidTarget(LevelReader level, BlockPos pos) {
        return level.isEmptyBlock(pos.above()) && level.getBlockState(pos).is(BlockTags.BEDS);
    }
}
