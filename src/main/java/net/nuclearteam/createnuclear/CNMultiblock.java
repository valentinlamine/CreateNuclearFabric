package net.nuclearteam.createnuclear;

import lib.multiblock.test.SimpleMultiBlockAislePatternBuilder;
import net.nuclearteam.createnuclear.block.CNBlocks;
import net.nuclearteam.createnuclear.multiblock.MultiBlockManagerBeta;
import net.nuclearteam.createnuclear.multiblock.TypeMultiBlock;


public class CNMultiblock {

    public static final MultiBlockManagerBeta<TypeMultiBlock> REGISTRATE_MULTIBLOCK = new MultiBlockManagerBeta<>();
    public static final String AAAAA = "AAAAA";
    public static final String AABAA = "AABAA";
    public static final String ADADA = "ADADA";
    public static final String BACAB = "BACAB";
    public static final String AAIAA = "AAIAA";
    public static final String AAAA = "AA*AA";
    public static final String AAOAA = "AAOAA";

    static {
        REGISTRATE_MULTIBLOCK.register("createnuclear:reactor",
                TypeMultiBlock.REACTOR,
                SimpleMultiBlockAislePatternBuilder.start()
                    .aisle(AAAAA, AAAAA, AAAAA, AAAAA, AAAAA)
                    .aisle(AABAA, ADADA, BACAB, ADADA, AABAA)
                    .aisle(AABAA, ADADA, BACAB, ADADA, AABAA)
                    .aisle(AAIAA, ADADA, BACAB, ADADA, AAAA)
                    .aisle(AABAA, ADADA, BACAB, ADADA, AABAA)
                    .aisle(AABAA, ADADA, BACAB, ADADA, AABAA)
                    .aisle(AAAAA, AAAAA, AAAAA, AAAAA, AAOAA)
                    .where('A', a -> a.getState().is(CNBlocks.REACTOR_CASING.get()))
                    .where('B', a -> a.getState().is(CNBlocks.REACTOR_FRAME.get()))
                    .where('C', a -> a.getState().is(CNBlocks.REACTOR_CORE.get()))
                    .where('D', a -> a.getState().is(CNBlocks.REACTOR_COOLER.get()))
                    .where('*', a -> a.getState().is(CNBlocks.REACTOR_CONTROLLER.get()))
                    .where('O', a -> a.getState().is(CNBlocks.REACTOR_OUTPUT.get()))
                    .where('I', a -> a.getState().is(CNBlocks.REACTOR_INPUT.get()))
                .build()
        );
    }
}