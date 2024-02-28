//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.ynov.createnuclear.tools;

import java.util.Objects;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Clearable;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEvent.Context;
import org.jetbrains.annotations.Nullable;

public class EnrichingCampfireBlockEntity extends BlockEntity implements Clearable {
    private static final int BURN_COOL_SPEED = 2;
    private static final int NUM_SLOTS = 4;
    private static NonNullList<ItemStack> items = null;
    private static int[] cookingProgress = new int[0];
    private static int[] cookingTime = new int[0];
    private final RecipeManager.CachedCheck<Container, CampfireCookingRecipe> quickCheck;

    public EnrichingCampfireBlockEntity(BlockPos pos, BlockState blockState) {
        super(BlockEntityType.CAMPFIRE, pos, blockState);
        items = NonNullList.withSize(4, ItemStack.EMPTY);
        cookingProgress = new int[4];
        cookingTime = new int[4];
        this.quickCheck = RecipeManager.createCheck(RecipeType.CAMPFIRE_COOKING);
    }

    public static void cooldownTick(Level level, BlockPos pos, BlockState state, CampfireBlockEntity blockEntity) {
        boolean bl = false;

        for(int i = 0; i < items.size(); ++i) {
            if (cookingProgress[i] > 0) {
                bl = true;
                cookingProgress[i] = Mth.clamp(cookingProgress[i] - 2, 0, cookingTime[i]);
            }
        }

        if (bl) {
            setChanged(level, pos, state);
        }

    }

    public static void particleTick(Level level, BlockPos pos, BlockState state, CampfireBlockEntity blockEntity) {
        RandomSource randomSource = level.random;
        int i;
        if (randomSource.nextFloat() < 0.11F) {
            for(i = 0; i < randomSource.nextInt(2) + 2; ++i) {
                CampfireBlock.makeParticles(level, pos, (Boolean)state.getValue(CampfireBlock.SIGNAL_FIRE), false);
            }
        }

        i = ((Direction)state.getValue(CampfireBlock.FACING)).get2DDataValue();

        for(int j = 0; j < items.size(); ++j) {
            if (!((ItemStack)items.get(j)).isEmpty() && randomSource.nextFloat() < 0.2F) {
                Direction direction = Direction.from2DDataValue(Math.floorMod(j + i, 4));
                float f = 0.3125F;
                double d = (double)pos.getX() + 0.5 - (double)((float)direction.getStepX() * 0.3125F) + (double)((float)direction.getClockWise().getStepX() * 0.3125F);
                double e = (double)pos.getY() + 0.5;
                double g = (double)pos.getZ() + 0.5 - (double)((float)direction.getStepZ() * 0.3125F) + (double)((float)direction.getClockWise().getStepZ() * 0.3125F);

                for(int k = 0; k < 4; ++k) {
                    level.addParticle(ParticleTypes.SMOKE, d, e, g, 0.0, 5.0E-4, 0.0);
                }
            }
        }

    }

    public NonNullList<ItemStack> getItems() {
        return items;
    }

    public void load(CompoundTag tag) {
        super.load(tag);
        items.clear();
        ContainerHelper.loadAllItems(tag, items);
        int[] is;
        if (tag.contains("CookingTimes", 11)) {
            is = tag.getIntArray("CookingTimes");
            System.arraycopy(is, 0, cookingProgress, 0, Math.min(cookingTime.length, is.length));
        }

        if (tag.contains("CookingTotalTimes", 11)) {
            is = tag.getIntArray("CookingTotalTimes");
            System.arraycopy(is, 0, cookingTime, 0, Math.min(cookingTime.length, is.length));
        }

    }

    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        ContainerHelper.saveAllItems(tag, items, true);
        tag.putIntArray("CookingTimes", cookingProgress);
        tag.putIntArray("CookingTotalTimes", cookingTime);
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public CompoundTag getUpdateTag() {
        CompoundTag compoundTag = new CompoundTag();
        ContainerHelper.saveAllItems(compoundTag, items, true);
        return compoundTag;
    }

    public Optional<CampfireCookingRecipe> getCookableRecipe(ItemStack stack) {
        if (items.stream().noneMatch(ItemStack::isEmpty)) {
            return Optional.empty();
        } else {
            assert this.level != null;
            return this.quickCheck.getRecipeFor(new SimpleContainer(new ItemStack[]{stack}), this.level);
        }
    }

    public boolean placeFood(@Nullable Entity entity, ItemStack stack, int cookTime) {
        for(int i = 0; i < items.size(); ++i) {
            ItemStack itemStack = (ItemStack) items.get(i);
            if (itemStack.isEmpty()) {
                cookingTime[i] = cookTime;
                cookingProgress[i] = 0;
                items.set(i, stack.split(1));
                assert this.level != null;
                this.level.gameEvent(GameEvent.BLOCK_CHANGE, this.getBlockPos(), Context.of(entity, this.getBlockState()));
                this.markUpdated();
                return true;
            }
        }

        return false;
    }

    private void markUpdated() {
        this.setChanged();
        Objects.requireNonNull(this.getLevel()).sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
    }

    public void clearContent() {
        items.clear();
    }

    public void dowse() {
        if (this.level != null) {
            this.markUpdated();
        }

    }
}
