package net.nuclearteam.createnuclear.multiblock.frame;

import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.StringRepresentable;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeResolver;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.nuclearteam.createnuclear.CreateNuclear;
import net.nuclearteam.createnuclear.block.CNBlocks;
import net.nuclearteam.createnuclear.blockentity.CNBlockEntities;
import net.nuclearteam.createnuclear.multiblock.controller.ReactorControllerBlock;
import net.nuclearteam.createnuclear.multiblock.controller.ReactorControllerBlockEntity;

import net.nuclearteam.createnuclear.multiblock.input.ReactorInput;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ReactorBlock extends Block implements IWrenchable, IBE<ReactorBlockEntity> {
    private TypeBlock typeBlock;
    public boolean biomeUnchanged = true;

    public ReactorBlock(Properties properties, TypeBlock tBlock) {
        super(properties);
        this.typeBlock = tBlock;
    }


    @Override // Called when the block is placed on the world
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        CreateNuclear.LOGGER.info("ReactorBlock.onPlace");
        changeBiome(Biomes.DESERT, 12, new Vec3(20, 20, 20), (ServerLevel) level);
        List<? extends Player> players = level.players();
        FindController(pos, level, players, true);
    }

    @Override // called when the player destroys the block, with or without a tool
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
        super.playerDestroy(level, player, pos, state, blockEntity, tool);
        List<? extends Player> players = level.players();
        FindController(pos, level, players, false);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        super.onRemove(state, level, pos, newState, movedByPiston);
        List<? extends Player> players = level.players();
        FindController(pos, level, players, false);
    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();
        Player player = context.getPlayer();

        if (player.getItemInHand(InteractionHand.OFF_HAND).is(Blocks.HOPPER.asItem())) {
            level.setBlock(pos, CNBlocks.REACTOR_INPUT.getDefaultState().setValue(ReactorInput.FACING, context.getClickedFace()), 2);
            player.sendSystemMessage(Component.translatable("reactor.update.casing.input"));
        }

        return InteractionResult.SUCCESS;
    }

    public ReactorControllerBlock FindController(BlockPos blockPos, Level level, List<? extends Player> players, boolean first){ // Function that checks the surrounding blocks in order
        BlockPos newBlock; // to find the controller and verify the pattern
        Vec3i pos = new Vec3i(blockPos.getX(), blockPos.getY(), blockPos.getZ());
        for (int y = pos.getY()-3; y != pos.getY()+4; y+=1) {
            for (int x = pos.getX()-5; x != pos.getX()+5; x+=1) {
                for (int z = pos.getZ()-5; z != pos.getZ()+5; z+=1) {
                    newBlock = new BlockPos(x, y, z);
                    if (level.getBlockState(newBlock).is(CNBlocks.REACTOR_CONTROLLER.get())) { // verifying the pattern
                        CreateNuclear.LOGGER.info("ReactorController FOUND!!!!!!!!!!: ");      // from the controller
                        ReactorControllerBlock controller = (ReactorControllerBlock) level.getBlockState(newBlock).getBlock();
                        controller.Verify(level.getBlockState(newBlock), newBlock, level, players, first);
                        ReactorControllerBlockEntity entity = controller.getBlockEntity(level, newBlock);
                        if (entity.created) {
                            return controller;
                        }
                    }
                    //else CreateNuclear.LOGGER.info("newBlock: " + level.getBlockState(newBlock).getBlock());
                }
            }
        }
        return null;
    }

    public static Vec3i floorAll(double x, double y, double z) {
        return new Vec3i(Mth.floor(x), Mth.floor(y), Mth.floor(z));
    }

    public void changeBiome(ResourceKey<Biome> biomeResourceKey, int pass, Vec3 effectCenter, ServerLevel serverLevel) {
        CreateNuclear.LOGGER.info("ReactorBlock.changeBiome");
        //if(!biomeUnchanged) return;
        double range = 300;
        double passes = 13;
        BoundingBox boundingbox = BoundingBox.fromCorners(floorAll(300 + (passes - 1) * 5.0 + effectCenter.x, 300 + (passes - 1) * 5.0 + effectCenter.y, range + (passes - 1) * 5.0 + effectCenter.z), floorAll(effectCenter.x - range + (passes - 1) * 5.0, effectCenter.y - range + (passes - 1) * 5.0, effectCenter.z - range + (passes - 1) * 5.0));
        ArrayList<ChunkAccess> chunks = new ArrayList<>();
CreateNuclear.LOGGER.info("ReactorBlock.change: {}", boundingbox);
        for (int k = SectionPos.blockToSectionCoord(boundingbox.minZ()); k <= SectionPos.blockToSectionCoord(boundingbox.maxZ()); ++k) {
            for (int l = SectionPos.blockToSectionCoord(boundingbox.minX()); l <= SectionPos.blockToSectionCoord(boundingbox.maxX()); ++l) {
                ChunkAccess chunkAccess = serverLevel.getChunk(l, k, ChunkStatus.FULL, false);
                if (chunkAccess != null)
                    chunks.add(chunkAccess);
            }
        }

        for (ChunkAccess chunkAccess : chunks) {
            Registry<Biome> biomeRegistry = serverLevel.registryAccess().registryOrThrow(Registries.BIOME);
            Biome biome = biomeRegistry.get(biomeResourceKey);
            assert biome != null;
            chunkAccess.fillBiomesFromNoise(makeResolver(biomeRegistry.wrapAsHolder(biome)), serverLevel.getChunkSource().randomState().sampler());
            chunkAccess.setUnsaved(true);
        }

        serverLevel.getChunkSource().chunkMap.resendBiomesForChunks(chunks);
        biomeUnchanged = false;

    }

    public static BiomeResolver makeResolver(Holder<Biome> biomeHolder) {
        return (x, y, z, climateSampler) -> biomeHolder;
    }

    @Override
    public Class<ReactorBlockEntity> getBlockEntityClass() {
        return ReactorBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends ReactorBlockEntity> getBlockEntityType() {
        return switch (typeBlock) {
            case CORE -> CNBlockEntities.REACTOR_CORE.get();
            case CASING -> CNBlockEntities.REACTOR_CASING.get();
        };

    }

    public enum TypeBlock implements StringRepresentable {
        CASING,
        CORE,
        ;

        @Override
        public String getSerializedName() {
            return Lang.asId(name());
        }
    }
}
