package net.nuclearteam.createnuclear.world.biome;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.nuclearteam.createnuclear.CreateNuclear;
import net.nuclearteam.createnuclear.world.biome.surface.IrradiatedSurfaceRuleData;
import terrablender.api.Region;
import terrablender.api.Regions;
import terrablender.api.SurfaceRuleManager;
import terrablender.api.TerraBlenderApi;

public class Biomes implements TerraBlenderApi {
    @Override
    public void onTerraBlenderInitialized() {
        Regions.register(new IrradiatedBiomesRegion(CreateNuclear.asResource("overworld_1"), 2));

        SurfaceRuleManager.addSurfaceRules(SurfaceRuleManager.RuleCategory.OVERWORLD, CreateNuclear.MOD_ID, IrradiatedSurfaceRuleData.makeRules());
    }
}
