package net.nuclearteam.createnuclear.content.contraptions.irradiated.cat;

import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.VariantHolder;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.target.NonTameRandomTargetGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.CatVariant;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.CatVariantTags;
import net.minecraft.tags.StructureTags;
import net.minecraft.tags.TagKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.InteractionHand;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.EntityGetter;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.*;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class IrradiatedCat extends TamableAnimal implements VariantHolder<CatVariant> {
    public static final double TEMPT_SPEED_MOD = 0.6;
    public static final double WALK_SPEED_MOD = 0.8;
    public static final double SPRINT_SPEED_MOD = 1.33;
    private static final Ingredient TEMPT_INGREDIENT;
    private static final EntityDataAccessor<CatVariant> DATA_VARIANT_ID;
    private static final EntityDataAccessor<Boolean> IS_LYING;
    private static final EntityDataAccessor<Boolean> RELAX_STATE_ONE;
    private static final EntityDataAccessor<Integer> DATA_COLLAR_COLOR;
    private CatAvoidEntityGoal<Player> avoidPlayersGoal;
    @Nullable
    private TemptGoal temptGoal;
    private float lieDownAmount;
    private float lieDownAmountO;
    private float lieDownAmountTail;
    private float lieDownAmountOTail;
    private float relaxStateOneAmount;
    private float relaxStateOneAmountO;

    public IrradiatedCat(EntityType<? extends IrradiatedCat> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public EntityGetter method_48926() {
        return this.level();
    }

    public ResourceLocation getResourceLocation() {
        return this.getVariant().texture();
    }

    protected void initGoals() {
        this.temptGoal = new CatTemptGoal(this, 0.6, TEMPT_INGREDIENT, true);
        this.goalSelector.add(1, new FloatGoal(this));
        this.goalSelector.add(1, new PanicGoal(this, 1.5));
        this.goalSelector.add(2, new SitWhenOrderedToGoal(this));
        this.goalSelector.add(3, new CatRelaxOnOwnerGoal(this));
        this.goalSelector.add(4, this.temptGoal);
        this.goalSelector.add(5, new CatLieOnBedGoal(this, 1.1, 8));
        this.goalSelector.add(6, new FollowOwnerGoal(this, 1.0, 10.0F, 5.0F, false));
        this.goalSelector.add(7, new CatSitOnBlockGoal(this, 0.8));
        this.goalSelector.add(8, new LeapAtTargetGoal(this, 0.3F));
        this.goalSelector.add(9, new MeleeAttackGoal(this));
        this.goalSelector.add(10, new BreedGoal(this, 0.8));
        this.goalSelector.add(11, new WaterAvoidingRandomStrollGoal(this, 0.8, 1.0000001E-5F));
        this.goalSelector.add(12, new LookAtPlayerGoal(this, Player.class, 10.0F));
        this.targetSelector.add(1, new NonTameRandomTargetGoal<>(this, Rabbit.class, false, (Predicate)null));
        this.targetSelector.add(1, new NonTameRandomTargetGoal<>(this, Turtle.class, false, Turtle.BABY_TURTLE_ON_LAND_FILTER));
    }

    public CatVariant getVariant() {
        return (CatVariant)this.entityData.get(DATA_VARIANT_ID);
    }

    public void setVariant(CatVariant variant) {
        this.entityData.set(DATA_VARIANT_ID, variant);
    }

    public void setLying(boolean lying) {
        this.entityData.set(IS_LYING, lying);
    }

    public boolean isLying() {
        return (Boolean)this.entityData.get(IS_LYING);
    }

    public void setRelaxStateOne(boolean relaxStateOne) {
        this.entityData.set(RELAX_STATE_ONE, relaxStateOne);
    }

    public boolean isRelaxStateOne() {
        return (Boolean)this.entityData.get(RELAX_STATE_ONE);
    }

    public DyeColor getCollarColor() {
        return DyeColor.byId((Integer)this.entityData.get(DATA_COLLAR_COLOR));
    }

    public void setCollarColor(DyeColor color) {
        this.entityData.set(DATA_COLLAR_COLOR, color.getId());
    }

    protected void initDataTracker() {
        super.initDataTracker();
        this.entityData.define(DATA_VARIANT_ID, (CatVariant) BuiltInRegistries.CAT_VARIANT.getOrThrow(CatVariant.BLACK));
        this.entityData.define(IS_LYING, false);
        this.entityData.define(RELAX_STATE_ONE, false);
        this.entityData.define(DATA_COLLAR_COLOR, DyeColor.RED.getId());
    }

    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putString("variant", BuiltInRegistries.CAT_VARIANT.getKey(this.getVariant()).toString());
        compound.putByte("CollarColor", (byte)this.getCollarColor().getId());
    }

    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        CatVariant catVariant = (CatVariant)BuiltInRegistries.CAT_VARIANT.get(ResourceLocation.tryParse(compound.getString("variant")));
        if (catVariant != null) {
            this.setVariant(catVariant);
        }

        if (compound.contains("CollarColor", 99)) {
            this.setCollarColor(DyeColor.byId(compound.getInt("CollarColor")));
        }

    }

    public void mobTick() {
        if (this.getMoveControl().isMoving()) {
            double d = this.getMoveControl().getSpeed();
            if (d == 0.6) {
                this.setPose(Pose.CROUCHING);
                this.setSprinting(false);
            } else if (d == 1.33) {
                this.setPose(Pose.STANDING);
                this.setSprinting(true);
            } else {
                this.setPose(Pose.STANDING);
                this.setSprinting(false);
            }
        } else {
            this.setPose(Pose.STANDING);
            this.setSprinting(false);
        }

    }

    @Nullable
    protected SoundEvent getAmbientSound() {
        if (this.isTame()) {
            if (this.isInLove()) {
                return SoundEvents.ENTITY_CAT_PURR;
            } else {
                return this.random.nextInt(4) == 0 ? SoundEvents.ENTITY_CAT_PURREOW : SoundEvents.ENTITY_CAT_AMBIENT;
            }
        } else {
            return SoundEvents.ENTITY_CAT_STRAY_AMBIENT;
        }
    }

    public int getMinAmbientSoundDelay() {
        return 120;
    }

    public void hiss() {
        this.playSound(SoundEvents.ENTITY_CAT_HISS, this.getSoundVolume(), this.getSoundPitch());
    }

    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.ENTITY_CAT_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_CAT_DEATH;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0).add(Attributes.MOVEMENT_SPEED, 0.30000001192092896).add(Attributes.ATTACK_DAMAGE, 3.0);
    }

    protected void eat(Player player, InteractionHand hand, ItemStack stack) {
        if (this.isBreedingItem(stack)) {
            this.playSound(SoundEvents.ENTITY_CAT_EAT, 1.0F, 1.0F);
        }

        super.eat(player, hand, stack);
    }

    private float getAttackDamage() {
        return (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE);
    }

    public boolean tryAttack(Entity target) {
        return target.damage(this.getDamageSources().mobAttack(this), this.getAttackDamage());
    }

    public void tick() {
        super.tick();
        if (this.temptGoal != null && this.temptGoal.isActive() && !this.isTame() && this.age % 100 == 0) {
            this.playSound(SoundEvents.ENTITY_CAT_BEG_FOR_FOOD, 1.0F, 1.0F);
        }

        this.handleLieDown();
    }

    private void handleLieDown() {
        if ((this.isLying() || this.isRelaxStateOne()) && this.age % 5 == 0) {
            this.playSound(SoundEvents.ENTITY_CAT_PURR, 0.6F + 0.4F * (this.random.nextFloat() - this.random.nextFloat()), 1.0F);
        }

        this.updateLieDownAmount();
        this.updateRelaxStateOneAmount();
    }

    private void updateLieDownAmount() {
        this.lieDownAmountO = this.lieDownAmount;
        this.lieDownAmountOTail = this.lieDownAmountTail;
        if (this.isLying()) {
            this.lieDownAmount = Math.min(1.0F, this.lieDownAmount + 0.15F);
            this.lieDownAmountTail = Math.min(1.0F, this.lieDownAmountTail + 0.08F);
        } else {
            this.lieDownAmount = Math.max(0.0F, this.lieDownAmount - 0.22F);
            this.lieDownAmountTail = Math.max(0.0F, this.lieDownAmountTail - 0.13F);
        }

    }

    private void updateRelaxStateOneAmount() {
        this.relaxStateOneAmountO = this.relaxStateOneAmount;
        if (this.isRelaxStateOne()) {
            this.relaxStateOneAmount = Math.min(1.0F, this.relaxStateOneAmount + 0.1F);
        } else {
            this.relaxStateOneAmount = Math.max(0.0F, this.relaxStateOneAmount - 0.13F);
        }

    }

    public float getLieDownAmount(float partialTicks) {
        return Mth.lerp(partialTicks, this.lieDownAmountO, this.lieDownAmount);
    }

    public float getLieDownAmountTail(float partialTicks) {
        return Mth.lerp(partialTicks, this.lieDownAmountOTail, this.lieDownAmountTail);
    }

    public float getRelaxStateOneAmount(float partialTicks) {
        return Mth.lerp(partialTicks, this.relaxStateOneAmountO, this.relaxStateOneAmount);
    }

    @Nullable
    public net.minecraft.world.entity.animal.Cat createChild(ServerLevel level, AgeableMob otherParent) {
        net.minecraft.world.entity.animal.Cat cat = (Cat)EntityType.CAT.create(level);
        if (cat != null && otherParent instanceof net.minecraft.world.entity.animal.Cat cat2) {
            if (this.random.nextBoolean()) {
                cat.setVariant(this.getVariant());
            } else {
                cat.setVariant(cat2.getVariant());
            }

            if (this.isTame()) {
                cat.setOwnerUuid(this.getOwnerUuid());
                cat.setTamed(true);
                if (this.random.nextBoolean()) {
                    cat.setCollarColor(this.getCollarColor());
                } else {
                    cat.setCollarColor(cat2.getCollarColor());
                }
            }
        }

        return cat;
    }

    public boolean canBreedWith(Animal otherAnimal) {
        if (!this.isTame()) {
            return false;
        } else if (!(otherAnimal instanceof Cat)) {
            return false;
        } else {
            Cat cat = (Cat)otherAnimal;
            return cat.isTame() && super.canBreedWith(otherAnimal);
        }
    }

    @Nullable
    public SpawnGroupData initialize(ServerLevelAccessor level, Difficulty difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataTag) {
        spawnData = super.initialize(level, difficulty, reason, spawnData, dataTag);
        boolean bl = level.getMoonSize() > 0.9F;
        TagKey<CatVariant> tagKey = bl ? CatVariantTags.FULL_MOON_SPAWNS : CatVariantTags.DEFAULT_SPAWNS;
        BuiltInRegistries.CAT_VARIANT.getEntryList(tagKey).flatMap((named) -> {
            return named.getRandom(level.getRandom());
        }).ifPresent((holder) -> {
            this.setVariant((CatVariant)holder.value());
        });
        ServerLevel serverLevel = level.toServerWorld();
        if (serverLevel.getStructureAccessor().getStructureContaining(this.blockPosition(), StructureTags.CATS_SPAWN_AS_BLACK).hasChildren()) {
            this.setVariant((CatVariant)BuiltInRegistries.CAT_VARIANT.getOrThrow(CatVariant.ALL_BLACK));
            this.setPersistenceRequired();
        }

        return spawnData;
    }

    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        Item item = itemStack.getItem();
        if (this.level().isClientSide) {
            if (this.isTame() && this.isOwnedBy(player)) {
                return InteractionResult.SUCCESS;
            } else {
                return !this.isBreedingItem(itemStack) || !(this.getHealth() < this.getMaxHealth()) && this.isTame() ? InteractionResult.PASS : InteractionResult.SUCCESS;
            }
        } else {
            InteractionResult interactionResult;
            if (this.isTame()) {
                if (this.isOwnedBy(player)) {
                    if (!(item instanceof DyeItem)) {
                        if (item.isFood() && this.isBreedingItem(itemStack) && this.getHealth() < this.getMaxHealth()) {
                            this.eat(player, hand, itemStack);
                            this.heal((float)item.getFoodComponent().getHunger());
                            return InteractionResult.CONSUME;
                        }

                        interactionResult = super.mobInteract(player, hand);
                        if (!interactionResult.isAccepted() || this.isBaby()) {
                            this.setInSittingPose(!this.isInSittingPose());
                        }

                        return interactionResult;
                    }

                    DyeColor dyeColor = ((DyeItem)item).getColor();
                    if (dyeColor != this.getCollarColor()) {
                        this.setCollarColor(dyeColor);
                        if (!player.getAbilities().creativeMode) {
                            itemStack.decrement(1);
                        }

                        this.setPersistenceRequired();
                        return InteractionResult.CONSUME;
                    }
                }
            } else if (this.isBreedingItem(itemStack)) {
                this.eat(player, hand, itemStack);
                if (this.random.nextInt(3) == 0) {
                    this.setOwner(player);
                    this.setInSittingPose(true);
                    this.level().sendEntityStatus(this, (byte)7);
                } else {
                    this.level().sendEntityStatus(this, (byte)6);
                }

                this.setPersistenceRequired();
                return InteractionResult.CONSUME;
            }

            interactionResult = super.mobInteract(player, hand);
            if (interactionResult.isAccepted()) {
                this.setPersistenceRequired();
            }

            return interactionResult;
        }
    }

    public boolean isBreedingItem(ItemStack stack) {
        return TEMPT_INGREDIENT.test(stack);
    }

    protected float getActiveEyeHeight(Pose pose, EntityDimensions dimensions) {
        return dimensions.height * 0.5F;
    }

    public boolean canImmediatelyDespawn(double distanceToClosestPlayer) {
        return !this.isTame() && this.age > 2400;
    }

    protected void onTamedChanged() {
        if (this.avoidPlayersGoal == null) {
            this.avoidPlayersGoal = new CatAvoidEntityGoal<>(this, Player.class, 16.0F, 0.8, 1.33);
        }

        this.goalSelector.remove(this.avoidPlayersGoal);
        if (!this.isTame()) {
            this.goalSelector.add(4, this.avoidPlayersGoal);
        }

    }

    public boolean bypassesSteppingEffects() {
        return this.isInSneakingPose() || super.bypassesSteppingEffects();
    }

    static {
        TEMPT_INGREDIENT = Ingredient.of(new ItemLike[]{Items.COD, Items.SALMON});
        DATA_VARIANT_ID = SynchedEntityData.registerData(Cat.class, EntityDataSerializers.CAT_VARIANT);
        IS_LYING = SynchedEntityData.registerData(Cat.class, EntityDataSerializers.BOOLEAN);
        RELAX_STATE_ONE = SynchedEntityData.registerData(Cat.class, EntityDataSerializers.BOOLEAN);
        DATA_COLLAR_COLOR = SynchedEntityData.registerData(Cat.class, EntityDataSerializers.INTEGER);
    }

    private static class CatTemptGoal extends TemptGoal {
        @Nullable
        private Player selectedPlayer;
        private final IrradiatedCat cat;

        public CatTemptGoal(IrradiatedCat cat, double speedModifier, Ingredient items, boolean canScare) {
            super(cat, speedModifier, items, canScare);
            this.cat = cat;
        }

        public void tick() {
            super.tick();
            if (this.selectedPlayer == null && this.mob.getRandom().nextInt(this.adjustedTickDelay(600)) == 0) {
                this.selectedPlayer = this.closestPlayer;
            } else if (this.mob.getRandom().nextInt(this.adjustedTickDelay(500)) == 0) {
                this.selectedPlayer = null;
            }

        }

        protected boolean canBeScared() {
            return this.selectedPlayer != null && this.selectedPlayer.equals(this.closestPlayer) ? false : super.canBeScared();
        }

        public boolean canUse() {
            return super.canUse() && !this.cat.isTame();
        }
    }

    static class CatRelaxOnOwnerGoal extends Goal {
        private final IrradiatedCat cat;
        @Nullable
        private Player ownerPlayer;
        @Nullable
        private BlockPos goalPos;
        private int onBedTicks;

        public CatRelaxOnOwnerGoal(IrradiatedCat cat) {
            this.cat = cat;
        }

        public boolean canUse() {
            if (!this.cat.isTame()) {
                return false;
            } else if (this.cat.isInSittingPose()) {
                return false;
            } else {
                LivingEntity livingEntity = this.cat.getOwner();
                if (livingEntity instanceof Player) {
                    this.ownerPlayer = (Player)livingEntity;
                    if (!livingEntity.isSleeping()) {
                        return false;
                    }

                    if (this.cat.squaredDistanceTo(this.ownerPlayer) > 100.0) {
                        return false;
                    }

                    BlockPos blockPos = this.ownerPlayer.blockPosition();
                    BlockState blockState = this.cat.level().getBlockState(blockPos);
                    if (blockState.is(BlockTags.BEDS)) {
                        this.goalPos = (BlockPos)blockState.getOrEmpty(BedBlock.FACING).map((direction) -> {
                            return blockPos.offset(direction.getOpposite());
                        }).orElseGet(() -> {
                            return new BlockPos(blockPos);
                        });
                        return !this.spaceIsOccupied();
                    }
                }

                return false;
            }
        }

        private boolean spaceIsOccupied() {
            assert this.goalPos != null;
            List<IrradiatedCat> list = this.cat.level().getNonSpectatingEntities(IrradiatedCat.class, (new AABB(this.goalPos)).expand(2.0));
            Iterator var2 = list.iterator();

            IrradiatedCat cat;
            do {
                do {
                    if (!var2.hasNext()) {
                        return false;
                    }

                    cat = (IrradiatedCat)var2.next();
                } while(cat == this.cat);
            } while(!cat.isLying() && !cat.isRelaxStateOne());

            return true;
        }

        public boolean shouldContinue() {
            return this.cat.isTame() && !this.cat.isInSittingPose() && this.ownerPlayer != null && this.ownerPlayer.isSleeping() && this.goalPos != null && !this.spaceIsOccupied();
        }

        public void start() {
            if (this.goalPos != null) {
                this.cat.setInSittingPose(false);
                this.cat.getNavigation().startMovingTo((double)this.goalPos.getX(), (double)this.goalPos.getY(), (double)this.goalPos.getZ(), 1.100000023841858);
            }

        }

        public void stop() {
            this.cat.setLying(false);
            float f = this.cat.level().getSkyAngle(1.0F);
            if (this.ownerPlayer.getSleepTimer() >= 100 && (double)f > 0.77 && (double)f < 0.8 && (double)this.cat.level().getRandom().nextFloat() < 0.7) {
                this.giveMorningGift();
            }

            this.onBedTicks = 0;
            this.cat.setRelaxStateOne(false);
            this.cat.getNavigation().stop();
        }

        private void giveMorningGift() {
            RandomSource randomSource = this.cat.getRandom();
            BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
            mutableBlockPos.set(this.cat.isLeashed() ? this.cat.getHoldingEntity().blockPosition() : this.cat.blockPosition());
            this.cat.teleport((double)(mutableBlockPos.getX() + randomSource.nextInt(11) - 5), (double)(mutableBlockPos.getY() + randomSource.nextInt(5) - 2), (double)(mutableBlockPos.getZ() + randomSource.nextInt(11) - 5), false);
            mutableBlockPos.set(this.cat.blockPosition());
            LootTable lootTable = this.cat.level().getServer().getLootManager().getLootTable(BuiltInLootTables.CAT_MORNING_GIFT_GAMEPLAY);
            LootContext lootParams = (new LootContext.Builder((ServerLevel)this.cat.level())).add(LootContextParams.ORIGIN, this.cat.position()).add(LootContextParams.THIS_ENTITY, this.cat).build(LootContextParamSets.GIFT);
            List<ItemStack> list = lootTable.generateLoot(lootParams);
            Iterator var6 = list.iterator();

            while(var6.hasNext()) {
                ItemStack itemStack = (ItemStack)var6.next();
                this.cat.level().spawnEntity(new ItemEntity(this.cat.level(), (double)mutableBlockPos.getX() - (double)Mth.sin(this.cat.bodyYaw * 0.017453292F), (double)mutableBlockPos.getY(), (double)mutableBlockPos.getZ() + (double)Mth.cos(this.cat.bodyYaw * 0.017453292F), itemStack));
            }

        }

        public void tick() {
            if (this.ownerPlayer != null && this.goalPos != null) {
                this.cat.setInSittingPose(false);
                this.cat.getNavigation().startMovingTo((double)this.goalPos.getX(), (double)this.goalPos.getY(), (double)this.goalPos.getZ(), 1.100000023841858);
                if (this.cat.squaredDistanceTo(this.ownerPlayer) < 2.5) {
                    ++this.onBedTicks;
                    if (this.onBedTicks > this.adjustedTickDelay(16)) {
                        this.cat.setLying(true);
                        this.cat.setRelaxStateOne(false);
                    } else {
                        this.cat.lookAtEntity(this.ownerPlayer, 45.0F, 45.0F);
                        this.cat.setRelaxStateOne(true);
                    }
                } else {
                    this.cat.setLying(false);
                }
            }

        }
    }

    static class CatAvoidEntityGoal<T extends LivingEntity> extends AvoidEntityGoal<T> {
        private final IrradiatedCat cat;

        public CatAvoidEntityGoal(IrradiatedCat cat, Class entityClassToAvoid, float maxDist, double walkSpeedModifier, double sprintSpeedModifier) {
            super(cat, entityClassToAvoid, maxDist, walkSpeedModifier, sprintSpeedModifier);
            Predicate var10006 = EntitySelector.EXCEPT_CREATIVE_OR_SPECTATOR;
            Objects.requireNonNull(var10006);
            this.cat = cat;
        }

        public boolean canUse() {
            return !this.cat.isTame() && super.canUse();
        }

        public boolean shouldContinue() {
            return !this.cat.isTame() && super.shouldContinue();
        }
    }
}
