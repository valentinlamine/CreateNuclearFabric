package net.nuclearteam.createnuclear.entity.irradiatedchicken;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NonTameRandomTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import net.nuclearteam.createnuclear.effects.CNEffects;
import net.nuclearteam.createnuclear.entity.CNMobEntityType;

import net.nuclearteam.createnuclear.entity.irradiatedwolf.IrradiatedWolf;
import net.nuclearteam.createnuclear.tags.CNTag;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.function.Predicate;

public class IrradiatedChicken extends TamableAnimal implements NeutralMob {
    public static final Predicate<LivingEntity> PREY_SELECTOR;

    private static final Ingredient FOOD_ITEMS = Ingredient.of(
            Items.WHEAT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS,
            Items.BEETROOT_SEEDS, Items.TORCHFLOWER_SEEDS, Items.PITCHER_POD
    );

    private static final EntityDataAccessor<Integer> DATA_REMAINING_ANGER_TIME;
    private static final UniformInt PERSISTENT_ANGER_TIME;
    private static final EntityDataAccessor<Boolean> DATA_INTERESTED_ID;
    private UUID persistentAngerTarget;

    public float flap;
    public float flapSpeed;
    public float oFlapSpeed;
    public float oFlap;
    public float flapping = 1.0F;
    private float nextFlap = 1.0F;
    public boolean isChickenJockey;

    public IrradiatedChicken(EntityType<? extends IrradiatedChicken> entityType, Level level) {
        super(entityType, level);
        //this.eggTime = this.random.nextInt(6000) + 6000;
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
    }

