package net.nuclearteam.createnuclear.world.biome.surface;


import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.material.MaterialRuleList;
import net.nuclearteam.createnuclear.block.CNBlocks;
import net.nuclearteam.createnuclear.world.biome.CNBiome;

public class CNMaterialRules {

    public static final SurfaceRules.RuleSource DIRT = makeStateRule(Blocks.DIRT);
    public static final SurfaceRules.RuleSource GRASS_BLOCK = makeStateRule(Blocks.GRASS_BLOCK);
    public static final SurfaceRules.RuleSource NETHERITE_BLOCK = makeStateRule(Blocks.NETHERITE_BLOCK);
    public static final SurfaceRules.RuleSource LEAD_ORE = makeStateRule(CNBlocks.LEAD_ORE.get());


    public static SurfaceRules.RuleSource makeRules()
    {
        SurfaceRules.ConditionSource isAtOrAboveWaterLevel = SurfaceRules.waterBlockCheck(-1, 0);
        SurfaceRules.RuleSource grassSurface = SurfaceRules.sequence(SurfaceRules.ifTrue(isAtOrAboveWaterLevel, GRASS_BLOCK), DIRT);

        return SurfaceRules.sequence(
                SurfaceRules.ifTrue(SurfaceRules.isBiome(CNBiome.IRRADIATED_BIOME), LEAD_ORE),

                // Default to a grass and dirt surface
                SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, grassSurface)

        );
    }

    public static SurfaceRules.RuleSource makeStateRule(Block block)
    {
        return SurfaceRules.state(block.defaultBlockState());
    }

}
