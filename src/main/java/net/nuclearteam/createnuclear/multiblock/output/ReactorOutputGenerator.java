package net.nuclearteam.createnuclear.multiblock.output;

import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.SpecialBlockStateGen;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import io.github.fabricators_of_create.porting_lib.models.generators.ModelFile;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

public class ReactorOutputGenerator extends SpecialBlockStateGen {

    @Override
    protected Property<?>[] getIgnoredProperties() {
        return new Property<?>[] {ReactorOutput.DIR};
    }

    @Override
    protected int getXRotation(BlockState state) {
        return state.getValue(ReactorOutput.FACING) == Direction.DOWN ? 180 : 0;
    }

    @Override
    protected int getYRotation(BlockState state) {
        return state.getValue(ReactorOutput.FACING).getAxis().isVertical()
                ? 0
                : horizontalAngle(state.getValue(ReactorOutput.FACING));
    }

    @Override
    public <T extends Block> ModelFile getModel(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov, BlockState state) {
        return state.getValue(ReactorOutput.FACING).getAxis().isVertical()
                ? AssetLookup.partialBaseModel(ctx, prov, "vertical")
                : AssetLookup.partialBaseModel(ctx, prov);
    }
}
