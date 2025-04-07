package net.nuclearteam.createnuclear.world.layer;

import com.simibubi.create.content.decoration.palettes.AllPaletteStoneTypes;
import com.simibubi.create.infrastructure.worldgen.LayerPattern;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.world.level.block.Blocks;
import net.nuclearteam.createnuclear.block.palette.CNPalettesStoneTypes;

public enum CNLayerPatterns {
    AUTUNITE(LayerPattern.builder()
            .layer(l -> l.weight(400)
                    .block(AllPaletteStoneTypes.LIMESTONE.getBaseBlock())
                    .size(10, 300))
            .layer(l -> l.weight(400)
                    .block(Blocks.CALCITE).block(Blocks.SANDSTONE)
                    .size(10, 300))
            .layer(l -> l.weight(400)
                    .block(Blocks.STONE)
                    .size(10, 300))
            .layer(l -> l.weight(400)
                    .block(Blocks.GLOWSTONE)
                    .block(Blocks.QUARTZ_BLOCK)
                    .size(10, 300))
            .layer(l -> l.weight(400)
                    .block(CNPalettesStoneTypes.AUTUNITE.getBaseBlock())
                    .size(10, 300))::build),
    ;

    private final NonNullSupplier<LayerPattern> layerPattern;

    CNLayerPatterns(NonNullSupplier<LayerPattern> pattern) {
        this.layerPattern = pattern;
    }

    public LayerPattern get() {
        return layerPattern.get();
    }
}
