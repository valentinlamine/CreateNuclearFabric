package net.ynov.createnuclear;

import lib.multiblock.test.SimpleMultiBlockAislePatternBuilder;
import net.minecraft.world.level.block.Blocks;
import net.ynov.createnuclear.block.CNBlocks;
import net.ynov.createnuclear.multiblock.Exemple;
import net.ynov.createnuclear.multiblock.MultiBlockManagerBeta;


public class CNMultiblock {
    public static final MultiBlockManagerBeta<Exemple> REGISTRATE_MULTIBLOCK = new MultiBlockManagerBeta<>();

    static {
        REGISTRATE_MULTIBLOCK.register("createnuclear:test",
                Exemple.EXEMPLE,
                SimpleMultiBlockAislePatternBuilder.start()
                    .aisle("AAAAA", "AAAAA", "AAAAA", "AAAAA", "AAAAA")
                    .aisle("AABAA", "ADADA", "BACAB", "ADADA", "AABAA")
                    .aisle("AABAA", "ADADA", "BACAB", "ADADA", "AABAA")
                    .aisle("AABAA", "ADADA", "BACAB", "ADADA", "AA*AA")
                    .aisle("AABAA", "ADADA", "BACAB", "ADADA", "AABAA")
                    .aisle("AABAA", "ADADA", "BACAB", "ADADA", "AABAA")
                    .aisle("AAAAA", "AAAAA", "AAAAA", "AAAAA", "AAAAA")
                    .where('A', a -> a.getState().is(CNBlocks.REACTOR_CASING.get()))
                    .where('B', a -> a.getState().is(CNBlocks.REACTOR_MAIN_FRAME.get()))
                    .where('C', a -> a.getState().is(CNBlocks.REACTOR_CORE.get()))
                    .where('D', a -> a.getState().is(CNBlocks.COOLING_FRAME.get()))
                    .where('*', a -> a.getState().is(CNBlocks.REACTOR_CONTROLLER.get()))
                .build()
        );
    }
}
