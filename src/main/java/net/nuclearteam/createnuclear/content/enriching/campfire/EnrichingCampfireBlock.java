package net.nuclearteam.createnuclear.content.enriching.campfire;


import com.simibubi.create.foundation.block.IBE;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.gameevent.GameEvent;
import net.nuclearteam.createnuclear.CNBlockEntityTypes;

import net.nuclearteam.createnuclear.CNEffects;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings({"deprecation", "NullableProblems", "unused"})
public class EnrichingCampfireBlock extends BaseEntityBlock
        implements SimpleWaterloggedBlock, IBE<EnrichingCampfireBlockEntity> {
    protected static final VoxelShape SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 7.0, 16.0);
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    private final boolean spawnParticles;

    public EnrichingCampfireBlock(boolean spawnParticles, int fireDamage, BlockBehaviour.Properties properties) {
        super(properties);
        this.spawnParticles = spawnParticles;
        this.setDefaultState(this.stateManager.defaultBlockState().setValue(LIT, true).setValue(WATERLOGGED, false).setValue(FACING, Direction.NORTH));
    }

    @Override
    public InteractionResult onUse(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        Direction face = hit.getDirection();
        ItemStack heldItem = player.getItemInHand(hand);

        if (face == Direction.DOWN) return InteractionResult.PASS;

        if (heldItem.is(ItemTags.SHOVELS)) {
            if (state.getValue(LIT)) {
                if (!level.isClientSide) {
                    level.syncWorldEvent(null, 1009, pos, 0);
                    EnrichingCampfireBlock.dowse(player, level, pos, state);
                    BlockState newState = state.setValue(LIT, false);
                    level.setBlock(pos, newState, 11);
                    level.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(player, newState));
                    heldItem.damage(1, player, p -> p.sendToolBreakStatus(hand));
                }
                return InteractionResult.success(level.isClientSide);
            }
        }

        if (heldItem.is(ItemTags.CREEPER_IGNITERS) && canLight(state)) {
            if (!level.isClientSide) {
                level.playSound(player, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0f, level.getRandom().nextFloat() * 0.4f + 0.8f);
                level.setBlock(pos, state.setValue(LIT, true), 11);
                level.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);
                heldItem.damage(1, player, p -> p.sendToolBreakStatus(hand));
            }
            return InteractionResult.success(level.isClientSide);
        }

        return InteractionResult.PASS;
    }

    @Override
    public void onEntityCollision(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (state.getValue(LIT) && entity instanceof LivingEntity && !EnchantmentHelper.hasFrostWalker((LivingEntity)entity)) {
            ((LivingEntity) entity).addEffect(new MobEffectInstance(CNEffects.RADIATION.get(), 100, 0));
        }
        super.onEntityCollision(state, level, pos, entity);
    }

    @Override
    @Nullable
    public BlockState getPlacementState(BlockPlaceContext context) {
        BlockPos blockPos;
        Level levelAccessor = context.getLevel();
        boolean bl = levelAccessor.getFluidState(context.getClickedPos()).getFluid() == Fluids.WATER;
        return this.defaultBlockState().setValue(WATERLOGGED, bl).setValue(LIT, !bl).setValue(FACING, context.getHorizontalPlayerFacing());
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(level));
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public RenderShape getRenderType(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public void randomDisplayTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (!state.getValue(LIT)) {
            return;
        }
        if (random.nextInt(10) == 0) {
            level.playSound((double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, SoundEvents.BLOCK_CAMPFIRE_CRACKLE, SoundSource.BLOCKS, 0.5f + random.nextFloat(), random.nextFloat() * 0.7f + 0.6f, false);
        }
        if (this.spawnParticles && random.nextInt(5) == 0) {
            for (int i = 0; i < random.nextInt(1) + 1; ++i) {
                level.addParticle(ParticleTypes.LAVA, (double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, random.nextFloat() / 2.0f, 5.0E-5, random.nextFloat() / 2.0f);
            }
        }
    }

    public static void dowse(@Nullable Entity entity, LevelAccessor level, BlockPos pos, BlockState state) {
        BlockEntity blockEntity;
        if (level.isClientSide()) {
            for (int i = 0; i < 20; ++i) {
                EnrichingCampfireBlock.makeParticles((Level)level, pos);
            }
        }
        if ((blockEntity = level.getBlockEntity(pos)) instanceof EnrichingCampfireBlockEntity) {
            ((EnrichingCampfireBlockEntity)blockEntity).dowse();
        }
        level.emitGameEvent(entity, GameEvent.BLOCK_CHANGE, pos);
    }

    @Override
    public boolean tryFillWithFluid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluidState) {
        if (!state.getValue(BlockStateProperties.WATERLOGGED) && fluidState.getFluid() == Fluids.WATER) {
            boolean bl = state.getValue(LIT);
            if (bl) {
                if (!level.isClientSide()) {
                    level.playSound(null, pos, SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, SoundSource.BLOCKS, 1.0f, 1.0f);
                }
                EnrichingCampfireBlock.dowse(null, level, pos, state);
            }
            level.setBlock(pos, state.setValue(WATERLOGGED, true).setValue(LIT, false), 3);
            level.scheduleFluidTick(pos, fluidState.getFluid(), fluidState.getFluid().getTickRate(level));
            return true;
        }
        return false;
    }

    @Override
    public void onProjectileHit(Level level, BlockState state, BlockHitResult hit, Projectile projectile) {
        BlockPos blockPos = hit.getClickedPos();
        if (!level.isClientSide && projectile.isOnFire() && projectile.canModifyAt(level, blockPos) && !state.getValue(LIT) && !state.getValue(WATERLOGGED)) {
            level.setBlock(blockPos, state.setValue(BlockStateProperties.LIT, true), 11);
        }
    }

    public static void makeParticles(Level level, BlockPos pos) {
        RandomSource randomSource = level.getRandom();
        level.addImportantParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, true, (double)pos.getX() + 0.5 + randomSource.nextDouble() / 3.0 * (double)(randomSource.nextBoolean() ? 1 : -1), (double)pos.getY() + randomSource.nextDouble() + randomSource.nextDouble(), (double)pos.getZ() + 0.5 + randomSource.nextDouble() / 3.0 * (double)(randomSource.nextBoolean() ? 1 : -1), 0.0, 0.07, 0.0);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        if (state.getValue(WATERLOGGED)) {
            return Fluids.WATER.getStill(false);
        }
        return super.getFluidState(state);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT, WATERLOGGED, FACING);
    }

    @Override
    public Class<EnrichingCampfireBlockEntity> getBlockEntityClass() {
        return EnrichingCampfireBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends EnrichingCampfireBlockEntity> getBlockEntityType() {
        return CNBlockEntityTypes.ENRICHING_CAMPFIRE_BLOCK.get();
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new EnrichingCampfireBlockEntity(CNBlockEntityTypes.ENRICHING_CAMPFIRE_BLOCK.get(), pos, state);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if (level.isClientSide) {
            if (state.getValue(LIT)) {
                return EnrichingCampfireBlock.checkType(blockEntityType, CNBlockEntityTypes.ENRICHING_CAMPFIRE_BLOCK.get(), EnrichingCampfireBlockEntity::particleTick);
            }
        }
        return null;
    }

    @Override
    public boolean canPathfindThrough(BlockState state, BlockGetter level, BlockPos pos, PathComputationType type) {
        return false;
    }

    public static boolean canLight(BlockState state) {
        return state.is(BlockTags.CAMPFIRES, s -> s.hasProperty(WATERLOGGED) && s.hasProperty(LIT)) && !state.getValue(WATERLOGGED) && !state.getValue(LIT);
    }
}
