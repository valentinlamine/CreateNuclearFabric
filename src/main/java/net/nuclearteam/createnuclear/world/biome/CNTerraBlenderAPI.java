package net.nuclearteam.createnuclear.world.biome;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.nuclearteam.createnuclear.CreateNuclear;
import net.nuclearteam.createnuclear.world.biome.surface.CNMaterialRules;
import terrablender.api.Region;
import terrablender.api.Regions;
import terrablender.api.SurfaceRuleManager;
import terrablender.api.TerraBlenderApi;

import java.util.concurrent.locks.ReentrantLock;

public class CNTerraBlenderAPI implements TerraBlenderApi {
    @Override
    public void onTerraBlenderInitialized() {
        Regions.register(new CNOverworldRegion(new ResourceLocation(CreateNuclear.MOD_ID, "irradiated_biome"), 4));

        SurfaceRuleManager.addSurfaceRules(SurfaceRuleManager.RuleCategory.OVERWORLD, CreateNuclear.MOD_ID, CNMaterialRules.makeRules());
    }
}
