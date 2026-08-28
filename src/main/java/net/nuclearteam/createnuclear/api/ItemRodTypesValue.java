package net.nuclearteam.createnuclear.api;

import com.simibubi.create.api.registry.SimpleRegistry;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.nuclearteam.createnuclear.api.multiblock.rods.RodType;
import static net.nuclearteam.createnuclear.api.multiblock.rods.RodType.TypeRod.NONE;

public class ItemRodTypesValue {
    public static final SimpleRegistry<Item, RodType> ROD_TYPE = SimpleRegistry.create();
    /**
     * Default RodType returned when no mapping exists in the registry.
     * Built once to avoid allocating a new instance on each lookup.
     */
    public static final RodType DEFAULT_ROD_TYPE = new RodType(Items.AIR.builtInRegistryHolder(), -1, -1, -1, NONE);

    /**
     * Get the {@link RodType} associated with the given {@link Item}.
     * If no mapping exists, a default empty {@link RodType} is returned.
     *
     * @param item the item to look up (must not be null)
     * @return the associated {@link RodType} or a default instance if none present
     * @throws NullPointerException if {@code item} is null
     */
    public static RodType getRodType(Item item) {
        RodType type = ROD_TYPE.get(item);
        return type == null ? DEFAULT_ROD_TYPE : type;
    }

    /**
     * Create a consumer that registers the given {@link RodType} for an item.
     *
     * @param type the rod type to register (must not be null)
     * @return a non-null consumer that registers the mapping
     * @throws NullPointerException if {@code type} is null
     */
    public static NonNullConsumer<Item> setRodTypeInfos(RodType.Builder type) {
        return item -> ROD_TYPE.register(item, type.item(item).build());
    }

    /**
     * Create a consumer that builds a {@link RodType} using the provided numeric
     * parameters and registers it for the item passed to the consumer.
     *
     * The {@code type} parameter controls whether the builder configures the
     * rod as a cooler, fuel or keeps the default mixed behaviour. For MIXTE
     * we keep the builder default and do not call any explicit setter.
     *
     * @param baseRodHeat base heat value
     * @param proximityRodHeat heat influenced by proximity
     * @param rodTimer rod life / timer value
     * @param type enum controlling cooler/fuel/mixte behaviour (must not be null)
     * @return a non-null consumer that registers the built RodType
     * @throws NullPointerException if {@code type} is null
     */
    public static NonNullConsumer<Item> setRodTypeInfos(int baseRodHeat, int proximityRodHeat, int rodTimer, RodType.TypeRod type) {
        RodType.Builder builder = new RodType.Builder()
                .baseRodHeat(baseRodHeat)
                .proximityRodHeat(proximityRodHeat)
                .rodTimer(rodTimer);

        switch (type) {
            case COOLER -> builder.coolerRodType();
            case FUEL -> builder.fuelRodType();
            default -> throw new IllegalArgumentException("Unsupported TypeRod: " + type + ". This error occurred during rod stats initialization in the CreateNuclearForge mod.");
        }

        return item -> {
            RodType built = builder.item(item).build();
            ROD_TYPE.register(item, built);
        };
    }
}
