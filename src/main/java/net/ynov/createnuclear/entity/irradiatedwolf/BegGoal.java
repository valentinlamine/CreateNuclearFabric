package net.ynov.createnuclear.entity.irradiatedwolf;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class BegGoal extends Goal {
    private final IrradiatedWolf wolf;
    @Nullable
    private Player player;
    private final Level level;
    private final float lookDistance;
    private int lookTime;
    private final TargetingConditions begTargeting;

    public BegGoal(IrradiatedWolf wolf, float lookDistance) {
        this.wolf = wolf;
        this.level = wolf.level();
        this.lookDistance = lookDistance;
        this.begTargeting = TargetingConditions.forNonCombat().range((double)lookDistance);
        this.setFlags(EnumSet.of(Flag.LOOK));
    }

    public boolean canUse() {
        this.player = this.level.getNearestPlayer(this.begTargeting, this.wolf);
        return this.player != null && this.playerHoldingInteresting(this.player);
    }

    public boolean canContinueToUse() {
        if (!this.player.isAlive()) {
            return false;
        } else if (this.wolf.distanceToSqr(this.player) > (double)(this.lookDistance * this.lookDistance)) {
            return false;
        } else {
            return this.lookTime > 0 && this.playerHoldingInteresting(this.player);
        }
    }

    public void start() {
        this.wolf.setIsInterested(true);
        this.lookTime = this.adjustedTickDelay(40 + this.wolf.getRandom().nextInt(40));
    }

    public void stop() {
        this.wolf.setIsInterested(false);
        this.player = null;
    }

    public void tick() {
        this.wolf.getLookControl().setLookAt(this.player.getX(), this.player.getEyeY(), this.player.getZ(), 10.0F, (float)this.wolf.getMaxHeadXRot());
        --this.lookTime;
    }

    private boolean playerHoldingInteresting(Player player) {
        InteractionHand[] var2 = InteractionHand.values();
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            InteractionHand interactionHand = var2[var4];
            ItemStack itemStack = player.getItemInHand(interactionHand);
            if (this.wolf.isTame() && itemStack.is(Items.BONE)) {
                return true;
            }

            if (this.wolf.isFood(itemStack)) {
                return true;
            }
        }

        return false;
    }
}
