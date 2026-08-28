package net.nuclearteam.createnuclear.content.multiblock;

import lib.multiblock.SimpleMultiBlockAislePatternBuilder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.nuclearteam.createnuclear.CNBlocks;
import net.nuclearteam.createnuclear.api.multiblock.MultiBlockManagerBeta;
import net.nuclearteam.createnuclear.api.multiblock.TypeMultiblock;

import java.util.function.Predicate;

public class CNMultiblock {
    public static final MultiBlockManagerBeta<TypeMultiblock> REGISTRATE_MULTIBLOCK = new MultiBlockManagerBeta<>();

    private static final Predicate<BlockInWorld> blockInWorldAPredicate = state ->
            stateIs(CNBlocks.REACTOR_CASING.get()).test(state)
            || stateIs(CNBlocks.REACTOR_OUTPUT.get()).test(state)
            || stateIs(CNBlocks.REACTOR_ROD_INPUT.get()).test(state)
            || stateIs(CNBlocks.REACTOR_FLUID_INPUT.get()).test(state)
            || stateIs(CNBlocks.REACTOR_ALARM.get()).test(state)
    ;

    static {
        REGISTRATE_MULTIBLOCK.register("createnuclear:reactor5x5",
                TypeMultiblock.REACTOR_T1,
                SimpleMultiBlockAislePatternBuilder.start()
                        .aisle("OOOOO", "OAAAO", "OAAAO", "OAAAO", "OOOOO")
                        .aisle("OABAO", "ADDDA", "BDCDB", "ADDDA", "OABAO")
                        .aisle("OABAO", "ADDDA", "BDCDB", "ADDDA", "OABAO")
                        .aisle("OABAO", "ADDDA", "BDCDB", "ADDDA", "OA*AO")
                        .aisle("OABAO", "ADDDA", "BDCDB", "ADDDA", "OABAO")
                        .aisle("OABAO", "ADDDA", "BDCDB", "ADDDA", "OABAO")
                        .aisle("OOOOO", "OAAAO", "OAAAO", "OAAAO", "OOOOO")
                        .where('A', blockInWorldAPredicate)
                        .where('B', stateIs(CNBlocks.REACTOR_FRAME.get()))
                        .where('C', stateIs(CNBlocks.REACTOR_CORE.get()))
                        .where('D', stateIs(CNBlocks.REACTOR_COOLER.get()))
                        .where('*', stateIs(CNBlocks.REACTOR_CONTROLLER.get()))
                        .where('O', stateIs(CNBlocks.REACTOR_CASING.get()))
                        .build()
        );

        REGISTRATE_MULTIBLOCK.register("createnuclear:reactor7x7",
                TypeMultiblock.REACTOR_T2,
                SimpleMultiBlockAislePatternBuilder.start()
                        .aisle("OOOOOOO", "OAAAAAO", "OAAAAAO", "OAAAAAO", "OAAAAAO", "OAAAAAO", "OOOOOOO")
                        .aisle("OABABAO", "ADDDDDA", "BDCDCDB", "ADDDDDA", "BDCDCDB", "ADDDDDA", "OABABAO")
                        .aisle("OABABAO", "ADDDDDA", "BDCDCDB", "ADDDDDA", "BDCDCDB", "ADDDDDA", "OABABAO")
                        .aisle("OABABAO", "ADDDDDA", "BDCDCDB", "ADDDDDA", "BDCDCDB", "ADDDDDA", "OABABAO")
                        .aisle("OABABAO", "ADDDDDA", "BDCDCDB", "ADDDDDA", "BDCDCDB", "ADDDDDA", "OAB*BAO")
                        .aisle("OABABAO", "ADDDDDA", "BDCDCDB", "ADDDDDA", "BDCDCDB", "ADDDDDA", "OABABAO")
                        .aisle("OABABAO", "ADDDDDA", "BDCDCDB", "ADDDDDA", "BDCDCDB", "ADDDDDA", "OABABAO")
                        .aisle("OABABAO", "ADDDDDA", "BDCDCDB", "ADDDDDA", "BDCDCDB", "ADDDDDA", "OABABAO")
                        .aisle("OOOOOOO", "OAAAAAO", "OAAAAAO", "OAAAAAO", "OAAAAAO", "OAAAAAO", "OOOOOOO")
                        .where('A', blockInWorldAPredicate)
                        .where('B', a -> a.getState().is(CNBlocks.REACTOR_FRAME.get()))
                        .where('C', a -> a.getState().is(CNBlocks.REACTOR_CORE.get()))
                        .where('D', a -> a.getState().is(CNBlocks.REACTOR_COOLER.get()))
                        .where('*', a -> a.getState().is(CNBlocks.REACTOR_CONTROLLER.get()))
                        .where('O', a -> a.getState().is(CNBlocks.REACTOR_CASING.get()))
                        .build()
        );

        REGISTRATE_MULTIBLOCK.register("createnuclear:reactor9x9",
                TypeMultiblock.REACTOR_T3,
                SimpleMultiBlockAislePatternBuilder.start()
                        .aisle("OOOOOOOOO", "OAAAAAAAO", "OAAAAAAAO", "OAAAAAAAO", "OAAAAAAAO", "OAAAAAAAO", "OAAAAAAAO", "OAAAAAAAO", "OOOOOOOOO")
                        .aisle("OBAABAABO", "BDDDDDDDB", "ADCDCDCDA", "ADDDDDDDA", "BDCDCDCDB", "ADDDDDDDA", "ADCDCDCDA", "BDDDDDDDB", "OBAABAABO")
                        .aisle("OBAABAABO", "BDDDDDDDB", "ADCDCDCDA", "ADDDDDDDA", "BDCDCDCDB", "ADDDDDDDA", "ADCDCDCDA", "BDDDDDDDB", "OBAABAABO")
                        .aisle("OBAABAABO", "BDDDDDDDB", "ADCDCDCDA", "ADDDDDDDA", "BDCDCDCDB", "ADDDDDDDA", "ADCDCDCDA", "BDDDDDDDB", "OBAABAABO")
                        .aisle("OBAABAABO", "BDDDDDDDB", "ADCDCDCDA", "ADDDDDDDA", "BDCDCDCDB", "ADDDDDDDA", "ADCDCDCDA", "BDDDDDDDB", "OBAABAABO")
                        .aisle("OBAABAABO", "BDDDDDDDB", "ADCDCDCDA", "ADDDDDDDA", "BDCDCDCDB", "ADDDDDDDA", "ADCDCDCDA", "BDDDDDDDB", "OBAA*AABO")
                        .aisle("OBAABAABO", "BDDDDDDDB", "ADCDCDCDA", "ADDDDDDDA", "BDCDCDCDB", "ADDDDDDDA", "ADCDCDCDA", "BDDDDDDDB", "OBAABAABO")
                        .aisle("OBAABAABO", "BDDDDDDDB", "ADCDCDCDA", "ADDDDDDDA", "BDCDCDCDB", "ADDDDDDDA", "ADCDCDCDA", "BDDDDDDDB", "OBAABAABO")
                        .aisle("OBAABAABO", "BDDDDDDDB", "ADCDCDCDA", "ADDDDDDDA", "BDCDCDCDB", "ADDDDDDDA", "ADCDCDCDA", "BDDDDDDDB", "OBAABAABO")
                        .aisle("OBAABAABO", "BDDDDDDDB", "ADCDCDCDA", "ADDDDDDDA", "BDCDCDCDB", "ADDDDDDDA", "ADCDCDCDA", "BDDDDDDDB", "OBAABAABO")
                        .aisle("OOOOOOOOO", "OAAAAAAAO", "OAAAAAAAO", "OAAAAAAAO", "OAAAAAAAO", "OAAAAAAAO", "OAAAAAAAO", "OAAAAAAAO", "OOOOOOOOO")
                        .where('A', blockInWorldAPredicate)
                        .where('B', a -> a.getState().is(CNBlocks.REACTOR_FRAME.get()))
                        .where('C', a -> a.getState().is(CNBlocks.REACTOR_CORE.get()))
                        .where('D', a -> a.getState().is(CNBlocks.REACTOR_COOLER.get()))
                        .where('*', a -> a.getState().is(CNBlocks.REACTOR_CONTROLLER.get()))
                        .where('O', a -> a.getState().is(CNBlocks.REACTOR_CASING.get()))
                        .build()
        );
    }

    private static Predicate<BlockInWorld> stateIs(Block block) {
        return a -> {
            BlockState state = a.getState();
            return state != null && state.is(block);
        };
    }
}
