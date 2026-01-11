package net.nuclearteam.createnuclear.content.decoration.palettes;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.createmod.catnip.lang.Lang;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.MapColor;
import net.nuclearteam.createnuclear.CNTags;
import net.nuclearteam.createnuclear.CreateNuclear;

import java.util.function.Function;

import static net.nuclearteam.createnuclear.content.decoration.palettes.PaletteBlockPattern.STANDARD_RANGE;


public enum CNPaletteStoneTypes {

    AUTUNITE(STANDARD_RANGE, r -> r.paletteStoneBlock("autunite", () -> Blocks.ANDESITE, true, true)
            .properties(p -> p.destroyTime(1.25f)
                    .mapColor(MapColor.COLOR_GREEN))
            .register()),
    ;


    private final Function<CreateRegistrate, NonNullSupplier<Block>> factory;
    private PalettesVariantEntry variants;

    public NonNullSupplier<Block> baseBlock;
    public final PaletteBlockPattern[] variantTypes;
    public TagKey<Item> materialTag;

    CNPaletteStoneTypes(PaletteBlockPattern[] variantTypes,
                        Function<CreateRegistrate, NonNullSupplier<Block>> factory) {
        this.factory = factory;
        this.variantTypes = variantTypes;
    }

    public NonNullSupplier<Block> getBaseBlock() {
        return baseBlock;
    }

    public PalettesVariantEntry getVariants() {
        return variants;
    }

    public static void register(CreateRegistrate registrate) {
        for (CNPaletteStoneTypes cnPalettesStoneTypes : values()) {
            cnPalettesStoneTypes.baseBlock = cnPalettesStoneTypes.factory.apply(registrate);
            String id = Lang.asId(cnPalettesStoneTypes.name());
            cnPalettesStoneTypes.materialTag =
                    CNTags.optionalTag(BuiltInRegistries.ITEM, CreateNuclear.asResource("stone_types/" + id));
            cnPalettesStoneTypes.variants = new PalettesVariantEntry(id, cnPalettesStoneTypes);
        }
    }
}
