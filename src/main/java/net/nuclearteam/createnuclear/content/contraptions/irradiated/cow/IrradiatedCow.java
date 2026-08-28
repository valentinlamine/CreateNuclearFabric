package net.nuclearteam.createnuclear.content.contraptions.irradiated.cow;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.nuclearteam.createnuclear.CNEntityType;
import net.nuclearteam.createnuclear.CNItems;
import net.nuclearteam.createnuclear.content.contraptions.irradiated.AnimalUtil;

import javax.annotation.Nullable;

public class IrradiatedCow extends Animal {
    public IrradiatedCow(EntityType<? extends IrradiatedCow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    protected void initGoals() {
        this.goalSelector.add(0, new FloatGoal(this));
        this.goalSelector.add(1, new PanicGoal(this, 2.0D));
        this.goalSelector.add(2, new BreedGoal(this, 1.0D));
        this.goalSelector.add(3, new TemptGoal(this, 1.25D, Ingredient.of(Items.WHEAT), false));
        this.goalSelector.add(4, new FollowParentGoal(this, 1.25D));
        this.goalSelector.add(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.add(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.add(7, new RandomLookAroundGoal(this));
    }

    // Define the base food of the animal (e.g., Wheat for Cows)
    private static final Ingredient FOOD_ITEMS = Ingredient.of(CNItems.YELLOWCAKE);

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        // Check if the stack is a valid Yellowcake or the base food
        return AnimalUtil.isFood(stack, FOOD_ITEMS);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0D).add(Attributes.MOVEMENT_SPEED, (double)0.2F);
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_COW_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.ENTITY_COW_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_COW_DEATH;
    }

    protected void playStepSound(BlockPos pPos, BlockState pBlock) {
        this.playSound(SoundEvents.ENTITY_COW_STEP, 0.15F, 1.0F);
    }

    /**
     * Returns the volume for the sounds this mob makes.
     */
    protected float getSoundVolume() {
        return 0.4F;
    }

    public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        if (itemstack.is(Items.BUCKET) && !this.isBaby()) {
            pPlayer.playSound(SoundEvents.ENTITY_COW_MILK, 1.0F, 1.0F);
            ItemStack itemstack1 = ItemUtils.exchangeStack(itemstack, pPlayer, Items.MILK_BUCKET.getDefaultInstance());
            pPlayer.setStackInHand(pHand, itemstack1);
            return InteractionResult.success(this.level().isClientSide);
        } else {
            return super.mobInteract(pPlayer, pHand);
        }
    }

    @Nullable
    public IrradiatedCow createChild(ServerLevel pLevel, AgeableMob pOtherParent) {
        return CNEntityType.IRRADIATED_COW.create(pLevel);
    }

    protected float getActiveEyeHeight(Pose pPose, EntityDimensions pSize) {
        return this.isBaby() ? pSize.height * 0.95F : 1.3F;
    }
}
