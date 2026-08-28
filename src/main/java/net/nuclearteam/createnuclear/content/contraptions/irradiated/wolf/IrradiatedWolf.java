package net.nuclearteam.createnuclear.content.contraptions.irradiated.wolf;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NonTameRandomTargetGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.tags.BlockTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.util.TimeUtil;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.EntityGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.gameevent.GameEvent;
import net.nuclearteam.createnuclear.CNEntityType;
import net.nuclearteam.createnuclear.CNItems;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.UUID;
import java.util.function.Predicate;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
@SuppressWarnings({"unused", "Unchecked"})
public class IrradiatedWolf extends TamableAnimal implements NeutralMob {
    private static final EntityDataAccessor<Boolean> DATA_INTERESTED_ID;
    //private static final EntityDataAccessor<Integer> DATA_COLLAR_COLOR;
    private static final EntityDataAccessor<Integer> DATA_REMAINING_ANGER_TIME;
    public static final Predicate<LivingEntity> PREY_SELECTOR;
    public static final float START_HEALTH = 8.0F;
    public static final float TAME_HEALTH = 20.0F;
    private float interestedAngle;
    private float interestedAngleO;
    private boolean isWet;
    private boolean isShaking;
    private float shakeAnim;
    private float shakeAnimO;
    private static final UniformInt PERSISTENT_ANGER_TIME;
    @Nullable
    private UUID persistentAngerTarget;

    public IrradiatedWolf(EntityType<? extends IrradiatedWolf> entityType, Level level) {
        super(entityType, level);
        this.setTamed(false);
        this.setPathfindingMalus(BlockPathTypes.POWDER_SNOW, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.DANGER_POWDER_SNOW, -1.0F);
    }

    @Override
    public EntityGetter method_48926() {
        return this.level();
    }

    protected void initGoals() {
        this.goalSelector.add(1, new FloatGoal(this));
        this.goalSelector.add(1, new WolfPanicGoal(1.5));
        this.goalSelector.add(2, new SitWhenOrderedToGoal(this));
        this.goalSelector.add(3, new WolfAvoidEntityGoal<>(this, Llama.class, 24.0F, 1.5, 1.5));
        this.goalSelector.add(4, new LeapAtTargetGoal(this, 0.4F));
        this.goalSelector.add(5, new MeleeAttackGoal(this, 1.0, true));
        this.goalSelector.add(6, new FollowOwnerGoal(this, 1.0, 10.0F, 2.0F, false));
        this.goalSelector.add(7, new BreedGoal(this, 1.0));
        this.goalSelector.add(8, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.add(9, new BegGoal(this, 8.0F));
        this.goalSelector.add(10, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.add(10, new RandomLookAroundGoal(this));
        this.targetSelector.add(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.add(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.add(3, (new HurtByTargetGoal(this)).setGroupRevenge());
        this.targetSelector.add(4, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, this::shouldAngerAt));
        this.targetSelector.add(5, new NonTameRandomTargetGoal<>(this, Animal.class, false, PREY_SELECTOR));
        this.targetSelector.add(6, new NonTameRandomTargetGoal<>(this, Turtle.class, false, Turtle.BABY_TURTLE_ON_LAND_FILTER));
        this.targetSelector.add(7, new NearestAttackableTargetGoal<>(this, AbstractSkeleton.class, false));
        this.targetSelector.add(8, new ResetUniversalAngerTargetGoal<>(this, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.30000001192092896)
                .add(Attributes.MAX_HEALTH, 8.0)
                .add(Attributes.ATTACK_DAMAGE, 2.0);
    }

    protected void initDataTracker() {
        super.initDataTracker();
        this.entityData.define(DATA_INTERESTED_ID, false);
        //this.entityData.define(DATA_COLLAR_COLOR, DyeColor.RED.getId());
        this.entityData.define(DATA_REMAINING_ANGER_TIME, 0);
    }

    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.ENTITY_WOLF_STEP, 0.15F, 1.0F);
    }

    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        this.writeAngerToNbt(compound);
    }

    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.readAngerFromNbt(this.level(), compound);
    }

