package net.nuclearteam.createnuclear.block.palette;

import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.utility.Lang;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.MapColor;
import net.nuclearteam.createnuclear.CreateNuclear;

import java.util.function.Function;

import static net.nuclearteam.createnuclear.block.palette.CNPaletteBlockPattern.STANDARD_RANGE;

public enum CNPalettesStoneTypes {

    AUTUNITE(STANDARD_RANGE, r -> r.paletteStoneBlock("autunite", () -> Blocks.ANDESITE, true, true)
            .properties(p -> p.destroyTime(1.25f)
                    .mapColor(MapColor.COLOR_GREEN))
            .register()),
    ;


    private final Function<CreateRegistrate, NonNullSupplier<Block>> factory;
    private CNPalettesVariantEntry variants;

    public NonNullSupplier<Block> baseBlock;
    public final CNPaletteBlockPattern[] variantTypes;
    public TagKey<Item> materialTag;

    CNPalettesStoneTypes(CNPaletteBlockPattern[] variantTypes,
                         Function<CreateRegistrate, NonNullSupplier<Block>> factory) {
        this.factory = factory;
        this.variantTypes = variantTypes;
    }

    public NonNullSupplier<Block> getBaseBlock() {
        return baseBlock;
    }

    public CNPalettesVariantEntry getVariants() {
        return variants;
    }

    public static void register(CreateRegistrate registrate) {
        for (CNPalettesStoneTypes cnPalettesStoneTypes : values()) {
            cnPalettesStoneTypes.baseBlock = cnPalettesStoneTypes.factory.apply(registrate);
            String id = Lang.asId(cnPalettesStoneTypes.name());
            cnPalettesStoneTypes.materialTag =
                    AllTags.optionalTag(BuiltInRegistries.ITEM, CreateNuclear.asResource("stone_types/" + id));
            cnPalettesStoneTypes.variants = new CNPalettesVariantEntry(id, cnPalettesStoneTypes);
        }
    }
}
