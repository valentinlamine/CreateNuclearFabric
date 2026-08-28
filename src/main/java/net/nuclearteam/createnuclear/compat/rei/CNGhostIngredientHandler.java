package net.nuclearteam.createnuclear.compat.rei;

import com.simibubi.create.AllPackets;
import com.simibubi.create.content.logistics.filter.AttributeFilterScreen;
import com.simibubi.create.foundation.gui.menu.AbstractSimiContainerScreen;
import com.simibubi.create.foundation.gui.menu.GhostItemMenu;
import com.simibubi.create.foundation.gui.menu.GhostItemSubmitPacket;
import io.github.fabricators_of_create.porting_lib.mixin.accessors.client.accessor.AbstractContainerScreenAccessor;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.drag.DraggableStack;
import me.shedaniel.rei.api.client.gui.drag.DraggableStackVisitor;
import me.shedaniel.rei.api.client.gui.drag.DraggedAcceptorResult;
import me.shedaniel.rei.api.client.gui.drag.DraggingContext;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.nuclearteam.createnuclear.content.multiblock.bluePrintItem.ReactorBluePrintItemScreen;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class CNGhostIngredientHandler<T extends GhostItemMenu<?>>
        implements DraggableStackVisitor<AbstractSimiContainerScreen<T>> {

    @Override
    public DraggedAcceptorResult acceptDraggedStack(DraggingContext<AbstractSimiContainerScreen<T>> context, DraggableStack stack) {
        Stream<BoundsProvider> bounds = getDraggableAcceptingBounds(context, stack);
        Point cursor = context.getCurrentPosition();
        if (cursor != null) {
            int x = cursor.getX();
            int y = cursor.getY();
            Optional<BoundsProvider> target = bounds.filter(b -> {
                AABB box = b.bounds().bounds();
                double minX = box.minX;
                double minY = box.minY;
                double maxX = box.maxX;
                double maxY = box.maxY;
                return x >= minX && x <= maxX && y >= minY && y <= maxY && b instanceof GhostTarget;
            }).findFirst();
            if (target.isPresent() && target.get() instanceof GhostTarget ghost) {
                Object held = stack.getStack().getValue();
                if (held instanceof ItemStack item) {
                    ghost.accept(item);
                    return DraggedAcceptorResult.CONSUMED;
                }
            }
        }
        return DraggableStackVisitor.super.acceptDraggedStack(context, stack);
    }

    @Override
    public Stream<BoundsProvider> getDraggableAcceptingBounds(DraggingContext<AbstractSimiContainerScreen<T>> context, DraggableStack stack) {
        List<BoundsProvider> targets = new ArrayList<>();
        AbstractSimiContainerScreen<T> gui = context.getScreen();
        boolean isAttributeFilter = gui instanceof AttributeFilterScreen;

        if (stack.getStack().getValue() instanceof ItemStack) {
            for (int i = 36; i < gui.getMenu().slots.size(); i++) {
                if (gui.getMenu().slots.get(i)
                        .isActive())
                    targets.add(new GhostTarget<>(gui, i - 36, isAttributeFilter));

                // Only accept items in 1st slot. 2nd is used for functionality, don't wanna
                // override that one
                if (isAttributeFilter)
                    break;
            }
        }

        return targets.stream();
    }

    @Override
    public <R extends Screen> boolean isHandingScreen(R screen) {
        return screen instanceof ReactorBluePrintItemScreen;
    }

    private static class GhostTarget<I, T extends GhostItemMenu<?>> implements BoundsProvider {

        private final Rectangle area;
        private final AbstractSimiContainerScreen<T> gui;
        private final int slotIndex;
        private final boolean isAttributeFilter;

        public GhostTarget(AbstractSimiContainerScreen<T> gui, int slotIndex, boolean isAttributeFilter) {
            this.gui = gui;
            this.slotIndex = slotIndex;
            this.isAttributeFilter = isAttributeFilter;
            Slot slot = gui.getMenu().slots.get(slotIndex + 36);
            AbstractContainerScreenAccessor access = (AbstractContainerScreenAccessor) gui;
            this.area = new Rectangle(access.port_lib$getGuiLeft() + slot.x, access.port_lib$getGuiTop() + slot.y, 16, 16);
        }

        public void accept(I ingredient) {
            ItemStack stack = ((ItemStack) ingredient).copy();
            stack.setCount(1);
            gui.getMenu().ghostInventory.setStackInSlot(slotIndex, stack);

            if (isAttributeFilter)
                return;

            // sync new filter contents with server
            AllPackets.getChannel().sendToServer(new GhostItemSubmitPacket(stack, slotIndex));
        }

        @Override
        public VoxelShape bounds() {
            return BoundsProvider.fromRectangle(area);
        }
    }
}
