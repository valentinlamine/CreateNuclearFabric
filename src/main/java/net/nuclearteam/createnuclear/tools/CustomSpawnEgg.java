package net.nuclearteam.createnuclear.tools;

import com.tterrag.registrate.util.entry.EntityEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.nuclearteam.createnuclear.CreateNuclear;
import org.jetbrains.annotations.Nullable;

public class CustomSpawnEgg extends SpawnEggItem {
    private final EntityEntry<? extends Mob> test;
    private final EntityType<?> defaultType;

    public CustomSpawnEgg(Properties properties, int backgroundColor, int highlightColor, EntityEntry<? extends Mob> test) {
        super(test.get(), backgroundColor, highlightColor, properties);
        this.test = test;
        this.defaultType = test.get();
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        BlockEntity blockEntity;
        Level level = context.getLevel();
        if (!(context.getLevel() instanceof ServerLevel)) {
            return InteractionResult.SUCCESS;
        }
        ItemStack itemStack = context.getItemInHand();
        BlockPos blockPos = context.getClickedPos();
        Direction direction = context.getClickedFace();
        BlockState blockState = level.getBlockState(blockPos);
        if (blockState.is(Blocks.SPAWNER) && (blockEntity = level.getBlockEntity(blockPos)) instanceof SpawnerBlockEntity) {
            SpawnerBlockEntity spawnerBlockEntity = (SpawnerBlockEntity)blockEntity;
            spawnerBlockEntity.setEntityId(EntityEntry.cast(this.test).lazy().get(), level.getRandom());
            blockEntity.setChanged();
            level.sendBlockUpdated(blockPos, blockState, blockState, 3);
            level.gameEvent((Entity)context.getPlayer(), GameEvent.BLOCK_CHANGE, blockPos);
            itemStack.shrink(1);
            return InteractionResult.CONSUME;
        }

        BlockPos blockPos2 = blockState.getCollisionShape(level, blockPos).isEmpty() ? blockPos : blockPos.relative(direction);

        if (EntityEntry.cast(this.test).lazy().get().spawn((ServerLevel) context.getLevel(), context.getItemInHand(), context.getPlayer(), blockPos2, MobSpawnType.SPAWN_EGG, true, true) != null) {
            itemStack.shrink(1);
            level.gameEvent((Entity)context.getPlayer(), GameEvent.ENTITY_PLACE, blockPos);
            return InteractionResult.SUCCESS;
        }
        return super.useOn(context);
    }

    public EntityType<?> getType(@Nullable CompoundTag nbt) {
        CompoundTag compoundTag;
        if (nbt != null && nbt.contains("EntityTag", 10) && (compoundTag = nbt.getCompound("EntityTag")).contains("id", 8)) {
            CreateNuclear.LOGGER.warn("getType: {}, {}", compoundTag.getString("id"), EntityType.byString(compoundTag.getString("id")));
            return EntityType.byString(compoundTag.getString("id")).orElse(this.defaultType);
        }
        return this.defaultType;
    }
}
