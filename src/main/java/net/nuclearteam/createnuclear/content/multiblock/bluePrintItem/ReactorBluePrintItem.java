package net.nuclearteam.createnuclear.content.multiblock.bluePrintItem;

import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.ChatFormatting;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.level.Level;
import io.github.fabricators_of_create.porting_lib.transfer.item.ItemStackHandler;
import io.github.fabricators_of_create.porting_lib.util.NetworkHooks;
import net.nuclearteam.createnuclear.CNItems;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ReactorBluePrintItem extends Item implements MenuProvider {

    public ReactorBluePrintItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendTooltip(stack, level, tooltip, flag);

        tooltip.add(Component.translatable("item.createnuclear.reactor_blueprint.tooltip")
                .withStyle(ChatFormatting.GRAY));

        // Adjust the tooltip text to hint at the available action
        tooltip.add(Component.translatable("item.createnuclear.reactor_blueprint.tooltip_hint")
                .withStyle(ChatFormatting.DARK_GRAY));
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("reactor.item.gui.name");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        ItemStack heldItem = player.getMainHandStack();
        return ReactorBluePrintMenu.create(id, inv, heldItem);
    }

    @Override
    public InteractionResult useOnBlock(UseOnContext context) {
        if (context.getPlayer() == null) return InteractionResult.PASS;
        return use(context.getLevel(), context.getPlayer(), context.getHand()).getResult();
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack heldItem = player.getItemInHand(hand);

        // Plain right-click -> Opens the Blueprint screen
        if (!player.isShiftKeyDown() && hand == InteractionHand.MAIN_HAND) {
            if (!world.isClientSide && player instanceof ServerPlayer)
                NetworkHooks.openScreen((ServerPlayer) player, this, buf -> buf.writeItemStack(heldItem));
            return InteractionResultHolder.success(heldItem);
        }
        // Shift + right-click -> Sends a clickable link in chat!
        else if (player.isShiftKeyDown() && hand == InteractionHand.MAIN_HAND) {
            if (!world.isClientSide) {
                MutableComponent message = Component.translatable("item.createnuclear.reactor_blueprint.chat_info")
                        .withStyle(ChatFormatting.GREEN)
                        .append(" ")
                        .append(Component.translatable("item.createnuclear.reactor_blueprint.wiki_link")
                                .styled(style -> style
                                        .withColor(ChatFormatting.AQUA)
                                        .withUnderline(true)
                                        .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://wiki.createnuclear.net/wiki/block&items/reactor_blueprint_item"))
                                ));

                player.displayClientMessage(message, false);
            }
            return InteractionResultHolder.success(heldItem);
        }
        return InteractionResultHolder.pass(heldItem);
    }

    public static ItemStackHandler getItemStorage(ItemStack stack) {
        ItemStackHandler newInv = new ItemStackHandler(57);
        if (CNItems.REACTOR_BLUEPRINT.get() != stack.getItem()) throw new IllegalArgumentException("Cannot get configured items from non item: " + stack);
        if (!stack.hasTag()) return newInv;
        CompoundTag invNBT = stack.getOrCreateSubNbt("pattern");
        if (!invNBT.isEmpty())
            newInv.deserializeNBT(invNBT);
        return newInv;
    }
}
