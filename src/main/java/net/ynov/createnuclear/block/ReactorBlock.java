package net.ynov.createnuclear.block;

import io.github.fabricators_of_create.porting_lib.event.common.GrindstoneEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.ynov.createnuclear.CreateNuclear;
import net.ynov.createnuclear.block.CNBlocks;
import org.jetbrains.annotations.Nullable;

import static org.apache.logging.log4j.Level.getLevel;

public class ReactorBlock extends Block {
    public ReactorBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        CreateNuclear.LOGGER.info("position block test: " + pos);
        FindController(pos, level);
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
        super.playerDestroy(level, player, pos, state, blockEntity, tool);
        CreateNuclear.LOGGER.info("player destroy test: " + pos);
        FindController(pos, level);
    }

    public void FindController(BlockPos blockPos, Level level){
        BlockPos newBlock;
        Vec3i pos = new Vec3i(blockPos.getX(), blockPos.getY(), blockPos.getZ());
        CreateNuclear.LOGGER.info("blockPos: " +pos.toString());
        for (int y = pos.getY()-3; y != pos.getY()+4; y+=1) {
            for (int x = pos.getX()-5; x != pos.getX()+5; x+=1) {
                for (int z = pos.getZ()-5; z != pos.getZ()+5; z+=1) {
                    newBlock = new BlockPos(x, y, z);
                    if (level.getBlockState(newBlock).is(CNBlocks.REACTOR_CONTROLLER.get())) {
                        CreateNuclear.LOGGER.info("ReactorController FOUND!!!!!!!!!!: ");
                        ReactorController controller = (ReactorController) level.getBlockState(newBlock).getBlock();
                        controller.Verify(newBlock, level);
                        return;
                    }
                    //else CreateNuclear.LOGGER.info("newBlock: " + level.getBlockState(newBlock).getBlock());
                }
            }
        }
    }
}
