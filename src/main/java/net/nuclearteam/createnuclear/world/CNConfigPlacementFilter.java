package net.nuclearteam.createnuclear.world;

import com.mojang.serialization.Codec;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementFilter;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.nuclearteam.createnuclear.config.CNConfigs;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CNConfigPlacementFilter extends PlacementFilter {
    public static final CNConfigPlacementFilter INSTANCE = new CNConfigPlacementFilter();
    public static final Codec<CNConfigPlacementFilter> CODEC = Codec.unit(() -> INSTANCE);

    @Override
    protected boolean shouldPlace(PlacementContext context, RandomSource random, BlockPos pos) {
        return !CNConfigs.common().worldGen.disable.get();
    }

    @Override
    public PlacementModifierType<?> type() {
        return CNPlacementModifiers.CONFIG_FILTER.get();
    }
}
