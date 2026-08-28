package net.nuclearteam.createnuclear.content.redstone.displayLink.source;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import com.simibubi.create.api.behaviour.display.DisplaySource;
import com.simibubi.create.content.redstone.displayLink.DisplayLinkContext;
import com.simibubi.create.content.redstone.displayLink.target.DisplayTargetStats;
import com.simibubi.create.content.trains.display.FlapDisplayBlockEntity;
import com.simibubi.create.content.trains.display.FlapDisplayLayout;
import com.simibubi.create.content.trains.display.FlapDisplaySection;
import com.simibubi.create.foundation.gui.ModularGuiLineBuilder;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import net.minecraft.world.item.Item;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.nuclearteam.createnuclear.api.multiblock.rods.RodType.TypeRodPredicate;
import net.nuclearteam.createnuclear.content.logistics.BigFluidStack;
import net.nuclearteam.createnuclear.content.multiblock.IHeat;
import net.nuclearteam.createnuclear.content.multiblock.MultiblockHelpers;
import net.nuclearteam.createnuclear.content.multiblock.controller.ReactorControllerBlockEntity;
import net.nuclearteam.createnuclear.foundation.utility.CreateNuclearLang;

/**
 * Display source rendering a reactor controller's summary (status, size, fuel,
 * cooler, fluid, heat) as one line per stat, for both regular displays and
 * flap displays.
 * <p>
 * The underlying data is built by {@link #getReactorSummary} into a
 * {@link ReactorSummary}, which is only converted to Create's positional
 * {@code List<List<MutableComponent>>} format at the last moment via
 * {@link ReactorSummary#toRows()}, right before returning it to the caller.
 */
public class ReactorSummaryDisplaySource extends DisplaySource {

    /** Fallback line shown when only a single display row is available. */
    public static final List<MutableComponent> notEnoughSpaceSingle =
            List.of(CreateNuclearLang.translateDirect("display_source.reactor.not_enough_space")
                    .append(CreateNuclearLang.translateDirect("display_source.reactor.for_reactor_status")));

    /** Fallback lines shown when fewer than 6 rows are available (not enough for the full summary). */
    public static final List<MutableComponent> notEnoughSpaceDouble =
            List.of(CreateNuclearLang.translateDirect("display_source.reactor.not_enough_space"),
                    CreateNuclearLang.translateDirect("display_source.reactor.for_reactor_status"));

    /** Fallback flap display rows, mirroring {@link #notEnoughSpaceDouble} for the flap layout. */
    public static final List<List<MutableComponent>> notEnoughSpaceFlap =
            List.of(List.of(CreateNuclearLang.translateDirect("display_source.reactor.not_enough_space")),
                    List.of(CreateNuclearLang.translateDirect("display_source.reactor.for_reactor_status")));

    /**
     * Provides the text lines for a regular (non-flap) display target.
     * Falls back to {@link #notEnoughSpaceSingle}/{@link #notEnoughSpaceDouble} when there
     * isn't enough room, and to the "no controller" message when no reactor controller
     * could be resolved for this source.
     */
    @Override
    public List<MutableComponent> provideText(DisplayLinkContext context, DisplayTargetStats stats) {
        if (stats.maxRows() < 2) return notEnoughSpaceSingle;
        if (stats.maxRows() < 6) return notEnoughSpaceDouble;

        int gaugeWidth = (stats.maxColumns() >= 80) ? 10 : 6;

        Optional<ReactorSummary> summary = getReactorSummary(context, gaugeWidth);
        if (summary.isEmpty()) {
            return List.of(CreateNuclearLang.translateDirect("display_source.reactor.no_controller"));
        }

        List<List<MutableComponent>> components = summary.get().toRows();

        if (context.getTargetBlockEntity() instanceof LecternBlockEntity) {
            Stream<MutableComponent> componentList = components.stream().map(list -> list.stream().reduce(MutableComponent::append).orElse(EMPTY_LINE));
            return List.of(componentList.reduce((c1, c2) -> c1.append(Component.literal("\n")).append(c2)).orElse(EMPTY_LINE));
        }

        return components.stream().map(list -> list.stream().reduce(MutableComponent::append).orElse(EMPTY_LINE)).toList();
    }

