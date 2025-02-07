package net.nuclearteam.createnuclear.entity.irradiatedbee;

import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.PoiTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.VisibleForDebug;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.AirAndWaterRandomPos;
import net.minecraft.world.entity.ai.util.AirRandomPos;
import net.minecraft.world.entity.ai.util.HoverRandomPos;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import net.nuclearteam.createnuclear.effects.CNEffects;
import net.nuclearteam.createnuclear.entity.CNMobEntityType;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IrradiatedBee extends Animal implements NeutralMob, FlyingAnimal {
    public static final int TICKS_PER_FLAP = Mth.ceil(1.4959966f);
    private static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(IrradiatedBee.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Integer> DATA_REMAINING_ANGER_TIME = SynchedEntityData.defineId(IrradiatedBee.class, EntityDataSerializers.INT);
    public static final String TAG_CROPS_GROWN_SINCE_POLLINATION = "CropsGrownSincePollination";
    public static final String TAG_CANNOT_ENTER_HIVE_TICKS = "CannotEnterHiveTicks";
    public static final String TAG_TICKS_SINCE_POLLINATION = "TicksSincePollination";
    public static final String TAG_HAS_STUNG = "HasStung";
    public static final String TAG_HAS_NECTAR = "HasNectar";
    public static final String TAG_FLOWER_POS = "FlowerPos";
    public static final String TAG_HIVE_POS = "HivePos";
    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
    @Nullable
    private UUID persistentAngerTarget;
    private float rollAmount;
    private float rollAmountO;
    private int timeSinceSting;
    int ticksWithoutNectarSinceExitingHive;
    private int stayOutOfHiveCountdown;
    private int numCropsGrownSincePollination;
    private static final int COOLDOWN_BEFORE_LOCATING_NEW_HIVE = 200;
    int remainingCooldownBeforeLocatingNewHive;
    private static final int COOLDOWN_BEFORE_LOCATING_NEW_FLOWER = 200;
    int remainingCooldownBeforeLocatingNewFlower;
    @Nullable
    BlockPos savedFlowerPos;
    @Nullable
    BlockPos hivePos;
    BeePollinateGoal beePollinateGoal;
    BeeGoToHiveGoal goToHiveGoal;
    private BeeGoToKnownFlowerGoal goToKnownFlowerGoal;
    private int underWaterTicks;

    public IrradiatedBee(EntityType<? extends IrradiatedBee> entityType, Level level) {
        super(entityType, level);
        this.remainingCooldownBeforeLocatingNewFlower = Mth.nextInt(this.random, 20, 60);
        this.moveControl = new FlyingMoveControl(this, 20, true);
        this.lookControl = new IrradiatedBee.BeeLookControl(this);
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, -1.0f);
        this.setPathfindingMalus(BlockPathTypes.WATER, -1.0f);
        this.setPathfindingMalus(BlockPathTypes.WATER_BORDER, 16.0f);
        this.setPathfindingMalus(BlockPathTypes.COCOA, -1.0f);
        this.setPathfindingMalus(BlockPathTypes.FENCE, -1.0f);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_FLAGS_ID, (byte)0);
        this.entityData.define(DATA_REMAINING_ANGER_TIME, 0);
    }

    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader level) {
        if (level.getBlockState(pos).isAir()) {
            return 10.0f;
        }
        return 0.0f;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new BeeAttackGoal(this, 1.4f, true));
        this.goalSelector.addGoal(1, new BeeEnterHiveGoal());
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0));
        //this.goalSelector.addGoal(3, new TemptGoal(this, 1.25, Ingredient.of(ItemTags.FLOWERS), false));
        this.beePollinateGoal = new BeePollinateGoal();
        /*this.goalSelector.addGoal(4, this.beePollinateGoal);*/
        this.goalSelector.addGoal(5, new FollowParentGoal(this, 1.25));
        this.goalSelector.addGoal(5, new BeeLocateHiveGoal());
        this.goToHiveGoal = new BeeGoToHiveGoal();
        this.goalSelector.addGoal(5, this.goToHiveGoal);
        this.goToKnownFlowerGoal = new BeeGoToKnownFlowerGoal();
        this.goalSelector.addGoal(6, this.goToKnownFlowerGoal);
        this.goalSelector.addGoal(7, new BeeGrowCropGoal());
        this.goalSelector.addGoal(8, new BeeWanderGoal());
        this.goalSelector.addGoal(9, new FloatGoal(this));
        this.targetSelector.addGoal(1, new BeeHurtByOtherGoal(this).setAlertOthers());
        this.targetSelector.addGoal(2, new IrradiatedBecomeAngryTargetGoal(this));
        this.targetSelector.addGoal(3, new ResetUniversalAngerTargetGoal<>(this, true));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        if (this.hasHive()) {
            compound.put(TAG_HIVE_POS, NbtUtils.writeBlockPos(this.getHivePos()));
        }
        if (this.hasSavedFlowerPos()) {
            compound.put(TAG_FLOWER_POS, NbtUtils.writeBlockPos(this.getSavedFlowerPos()));
        }
        compound.putBoolean(TAG_HAS_NECTAR, this.hasNectar());
        compound.putBoolean(TAG_HAS_STUNG, this.hasStung());
        compound.putInt(TAG_TICKS_SINCE_POLLINATION, this.ticksWithoutNectarSinceExitingHive);
        compound.putInt(TAG_CANNOT_ENTER_HIVE_TICKS, this.stayOutOfHiveCountdown);
        compound.putInt(TAG_CROPS_GROWN_SINCE_POLLINATION, this.numCropsGrownSincePollination);
        this.addPersistentAngerSaveData(compound);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        this.hivePos = null;
        if (compound.contains(TAG_HIVE_POS)) {
            this.hivePos = NbtUtils.readBlockPos(compound.getCompound(TAG_HIVE_POS));
        }
        this.savedFlowerPos = null;
        if (compound.contains(TAG_FLOWER_POS)) {
            this.savedFlowerPos = NbtUtils.readBlockPos(compound.getCompound(TAG_FLOWER_POS));
        }
        super.readAdditionalSaveData(compound);
        this.setHasNectar(compound.getBoolean(TAG_HAS_NECTAR));
        this.setHasStung(compound.getBoolean(TAG_HAS_STUNG));
        this.ticksWithoutNectarSinceExitingHive = compound.getInt(TAG_TICKS_SINCE_POLLINATION);
        this.stayOutOfHiveCountdown = compound.getInt(TAG_CANNOT_ENTER_HIVE_TICKS);
        this.numCropsGrownSincePollination = compound.getInt(TAG_CROPS_GROWN_SINCE_POLLINATION);
        this.readPersistentAngerSaveData(this.level(), compound);
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        boolean bl = target.hurt(this.damageSources().sting(this), (int)this.getAttributeValue(Attributes.ATTACK_DAMAGE));
        if (bl) {
            this.doEnchantDamageEffects(this, target);
            if (target instanceof LivingEntity) {
                ((LivingEntity)target).setStingerCount(((LivingEntity)target).getStingerCount() + 1);
                int i = 0;
                if (this.level().getDifficulty() == Difficulty.NORMAL) {
                    i = 10;
                } else if (this.level().getDifficulty() == Difficulty.HARD) {
                    i = 18;
                }
                if (i > 0) {
                    ((LivingEntity)target).addEffect(new MobEffectInstance(CNEffects.RADIATION.get(), i * 20, 0), this);
                }
            }
            this.setHasStung(true);
            this.stopBeingAngry();
            this.playSound(SoundEvents.BEE_STING, 1.0f, 1.0f);
        }
        return bl;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.hasNectar() && this.getCropsGrownSincePollination() < 10 && this.random.nextFloat() < 0.05f) {
            for (int i = 0; i < this.random.nextInt(2) + 1; ++i) {
                this.spawnFluidParticle(this.level(), this.getX() - (double)0.3f, this.getX() + (double)0.3f, this.getZ() - (double)0.3f, this.getZ() + (double)0.3f, this.getY(0.5), ParticleTypes.FALLING_NECTAR);
            }
        }
        this.updateRollAmount();
    }

    private void spawnFluidParticle(Level level, double startX, double endX, double startZ, double endZ, double posY, ParticleOptions particleOption) {
        level.addParticle(particleOption, Mth.lerp(level.random.nextDouble(), startX, endX), posY, Mth.lerp(level.random.nextDouble(), startZ, endZ), 0.0, 0.0, 0.0);
    }

    void pathfindRandomlyTowards(BlockPos pos) {
        Vec3 vec32;
        Vec3 vec3 = Vec3.atBottomCenterOf(pos);
        int i = 0;
        BlockPos blockPos = this.blockPosition();
        int j = (int)vec3.y - blockPos.getY();
        if (j > 2) {
            i = 4;
        } else if (j < -2) {
            i = -4;
        }
        int k = 6;
        int l = 8;
        int m = blockPos.distManhattan(pos);
        if (m < 15) {
            k = m / 2;
            l = m / 2;
        }
        if ((vec32 = AirRandomPos.getPosTowards(this, k, l, i, vec3, 0.3141592741012573)) == null) {
            return;
        }
        this.navigation.setMaxVisitedNodesMultiplier(0.5f);
        this.navigation.moveTo(vec32.x, vec32.y, vec32.z, 1.0);
    }

    @Nullable
    public BlockPos getSavedFlowerPos() {
        return this.savedFlowerPos;
    }

    public boolean hasSavedFlowerPos() {
        return this.savedFlowerPos != null;
    }

    public void setSavedFlowerPos(BlockPos savedFlowerPos) {
        this.savedFlowerPos = savedFlowerPos;
    }

    @VisibleForDebug
    public int getTravellingTicks() {
        return Math.max(this.goToHiveGoal.travellingTicks, this.goToKnownFlowerGoal.travellingTicks);
    }

    @VisibleForDebug
    public List<BlockPos> getBlacklistedHives() {
        return this.goToHiveGoal.blacklistedTargets;
    }

    private boolean isTiredOfLookingForNectar() {
        return this.ticksWithoutNectarSinceExitingHive > 3600;
    }

    boolean wantsToEnterHive() {
        if (this.stayOutOfHiveCountdown > 0 || this.beePollinateGoal.isPollinating() || this.hasStung() || this.getTarget() != null) {
            return false;
        }
        boolean bl = this.isTiredOfLookingForNectar() || this.level().isRaining() || this.level().isNight() || this.hasNectar();
        return bl && !this.isHiveNearFire();
    }

    public void setStayOutOfHiveCountdown(int stayOutOfHiveCountdown) {
        this.stayOutOfHiveCountdown = stayOutOfHiveCountdown;
    }

    public float getRollAmount(float partialTick) {
        return Mth.lerp(partialTick, this.rollAmountO, this.rollAmount);
    }

    private void updateRollAmount() {
        this.rollAmountO = this.rollAmount;
        this.rollAmount = this.isRolling() ? Math.min(1.0f, this.rollAmount + 0.2f) : Math.max(0.0f, this.rollAmount - 0.24f);
    }

    @Override
    protected void customServerAiStep() {
        boolean bl = this.hasStung();
        this.underWaterTicks = this.isInWaterOrBubble() ? ++this.underWaterTicks : 0;
        if (this.underWaterTicks > 20) {
            this.hurt(this.damageSources().drown(), 1.0f);
        }
        if (bl) {
            ++this.timeSinceSting;
            if (this.timeSinceSting % 5 == 0 && this.random.nextInt(Mth.clamp(1200 - this.timeSinceSting, 1, 1200)) == 0) {
                this.hurt(this.damageSources().generic(), this.getHealth());
            }
        }
        if (!this.hasNectar()) {
            ++this.ticksWithoutNectarSinceExitingHive;
        }
        if (!this.level().isClientSide) {
            this.updatePersistentAnger((ServerLevel)this.level(), false);
        }
    }

    public void resetTicksWithoutNectarSinceExitingHive() {
        this.ticksWithoutNectarSinceExitingHive = 0;
    }

    private boolean isHiveNearFire() {
        if (this.hivePos == null) {
            return false;
        }
        BlockEntity blockEntity = this.level().getBlockEntity(this.hivePos);
        return blockEntity instanceof BeehiveBlockEntity && ((BeehiveBlockEntity)blockEntity).isFireNearby();
    }

    @Override
    public int getRemainingPersistentAngerTime() {
        return this.entityData.get(DATA_REMAINING_ANGER_TIME);
    }

    @Override
    public void setRemainingPersistentAngerTime(int remainingPersistentAngerTime) {
        this.entityData.set(DATA_REMAINING_ANGER_TIME, remainingPersistentAngerTime);
    }

    @Override
    @Nullable
    public UUID getPersistentAngerTarget() {
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

    private boolean doesHiveHaveSpace(BlockPos hivePos) {
        BlockEntity blockEntity = this.level().getBlockEntity(hivePos);
        if (blockEntity instanceof BeehiveBlockEntity) {
            return !((BeehiveBlockEntity)blockEntity).isFull();
        }
        return false;
    }

    @VisibleForDebug
    public boolean hasHive() {
        return this.hivePos != null;
    }

    @Nullable
    @VisibleForDebug
    public BlockPos getHivePos() {
        return this.hivePos;
    }

    @VisibleForDebug
    public GoalSelector getGoalSelector() {
        return this.goalSelector;
    }

    @Override
    protected void sendDebugPackets() {
        super.sendDebugPackets();
        //DebugPackets.sendBeeInfo(this);
    }

    int getCropsGrownSincePollination() {
        return this.numCropsGrownSincePollination;
    }

    private void resetNumCropsGrownSincePollination() {
        this.numCropsGrownSincePollination = 0;
    }

    void incrementNumCropsGrownSincePollination() {
        ++this.numCropsGrownSincePollination;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide) {
            if (this.stayOutOfHiveCountdown > 0) {
                --this.stayOutOfHiveCountdown;
            }
            if (this.remainingCooldownBeforeLocatingNewHive > 0) {
                --this.remainingCooldownBeforeLocatingNewHive;
            }
            if (this.remainingCooldownBeforeLocatingNewFlower > 0) {
                --this.remainingCooldownBeforeLocatingNewFlower;
            }
            boolean bl = this.isAngry() && !this.hasStung() && this.getTarget() != null && this.getTarget().distanceToSqr(this) < 4.0;
            this.setRolling(bl);
            if (this.tickCount % 20 == 0 && !this.isHiveValid()) {
                this.hivePos = null;
            }
        }
    }

    boolean isHiveValid() {
        if (!this.hasHive()) {
            return false;
        }
        if (this.isTooFarAway(this.hivePos)) {
            return false;
        }
        BlockEntity blockEntity = this.level().getBlockEntity(this.hivePos);
        return blockEntity != null && blockEntity.getType() == BlockEntityType.BEEHIVE;
    }

    public boolean hasNectar() {
        return this.getFlag(8);
    }

    void setHasNectar(boolean hasNectar) {
        if (hasNectar) {
            this.resetTicksWithoutNectarSinceExitingHive();
        }
        this.setFlag(8, hasNectar);
    }

    public boolean hasStung() {
        return this.getFlag(4);
    }

    private void setHasStung(boolean hasStung) {
        this.setFlag(4, hasStung);
    }

    private boolean isRolling() {
        return this.getFlag(2);
    }

    private void setRolling(boolean isRolling) {
        this.setFlag(2, isRolling);
    }

    boolean isTooFarAway(BlockPos pos) {
        return !this.closerThan(pos, 32);
    }

    private void setFlag(int flagId, boolean value) {
        if (value) {
            this.entityData.set(DATA_FLAGS_ID, (byte)(this.entityData.get(DATA_FLAGS_ID) | flagId));
        } else {
            this.entityData.set(DATA_FLAGS_ID, (byte)(this.entityData.get(DATA_FLAGS_ID) & ~flagId));
        }
    }

    private boolean getFlag(int flagId) {
        return (this.entityData.get(DATA_FLAGS_ID) & flagId) != 0;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10.0)
                .add(Attributes.FLYING_SPEED, 0.6f)
                .add(Attributes.MOVEMENT_SPEED, 0.3f)
                .add(Attributes.ATTACK_DAMAGE, 2.0)
                .add(Attributes.FOLLOW_RANGE, 48.0);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        FlyingPathNavigation flyingPathNavigation = new FlyingPathNavigation(this, level){

            @Override
            public boolean isStableDestination(BlockPos pos) {
                return !this.level.getBlockState(pos.below()).isAir();
            }

            @Override
            public void tick() {
                if (IrradiatedBee.this.beePollinateGoal.isPollinating()) {
                    return;
                }
                super.tick();
            }
        };
        flyingPathNavigation.setCanOpenDoors(false);
        flyingPathNavigation.setCanFloat(false);
        flyingPathNavigation.setCanPassDoors(true);
        return flyingPathNavigation;
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(ItemTags.FLOWERS);
    }

    boolean isFlowerValid(BlockPos pos) {
        return this.level().isLoaded(pos) && this.level().getBlockState(pos).is(BlockTags.FLOWERS);
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.BEE_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.BEE_DEATH;
    }

    @Override
    protected float getSoundVolume() {
        return 0.4f;
    }

    @Override
    @Nullable
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        return CNMobEntityType.IRRADIATED_BEE.create(level);
    }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
        if (this.isBaby()) {
            return dimensions.height * 0.5f;
        }
        return dimensions.height * 0.5f;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
    }

    @Override
    public boolean isFlapping() {
        return this.isFlying() && this.tickCount % TICKS_PER_FLAP == 0;
    }

    @Override
    public boolean isFlying() {
        return !this.onGround();
    }

    public void dropOffNectar() {
        this.setHasNectar(false);
        this.resetNumCropsGrownSincePollination();
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        }
        if (!this.level().isClientSide) {
            this.beePollinateGoal.stopPollinating();
        }
        return super.hurt(source, amount);
    }

    @Override
    public MobType getMobType() {
        return MobType.ARTHROPOD;
    }

    @Override
    protected void jumpInLiquid(TagKey<Fluid> fluidTag) {
        this.setDeltaMovement(this.getDeltaMovement().add(0.0, 0.01, 0.0));
    }

    @Override
    public Vec3 getLeashOffset() {
        return new Vec3(0.0, 0.5f * this.getEyeHeight(), this.getBbWidth() * 0.2f);
    }

    boolean closerThan(BlockPos pos, int distance) {
        return pos.closerThan(this.blockPosition(), distance);
    }

    class BeePollinateGoal
            extends IrradiatedBaseBeeGoal {
        private static final int MIN_POLLINATION_TICKS = 400;
        private static final int MIN_FIND_FLOWER_RETRY_COOLDOWN = 20;
        private static final int MAX_FIND_FLOWER_RETRY_COOLDOWN = 60;
        private final Predicate<BlockState> VALID_POLLINATION_BLOCKS;
        private static final double ARRIVAL_THRESHOLD = 0.1;
        private static final int POSITION_CHANGE_CHANCE = 25;
        private static final float SPEED_MODIFIER = 0.35f;
        private static final float HOVER_HEIGHT_WITHIN_FLOWER = 0.6f;
        private static final float HOVER_POS_OFFSET = 0.33333334f;
        private int successfulPollinatingTicks;
        private int lastSoundPlayedTick;
        private boolean pollinating;
        @Nullable
        private Vec3 hoverPos;
        private int pollinatingTicks;
        private static final int MAX_POLLINATING_TICKS = 600;

        BeePollinateGoal() {
            this.VALID_POLLINATION_BLOCKS = state -> {
                if (state.hasProperty(BlockStateProperties.WATERLOGGED) && state.getValue(BlockStateProperties.WATERLOGGED).booleanValue()) {
                    return false;
                }
                if (state.is(BlockTags.FLOWERS)) {
                    if (state.is(Blocks.SUNFLOWER)) {
                        return state.getValue(DoublePlantBlock.HALF) == DoubleBlockHalf.UPPER;
                    }
                    return true;
                }
                return false;
            };
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canBeeUse() {
            if (IrradiatedBee.this.remainingCooldownBeforeLocatingNewFlower > 0) {
                return false;
            }
            if (IrradiatedBee.this.hasNectar()) {
                return false;
            }
            if (IrradiatedBee.this.level().isRaining()) {
                return false;
            }
            Optional<BlockPos> optional = this.findNearbyFlower();
            if (optional.isPresent()) {
                IrradiatedBee.this.savedFlowerPos = optional.get();
                IrradiatedBee.this.navigation.moveTo((double)IrradiatedBee.this.savedFlowerPos.getX() + 0.5, (double)IrradiatedBee.this.savedFlowerPos.getY() + 0.5, (double)IrradiatedBee.this.savedFlowerPos.getZ() + 0.5, 1.2f);
                return true;
            }
            IrradiatedBee.this.remainingCooldownBeforeLocatingNewFlower = Mth.nextInt(IrradiatedBee.this.random, 20, 60);
            return false;
        }

        @Override
        public boolean canBeeContinueToUse() {
            if (!this.pollinating) {
                return false;
            }
            if (!IrradiatedBee.this.hasSavedFlowerPos()) {
                return false;
            }
            if (IrradiatedBee.this.level().isRaining()) {
                return false;
            }
            if (this.hasPollinatedLongEnough()) {
                return IrradiatedBee.this.random.nextFloat() < 0.2f;
            }
            if (IrradiatedBee.this.tickCount % 20 == 0 && !IrradiatedBee.this.isFlowerValid(IrradiatedBee.this.savedFlowerPos)) {
                IrradiatedBee.this.savedFlowerPos = null;
                return false;
            }
            return true;
        }

        private boolean hasPollinatedLongEnough() {
            return this.successfulPollinatingTicks > 400;
        }

        boolean isPollinating() {
            return this.pollinating;
        }

        void stopPollinating() {
            this.pollinating = false;
        }

        @Override
        public void start() {
            this.successfulPollinatingTicks = 0;
            this.pollinatingTicks = 0;
            this.lastSoundPlayedTick = 0;
            this.pollinating = true;
            IrradiatedBee.this.resetTicksWithoutNectarSinceExitingHive();
        }

        @Override
        public void stop() {
            if (this.hasPollinatedLongEnough()) {
                IrradiatedBee.this.setHasNectar(true);
            }
            this.pollinating = false;
            IrradiatedBee.this.navigation.stop();
            IrradiatedBee.this.remainingCooldownBeforeLocatingNewFlower = 200;
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            ++this.pollinatingTicks;
            if (this.pollinatingTicks > 600) {
                IrradiatedBee.this.savedFlowerPos = null;
                return;
            }
            Vec3 vec3 = Vec3.atBottomCenterOf(IrradiatedBee.this.savedFlowerPos).add(0.0, 0.6f, 0.0);
            if (vec3.distanceTo(IrradiatedBee.this.position()) > 1.0) {
                this.hoverPos = vec3;
                this.setWantedPos();
                return;
            }
            if (this.hoverPos == null) {
                this.hoverPos = vec3;
            }
            boolean bl = IrradiatedBee.this.position().distanceTo(this.hoverPos) <= 0.1;
            boolean bl2 = true;
            if (!bl && this.pollinatingTicks > 600) {
                IrradiatedBee.this.savedFlowerPos = null;
                return;
            }
            if (bl) {
                boolean bl3;
                boolean bl4 = bl3 = IrradiatedBee.this.random.nextInt(25) == 0;
                if (bl3) {
                    this.hoverPos = new Vec3(vec3.x() + (double)this.getOffset(), vec3.y(), vec3.z() + (double)this.getOffset());
                    IrradiatedBee.this.navigation.stop();
                } else {
                    bl2 = false;
                }
                IrradiatedBee.this.getLookControl().setLookAt(vec3.x(), vec3.y(), vec3.z());
            }
            if (bl2) {
                this.setWantedPos();
            }
            ++this.successfulPollinatingTicks;
            if (IrradiatedBee.this.random.nextFloat() < 0.05f && this.successfulPollinatingTicks > this.lastSoundPlayedTick + 60) {
                this.lastSoundPlayedTick = this.successfulPollinatingTicks;
                IrradiatedBee.this.playSound(SoundEvents.BEE_POLLINATE, 1.0f, 1.0f);
            }
        }

        private void setWantedPos() {
            IrradiatedBee.this.getMoveControl().setWantedPosition(this.hoverPos.x(), this.hoverPos.y(), this.hoverPos.z(), 0.35f);
        }

        private float getOffset() {
            return (IrradiatedBee.this.random.nextFloat() * 2.0f - 1.0f) * 0.33333334f;
        }

        private Optional<BlockPos> findNearbyFlower() {
            return this.findNearestBlock(this.VALID_POLLINATION_BLOCKS, 5.0);
        }

        private Optional<BlockPos> findNearestBlock(Predicate<BlockState> predicate, double distance) {
            BlockPos blockPos = IrradiatedBee.this.blockPosition();
            BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
            int i = 0;
            while ((double)i <= distance) {
                int j = 0;
                while ((double)j < distance) {
                    int k = 0;
                    while (k <= j) {
                        int l;
                        int n = l = k < j && k > -j ? j : 0;
                        while (l <= j) {
                            mutableBlockPos.setWithOffset(blockPos, k, i - 1, l);
                            if (blockPos.closerThan(mutableBlockPos, distance) && predicate.test(IrradiatedBee.this.level().getBlockState(mutableBlockPos))) {
                                return Optional.of(mutableBlockPos);
                            }
                            l = l > 0 ? -l : 1 - l;
                        }
                        k = k > 0 ? -k : 1 - k;
                    }
                    ++j;
                }
                i = i > 0 ? -i : 1 - i;
            }
            return Optional.empty();
        }
    }

    class BeeLookControl
            extends LookControl {
        BeeLookControl(Mob mob) {
            super(mob);
        }

        @Override
        public void tick() {
            if (IrradiatedBee.this.isAngry()) {
                return;
            }
            super.tick();
        }

        @Override
        protected boolean resetXRotOnTick() {
            return !IrradiatedBee.this.beePollinateGoal.isPollinating();
        }
    }

    class BeeAttackGoal
            extends MeleeAttackGoal {
        BeeAttackGoal(PathfinderMob mob, double speedModifier, boolean followingTargetEvenIfNotSeen) {
            super(mob, speedModifier, followingTargetEvenIfNotSeen);
        }

        @Override
        public boolean canUse() {
            return super.canUse() && IrradiatedBee.this.isAngry() && !IrradiatedBee.this.hasStung();
        }

        @Override
        public boolean canContinueToUse() {
            return super.canContinueToUse() && IrradiatedBee.this.isAngry() && !IrradiatedBee.this.hasStung();
        }
    }

    class BeeEnterHiveGoal
            extends IrradiatedBaseBeeGoal {
        BeeEnterHiveGoal() {
        }

        @Override
        public boolean canBeeUse() {
            BlockEntity blockEntity;
            if (IrradiatedBee.this.hasHive() && IrradiatedBee.this.wantsToEnterHive() && IrradiatedBee.this.hivePos.closerToCenterThan(IrradiatedBee.this.position(), 2.0) && (blockEntity = IrradiatedBee.this.level().getBlockEntity(IrradiatedBee.this.hivePos)) instanceof BeehiveBlockEntity) {
                BeehiveBlockEntity beehiveBlockEntity = (BeehiveBlockEntity)blockEntity;
                if (beehiveBlockEntity.isFull()) {
                    IrradiatedBee.this.hivePos = null;
                } else {
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean canBeeContinueToUse() {
            return false;
        }

        @Override
        public void start() {
            BlockEntity blockEntity = IrradiatedBee.this.level().getBlockEntity(IrradiatedBee.this.hivePos);
            if (blockEntity instanceof BeehiveBlockEntity) {
                BeehiveBlockEntity beehiveBlockEntity = (BeehiveBlockEntity)blockEntity;
                beehiveBlockEntity.addOccupant(IrradiatedBee.this, IrradiatedBee.this.hasNectar());
            }
        }
    }

    class BeeLocateHiveGoal
            extends IrradiatedBaseBeeGoal {
        BeeLocateHiveGoal() {
        }

        @Override
        public boolean canBeeUse() {
            return IrradiatedBee.this.remainingCooldownBeforeLocatingNewHive == 0 && !IrradiatedBee.this.hasHive() && IrradiatedBee.this.wantsToEnterHive();
        }

        @Override
        public boolean canBeeContinueToUse() {
            return false;
        }

        @Override
        public void start() {
            IrradiatedBee.this.remainingCooldownBeforeLocatingNewHive = 200;
            List<BlockPos> list = this.findNearbyHivesWithSpace();
            if (list.isEmpty()) {
                return;
            }
            for (BlockPos blockPos : list) {
                if (IrradiatedBee.this.goToHiveGoal.isTargetBlacklisted(blockPos)) continue;
                IrradiatedBee.this.hivePos = blockPos;
                return;
            }
            IrradiatedBee.this.goToHiveGoal.clearBlacklist();
            IrradiatedBee.this.hivePos = list.get(0);
        }

        private List<BlockPos> findNearbyHivesWithSpace() {
            BlockPos blockPos = IrradiatedBee.this.blockPosition();
            PoiManager poiManager = ((ServerLevel)IrradiatedBee.this.level()).getPoiManager();
            Stream<PoiRecord> stream = poiManager.getInRange(point -> point.is(PoiTypeTags.BEE_HOME), blockPos, 20, PoiManager.Occupancy.ANY);
            return stream.map(PoiRecord::getPos).filter(IrradiatedBee.this::doesHiveHaveSpace).sorted(Comparator.comparingDouble(pos -> pos.distSqr(blockPos))).collect(Collectors.toList());
        }
    }

    @VisibleForDebug
    public class BeeGoToHiveGoal
            extends IrradiatedBaseBeeGoal {
        public static final int MAX_TRAVELLING_TICKS = 600;
        int travellingTicks;
        private static final int MAX_BLACKLISTED_TARGETS = 3;
        final List<BlockPos> blacklistedTargets;
        @Nullable
        private Path lastPath;
        private static final int TICKS_BEFORE_HIVE_DROP = 60;
        private int ticksStuck;

        BeeGoToHiveGoal() {
            this.travellingTicks = IrradiatedBee.this.level().random.nextInt(10);
            this.blacklistedTargets = Lists.newArrayList();
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canBeeUse() {
            return IrradiatedBee.this.hivePos != null
                    && !IrradiatedBee.this.hasRestriction() && IrradiatedBee.this.wantsToEnterHive()
                    && !this.hasReachedTarget(IrradiatedBee.this.hivePos) && IrradiatedBee.this.level().getBlockState(IrradiatedBee.this.hivePos).is(BlockTags.BEEHIVES);
        }

        @Override
        public boolean canBeeContinueToUse() {
            return this.canBeeUse();
        }

        @Override
        public void start() {
            this.travellingTicks = 0;
            this.ticksStuck = 0;
            super.start();
        }

        @Override
        public void stop() {
            this.travellingTicks = 0;
            this.ticksStuck = 0;
            IrradiatedBee.this.navigation.stop();
            IrradiatedBee.this.navigation.resetMaxVisitedNodesMultiplier();
        }

        @Override
        public void tick() {
            if (IrradiatedBee.this.hivePos == null) {
                return;
            }
            ++this.travellingTicks;
            if (this.travellingTicks > this.adjustedTickDelay(600)) {
                this.dropAndBlacklistHive();
                return;
            }
            if (IrradiatedBee.this.navigation.isInProgress()) {
                return;
            }
            if (IrradiatedBee.this.closerThan(IrradiatedBee.this.hivePos, 16)) {
                boolean bl = this.pathfindDirectlyTowards(IrradiatedBee.this.hivePos);
                if (!bl) {
                    this.dropAndBlacklistHive();
                } else if (this.lastPath != null && IrradiatedBee.this.navigation.getPath().sameAs(this.lastPath)) {
                    ++this.ticksStuck;
                    if (this.ticksStuck > 60) {
                        this.dropHive();
                        this.ticksStuck = 0;
                    }
                } else {
                    this.lastPath = IrradiatedBee.this.navigation.getPath();
                }
                return;
            }
            if (IrradiatedBee.this.isTooFarAway(IrradiatedBee.this.hivePos)) {
                this.dropHive();
                return;
            }
            IrradiatedBee.this.pathfindRandomlyTowards(IrradiatedBee.this.hivePos);
        }

        private boolean pathfindDirectlyTowards(BlockPos pos) {
            IrradiatedBee.this.navigation.setMaxVisitedNodesMultiplier(10.0f);
            IrradiatedBee.this.navigation.moveTo(pos.getX(), pos.getY(), pos.getZ(), 1.0);
            return IrradiatedBee.this.navigation.getPath() != null && IrradiatedBee.this.navigation.getPath().canReach();
        }

        boolean isTargetBlacklisted(BlockPos pos) {
            return this.blacklistedTargets.contains(pos);
        }

        private void blacklistTarget(BlockPos pos) {
            this.blacklistedTargets.add(pos);
            while (this.blacklistedTargets.size() > 3) {
                this.blacklistedTargets.remove(0);
            }
        }

        void clearBlacklist() {
            this.blacklistedTargets.clear();
        }

        private void dropAndBlacklistHive() {
            if (IrradiatedBee.this.hivePos != null) {
                this.blacklistTarget(IrradiatedBee.this.hivePos);
            }
            this.dropHive();
        }

        private void dropHive() {
            IrradiatedBee.this.hivePos = null;
            IrradiatedBee.this.remainingCooldownBeforeLocatingNewHive = 200;
        }

        private boolean hasReachedTarget(BlockPos pos) {
            if (IrradiatedBee.this.closerThan(pos, 2)) {
                return true;
            }
            Path path = IrradiatedBee.this.navigation.getPath();
            return path != null && path.getTarget().equals(pos) && path.canReach() && path.isDone();
        }
    }

    public class BeeGoToKnownFlowerGoal
            extends IrradiatedBaseBeeGoal {
        private static final int MAX_TRAVELLING_TICKS = 600;
        int travellingTicks;

        BeeGoToKnownFlowerGoal() {
            this.travellingTicks = IrradiatedBee.this.level().random.nextInt(10);
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canBeeUse() {
            return IrradiatedBee.this.savedFlowerPos != null && !IrradiatedBee.this.hasRestriction() && this.wantsToGoToKnownFlower() && IrradiatedBee.this.isFlowerValid(IrradiatedBee.this.savedFlowerPos) && !IrradiatedBee.this.closerThan(IrradiatedBee.this.savedFlowerPos, 2);
        }

        @Override
        public boolean canBeeContinueToUse() {
            return this.canBeeUse();
        }

        @Override
        public void start() {
            this.travellingTicks = 0;
            super.start();
        }

        @Override
        public void stop() {
            this.travellingTicks = 0;
            IrradiatedBee.this.navigation.stop();
            IrradiatedBee.this.navigation.resetMaxVisitedNodesMultiplier();
        }

        @Override
        public void tick() {
            if (IrradiatedBee.this.savedFlowerPos == null) {
                return;
            }
            ++this.travellingTicks;
            if (this.travellingTicks > this.adjustedTickDelay(600)) {
                IrradiatedBee.this.savedFlowerPos = null;
                return;
            }
            if (IrradiatedBee.this.navigation.isInProgress()) {
                return;
            }
            if (IrradiatedBee.this.isTooFarAway(IrradiatedBee.this.savedFlowerPos)) {
                IrradiatedBee.this.savedFlowerPos = null;
                return;
            }
            IrradiatedBee.this.pathfindRandomlyTowards(IrradiatedBee.this.savedFlowerPos);
        }

        private boolean wantsToGoToKnownFlower() {
            return IrradiatedBee.this.ticksWithoutNectarSinceExitingHive > 2400;
        }
    }

    class BeeGrowCropGoal
            extends IrradiatedBaseBeeGoal {
        static final int GROW_CHANCE = 30;

        BeeGrowCropGoal() {
        }

        @Override
        public boolean canBeeUse() {
            if (IrradiatedBee.this.getCropsGrownSincePollination() >= 10) {
                return false;
            }
            if (IrradiatedBee.this.random.nextFloat() < 0.3f) {
                return false;
            }
            return IrradiatedBee.this.hasNectar() && IrradiatedBee.this.isHiveValid();
        }

        @Override
        public boolean canBeeContinueToUse() {
            return this.canBeeUse();
        }

        @Override
        public void tick() {
            if (IrradiatedBee.this.random.nextInt(this.adjustedTickDelay(30)) != 0) {
                return;
            }
            int j;
            for (int i = 1; i <= 2; ++i) {
                BlockPos blockPos = IrradiatedBee.this.blockPosition().below(i);
                BlockState blockState = IrradiatedBee.this.level().getBlockState(blockPos);
                Block block = blockState.getBlock();
                BlockState blockState2 = null;
                if (!blockState.is(BlockTags.BEE_GROWABLES)) continue;
                if (block instanceof CropBlock) {
                    CropBlock cropBlock = (CropBlock)block;
                    if (!cropBlock.isMaxAge(blockState)) {
                        blockState2 = cropBlock.getStateForAge(cropBlock.getAge(blockState) + 1);
                    }
                } else if (block instanceof StemBlock) {
                    j = blockState.getValue(StemBlock.AGE);
                    if (j < 7) {
                        blockState2 = (BlockState)blockState.setValue(StemBlock.AGE, j + 1);
                    }
                } else if (blockState.is(Blocks.SWEET_BERRY_BUSH)) {
                    j = blockState.getValue(SweetBerryBushBlock.AGE);
                    if (j < 3) {
                        blockState2 = (BlockState)blockState.setValue(SweetBerryBushBlock.AGE, j + 1);
                    }
                } else if (blockState.is(Blocks.CAVE_VINES) || blockState.is(Blocks.CAVE_VINES_PLANT)) {
                    ((BonemealableBlock)((Object)blockState.getBlock())).performBonemeal((ServerLevel)IrradiatedBee.this.level(), IrradiatedBee.this.random, blockPos, blockState);
                }
                if (blockState2 == null) continue;
                IrradiatedBee.this.level().levelEvent(2005, blockPos, 0);
                IrradiatedBee.this.level().setBlockAndUpdate(blockPos, blockState2);
                IrradiatedBee.this.incrementNumCropsGrownSincePollination();
            }
        }
    }

    class BeeWanderGoal
            extends Goal {
        private static final int WANDER_THRESHOLD = 22;

        BeeWanderGoal() {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return IrradiatedBee.this.navigation.isDone() && IrradiatedBee.this.random.nextInt(10) == 0;
        }

        @Override
        public boolean canContinueToUse() {
            return IrradiatedBee.this.navigation.isInProgress();
        }

        @Override
        public void start() {
            Vec3 vec3 = this.findPos();
            if (vec3 != null) {
                IrradiatedBee.this.navigation.moveTo(IrradiatedBee.this.navigation.createPath(BlockPos.containing(vec3), 1), 1.0);
            }
        }

        @Nullable
        private Vec3 findPos() {
            Vec3 vec32;
            if (IrradiatedBee.this.isHiveValid() && !IrradiatedBee.this.closerThan(IrradiatedBee.this.hivePos, 22)) {
                Vec3 vec3 = Vec3.atCenterOf(IrradiatedBee.this.hivePos);
                vec32 = vec3.subtract(IrradiatedBee.this.position()).normalize();
            } else {
                vec32 = IrradiatedBee.this.getViewVector(0.0f);
            }
            int i = 8;
            Vec3 vec33 = HoverRandomPos.getPos(IrradiatedBee.this, 8, 7, vec32.x, vec32.z, 1.5707964f, 3, 1);
            if (vec33 != null) {
                return vec33;
            }
            return AirAndWaterRandomPos.getPos(IrradiatedBee.this, 8, 4, -2, vec32.x, vec32.z, 1.5707963705062866);
        }
    }

    class BeeHurtByOtherGoal
            extends HurtByTargetGoal {
        BeeHurtByOtherGoal(IrradiatedBee mob) {
            super(mob, new Class[0]);
        }

        @Override
        public boolean canContinueToUse() {
            return IrradiatedBee.this.isAngry() && super.canContinueToUse();
        }

        @Override
        protected void alertOther(Mob mob, LivingEntity target) {
            if (mob instanceof IrradiatedBee && this.mob.hasLineOfSight(target)) {
                mob.setTarget(target);
            }
        }
    }

    static class IrradiatedBecomeAngryTargetGoal
            extends NearestAttackableTargetGoal<Player> {
        IrradiatedBecomeAngryTargetGoal(IrradiatedBee mob) {
            super(mob, Player.class, 10, true, false, mob::isAngryAt);
        }

        @Override
        public boolean canUse() {
            return this.beeCanTarget() && super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            boolean bl = this.beeCanTarget();
            if (!bl || this.mob.getTarget() == null) {
                this.targetMob = null;
                return false;
            }
            return super.canContinueToUse();
        }

        private boolean beeCanTarget() {
            IrradiatedBee bee = (IrradiatedBee)this.mob;
            return bee.isAngry() && !bee.hasStung();
        }
    }

    abstract class IrradiatedBaseBeeGoal
            extends Goal {
        IrradiatedBaseBeeGoal() {
        }

        public abstract boolean canBeeUse();

        public abstract boolean canBeeContinueToUse();

        @Override
        public boolean canUse() {
            return this.canBeeUse() && !IrradiatedBee.this.isAngry();
        }

        @Override
        public boolean canContinueToUse() {
            return this.canBeeContinueToUse() && !IrradiatedBee.this.isAngry();
        }
    }
}
