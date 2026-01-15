package net.nuclearteam.createnuclear.content.multiblock.liquidInput;

import com.simibubi.create.foundation.data.SpecialBlockStateGen;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import io.github.fabricators_of_create.porting_lib.models.generators.ModelFile;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class ReactorLiquidInputGenerator extends SpecialBlockStateGen {
    @Override
    protected int getXRotation(BlockState state) {
        return state.getValue(ReactorLiquidInput.FACING) == Direction.DOWN ? 180 : 0;
    }

    @Override
    protected int getYRotation(BlockState state) {
        return state.getValue(ReactorLiquidInput.FACING).getAxis().isVertical()
                ? 0
                : horizontalAngle(state.getValue(ReactorLiquidInput.FACING));
    }

    @Override
    public <T extends Block> ModelFile getModel(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov, BlockState state) {
        return prov
                .models()
                .getExistingFile(prov
                        .modLoc("block/reactor/liquid_input/liquid_input" + (state.getValue(ReactorLiquidInput.FACING).getAxis().isVertical()
                                ? "_vertical"
                                : ""
                        )));
    }
}