    /**
     * Provides the rows for a flap display target. Disables the flap display context
     * (falls back to the default layout) when there isn't enough room, either because
     * of the row count or because the fuel value wouldn't fit the available columns.
     */
    @Override
    public List<List<MutableComponent>> provideFlapDisplayText(DisplayLinkContext context, DisplayTargetStats stats) {
        if (stats.maxRows() < 6) {
            context.flapDisplayContext = Boolean.FALSE;
            return notEnoughSpaceFlap;
        }

        int gaugeWidth = 6;
        Optional<ReactorSummary> summary = getReactorSummary(context, gaugeWidth);

        if (summary.isEmpty()) {
            return notEnoughSpaceFlap;
        }

        ReactorSummary reactorSummary = summary.get();

        if (stats.maxColumns() * FlapDisplaySection.MONOSPACE < 6 * FlapDisplaySection.MONOSPACE + reactorSummary.fuel()
                .value().getString().length() * FlapDisplaySection.WIDE_MONOSPACE) {
            context.flapDisplayContext = Boolean.FALSE;
            return notEnoughSpaceFlap;
        }

        return reactorSummary.toRows();
    }

    /**
     * Configures the flap display layout: the default layout for the first line
     * (or when the flap display context was disabled by {@link #provideFlapDisplayText}),
     * otherwise a two-section "Reactor" layout (label column + value/gauge column).
     */
    @Override
    public void loadFlapDisplayLayout(DisplayLinkContext context, FlapDisplayBlockEntity flapDisplay, FlapDisplayLayout layout, int lineIndex) {
        if (lineIndex == 0 || context.flapDisplayContext instanceof Boolean b && !b) {
            if (layout.isLayout("Default")) return;
            layout.loadDefault(flapDisplay.getMaxCharCount());
            return;
        }

        String layoutKey = "Reactor";
        if (layout.isLayout(layoutKey)) return;

        int lw = labelWidth();
        int labelLength = (int) (lw * FlapDisplaySection.MONOSPACE);
        float maxSpace = flapDisplay.getMaxCharCount(1) * FlapDisplaySection.MONOSPACE;

        FlapDisplaySection label = new FlapDisplaySection(labelLength, "alphabet", false, true);
        FlapDisplaySection symbols = new FlapDisplaySection(maxSpace - labelLength, "pixel", false, false).wideFlaps();

        layout.configure(layoutKey, List.of(label, symbols));
    }

