package net.nuclearteam.createnuclear.content.multiblock.rod;

import net.nuclearteam.createnuclear.CreateNuclear;
import net.nuclearteam.createnuclear.api.CreateNuclearRegistries;
import net.nuclearteam.createnuclear.api.multiblock.rods.RodType;

import static net.nuclearteam.createnuclear.api.ItemRodTypesValue.DEFAULT_ROD_TYPE;

import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;

public class CNRodTypes {
    /**
     * Static registry of rod types ({@code RodType}) for the CreateNuclear mod.
     *
     * <p>This class exposes the {@link ResourceKey}s and the {@code bootstrap}
     * method invoked by the datapack system to register rod types used by the
     * multiblock (for example, fuel rods and cooler rods). The examples below
     * show how to create a {@code RodType} via its {@code RodType.Builder}.</p>
     *
     * <p>Example — Custom Rod:</p>
     * <pre>{@code
     * register(ctx, "name", new RodType.Builder()
     *     .baseRodHeat(int)
     *     .proximityRodHeat(int)
     *     .rodTimer(int)
     *     .item(Item)
     *     .fuelRodType()
     *     .build());
     * }</pre>
     *
     * @see net.nuclearteam.createnuclear.api.multiblock.rods.RodType
     * @see net.nuclearteam.createnuclear.api.CreateNuclearRegistries
     */
    public static void bootstrap(BootstapContext<RodType> ctx) {
        register(ctx, "fallback", DEFAULT_ROD_TYPE);
    }

    private static void register(BootstapContext<RodType> ctx, String name, RodType type) {
        ctx.register(ResourceKey.create(CreateNuclearRegistries.ROD_TYPE, CreateNuclear.asResource(name)), type);
    }
}
