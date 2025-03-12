package net.nuclearteam.createnuclear.block.CNPalette;

import com.simibubi.create.AllTags;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.utility.Lang;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.MapColor;

import java.util.function.Function;

import static net.nuclearteam.createnuclear.block.CNPalette.CNPaletteBlockPattern.STANDARD_RANGE;
import static net.nuclearteam.createnuclear.block.CNPalette.CNPaletteBlockPattern.VANILLA_RANGE;

public enum CNPalettesStoneTypes {
    DEEPSLATE(VANILLA_RANGE, r -> () -> Blocks.DEEPSLATE),


    AUTUNITE(STANDARD_RANGE, r -> r.paletteStoneBlock("autunite", () -> Blocks.DEEPSLATE, true, true)
            .properties(p -> p.destroyTime(1.25f)
                    .mapColor(MapColor.COLOR_GREEN))
            .register()),
    ;


    private Function<CreateRegistrate, NonNullSupplier<Block>> factory;
    private CNPalettesVariantEntry variants;

    public NonNullSupplier<Block> baseBlock;
    public CNPaletteBlockPattern[] variantTypes;
    public TagKey<Item> materialTag;

    private CNPalettesStoneTypes(CNPaletteBlockPattern[] variantTypes,
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
        for (CNPalettesStoneTypes cnpaletteStoneVariants : values()) {
            NonNullSupplier<Block> baseBlock = cnpaletteStoneVariants.factory.apply(registrate);
            cnpaletteStoneVariants.baseBlock = baseBlock;
            String id = Lang.asId(cnpaletteStoneVariants.name());
            cnpaletteStoneVariants.materialTag =
                    AllTags.optionalTag(BuiltInRegistries.ITEM, Create.asResource("stone_types/" + id));
            cnpaletteStoneVariants.variants = new CNPalettesVariantEntry(id, cnpaletteStoneVariants);
        }
    }


}
