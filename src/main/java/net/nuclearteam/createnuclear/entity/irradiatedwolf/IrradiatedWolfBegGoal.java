package net.nuclearteam.createnuclear.entity.irradiatedwolf;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.EnumSet;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class IrradiatedWolfBegGoal extends Goal {
    private final IrradiatedWolf irradiatedWolf;
    private Player player;
    private final Level level;
    private final float lookDistance;
    private int lookTime;
    private final TargetingConditions begTargeting;

    public IrradiatedWolfBegGoal(IrradiatedWolf irradiatedWolf, float lookDistance) {
        this.irradiatedWolf = irradiatedWolf;
        this.level = irradiatedWolf.level();
        this.lookDistance = lookDistance;
        this.begTargeting = TargetingConditions.forNonCombat().range(lookDistance);
        this.setFlags(EnumSet.of(Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        this.player = this.level.getNearestPlayer(this.begTargeting, this.irradiatedWolf);
        return this.player != null && playerHoldingInteresting(this.player);
    }

    @Override
    public boolean canContinueToUse() {
        if (this.player == null || !this.player.isAlive()) {
            return false;
        }
        if (this.irradiatedWolf.distanceToSqr(this.player) > lookDistance * lookDistance) {
            return false;
        }
        return this.lookTime > 0 && playerHoldingInteresting(this.player);
    }

    @Override
    public void start() {
        this.irradiatedWolf.setIsInterested(true);
        this.lookTime = this.adjustedTickDelay(40 + this.irradiatedWolf.getRandom().nextInt(40));
    }

    @Override
    public void stop() {
        this.irradiatedWolf.setIsInterested(false);
        this.player = null;
    }

    @Override
    public void tick() {
        if (this.player != null) {
            this.irradiatedWolf.getLookControl().setLookAt(this.player.getX(), this.player.getEyeY(), this.player.getZ(), 10.0F, (float)this.irradiatedWolf.getMaxHeadXRot());
        }
        --this.lookTime;
    }

    private boolean playerHoldingInteresting(final Player player) {
        for (final InteractionHand hand : InteractionHand.values()) {
            final ItemStack itemStack = player.getItemInHand(hand);
            if ((this.irradiatedWolf.isTame() && itemStack.is(Items.BONE)) || this.irradiatedWolf.isFood(itemStack)) {
                return true;
            }
        }
        return false;
    }
}
