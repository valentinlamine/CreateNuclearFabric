package net.nuclearteam.createnuclear.foundation.ponder;


import com.tterrag.registrate.util.entry.ItemProviderEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.minecraft.resources.ResourceLocation;
import net.nuclearteam.createnuclear.CNBlocks;
import net.nuclearteam.createnuclear.CNItems;
import net.nuclearteam.createnuclear.infrastructure.ponder.scenes.CNPonderReactorScenes;


public class CNPonderIndex {

    public static void register(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        PonderSceneRegistrationHelper<ItemProviderEntry<?>> HELPER = helper.withKeyFunction(RegistryEntry::getId);

        // Reactor - Storyboards pour chaque taille
        HELPER.forComponents(CNBlocks.REACTOR_CONTROLLER)
                .addStoryBoard("reactor/reactor_t1_ponder", CNPonderReactorScenes::t1)
                .addStoryBoard("reactor/reactor_t2_ponder", CNPonderReactorScenes::t2)
                .addStoryBoard("reactor/reactor_t3_ponder", CNPonderReactorScenes::t3)
                .addStoryBoard("reactor/reactor_t1_ponder", CNPonderReactorScenes::ioPlacement);

        HELPER.forComponents(CNItems.REACTOR_BLUEPRINT)
                .addStoryBoard("reactor/reactor_t1_ponder", CNPonderReactorScenes::t1)
                .addStoryBoard("reactor/reactor_t2_ponder", CNPonderReactorScenes::t2)
                .addStoryBoard("reactor/reactor_t3_ponder", CNPonderReactorScenes::t3)
                .addStoryBoard("reactor/reactor_t1_ponder", CNPonderReactorScenes::ioPlacement);
    }
}