package net.nuclearteam.createnuclear.entity.irradiatedphantom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;

public class irradiatedPhantom extends FlyingMob
        implements Enemy {
    public static final float FLAP_DEGREES_PER_TICK = 7.448451f;
    public static final int TICKS_PER_FLAP = Mth.ceil(24.166098f);
    private static final EntityDataAccessor<Integer> ID_SIZE = SynchedEntityData.defineId(irradiatedPhantom.class, EntityDataSerializers.INT);
    Vec3 moveTargetPoint = Vec3.ZERO;
    BlockPos anchorPoint = BlockPos.ZERO;
    AttackPhase attackPhase = AttackPhase.CIRCLE;

    public irradiatedPhantom(EntityType<? extends Phantom> entityType, Level level) {
        super((EntityType<? extends FlyingMob>)entityType, level);
        this.xpReward = 5;
        this.moveControl = new PhantomMoveControl(this);
        this.lookControl = new PhantomLookControl(this);
    }

    @Override
    public boolean isFlapping() {
        return (this.getUniqueFlapTickOffset() + this.tickCount) % TICKS_PER_FLAP == 0;
    }

    @Override
    protected BodyRotationControl createBodyControl() {
        return new PhantomBodyRotationControl(this);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new PhantomAttackStrategyGoal());
        this.goalSelector.addGoal(2, new PhantomSweepAttackGoal());
        this.goalSelector.addGoal(3, new PhantomCircleAroundAnchorGoal());
        this.targetSelector.addGoal(1, new PhantomAttackPlayerTargetGoal());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ID_SIZE, 0);
    }

    public void setPhantomSize(int phantomSize) {
        this.entityData.set(ID_SIZE, Mth.clamp(phantomSize, 0, 64));
    }

    private void updatePhantomSizeInfo() {
        this.refreshDimensions();
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(6 + this.getPhantomSize());
    }

    public int getPhantomSize() {
        return this.entityData.get(ID_SIZE);
    }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
        return dimensions.height * 0.35f;
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        if (ID_SIZE.equals(key)) {
            this.updatePhantomSizeInfo();
        }
        super.onSyncedDataUpdated(key);
    }

    public int getUniqueFlapTickOffset() {
        return this.getId() * 3;
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return true;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            float f = Mth.cos((float)(this.getUniqueFlapTickOffset() + this.tickCount) * 7.448451f * ((float)Math.PI / 180) + (float)Math.PI);
            float g = Mth.cos((float)(this.getUniqueFlapTickOffset() + this.tickCount + 1) * 7.448451f * ((float)Math.PI / 180) + (float)Math.PI);
            if (f > 0.0f && g <= 0.0f) {
                this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.PHANTOM_FLAP, this.getSoundSource(), 0.95f + this.random.nextFloat() * 0.05f, 0.95f + this.random.nextFloat() * 0.05f, false);
            }
            int i = this.getPhantomSize();
            float h = Mth.cos(this.getYRot() * ((float)Math.PI / 180)) * (1.3f + 0.21f * (float)i);
            float j = Mth.sin(this.getYRot() * ((float)Math.PI / 180)) * (1.3f + 0.21f * (float)i);
            float k = (0.3f + f * 0.45f) * ((float)i * 0.2f + 1.0f);
            this.level().addParticle(ParticleTypes.MYCELIUM, this.getX() + (double)h, this.getY() + (double)k, this.getZ() + (double)j, 0.0, 0.0, 0.0);
            this.level().addParticle(ParticleTypes.MYCELIUM, this.getX() - (double)h, this.getY() + (double)k, this.getZ() - (double)j, 0.0, 0.0, 0.0);
        }
    }

    @Override
    public void aiStep() {
        if (this.isAlive() && this.isSunBurnTick()) {
            this.setSecondsOnFire(8);
        }
        super.aiStep();
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataTag) {
        this.anchorPoint = this.blockPosition().above(5);
        this.setPhantomSize(0);
        return super.finalizeSpawn(level, difficulty, reason, spawnData, dataTag);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("AX")) {
            this.anchorPoint = new BlockPos(compound.getInt("AX"), compound.getInt("AY"), compound.getInt("AZ"));
        }
        this.setPhantomSize(compound.getInt("Size"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("AX", this.anchorPoint.getX());
        compound.putInt("AY", this.anchorPoint.getY());
        compound.putInt("AZ", this.anchorPoint.getZ());
        compound.putInt("Size", this.getPhantomSize());
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return true;
    }

    @Override
    public SoundSource getSoundSource() {
        return SoundSource.HOSTILE;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.PHANTOM_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.PHANTOM_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.PHANTOM_DEATH;
    }

    @Override
    public MobType getMobType() {
        return MobType.UNDEAD;
    }

    @Override
    protected float getSoundVolume() {
        return 1.0f;
    }

    @Override
    public boolean canAttackType(EntityType<?> entityType) {
        return true;
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        int i = this.getPhantomSize();
        EntityDimensions entityDimensions = super.getDimensions(pose);
        float f = (entityDimensions.width + 0.2f * (float)i) / entityDimensions.width;
        return entityDimensions.scale(f);
    }

    @Override
    public double getPassengersRidingOffset() {
        return this.getEyeHeight();
    }

    static enum AttackPhase {
        CIRCLE,
        SWOOP;

    }

    class PhantomMoveControl
            extends MoveControl {
        private float speed;

        public PhantomMoveControl(Mob mob) {
            super(mob);
            this.speed = 0.1f;
        }

        @Override
        public void tick() {
            if (irradiatedPhantom.this.horizontalCollision) {
                irradiatedPhantom.this.setYRot(irradiatedPhantom.this.getYRot() + 180.0f);
                this.speed = 0.1f;
            }
            double d = irradiatedPhantom.this.moveTargetPoint.x - irradiatedPhantom.this.getX();
            double e = irradiatedPhantom.this.moveTargetPoint.y - irradiatedPhantom.this.getY();
            double f = irradiatedPhantom.this.moveTargetPoint.z - irradiatedPhantom.this.getZ();
            double g = Math.sqrt(d * d + f * f);
            if (Math.abs(g) > (double)1.0E-5f) {
                double h = 1.0 - Math.abs(e * (double)0.7f) / g;
                g = Math.sqrt((d *= h) * d + (f *= h) * f);
                double i = Math.sqrt(d * d + f * f + e * e);
                float j = irradiatedPhantom.this.getYRot();
                float k = (float)Mth.atan2(f, d);
                float l = Mth.wrapDegrees(irradiatedPhantom.this.getYRot() + 90.0f);
                float m = Mth.wrapDegrees(k * 57.295776f);
                irradiatedPhantom.this.setYRot(Mth.approachDegrees(l, m, 4.0f) - 90.0f);
                irradiatedPhantom.this.yBodyRot = irradiatedPhantom.this.getYRot();
                this.speed = Mth.degreesDifferenceAbs(j, irradiatedPhantom.this.getYRot()) < 3.0f ? Mth.approach(this.speed, 1.8f, 0.005f * (1.8f / this.speed)) : Mth.approach(this.speed, 0.2f, 0.025f);
                float n = (float)(-(Mth.atan2(-e, g) * 57.2957763671875));
                irradiatedPhantom.this.setXRot(n);
                float o = irradiatedPhantom.this.getYRot() + 90.0f;
                double p = (double)(this.speed * Mth.cos(o * ((float)Math.PI / 180))) * Math.abs(d / i);
                double q = (double)(this.speed * Mth.sin(o * ((float)Math.PI / 180))) * Math.abs(f / i);
                double r = (double)(this.speed * Mth.sin(n * ((float)Math.PI / 180))) * Math.abs(e / i);
                Vec3 vec3 = irradiatedPhantom.this.getDeltaMovement();
                irradiatedPhantom.this.setDeltaMovement(vec3.add(new Vec3(p, r, q).subtract(vec3).scale(0.2)));
            }
        }
    }

    class PhantomLookControl
            extends LookControl {
        public PhantomLookControl(Mob mob) {
            super(mob);
        }

        @Override
        public void tick() {
        }
    }

    class PhantomBodyRotationControl
            extends BodyRotationControl {
        public PhantomBodyRotationControl(Mob mob) {
            super(mob);
        }

        @Override
        public void clientTick() {
            irradiatedPhantom.this.yHeadRot = irradiatedPhantom.this.yBodyRot;
            irradiatedPhantom.this.yBodyRot = irradiatedPhantom.this.getYRot();
        }
    }

    class PhantomAttackStrategyGoal
            extends Goal {
        private int nextSweepTick;

        PhantomAttackStrategyGoal() {
        }

        @Override
        public boolean canUse() {
            LivingEntity livingEntity = irradiatedPhantom.this.getTarget();
            if (livingEntity != null) {
                return irradiatedPhantom.this.canAttack(livingEntity, TargetingConditions.DEFAULT);
            }
            return false;
        }

        @Override
        public void start() {
            this.nextSweepTick = this.adjustedTickDelay(10);
            irradiatedPhantom.this.attackPhase = AttackPhase.CIRCLE;
            this.setAnchorAboveTarget();
        }

        @Override
        public void stop() {
            irradiatedPhantom.this.anchorPoint = irradiatedPhantom.this.level().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, irradiatedPhantom.this.anchorPoint).above(10 + irradiatedPhantom.this.random.nextInt(20));
        }

        @Override
        public void tick() {
            if (irradiatedPhantom.this.attackPhase == AttackPhase.CIRCLE) {
                --this.nextSweepTick;
                if (this.nextSweepTick <= 0) {
                    irradiatedPhantom.this.attackPhase = AttackPhase.SWOOP;
                    this.setAnchorAboveTarget();
                    this.nextSweepTick = this.adjustedTickDelay((8 + irradiatedPhantom.this.random.nextInt(4)) * 20);
                    irradiatedPhantom.this.playSound(SoundEvents.PHANTOM_SWOOP, 10.0f, 0.95f + irradiatedPhantom.this.random.nextFloat() * 0.1f);
                }
            }
        }

        private void setAnchorAboveTarget() {
            irradiatedPhantom.this.anchorPoint = irradiatedPhantom.this.getTarget().blockPosition().above(20 + irradiatedPhantom.this.random.nextInt(20));
            if (irradiatedPhantom.this.anchorPoint.getY() < irradiatedPhantom.this.level().getSeaLevel()) {
                irradiatedPhantom.this.anchorPoint = new BlockPos(irradiatedPhantom.this.anchorPoint.getX(), irradiatedPhantom.this.level().getSeaLevel() + 1, irradiatedPhantom.this.anchorPoint.getZ());
            }
        }
    }

    class PhantomSweepAttackGoal
            extends PhantomMoveTargetGoal {
        private static final int CAT_SEARCH_TICK_DELAY = 20;
        private boolean isScaredOfCat;
        private int catSearchTick;

        PhantomSweepAttackGoal() {
        }

        @Override
        public boolean canUse() {
            return irradiatedPhantom.this.getTarget() != null && irradiatedPhantom.this.attackPhase == AttackPhase.SWOOP;
        }

        @Override
        public boolean canContinueToUse() {
            LivingEntity livingEntity = irradiatedPhantom.this.getTarget();
            if (livingEntity == null) {
                return false;
            }
            if (!livingEntity.isAlive()) {
                return false;
            }
            if (livingEntity instanceof Player) {
                Player player = (Player)livingEntity;
                if (livingEntity.isSpectator() || player.isCreative()) {
                    return false;
                }
            }
            if (!this.canUse()) {
                return false;
            }
            if (irradiatedPhantom.this.tickCount > this.catSearchTick) {
                this.catSearchTick = irradiatedPhantom.this.tickCount + 20;
                this.isScaredOfCat = false;
            }
            return !this.isScaredOfCat;
        }

        @Override
        public void start() {
        }

        @Override
        public void stop() {
            irradiatedPhantom.this.setTarget(null);
            irradiatedPhantom.this.attackPhase = AttackPhase.CIRCLE;
        }

        @Override
        public void tick() {
            LivingEntity livingEntity = irradiatedPhantom.this.getTarget();
            if (livingEntity == null) {
                return;
            }
            irradiatedPhantom.this.moveTargetPoint = new Vec3(livingEntity.getX(), livingEntity.getY(0.5), livingEntity.getZ());
            if (irradiatedPhantom.this.getBoundingBox().inflate(0.2f).intersects(livingEntity.getBoundingBox())) {
                irradiatedPhantom.this.doHurtTarget(livingEntity);
                irradiatedPhantom.this.attackPhase = AttackPhase.CIRCLE;
                if (!irradiatedPhantom.this.isSilent()) {
                    irradiatedPhantom.this.level().levelEvent(1039, irradiatedPhantom.this.blockPosition(), 0);
                }
            } else if (irradiatedPhantom.this.horizontalCollision || irradiatedPhantom.this.hurtTime > 0) {
                irradiatedPhantom.this.attackPhase = AttackPhase.CIRCLE;
            }
        }
    }

    class PhantomCircleAroundAnchorGoal
            extends PhantomMoveTargetGoal {
        private float angle;
        private float distance;
        private float height;
        private float clockwise;

        PhantomCircleAroundAnchorGoal() {
        }

        @Override
        public boolean canUse() {
            return irradiatedPhantom.this.getTarget() == null || irradiatedPhantom.this.attackPhase == AttackPhase.CIRCLE;
        }

        @Override
        public void start() {
            this.distance = 5.0f + irradiatedPhantom.this.random.nextFloat() * 10.0f;
            this.height = -4.0f + irradiatedPhantom.this.random.nextFloat() * 9.0f;
            this.clockwise = irradiatedPhantom.this.random.nextBoolean() ? 1.0f : -1.0f;
            this.selectNext();
        }

        @Override
        public void tick() {
            if (irradiatedPhantom.this.random.nextInt(this.adjustedTickDelay(350)) == 0) {
                this.height = -4.0f + irradiatedPhantom.this.random.nextFloat() * 9.0f;
            }
            if (irradiatedPhantom.this.random.nextInt(this.adjustedTickDelay(250)) == 0) {
                this.distance += 1.0f;
                if (this.distance > 15.0f) {
                    this.distance = 5.0f;
                    this.clockwise = -this.clockwise;
                }
            }
            if (irradiatedPhantom.this.random.nextInt(this.adjustedTickDelay(450)) == 0) {
                this.angle = irradiatedPhantom.this.random.nextFloat() * 2.0f * (float)Math.PI;
                this.selectNext();
            }
            if (this.touchingTarget()) {
                this.selectNext();
            }
            if (irradiatedPhantom.this.moveTargetPoint.y < irradiatedPhantom.this.getY() && !irradiatedPhantom.this.level().isEmptyBlock(irradiatedPhantom.this.blockPosition().below(1))) {
                this.height = Math.max(1.0f, this.height);
                this.selectNext();
            }
            if (irradiatedPhantom.this.moveTargetPoint.y > irradiatedPhantom.this.getY() && !irradiatedPhantom.this.level().isEmptyBlock(irradiatedPhantom.this.blockPosition().above(1))) {
                this.height = Math.min(-1.0f, this.height);
                this.selectNext();
            }
        }

        private void selectNext() {
            if (BlockPos.ZERO.equals(irradiatedPhantom.this.anchorPoint)) {
                irradiatedPhantom.this.anchorPoint = irradiatedPhantom.this.blockPosition();
            }
            this.angle += this.clockwise * 15.0f * ((float)Math.PI / 180);
            irradiatedPhantom.this.moveTargetPoint = Vec3.atLowerCornerOf(irradiatedPhantom.this.anchorPoint).add(this.distance * Mth.cos(this.angle), -4.0f + this.height, this.distance * Mth.sin(this.angle));
        }
    }

    class PhantomAttackPlayerTargetGoal
            extends Goal {
        private final TargetingConditions attackTargeting = TargetingConditions.forCombat().range(64.0);
        private int nextScanTick = PhantomAttackPlayerTargetGoal.reducedTickDelay(20);

        PhantomAttackPlayerTargetGoal() {
        }

        @Override
        public boolean canUse() {
            if (this.nextScanTick > 0) {
                --this.nextScanTick;
                return false;
            }
            this.nextScanTick = PhantomAttackPlayerTargetGoal.reducedTickDelay(60);
            List<Player> list = irradiatedPhantom.this.level().getNearbyPlayers(this.attackTargeting, irradiatedPhantom.this, irradiatedPhantom.this.getBoundingBox().inflate(16.0, 64.0, 16.0));
            if (!list.isEmpty()) {
                list.sort(Comparator.comparing(Entity::getY).reversed());
                for (Player player : list) {
                    if (!irradiatedPhantom.this.canAttack(player, TargetingConditions.DEFAULT)) continue;
                    irradiatedPhantom.this.setTarget(player);
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean canContinueToUse() {
            LivingEntity livingEntity = irradiatedPhantom.this.getTarget();
            if (livingEntity != null) {
                return irradiatedPhantom.this.canAttack(livingEntity, TargetingConditions.DEFAULT);
            }
            return false;
        }
    }

    abstract class PhantomMoveTargetGoal
            extends Goal {
        public PhantomMoveTargetGoal() {
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        protected boolean touchingTarget() {
            return irradiatedPhantom.this.moveTargetPoint.distanceToSqr(irradiatedPhantom.this.getX(), irradiatedPhantom.this.getY(), irradiatedPhantom.this.getZ()) < 4.0;
        }
    }
}
