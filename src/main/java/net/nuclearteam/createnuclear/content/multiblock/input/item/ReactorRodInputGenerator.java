package net.nuclearteam.createnuclear.content.multiblock.input.item;

import com.simibubi.create.foundation.data.SpecialBlockStateGen;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.Direction;
import io.github.fabricators_of_create.porting_lib.models.generators.ModelFile;

public class ReactorRodInputGenerator extends SpecialBlockStateGen {
    @Override
    protected int getXRotation(BlockState state) {
        return state.getValue(ReactorRodInput.FACING) == Direction.DOWN ? 180 : 0;
    }

    @Override
    protected int getYRotation(BlockState state) {
        return state.getValue(ReactorRodInput.FACING).getAxis().isVertical()
                ? 0
                : horizontalAngle(state.getValue(ReactorRodInput.FACING));
    }

    @Override
    public <T extends Block> ModelFile getModel(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov, BlockState state) {
        return prov
                .models()
                .getExistingFile(prov
                        .modLoc("block/reactor/rod_input/rod_input" + (state.getValue(ReactorRodInput.FACING).getAxis().isVertical()
                                ? "_vertical"
                                : ""
                        )));
    }
}