    static {
        DATA_REMAINING_ANGER_TIME = SynchedEntityData.defineId(IrradiatedChicken.class, EntityDataSerializers.INT);
        PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
        PREY_SELECTOR = (entity) -> {
            EntityType<?> entityType = entity.getType();
            return !CNTag.EntityTypeTags.IRRADIATED_IMMUNE.matches(entityType);
        };
        DATA_INTERESTED_ID = SynchedEntityData.defineId(IrradiatedChicken.class, EntityDataSerializers.BOOLEAN);

    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_INTERESTED_ID, false);
        //this.entityData.define(DATA_COLLAR_COLOR, DyeColor.RED.getId());
        this.entityData.define(DATA_REMAINING_ANGER_TIME, 0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.4));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.0, FOOD_ITEMS, false));
        this.goalSelector.addGoal(5, new MeleeAttackGoal(this, 1.0, true));
        this.goalSelector.addGoal(4, new EvilIrradiationChickenAttackGoal(this));
        this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, PathfinderMob.class, false, PREY_SELECTOR));
        //this.goalSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Animal.class, false, PREY_SELECTOR));
        this.goalSelector.addGoal(5, new FollowParentGoal(this, 1.1));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));


    }

    public void setIsInterested(boolean isInterested) {
        this.entityData.set(DATA_INTERESTED_ID, isInterested);
    }

    public boolean isInterested() {
        return (Boolean)this.entityData.get(DATA_INTERESTED_ID);
    }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
        return this.isBaby() ? dimensions.height * 0.85F : dimensions.height * 0.92F;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 4.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.ATTACK_DAMAGE, 2.0);
    }
    @Override
    public void aiStep() {
        super.aiStep();
        this.oFlap = this.flap;
        this.oFlapSpeed = this.flapSpeed;
        this.flapSpeed += (this.onGround() ? -1.0F : 4.0F) * 0.3F;
        this.flapSpeed = Mth.clamp(this.flapSpeed, 0.0F, 1.0F);
        if (!this.onGround() && this.flapping < 1.0F) {
            this.flapping = 1.0F;
        }

        this.flapping *= 0.9F;
        Vec3 vec3 = this.getDeltaMovement();
        if (!this.onGround() && vec3.y < 0.0) {
            this.setDeltaMovement(vec3.multiply(1.0, 0.6, 1.0));
        }

        if (!this.level().isClientSide) {
            this.updatePersistentAnger((ServerLevel)this.level(), true);
        }

        this.flap += this.flapping * 2.0F;
    }
    @Override
    public boolean doHurtTarget(Entity target) {
        if (super.doHurtTarget(target)) {
            if (target instanceof LivingEntity) {
                int i = 0;
                if (this.level().getDifficulty() == Difficulty.NORMAL) {
                    i = 7;
                } else if (this.level().getDifficulty() == Difficulty.HARD) {
                    i = 15;
                }
                if (i > 0) {
                    ((LivingEntity)target).addEffect(new MobEffectInstance(CNEffects.RADIATION.get(), i * 20, 0), this);
                }
            }
            return true;
        }
        return false;
    }
    @Override
    protected boolean isFlapping() {
        return this.flyDist > this.nextFlap;
    }
    @Override
    protected void onFlap() {
        this.nextFlap = this.flyDist + this.flapSpeed / 2.0F;
    }
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.CHICKEN_AMBIENT;
    }
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.CHICKEN_HURT;
    }
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.CHICKEN_DEATH;
    }

    public boolean wantsToAttack(LivingEntity target, LivingEntity owner) {
        if (!(target instanceof Creeper) && !(target instanceof Ghast)) {
            if (target instanceof Player && owner instanceof Player && !((Player)owner).canHarmPlayer((Player)target)) {
                return false;
            } else if (target instanceof AbstractHorse && ((AbstractHorse)target).isTamed()) {
                return false;
            } else {
                return !(target instanceof TamableAnimal) || !((TamableAnimal)target).isTame();
            }
        } else {
            return false;
        }
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.CHICKEN_STEP, 0.15F, 1.0F);
    }

    @Nullable
    public IrradiatedChicken getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        return CNMobEntityType.IRRADIATED_CHICKEN.create(level);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return FOOD_ITEMS.test(stack);
    }

    @Override
    public int getExperienceReward() {
        return this.isChickenJockey() ? 10 : super.getExperienceReward();
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.isChickenJockey = compound.getBoolean("IsChickenJockey");
        this.readPersistentAngerSaveData(this.level(), compound);

    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("IsChickenJockey", this.isChickenJockey);
        this.addPersistentAngerSaveData(compound);
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return this.isChickenJockey();
    }

    @Override
    protected void positionRider(Entity passenger, MoveFunction callback) {
        super.positionRider(passenger, callback);
        float f = Mth.sin(this.yBodyRot * 0.017453292F);
        float g = Mth.cos(this.yBodyRot * 0.017453292F);
        float h = 0.1F;
        float i = 0.0F;
        callback.accept(passenger, this.getX() + (double)(0.1F * f), this.getY(0.5) + passenger.getMyRidingOffset() + 0.0, this.getZ() - (double)(0.1F * g));
        if (passenger instanceof LivingEntity) {
            ((LivingEntity)passenger).yBodyRot = this.yBodyRot;
        }

    }

    public boolean isChickenJockey() {
        return this.isChickenJockey;
    }

    public void setChickenJockey(boolean isChickenJockey) {
        this.isChickenJockey = isChickenJockey;
    }

    @Override
    public int getRemainingPersistentAngerTime() {
        return (Integer)this.entityData.get(DATA_REMAINING_ANGER_TIME);
    }

    @Override
    public void setRemainingPersistentAngerTime(int remainingPersistentAngerTime) {
        this.entityData.set(DATA_REMAINING_ANGER_TIME, remainingPersistentAngerTime);
    }

    @Override
    public @Nullable UUID getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable UUID persistentAngerTarget) {
        this.persistentAngerTarget = persistentAngerTarget;
    }

    @Override
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.random));
    }

    static class EvilIrradiationChickenAttackGoal
            extends MeleeAttackGoal {
        public EvilIrradiationChickenAttackGoal(IrradiatedChicken chicken) {
            super(chicken, 1.4, true);
        }

        @Override
        protected double getAttackReachSqr(LivingEntity attackTarget) {
            return 4.0f + attackTarget.getBbWidth();
        }
    }
}
