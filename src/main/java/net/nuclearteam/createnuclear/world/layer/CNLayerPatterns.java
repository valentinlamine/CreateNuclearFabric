package net.nuclearteam.createnuclear.world.layer;

import com.simibubi.create.content.decoration.palettes.AllPaletteStoneTypes;
import com.simibubi.create.infrastructure.worldgen.LayerPattern;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.world.level.block.Blocks;
import net.nuclearteam.createnuclear.block.CNBlocks;
import net.nuclearteam.createnuclear.block.palette.CNPalettesStoneTypes;

@SuppressWarnings("unused")
public enum CNLayerPatterns {
    AUTUNITE(LayerPattern.builder()
        .layer(l -> l.weight(2)
            .block(CNPalettesStoneTypes.AUTUNITE.getBaseBlock())
            .size(2, 5))
        .layer(l -> l.weight(1)
            .block(AllPaletteStoneTypes.LIMESTONE.getBaseBlock())
            .size(1, 2))
        .layer(l -> l.weight(1)
            .block(Blocks.CALCITE)
            .size(2, 3))
        .layer(l -> l.weight(1)
            .passiveBlock()
            .size(2, 2))
        .layer(l -> l.weight(1)
            .blocks(CNBlocks.URANIUM_ORE.get(), CNBlocks.DEEPSLATE_URANIUM_ORE.get())
            .size(1, 1))),
    ;

    private final NonNullSupplier<LayerPattern> layerPattern;

    CNLayerPatterns(NonNullSupplier<LayerPattern> pattern) {
        this.layerPattern = pattern;
    }


    CNLayerPatterns(LayerPattern.Builder pattern) {
        this.layerPattern = pattern::build;
    }

    public LayerPattern get() {
        return layerPattern.get();
    }
}
