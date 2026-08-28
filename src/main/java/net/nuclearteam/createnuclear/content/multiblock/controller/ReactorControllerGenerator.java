package net.nuclearteam.createnuclear.content.multiblock.controller;

import com.simibubi.create.foundation.data.SpecialBlockStateGen;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import io.github.fabricators_of_create.porting_lib.models.generators.ModelFile;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class ReactorControllerGenerator extends SpecialBlockStateGen {
    @Override
    protected int getXRotation(BlockState state) {
        return 0;
    }

    @Override
    protected int getYRotation(BlockState state) {
        if (state == null || !state.contains(ReactorControllerBlock.FACING)) {
            return 0;
        }
        return horizontalAngle(state.getValue(ReactorControllerBlock.FACING));
    }

    @Override
    public <T extends Block> ModelFile getModel(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov, BlockState state) {
        final String controllerPath = "block/reactor/controller/controller_panel_";

        ControllerVisualState visualState = ControllerVisualState.OFF;
        if (state.contains(ReactorControllerBlock.ASSEMBLED)
            && state.getValue(ReactorControllerBlock.ASSEMBLED)) {
            visualState = state.contains(ReactorControllerBlock.ACTIVE)
                && state.getValue(ReactorControllerBlock.ACTIVE)
                ? ControllerVisualState.ON
                : ControllerVisualState.STANDBY;
        }

        return prov.models()
            .withExistingParent(
                "block/reactor/controller/" + visualState.suffix,
                prov.modLoc("block/reactor/controller/block")
            )
            .texture("1", prov.modLoc(controllerPath + visualState.suffix))
            .texture("particle", prov.modLoc(controllerPath + visualState.suffix));
    }

    private enum ControllerVisualState {
        OFF("off"),
        ON("on"),
        STANDBY("standby");

        private final String suffix;

        ControllerVisualState(String suffix) {
            this.suffix = suffix;
        }
    }
}
