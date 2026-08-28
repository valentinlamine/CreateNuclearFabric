package net.nuclearteam.createnuclear.foundation.ponder;

import net.createmod.ponder.api.registration.PonderPlugin;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.minecraft.resources.ResourceLocation;
import net.nuclearteam.createnuclear.CreateNuclear;
import net.nuclearteam.createnuclear.infrastructure.ponder.CNCreateNuclearPonderTags;

public class CreateNuclearPonderPlugin implements PonderPlugin {
    @Override
    public String getModId() {
        return CreateNuclear.MOD_ID;
    }

    @Override
    public void registerScenes(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        CNPonderIndex.register(helper);
    }

    @Override
    public void registerTags(PonderTagRegistrationHelper<ResourceLocation> helper) {
        CNCreateNuclearPonderTags.register(helper);
    }
}
