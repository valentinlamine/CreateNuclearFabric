package net.ynov.createnuclear;

import lib.multiblock.test.SimpleMultiBlockAislePatternBuilder;
import lib.multiblock.test.SimpleMultiBlockPattern;
import net.ynov.createnuclear.block.CNBlocks;
import net.ynov.createnuclear.fluid.CNFluids;
import net.ynov.createnuclear.multiblock.Exemple;
import net.ynov.createnuclear.multiblock.MultiBlockManagerBeta;


public class CNMultiblock {
    public static final MultiBlockManagerBeta<Exemple> REGISTRATE_MULTIBLOCK = new MultiBlockManagerBeta<>();

    static {
        REGISTRATE_MULTIBLOCK.register("createnuclear:test",
                Exemple.EXEMPLE,
                SimpleMultiBlockAislePatternBuilder.start()
                    .aisle("LLL", "L*L", "LLL")
                    .where('L', a -> a.getState().is(CNBlocks.DEEPSLATE_URANIUM_ORE.get()))
                    .where('*', a -> a.getState().is(CNBlocks.REINFORCED_GLASS.get()))
                .build()
        );
    }
}