    /**
     * Resolves the reactor controller for this source and builds a {@link ReactorSummary}
     * from its current state. Returns {@link Optional#empty()} when no controller is
     * assigned to this display source or it has been removed, so callers can distinguish
     * that case from a valid summary without relying on list size/positional access.
     */
    private Optional<ReactorSummary> getReactorSummary(DisplayLinkContext context, int gaugeWidth) {
        ReactorControllerBlockEntity controller = MultiblockHelpers.getControllerForPart(context.level(), context.getSourcePos());

        if (controller == null || controller.isRemoved()) {
            return Optional.empty();
        }

        int mode = context.sourceConfig().getInt("display_mode");

        int heat = (int) controller.getConfiguredPattern().getOrCreateTag().getDouble("heat");
        int fuel = 0;
        int cooler = 0;

        if (controller.getDisplayState() != null && controller.getDisplayState().items() != null) {
            for (Map.Entry<Item, Integer> entry : controller.getDisplayState().items().entrySet()) {
                if (TypeRodPredicate.isFuel(entry.getKey().getDefaultInstance(), context.level())) {
                    fuel += entry.getValue();
                } else if (TypeRodPredicate.isCooled(entry.getKey().getDefaultInstance(), context.level())) {
                    cooler += entry.getValue();
                }
            }
        }

        int size = controller.getMultiblockSize();
        List<BigFluidStack> fluidList = controller.getBigFluidStack();
        int fluid = (fluidList != null && !fluidList.isEmpty() && fluidList.get(0) != null)
                ? (int) Math.min(Integer.MAX_VALUE, Math.max(0L, fluidList.get(0).amount))
                : 0;

        int lw = labelWidth();
        MutableComponent lStatus = padLabel("status", lw).append(" ");
        MutableComponent lSize   = padLabel("size", lw).append(" ");
        MutableComponent lFuel   = padLabel("fuel", lw).append(" ");
        MutableComponent lCooler = padLabel("cooler", lw).append(" ");
        MutableComponent lFluid  = padLabel("fluid", lw).append(" ");
        MutableComponent lHeat   = padLabel("heat", lw).append(" ");

        return Optional.of(new ReactorSummary.Builder()
                .status(lStatus, controller.isAssembled() ?
                        CreateNuclearLang.translateDirect("display_source.reactor.active").withStyle(ChatFormatting.GOLD) :
                        CreateNuclearLang.translateDirect("display_source.reactor.idle").withStyle(ChatFormatting.GRAY))
                .size(lSize, formatSize(size))
                .fuel(lFuel, formatValue(fuel, ReactorDisplayConstants.MAX_FUEL, mode, false, ChatFormatting.GREEN, gaugeWidth))
                .cooler(lCooler, formatValue(cooler, ReactorDisplayConstants.MAX_COOLER, mode, false, ChatFormatting.AQUA, gaugeWidth))
                .fluid(lFluid, formatFluid(fluid, ReactorDisplayConstants.MAX_FLUID, mode, ChatFormatting.BLUE, gaugeWidth))
                .heat(lHeat, formatValue(heat, ReactorDisplayConstants.MAX_HEAT, mode, true, IHeat.HeatLevel.of(heat, controller.getMultiblockSize()).getTextColor(), gaugeWidth))
                .build());
    }

    private MutableComponent padLabel(String key, int lw) {
        return Component.literal(" ".repeat(lw - labelWidthOf(key))).append(labelOf(key));
    }

    private MutableComponent formatSize(int size) {
        String key = size <= 5 ? "small" : size <= 7 ? "medium" : "large";
        return CreateNuclearLang.translateDirect("display_source.reactor.size." + key).withStyle(ChatFormatting.BLUE);
    }

    private MutableComponent formatFluid(int current, int max, int mode, ChatFormatting color, int width) {
        if (mode == 0 || mode == 3) return ReactorGaugeRenderer.drawGauge(current, max, color, width);
        if (mode == 2) return Component.literal((current * 100 / max) + "%").withStyle(color);
        return Component.literal(String.valueOf(current)).append(" ").append(CreateNuclearLang.translateDirect("generic.unit.fluid.value")).withStyle(color);
    }

    private MutableComponent formatValue(int current, int max, int mode, boolean gaugeOnNormal, ChatFormatting color, int width) {
        if (mode == 3 || (mode == 0 && gaugeOnNormal)) return ReactorGaugeRenderer.drawGauge(current, max, color, width);
        if (mode == 2) return Component.literal((current * 100 / max) + "%").withStyle(color);
        return Component.literal(String.valueOf(current)).withStyle(color);
    }

    private int labelWidth() {
        return Stream.of("status", "size", "fuel", "cooler", "fluid", "heat").mapToInt(this::labelWidthOf).max().orElse(0);
    }

    private int labelWidthOf(String label) {
        return labelOf(label).getString().length();
    }

    private MutableComponent labelOf(String label) {
        return CreateNuclearLang.translateDirect("display_source.reactor." + label);
    }

    @Override
    public void initConfigurationWidgets(DisplayLinkContext context, ModularGuiLineBuilder builder, boolean isFirstLine) {
        if (isFirstLine) return;
        builder.addSelectionScrollInput(0, 100, (selectionScrollInput, label) -> selectionScrollInput
                .forOptions(CreateNuclearLang.translatedOptions("display_source.reactor.mode", "normal", "value", "percent", "gauge")), "display_mode");
    }

    @Override
    protected String getTranslationKey() {
        return "reactor_summary";
    }
}
