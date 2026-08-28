package net.nuclearteam.createnuclear.content.contraptions.irradiated.wolf;

import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;

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
        this.begTargeting = TargetingConditions.createNonAttackable().setBaseMaxDistance(lookDistance);
        this.setControls(EnumSet.of(Control.LOOK));
    }

    public boolean canUse() {
        this.player = this.level.getClosestPlayer(this.begTargeting, this.wolf);
        return this.player != null && this.playerHoldingInteresting(this.player);
    }

    public boolean shouldContinue() {
        if (!this.player.isAlive()) {
            return false;
        } else if (this.wolf.squaredDistanceTo(this.player) > (double)(this.lookDistance * this.lookDistance)) {
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
        this.wolf.getLookControl().lookAt(this.player.getX(), this.player.getEyeY(), this.player.getZ(), 10.0F, (float)this.wolf.getMaxLookPitchChange());
        --this.lookTime;
    }

    private boolean playerHoldingInteresting(Player player) {
        InteractionHand[] var2 = InteractionHand.values();

        for (InteractionHand interactionHand : var2) {
            ItemStack itemStack = player.getItemInHand(interactionHand);
            if (this.wolf.isTame() && itemStack.is(Items.BONE)) {
                return true;
            }

            if (this.wolf.isBreedingItem(itemStack)) {
                return true;
            }
        }

        return false;
    }
}
