package net.nuclearteam.createnuclear.content.multiblock.reinforced;

import com.simibubi.create.content.decoration.palettes.ConnectedGlassBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.nuclearteam.createnuclear.content.multiblock.MultiblockHelpers;
import net.nuclearteam.createnuclear.foundation.advancement.CNAdvancementBehaviour;

import javax.annotation.Nullable;

public class ReinforcedGlassBlock extends ConnectedGlassBlock {
    public ReinforcedGlassBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity pPlacer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, pPlacer, stack);
        MultiblockHelpers.handleAdvancedPlacedBy(pos, level, pPlacer);
    }
}
