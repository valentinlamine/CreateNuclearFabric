package net.ynov.createnuclear.multiblock.configuredItem;

import com.simibubi.create.AllItems;
import io.github.fabricators_of_create.porting_lib.transfer.item.ItemStackHandler;
import io.github.fabricators_of_create.porting_lib.util.NetworkHooks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.ynov.createnuclear.CreateNuclear;
import net.ynov.createnuclear.item.CNItems;
import net.ynov.createnuclear.multiblock.controller.ReactorControllerMenu;
import org.jetbrains.annotations.Nullable;

public class ConfiguredReactorItem extends Item implements MenuProvider {
    public ConfiguredReactorItem(Properties properties) {
        super(properties);
    }

    @Override
    public Component getDisplayName() {
        return getDescription();
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        ItemStack heldItem = player.getMainHandItem();
        CreateNuclear.LOGGER.warn("shift ");
        //return ConfiguredReactorSlotItemMenu.create(id, inv, heldItem);
        if (player.isShiftKeyDown()) return ConfiguredReactorSlotItemMenu.create(id, inv, heldItem);
        else return ConfiguredReactorItemMenu.create(id, inv, heldItem);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (context.getPlayer() == null) return InteractionResult.PASS;
        return use(context.getLevel(), context.getPlayer(), context.getHand()).getResult();
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack heldItem = player.getItemInHand(hand);

        if (!player.isShiftKeyDown() && hand == InteractionHand.MAIN_HAND) {
            if (!world.isClientSide && player instanceof ServerPlayer)
                NetworkHooks.openScreen((ServerPlayer) player, this, buf -> {
                    buf.writeItem(heldItem);
                });
            return InteractionResultHolder.success(heldItem);
        }
        else if (player.isShiftKeyDown() && hand == InteractionHand.MAIN_HAND) {
            if (!world.isClientSide && player instanceof ServerPlayer) {
                NetworkHooks.openScreen((ServerPlayer) player, this, buf -> {
                    buf.writeItem(heldItem);
                });
            }
            return InteractionResultHolder.success(heldItem);
        }
        return InteractionResultHolder.pass(heldItem);
    }

    public static ItemStackHandler getItemStorage(ItemStack stack) {
        ItemStackHandler newInv = new ItemStackHandler(57);
        if (CNItems.CONFIGURED_REACTOR_ITEM.get() != stack.getItem()) throw new IllegalArgumentException("Cannot get configured items from non item: " + stack);
        if (!stack.hasTag()) return newInv;
        CompoundTag invNBT = stack.getOrCreateTagElement("pattern");
        if (!newInv.empty()) newInv.deserializeNBT(invNBT);
        return newInv;
    }
}
