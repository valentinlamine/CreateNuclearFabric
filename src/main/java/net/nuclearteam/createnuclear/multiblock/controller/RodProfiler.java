package net.nuclearteam.createnuclear.multiblock.controller;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.nuclearteam.createnuclear.item.CNItems;
import net.nuclearteam.createnuclear.tags.CNTag;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the heat profile for a rod type, including base heat, proximity heat, and duration (in ticks).
 *
 * @param baseHeat        The heat value generated (or reduced) by the rod itself.
 * @param proximityHeat   The heat value applied when the rod is near others.
 * @param time            Duration in ticks the rod remains active.
 * @param items           The ItemStack associated with this rod profile.
 * @param tags            The TagKey<Item> for categorizing the rod in item tags.
 */
public record RodProfiler(int baseHeat, int proximityHeat, int time, ItemStack items, TagKey<Item> tags) {

    public static final Codec<RodProfiler> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("baseHeat").forGetter(RodProfiler::baseHeat),
            Codec.INT.fieldOf("proximityHeat").forGetter(RodProfiler::proximityHeat),
            Codec.INT.fieldOf("time").forGetter(RodProfiler::time),
            ItemStack.CODEC.fieldOf("items").forGetter(RodProfiler::items),
            TagKey.codec(BuiltInRegistries.ITEM.key()).fieldOf("tags").forGetter(RodProfiler::tags)
    ).apply(instance, RodProfiler::new));

    private boolean itemMatchesTag(ItemStack stack, TagKey<Item> tag) {
        return stack.is(tag);
    }

    private static final Map<ItemStack, HeatRodType> heatRodTypeMap = new HashMap<>();
    private static final Map<ItemStack, CoolerRodType> coolerRodTypeMap = new HashMap<>();

    static {
        for (HeatRodType heatRodType : HeatRodType.values()) {
            heatRodTypeMap.put(heatRodType.getProfiler().items(), heatRodType);
        }

        for (CoolerRodType coolerRodType : CoolerRodType.values()) {
            coolerRodTypeMap.put(coolerRodType.getProfiler().items(), coolerRodType);
        }
    }

    public HeatRodType getHeatRodType(ItemStack stack) {
        return heatRodTypeMap.get(stack);
    }

    public HeatRodType getHeatRodType(TagKey<Item> tags) {
        for (Map.Entry<ItemStack, HeatRodType> entry : heatRodTypeMap.entrySet()) {
            if (itemMatchesTag(entry.getKey(), tags)) {
                return entry.getValue();
            }
        }
        return null;
    }

    public CoolerRodType getCoolerRodType(ItemStack stack) {
        return coolerRodTypeMap.get(stack);
    }

    public CoolerRodType getCoolerRodType(TagKey<Item> tags) {
        for (Map.Entry<ItemStack, CoolerRodType> entry : coolerRodTypeMap.entrySet()) {
            if (itemMatchesTag(entry.getKey(), tags)) {
                return entry.getValue();
            }
        }
        return null;
    }

    /**
     * Rod types that generate heat, along with their heat profiles.
     */
    public enum HeatRodType {
        URANIUM(25, 5, 3600, CNItems.URANIUM_ROD.asStack(), CNTag.ItemTags.FUEL);

        private final RodProfiler profiler;

        HeatRodType(int baseHeat, int proximityHeat, int time, ItemStack stack, CNTag.ItemTags tags) {
            this.profiler = new RodProfiler(baseHeat, proximityHeat, time, stack, tags.tag);
        }

        /**
         * @return The heat profile associated with this heat-generating rod type.
         */
        public RodProfiler getProfiler() {
            return profiler;
        }
    }

    /**
     * Rod types that cool down the reactor, along with their cooling profiles.
     */
    public enum CoolerRodType {
        GRAPHITE(-10, -5, 3600, CNItems.GRAPHITE_ROD.asStack(), CNTag.ItemTags.COOLER);

        private final RodProfiler profiler;

        CoolerRodType(int baseHeat, int proximityHeat, int time, ItemStack stack, CNTag.ItemTags tags) {
            this.profiler = new RodProfiler(baseHeat, proximityHeat, time, stack, tags.tag);
        }

        /**
         * @return The cooling profile associated with this cooling rod type.
         */
        public RodProfiler getProfiler() {
            return profiler;
        }
    }
}