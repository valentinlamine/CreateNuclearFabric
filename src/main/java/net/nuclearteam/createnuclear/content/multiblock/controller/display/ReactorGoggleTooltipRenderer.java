package net.nuclearteam.createnuclear.content.multiblock.controller.display;

import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.utility.CreateLang;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.nuclearteam.createnuclear.content.logistics.BigFluidStack;
import net.nuclearteam.createnuclear.content.multiblock.IHeat;
import net.nuclearteam.createnuclear.foundation.utility.CreateNuclearLang;

import java.util.List;
import java.util.Map;

/**
 * Builds the Goggles tooltip shown when looking at an assembled reactor controller.
 * <p>
 * This is a stateless presentation helper: it only reads from a
 * {@link ReactorDisplayState} snapshot (plus the current heat level) and appends the
 * resulting lines to the tooltip. It performs no NBT access, inventory lookups or
 * other block-entity-specific logic, keeping the rendering separate from the block
 * entity's data/state management.
 * <p>
 * Typical usage from {@code ReactorControllerBlockEntity#addToGoggleTooltip}:
 * <pre>{@code
 * @Override
 * public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
 *     CompoundTag patternTag = this.getConfiguredPatternTag();
 *     if (patternTag == null || patternTag.isEmpty()) {
 *         return false;
 *     }
 *     ReactorGoggleTooltipRenderer.render(tooltip, displayState, patternTag.getInt("heat"), isPlayerSneaking);
 *     return true;
 * }
 * }</pre>
 */
public final class ReactorGoggleTooltipRenderer {
    private ReactorGoggleTooltipRenderer() {}

    /**
     * Appends the reactor controller's Goggles tooltip lines to {@code tooltip}.
     * <p>
     * Renders, in order: the gauge info header, the reactor heat level, the loaded
     * item rods (or a placeholder if none), and the loaded fluids with their fill
     * ratio as a progress bar (or a placeholder if none).
     *
     * @param tooltip          the tooltip line list to append to
     * @param state            the current display snapshot (items, fluids, max fluid capacity)
     * @param heat             the reactor's current heat value, used to render the heat tooltip line
     * @param isPlayerSneaking whether the viewing player is sneaking (currently unused,
     *                          kept for signature parity with other goggle tooltip providers)
     */
    public static void render(List<Component> tooltip, ReactorDisplayState state, int heat, boolean isPlayerSneaking, int reactorSize) {
        renderHeaderAndHeat(tooltip, heat, reactorSize);
        renderItemRods(tooltip, state);
        renderFluidTanks(tooltip, state);
    }

    /**
     * Appends the gauge info header and the reactor's current heat level line.
     *
     * @param tooltip the tooltip line list to append to
     * @param heat    the reactor's current heat value
     */
    private static void renderHeaderAndHeat(List<Component> tooltip, int heat, int reactorSize) {
        CreateLang.translate("gui.gauge.info_header")
                .style(ChatFormatting.GRAY)
                .forGoggles(tooltip);
        IHeat.HeatLevel.getName("reactor_controller").style(ChatFormatting.GRAY).forGoggles(tooltip);

        IHeat.HeatLevel.getFormattedHeatText(heat, reactorSize).forGoggles(tooltip);
    }

    /**
     * Appends one line per loaded item rod with its stacked count, or a placeholder
     * line showing a count of 0 if no rods are loaded.
     *
     * @param tooltip the tooltip line list to append to
     * @param state   the current display snapshot
     */
    private static void renderItemRods(List<Component> tooltip, ReactorDisplayState state) {
        if (state.items().isEmpty()) {
            CreateNuclearLang.builder()
                    .add(Component.translatable("createnuclear.tooltip.item.empty.rod").withStyle(ChatFormatting.GRAY))
                    .add(CreateNuclearLang.number(0).style(ChatFormatting.GOLD))
                    .forGoggles(tooltip);
        } else {
            for (Map.Entry<Item, Integer> entry : state.items().entrySet()) {
                ItemStack stack = new ItemStack(entry.getKey());
                int count = entry.getValue();
                CreateNuclearLang.itemName(stack)
                        .style(ChatFormatting.GRAY)
                        .text(": ")
                        .add(CreateNuclearLang.number(count).style(ChatFormatting.GOLD))
                        .forGoggles(tooltip);
            }
        }
    }

    /**
     * Appends one line per loaded fluid with a fill-ratio progress bar relative to
     * {@link ReactorDisplayState#maxFluidCapacity()}, or a "no fluid" placeholder
     * with an empty progress bar if no fluids are loaded.
     *
     * @param tooltip the tooltip line list to append to
     * @param state   the current display snapshot
     */
    private static void renderFluidTanks(List<Component> tooltip, ReactorDisplayState state) {
        if (state.fluids().isEmpty()) {
            CreateNuclearLang.translate("tooltip.fluid.none")
                    .style(ChatFormatting.GRAY)
                    .forGoggles(tooltip);

            CreateNuclearLang.builder()
                    .text(TooltipHelper.makeProgressBar(5, 0))
                    .style(ChatFormatting.BLUE)
                    .space()
                    .text("0%")
                    .forGoggles(tooltip);
        } else {
            long maxCapacity = state.maxFluidCapacity();
            for (BigFluidStack stack : state.fluids()) {
                float fillRatio = maxCapacity > 0 ? (float) stack.amount / maxCapacity : 0;
                if (fillRatio >= 0.99f)
                    fillRatio = 1.0f;
                int filledBars = Math.round(fillRatio * 5);

                CreateNuclearLang.translate("tooltip.fluid", stack.stack.getDisplayName())
                        .style(ChatFormatting.GRAY)
                        .forGoggles(tooltip);

                CreateNuclearLang.builder()
                        .text(TooltipHelper.makeProgressBar(5, filledBars))
                        .style(ChatFormatting.BLUE)
                        .space()
                        .text(Math.round(fillRatio * 100) + "%")
                        .forGoggles(tooltip);
            }
        }
    }
}
