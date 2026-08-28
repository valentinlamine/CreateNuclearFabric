package net.nuclearteam.createnuclear.content.multiblock.cooler;

import com.simibubi.create.content.equipment.wrench.IWrenchable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.nuclearteam.createnuclear.content.multiblock.MultiblockHelpers;
import net.nuclearteam.createnuclear.content.multiblock.pattern.ReactorPattern;
import net.nuclearteam.createnuclear.foundation.advancement.CNAdvancementBehaviour;

import javax.annotation.Nullable;

public class ReactorCooler extends Block implements IWrenchable {
    public ReactorCooler(Properties properties) {
        super(properties);
    }

    protected ReactorPattern pattern =  new ReactorPattern();

    @Override // Called when the block is placed on the world
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        pattern.findController(pos, level, true);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity pPlacer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, pPlacer, stack);
        MultiblockHelpers.handleAdvancedPlacedBy(pos, level, pPlacer);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        super.onRemove(state, level, pos, newState, movedByPiston);
        pattern.findController(pos, level, false);
    }
}
