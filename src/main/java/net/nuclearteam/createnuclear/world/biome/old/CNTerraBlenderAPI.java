package net.nuclearteam.createnuclear.world.biome.old;

import net.minecraft.resources.ResourceLocation;
import net.nuclearteam.createnuclear.CreateNuclear;
import net.nuclearteam.createnuclear.world.biome.old.surface.CNMaterialRules;
import terrablender.api.Regions;
import terrablender.api.SurfaceRuleManager;
import terrablender.api.TerraBlenderApi;

public class CNTerraBlenderAPI implements TerraBlenderApi {
    @Override
    public void onTerraBlenderInitialized() {
        Regions.register(new CNOverworldRegion(new ResourceLocation(CreateNuclear.MOD_ID, "irradiated_biome"), 4));

        SurfaceRuleManager.addSurfaceRules(SurfaceRuleManager.RuleCategory.OVERWORLD, CreateNuclear.MOD_ID, CNMaterialRules.makeRules());
    }
}