    protected SoundEvent getAmbientSound() {
        if (this.isAngry()) {
            return SoundEvents.ENTITY_WOLF_GROWL;
        } else if (this.random.nextInt(3) == 0) {
            return this.isTame() && this.getHealth() < 10.0F ? SoundEvents.ENTITY_WOLF_WHINE : SoundEvents.ENTITY_WOLF_PANT;
        } else {
            return SoundEvents.ENTITY_WOLF_AMBIENT;
        }
    }

    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.ENTITY_WOLF_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_WOLF_DEATH;
    }

    protected float getSoundVolume() {
        return 0.4F;
    }

    public void tickMovement() {
        super.tickMovement();
        if (!this.level().isClientSide && this.isWet && !this.isShaking && !this.isNavigating() && this.onGround()) {
            this.isShaking = true;
            this.shakeAnim = 0.0F;
            this.shakeAnimO = 0.0F;
            this.level().sendEntityStatus(this, (byte)8);
        }

        if (!this.level().isClientSide) {
            this.tickAngerLogic((ServerLevel)this.level(), true);
        }

    }

    public void tick() {
        super.tick();
        if (this.isAlive()) {
            this.interestedAngleO = this.interestedAngle;
            if (this.isInterested()) {
                this.interestedAngle += (1.0F - this.interestedAngle) * 0.4F;
            } else {
                this.interestedAngle += (0.0F - this.interestedAngle) * 0.4F;
            }

            if (this.isWet()) {
                this.isWet = true;
                if (this.isShaking && !this.level().isClientSide) {
                    this.level().sendEntityStatus(this, (byte)56);
                    this.cancelShake();
                }
            } else if ((this.isWet || this.isShaking) && this.isShaking) {
                if (this.shakeAnim == 0.0F) {
                    this.playSound(SoundEvents.ENTITY_WOLF_SHAKE, this.getSoundVolume(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
                    this.emitGameEvent(GameEvent.ENTITY_SHAKE);
                }

                this.shakeAnimO = this.shakeAnim;
                this.shakeAnim += 0.05F;
                if (this.shakeAnimO >= 2.0F) {
                    this.isWet = false;
                    this.isShaking = false;
                    this.shakeAnimO = 0.0F;
                    this.shakeAnim = 0.0F;
                }

                if (this.shakeAnim > 0.4F) {
                    float f = (float)this.getY();
                    int i = (int)(Mth.sin((this.shakeAnim - 0.4F) * 3.1415927F) * 7.0F);
                    Vec3 vec3 = this.getVelocity();

                    for(int j = 0; j < i; ++j) {
                        float g = (this.random.nextFloat() * 2.0F - 1.0F) * this.getWidth() * 0.5F;
                        float h = (this.random.nextFloat() * 2.0F - 1.0F) * this.getWidth() * 0.5F;
                        this.level().addParticle(ParticleTypes.SPLASH, this.getX() + (double)g, f + 0.8F, this.getZ() + (double)h, vec3.x, vec3.y, vec3.z);
                    }
                }
            }

        }
    }

    private void cancelShake() {
        this.isShaking = false;
        this.shakeAnim = 0.0F;
        this.shakeAnimO = 0.0F;
    }

    public void onDeath(DamageSource damageSource) {
        this.isWet = false;
        this.isShaking = false;
        this.shakeAnimO = 0.0F;
        this.shakeAnim = 0.0F;
        super.onDeath(damageSource);
    }

    public boolean isWet() {
        return this.isWet;
    }

    public float getWetShade(float partialTicks) {
        return Math.min(0.5F + Mth.lerp(partialTicks, this.shakeAnimO, this.shakeAnim) / 2.0F * 0.5F, 1.0F);
    }

    public float getBodyRollAngle(float partialTicks, float offset) {
        float f = (Mth.lerp(partialTicks, this.shakeAnimO, this.shakeAnim) + offset) / 1.8F;
        if (f < 0.0F) {
            f = 0.0F;
        } else if (f > 1.0F) {
            f = 1.0F;
        }

        return Mth.sin(f * 3.1415927F) * Mth.sin(f * 3.1415927F * 11.0F) * 0.15F * 3.1415927F;
    }

    public float getHeadRollAngle(float partialTicks) {
        return Mth.lerp(partialTicks, this.interestedAngleO, this.interestedAngle) * 0.15F * 3.1415927F;
    }

    protected float getActiveEyeHeight(Pose pose, EntityDimensions dimensions) {
        return dimensions.height * 0.8F;
    }

    public int getMaxLookPitchChange() {
        return this.isInSittingPose() ? 20 : super.getMaxLookPitchChange();
    }

    public boolean damage(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        } else {
            Entity entity = source.getAttacker();
            if (!this.level().isClientSide) {
                this.setInSittingPose(false);
            }

            if (entity != null && !(entity instanceof Player) && !(entity instanceof AbstractArrow)) {
                amount = (amount + 1.0F) / 2.0F;
            }

            return super.damage(source, amount);
        }
    }

    public boolean tryAttack(Entity target) {
        boolean bl = target.damage(this.getDamageSources().mobAttack(this), (float)((int)this.getAttributeValue(Attributes.ATTACK_DAMAGE)));
        if (bl) {
            this.applyDamageEffects(this, target);
        }

        return bl;
    }

    public void setTamed(boolean tamed) {
        super.setTamed(tamed);
        if (tamed) {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(20.0);
            this.setHealth(20.0F);
        } else {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(8.0);
        }

        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(4.0);
    }

    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        Item item = itemStack.getItem();
        if (this.level().isClientSide) {
            boolean bl = this.isOwnedBy(player) || this.isTame() || itemStack.is(Items.BONE) && !this.isTame() && !this.isAngry();
            return bl ? InteractionResult.CONSUME : InteractionResult.PASS;
        } else {
            {
                if (this.isTame()) {
                    if (this.isBreedingItem(itemStack) && this.getHealth() < this.getMaxHealth()) {
                        if (!player.getAbilities().creativeMode) {
                            itemStack.decrement(1);
                        }

                        this.heal((float) item.getFoodComponent().getHunger());
                        return InteractionResult.SUCCESS;
                    }
                }

                return super.mobInteract(player, hand);
            }
        }
    }

    public void handleStatus(byte id) {
        if (id == 8) {
            this.isShaking = true;
            this.shakeAnim = 0.0F;
            this.shakeAnimO = 0.0F;
        } else if (id == 56) {
            this.cancelShake();
        } else {
            super.handleStatus(id);
        }

    }

    public float getTailAngle() {
        if (this.isAngry()) {
            return 1.5393804F;
        } else {
            return this.isTame() ? (0.55F - (this.getMaxHealth() - this.getHealth()) * 0.02F) * 3.1415927F : 0.62831855F;
        }
    }

    public boolean isBreedingItem(ItemStack stack) {
        //Item item = stack.getItem();
        return stack.is(CNItems.URANIUM_POWDER.get()); //item.isEdible() && item.getFoodProperties().isMeat();
    }

    public int getLimitPerChunk() {
        return 8;
    }

    public int getAngerTime() {
        return this.entityData.get(DATA_REMAINING_ANGER_TIME);
    }

    public void setAngerTime(int remainingPersistentAngerTime) {
        this.entityData.set(DATA_REMAINING_ANGER_TIME, remainingPersistentAngerTime);
    }

    public void chooseRandomAngerTime() {
        this.setAngerTime(PERSISTENT_ANGER_TIME.get(this.random));
    }

    @Nullable
    public UUID getAngryAt() {
        return this.persistentAngerTarget;
    }

    public void setAngryAt(@Nullable UUID persistentAngerTarget) {
        this.persistentAngerTarget = persistentAngerTarget;
    }

    @Nullable
    public IrradiatedWolf createChild(ServerLevel level, AgeableMob otherParent) {
        return CNEntityType.IRRADIATED_WOLF.create(level);
    }

    public void setIsInterested(boolean isInterested) {
        this.entityData.set(DATA_INTERESTED_ID, isInterested);
    }

    public boolean canBreedWith(Animal otherAnimal) {
        if (otherAnimal == this) {
            return false;
        } else if (!this.isTame()) {
            return false;
        } else if (!(otherAnimal instanceof IrradiatedWolf irradiatedWolf)) {
            return false;
        } else {
            if (!irradiatedWolf.isTame()) {
                return false;
            } else if (irradiatedWolf.isInSittingPose()) {
                return false;
            } else {
                return this.isInLove() && irradiatedWolf.isInLove();
            }
        }
    }

    public boolean isInterested() {
        return this.entityData.get(DATA_INTERESTED_ID);
    }

    public boolean canAttackWithOwner(LivingEntity target, LivingEntity owner) {
        if (!(target instanceof Creeper) && !(target instanceof Ghast)) {
            if (target instanceof IrradiatedWolf irradiatedWolf) {
                return !irradiatedWolf.isTame() || irradiatedWolf.getOwner() != owner;
            } else if (target instanceof Player && owner instanceof Player && !((Player)owner).shouldDamagePlayer((Player)target)) {
                return false;
            } else if (target instanceof AbstractHorse && ((AbstractHorse)target).isTame()) {
                return false;
            } else {
                return !(target instanceof TamableAnimal) || !((TamableAnimal)target).isTame();
            }
        } else {
            return false;
        }
    }

    public boolean canBeLeashedBy(Player player) {
        return !this.isAngry() && super.canBeLeashedBy(player);
    }

    public @NotNull Vec3 getLeashOffset() {
        return new Vec3(0.0, 0.6F * this.getStandingEyeHeight(), this.getWidth() * 0.4F);
    }

    public static boolean checkWolfSpawnRules(EntityType<Wolf> wolf, LevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        return level.getBlockState(pos.down()).is(BlockTags.WOLVES_SPAWNABLE_ON) && isLightLevelValidForNaturalSpawn(level, pos);
    }

    static {
        DATA_INTERESTED_ID = SynchedEntityData.registerData(IrradiatedWolf.class, EntityDataSerializers.BOOLEAN);
        DATA_REMAINING_ANGER_TIME = SynchedEntityData.registerData(IrradiatedWolf.class, EntityDataSerializers.INTEGER);
        PREY_SELECTOR = (entity) -> {
            EntityType<?> entityType = entity.getType();
            return entityType == EntityType.SHEEP || entityType == EntityType.RABBIT || entityType == EntityType.FOX || entityType == CNEntityType.IRRADIATED_CAT.get();
        };
        PERSISTENT_ANGER_TIME = TimeUtil.betweenSeconds(20, 39);
    }

    private class WolfPanicGoal extends PanicGoal {
        public WolfPanicGoal(double speedModifier) {
            super(IrradiatedWolf.this, speedModifier);
        }

        protected boolean isInDanger() {
            return this.mob.shouldEscapePowderSnow() || this.mob.isOnFire();
        }
    }

    private class WolfAvoidEntityGoal<T extends LivingEntity> extends AvoidEntityGoal<T> {
        private final IrradiatedWolf wolf;

        public WolfAvoidEntityGoal(IrradiatedWolf wolf, Class entityClassToAvoid, float maxDist, double walkSpeedModifier, double sprintSpeedModifier) {
            super(wolf, entityClassToAvoid, maxDist, walkSpeedModifier, sprintSpeedModifier);
            this.wolf = wolf;
        }

        public boolean canUse() {
            if (super.canUse() && this.targetEntity instanceof Llama) {
                return !this.wolf.isTame() && this.avoidLlama((Llama)this.targetEntity);
            } else {
                return false;
            }
        }

        private boolean avoidLlama(Llama llama) {
            return llama.getStrength() >= IrradiatedWolf.this.random.nextInt(5);
        }

        public void start() {
            IrradiatedWolf.this.setTarget(null);
            super.start();
        }

        public void tick() {
            IrradiatedWolf.this.setTarget(null);
            super.tick();
        }
    }
}
