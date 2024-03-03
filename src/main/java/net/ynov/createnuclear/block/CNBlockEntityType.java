package net.ynov.createnuclear.block;
/*
import com.mojang.datafixers.types.Type;
import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.ynov.createnuclear.blockentity.EnrichingCampFireEntity;

import java.util.Set;

public class CNBlockEntityType<T extends BlockEntity>  {

    public static final BlockEntityType<CampfireBlockEntity> CAMPFIRE_TEST = register("campfire", BlockEntityType.Builder.of(EnrichingCampFireEntity::new, Blocks.CAMPFIRE, Blocks.SOUL_CAMPFIRE, CNBlocks.ENRICHING_CAMPFIRE.get()));

    private static <T extends BlockEntity> BlockEntityType<T> register(String key, BlockEntityType.Builder<T> builder) {
        Type<?> type = Util.fetchChoiceType(References.BLOCK_ENTITY, key);
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, key, builder.build(type));
    }


    public static void register() {

    }
}
*/