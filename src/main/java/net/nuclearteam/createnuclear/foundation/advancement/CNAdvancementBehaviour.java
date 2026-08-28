package net.nuclearteam.createnuclear.foundation.advancement;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.fabricmc.fabric.api.entity.FakePlayer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class CNAdvancementBehaviour extends BlockEntityBehaviour {
    public static final BehaviourType<CNAdvancementBehaviour> TYPE = new BehaviourType<>();

    private UUID playerId;
    private final Set<CreateNuclearAdvancement> advancements;

    public CNAdvancementBehaviour(SmartBlockEntity be, CreateNuclearAdvancement ...advancement) {
        super(be);
        this.advancements = new HashSet<>();
        add(advancement);
    }

    public void add(CreateNuclearAdvancement ...advancement) {
        this.advancements.addAll(Arrays.asList(advancement));
    }

    public boolean isOwnerPresent() {
        return playerId != null;
    }

    public void setPlayer(UUID id) {
        Player player = getWorld().getPlayerByUuid(id);
        if (player == null) return;
        playerId = id;
        removeAwarded();
        blockEntity.setChanged();
    }

    @Override
    public void initialize() {
        super.initialize();
        removeAwarded();
    }

    private void removeAwarded() {
        Player player = getPlayer();
        if (player == null) return;

        advancements.removeIf(c -> c.isAlreadyAwardedTo(player));
        if (advancements.isEmpty()) {
            playerId = null;
            blockEntity.setChanged();
        }
    }

    public void awardPlayer(CreateNuclearAdvancement advancement) {
        Player player = getPlayer();
        if (player == null)
            return;
        award(advancement, player);
    }

    public static void setPlacedBy(Level worldIn, BlockPos pos, LivingEntity placer) {
        CNAdvancementBehaviour behaviour = BlockEntityBehaviour.get(worldIn, pos, TYPE);
        if (behaviour == null)
            return;
        if (placer instanceof FakePlayer)
            return;
        if (placer instanceof ServerPlayer)
            behaviour.setPlayer(placer.getUUID());
    }

    @Override
    public void write(CompoundTag nbt, boolean clientPacket) {
        super.write(nbt, clientPacket);
        if (playerId != null)
            nbt.putUuid("Owner", playerId);
    }

    @Override
    public void read(CompoundTag nbt, boolean clientPacket) {
        super.read(nbt, clientPacket);
        if (nbt.contains("Owner"))
            playerId = nbt.getUuid("Owner");
    }

    @Override
    public BehaviourType<?> getType() {
        return TYPE;
    }

    private Player getPlayer() {
        if (playerId == null) return null;
        return getWorld().getPlayerByUuid(playerId);
    }

    private void award(CreateNuclearAdvancement advancement, Player player) {
        if (!advancement.isAlreadyAwardedTo(player)) advancement.awardTo(player);
        removeAwarded();
    }

    @Override
    public String toString() {
        return "CNAdvancementBehaviour: [playerId => " + playerId + ", advancement => " + advancements.toArray() + "]";
    }
}
