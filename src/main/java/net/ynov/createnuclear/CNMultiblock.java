package net.ynov.createnuclear;

import lib.multiblock.test.SimpleMultiBlockAislePatternBuilder;
import net.ynov.createnuclear.block.CNBlocks;
import net.ynov.createnuclear.multiblock.TypeMutliblock;
import net.ynov.createnuclear.multiblock.MultiBlockManagerBeta;


public class CNMultiblock {

    public static final MultiBlockManagerBeta<TypeMutliblock> REGISTRATE_MULTIBLOCK = new MultiBlockManagerBeta<>();
    public static final String AAAAA = "AAAAA";
    public static final String AABAA = "AABAA";
    public static final String ADADA = "ADADA";
    public static final String BACAB = "BACAB";
    public static final String AAIAA = "AAIAA";
    public static final String AAAA = "AA*AA";
    public static final String AAOAA = "AAOAA";

    static {
        REGISTRATE_MULTIBLOCK.register("createnuclear:reactor",
                TypeMutliblock.REACTOR,
                SimpleMultiBlockAislePatternBuilder.start()
                    .aisle(AAAAA, AAAAA, AAAAA, AAAAA, AAAAA)
                    .aisle(AABAA, ADADA, BACAB, ADADA, AABAA)
                    .aisle(AABAA, ADADA, BACAB, ADADA, AABAA)
                    .aisle(AAIAA, ADADA, BACAB, ADADA, AAAA)
                    .aisle(AABAA, ADADA, BACAB, ADADA, AABAA)
                    .aisle(AABAA, ADADA, BACAB, ADADA, AABAA)
                    .aisle(AAAAA, AAAAA, AAAAA, AAAAA, AAOAA)
                    .where('A', a -> a.getState().is(CNBlocks.REACTOR_CASING.get()))
                    .where('B', a -> a.getState().is(CNBlocks.REACTOR_MAIN_FRAME.get()))
                    .where('C', a -> a.getState().is(CNBlocks.REACTOR_CORE.get()))
                    .where('D', a -> a.getState().is(CNBlocks.REACTOR_COOLING_FRAME.get()))
                    .where('*', a -> a.getState().is(CNBlocks.REACTOR_CONTROLLER.get()))
                    .where('O', a -> a.getState().is(CNBlocks.REACTOR_OUTPUT.get()))
                    .where('I', a -> a.getState().is(CNBlocks.REACTOR_INPUT.get()))
                .build()
        );
    }
}