package net.nuclearteam.createnuclear.content.explosion;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.server.level.TicketType;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Explosion;
import net.nuclearteam.createnuclear.CNBlocks;
import net.nuclearteam.createnuclear.CNTags;
import net.nuclearteam.createnuclear.CNParticleRegistry;
import net.nuclearteam.createnuclear.foundation.damageTypes.CNDamageSources;
import net.nuclearteam.createnuclear.foundation.utility.Maths;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.UUID;

public class NuclearExplosionEntity extends Entity {
    private static final EntityDataAccessor<Float> SIZE =
        SynchedEntityData.defineId(NuclearExplosionEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Boolean> NO_GRIEFING =
        SynchedEntityData.defineId(NuclearExplosionEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> INTENTIONAL_GAME_DESIGN =
        SynchedEntityData.defineId(NuclearExplosionEntity.class, EntityDataSerializers.BOOLEAN);
    private static final TicketType<UUID> EXPLOSION_TICKET =
        TicketType.create("createnuclear:nuclear_explosion", UUID::compareTo);

    private boolean spawnedParticle;
    private final Stack<BlockPos> destroyingChunks = new Stack<>();
    private final Set<ChunkPos> ticketedChunks = new HashSet<>();
    private boolean loadingChunks;
    private boolean ticketsAcquired;
    private Explosion dummyExplosion;

    public NuclearExplosionEntity(EntityType<?> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }

    @Override
    public void tick() {
        super.tick();
        Level world = level();
        int chunksAffected = getChunksAffected();
        int radius = chunksAffected * 15;

        if (!spawnedParticle) {
            spawnedParticle = true;
            int particleY = (int) Math.ceil(getY());
            while (particleY > world.getMinBuildHeight()
                && particleY > getY() - radius / 2F
                && isDestroyable(world.getBlockState(BlockPos.containing(getX(), particleY, getZ())))) {
                particleY--;
            }
            world.addAlwaysVisibleParticle(
                CNParticleRegistry.NUCLEAR_MUSHROOM_CLOUD.get(), true,
                getX(), particleY + 2, getZ(), getSize(),
                isIntentionalGameDesign() ? 1.0F : 0.0F, 0
            );
        }

        if (tickCount > 40 && destroyingChunks.isEmpty()) {
            remove(RemovalReason.DISCARDED);
            return;
        }

        if (!world.isClientSide && !isNoGriefing()) {
            if (!ticketsAcquired && !isRemoved()) {
                loadingChunks = true;
                loadChunksAround(true);
                ticketsAcquired = true;
            }

            if (destroyingChunks.isEmpty()) {
                BlockPos center = blockPosition();
                for (int x = -chunksAffected; x <= chunksAffected; x++) {
                    for (int y = -chunksAffected; y <= chunksAffected; y++) {
                        for (int z = -chunksAffected; z <= chunksAffected; z++) {
                            destroyingChunks.push(center.offset(x * 16, y * 16, z * 16));
                        }
                    }
                }
                destroyingChunks.sort((first, second) -> Double.compare(
                    second.distManhattan(blockPosition()),
                    first.distManhattan(blockPosition())
                ));
            } else {
                int tickChunkCount = Math.min(destroyingChunks.size(), 3);
                for (int i = 0; i < tickChunkCount; i++) {
                    removeChunk(radius);
                }
            }
        }

        AABB killBox = getBoundingBox().inflate(radius + radius * 0.5F, radius * 0.6, radius + radius * 0.5F);
        float flingStrength = getSize() * 0.33F;
        float maximumDistance = radius + radius * 0.5F + 1;

        for (LivingEntity entity : world.getEntitiesOfClass(LivingEntity.class, killBox)) {
            float distance = entity.distanceTo(this);
            float damage = calculateDamage(distance, maximumDistance);
            Vec3 direction = entity.position().subtract(position()).add(0, 0.3, 0).normalize();
            float entityFling = entity instanceof Player ? 0.5F * flingStrength : flingStrength;

            if (damage > 0 && entity.getType().is(CNTags.CNEntityTypeTags.IRRADIATED_IMMUNE.tag)) {
                damage *= 0.25F;
                entityFling *= 0.1F;
            }
            if (damage > 0) {
                entity.hurt(CNDamageSources.radiation(world), damage);
            }
            entity.setDeltaMovement(direction.scale(damage * 0.1F * entityFling));
        }
    }

    @Override
    public void remove(Entity.RemovalReason removalReason) {
        if (!level().isClientSide && ticketsAcquired) {
            loadChunksAround(false);
            ticketsAcquired = false;
            loadingChunks = false;
        }
        super.remove(removalReason);
    }

    private int getChunksAffected() {
        return (int) Math.ceil(getSize());
    }

    private void loadChunksAround(boolean load) {
        if (!(level() instanceof ServerLevel serverWorld)) return;

        ServerChunkCache chunkManager = serverWorld.getChunkSource();
        UUID ticketArgument = getUUID();
        if (!load) {
            for (ChunkPos chunk : ticketedChunks) {
                chunkManager.removeRegionTicket(EXPLOSION_TICKET, chunk, 0, ticketArgument);
            }
            ticketedChunks.clear();
            return;
        }

        ChunkPos center = new ChunkPos(blockPosition());
        int distance = Math.max(
            getChunksAffected(),
            serverWorld.getServer().getPlayerList().getViewDistance() / 2
        );
        for (int x = -distance; x <= distance; x++) {
            for (int z = -distance; z <= distance; z++) {
                ChunkPos target = new ChunkPos(center.x + x, center.z + z);
                if (ticketedChunks.add(target)) {
                    chunkManager.addRegionTicket(EXPLOSION_TICKET, target, 0, ticketArgument);
                }
            }
        }
    }

    private float calculateDamage(float distance, float maximumDistance) {
        float remaining = (maximumDistance - distance) / maximumDistance;
        float baseDamage = getSize() <= 1.5F ? 100 : 100 + (getSize() - 1.5F) * 400;
        return remaining * baseDamage;
    }

    private void removeChunk(int radius) {
        Level world = level();
        BlockPos chunkCorner = destroyingChunks.pop();
        BlockPos.MutableBlockPos carve = new BlockPos.MutableBlockPos().set(chunkCorner);
        BlockPos.MutableBlockPos carveBelow = new BlockPos.MutableBlockPos().set(chunkCorner);

        if (dummyExplosion == null) {
            dummyExplosion = new Explosion(world, null, getX(), getY(), getZ(), 10.0F, List.of());
        }

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = 15; y >= 0; y--) {
                    boolean canSetToFire = false;
                    carve.set(
                        chunkCorner.getX() + x,
                        Mth.clamp(chunkCorner.getY() + y, world.getMinBuildHeight(), world.getMaxBuildHeight()),
                        chunkCorner.getZ() + z
                    );
                    float noise = (Maths.sampleNoise3D(carve.getX(), carve.getY(), carve.getZ(), radius) - 0.5F)
                        * 0.45F + 0.55F;
                    double yDistance = Maths.smin(
                        0.6F - Math.abs(blockPosition().getY() - carve.getY()) / (float) radius,
                        0.6F,
                        0.2F
                    );
                    double distanceToCenter = carve.distToLowCornerSqr(
                        blockPosition().getX(), carve.getY() - 1, blockPosition().getZ()
                    );
                    double targetRadius = yDistance * (radius + noise * radius) * radius;

                    if (distanceToCenter <= targetRadius) {
                        BlockState state = world.getBlockState(carve);
                        if ((!state.isAir() || !state.getFluidState().isEmpty()) && isDestroyable(state)) {
                            carveBelow.set(carve.getX(), carve.getY() - 1, carve.getZ());
                            canSetToFire = true;

                            BlockPos immutablePos = carve.immutable();
                            Block destroyedBlock = state.getBlock();
                            if (world.getBlockState(immutablePos).is(destroyedBlock)) {
                                world.destroyBlock(immutablePos, true, null, 512);
                            }
                            destroyedBlock.wasExploded(world, immutablePos, dummyExplosion);
                        }
                    }
                    if (canSetToFire && random.nextFloat() < 0.15F && !world.getBlockState(carveBelow).isAir()) {
                        world.setBlockAndUpdate(carveBelow.above(), CNBlocks.ENRICHING_FIRE.get().defaultBlockState());
                    }
                }
            }
        }
    }

    private boolean isDestroyable(BlockState state) {
        return state.getBlock().getExplosionResistance() < 3_600_000;
    }

    @Override
    protected void defineSynchedData() {
        entityData.define(SIZE, 1.0F);
        entityData.define(NO_GRIEFING, false);
        entityData.define(INTENTIONAL_GAME_DESIGN, false);
    }

    public float getSize() {
        return entityData.get(SIZE);
    }

    public void setSize(float size) {
        entityData.set(SIZE, size);
    }

    public boolean isNoGriefing() {
        return entityData.get(NO_GRIEFING);
    }

    public void setNoGriefing(boolean noGriefing) {
        entityData.set(NO_GRIEFING, noGriefing);
    }

    public boolean isIntentionalGameDesign() {
        return entityData.get(INTENTIONAL_GAME_DESIGN);
    }

    public void setIntentionalGameDesign(boolean intentionalGameDesign) {
        entityData.set(INTENTIONAL_GAME_DESIGN, intentionalGameDesign);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag nbt) {
        if (nbt.contains("Size")) setSize(nbt.getFloat("Size"));
        if (nbt.contains("NoGriefing")) setNoGriefing(nbt.getBoolean("NoGriefing"));
        if (nbt.contains("IntentionalGameDesign")) {
            setIntentionalGameDesign(nbt.getBoolean("IntentionalGameDesign"));
        }
        loadingChunks = nbt.getBoolean("WasLoadingChunks");
        ticketsAcquired = false;
        ticketedChunks.clear();
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag nbt) {
        nbt.putFloat("Size", getSize());
        nbt.putBoolean("NoGriefing", isNoGriefing());
        nbt.putBoolean("IntentionalGameDesign", isIntentionalGameDesign());
        nbt.putBoolean("WasLoadingChunks", loadingChunks || ticketsAcquired);
    }
}
