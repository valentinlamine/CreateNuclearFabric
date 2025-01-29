package net.nuclearteam.createnuclear.entity.irradiatedpig;

import com.google.common.collect.UnmodifiableIterator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.nuclearteam.createnuclear.entity.CNMobEntityType;
import org.jetbrains.annotations.Nullable;

public class IrradiatedPig extends Animal implements ItemSteerable, Saddleable {
    private static final EntityDataAccessor<Boolean> DATA_SADDLE_ID;
    private static final EntityDataAccessor<Integer> DATA_BOOST_TIME;
    private static final Ingredient FOOD_ITEMS;
    private final ItemBasedSteering steering;

    public IrradiatedPig(EntityType<? extends IrradiatedPig> entityType, Level level) {
        super(entityType, level);
        this.steering = new ItemBasedSteering(this.entityData, DATA_BOOST_TIME, DATA_SADDLE_ID);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.25));
        this.goalSelector.addGoal(3, new BreedGoal(this, 1.0));
        this.goalSelector.addGoal(4, new TemptGoal(this, 1.2, Ingredient.of(new ItemLike[]{Items.CARROT_ON_A_STICK}), false));
        this.goalSelector.addGoal(4, new TemptGoal(this, 1.2, FOOD_ITEMS, false));
        this.goalSelector.addGoal(5, new FollowParentGoal(this, 1.1));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0).add(Attributes.MOVEMENT_SPEED, 0.25);
    }

    @Nullable
    public LivingEntity getControllingPassenger() {
        if (this.isSaddled()) {
            Entity var2 = this.getFirstPassenger();
            if (var2 instanceof Player) {
                Player player = (Player)var2;
                if (player.getMainHandItem().is(Items.CARROT_ON_A_STICK) || player.getOffhandItem().is(Items.CARROT_ON_A_STICK)) {
                    return player;
                }
            }
        }

        return null;
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        if (DATA_BOOST_TIME.equals(key) && this.level().isClientSide) {
            this.steering.onSynced();
        }

        super.onSyncedDataUpdated(key);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_SADDLE_ID, false);
        this.entityData.define(DATA_BOOST_TIME, 0);
    }

    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        this.steering.addAdditionalSaveData(compound);
    }

    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.steering.readAdditionalSaveData(compound);
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.PIG_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.PIG_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.PIG_DEATH;
    }

    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.PIG_STEP, 0.15F, 1.0F);
    }

    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        boolean bl = this.isFood(player.getItemInHand(hand));
        if (!bl && this.isSaddled() && !this.isVehicle() && !player.isSecondaryUseActive()) {
            if (!this.level().isClientSide) {
                player.startRiding(this);
            }

            return InteractionResult.sidedSuccess(this.level().isClientSide);
        } else {
            InteractionResult interactionResult = super.mobInteract(player, hand);
            if (!interactionResult.consumesAction()) {
                ItemStack itemStack = player.getItemInHand(hand);
                return itemStack.is(Items.SADDLE) ? itemStack.interactLivingEntity(player, this, hand) : InteractionResult.PASS;
            } else {
                return interactionResult;
            }
        }
    }

    public boolean isSaddleable() {
        return this.isAlive() && !this.isBaby();
    }

    protected void dropEquipment() {
        super.dropEquipment();
        if (this.isSaddled()) {
            this.spawnAtLocation(Items.SADDLE);
        }

    }

    public boolean isSaddled() {
        return this.steering.hasSaddle();
    }

    public void equipSaddle(@Nullable SoundSource source) {
        this.steering.setSaddle(true);
        if (source != null) {
            this.level().playSound((Player)null, this, SoundEvents.PIG_SADDLE, source, 0.5F, 1.0F);
        }

    }

    public Vec3 getDismountLocationForPassenger(LivingEntity passenger) {
        Direction direction = this.getMotionDirection();
        if (direction.getAxis() == Direction.Axis.Y) {
            return super.getDismountLocationForPassenger(passenger);
        } else {
            int[][] is = DismountHelper.offsetsForDirection(direction);
            BlockPos blockPos = this.blockPosition();
            BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
            UnmodifiableIterator var6 = passenger.getDismountPoses().iterator();

            while(var6.hasNext()) {
                Pose pose = (Pose)var6.next();
                AABB aABB = passenger.getLocalBoundsForPose(pose);
                int[][] var9 = is;
                int var10 = is.length;

                for(int var11 = 0; var11 < var10; ++var11) {
                    int[] js = var9[var11];
                    mutableBlockPos.set(blockPos.getX() + js[0], blockPos.getY(), blockPos.getZ() + js[1]);
                    double d = this.level().getBlockFloorHeight(mutableBlockPos);
                    if (DismountHelper.isBlockFloorValid(d)) {
                        Vec3 vec3 = Vec3.upFromBottomCenterOf(mutableBlockPos, d);
                        if (DismountHelper.canDismountTo(this.level(), passenger, aABB.move(vec3))) {
                            passenger.setPose(pose);
                            return vec3;
                        }
                    }
                }
            }

            return super.getDismountLocationForPassenger(passenger);
        }
    }

    public void thunderHit(ServerLevel level, LightningBolt lightning) {
        if (level.getDifficulty() != Difficulty.PEACEFUL) {
            ZombifiedPiglin zombifiedPiglin = (ZombifiedPiglin)EntityType.ZOMBIFIED_PIGLIN.create(level);
            if (zombifiedPiglin != null) {
                zombifiedPiglin.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.GOLDEN_SWORD));
                zombifiedPiglin.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), this.getXRot());
                zombifiedPiglin.setNoAi(this.isNoAi());
                zombifiedPiglin.setBaby(this.isBaby());
                if (this.hasCustomName()) {
                    zombifiedPiglin.setCustomName(this.getCustomName());
                    zombifiedPiglin.setCustomNameVisible(this.isCustomNameVisible());
                }

                zombifiedPiglin.setPersistenceRequired();
                level.addFreshEntity(zombifiedPiglin);
                this.discard();
            } else {
                super.thunderHit(level, lightning);
            }
        } else {
            super.thunderHit(level, lightning);
        }

    }

    protected void tickRidden(Player player, Vec3 travelVector) {
        super.tickRidden(player, travelVector);
        this.setRot(player.getYRot(), player.getXRot() * 0.5F);
        this.yRotO = this.yBodyRot = this.yHeadRot = this.getYRot();
        this.steering.tickBoost();
    }

    protected Vec3 getRiddenInput(Player player, Vec3 travelVector) {
        return new Vec3(0.0, 0.0, 1.0);
    }

    protected float getRiddenSpeed(Player player) {
        return (float)(this.getAttributeValue(Attributes.MOVEMENT_SPEED) * 0.225 * (double)this.steering.boostFactor());
    }

    public boolean boost() {
        return this.steering.boost(this.getRandom());
    }

    @Nullable
    public IrradiatedPig getBreedOffspring(ServerLevel level, AgeableMob ageableMob) {
        return CNMobEntityType.IRRADIATED_PIG.create(level);
    }

    public boolean isFood(ItemStack stack) {
        return FOOD_ITEMS.test(stack);
    }

    public Vec3 getLeashOffset() {
        return new Vec3(0.0, (double)(0.6F * this.getEyeHeight()), (double)(this.getBbWidth() * 0.4F));
    }

    static {
        DATA_SADDLE_ID = SynchedEntityData.defineId(IrradiatedPig.class, EntityDataSerializers.BOOLEAN);
        DATA_BOOST_TIME = SynchedEntityData.defineId(IrradiatedPig.class, EntityDataSerializers.INT);
        FOOD_ITEMS = Ingredient.of(new ItemLike[]{Items.CARROT, Items.POTATO, Items.BEETROOT});
    }
}
