package net.nuclearteam.createnuclear.infrastructure.ponder;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.createmod.catnip.platform.CatnipServices;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.minecraft.world.level.ItemLike;
import net.minecraft.resources.ResourceLocation;
import net.nuclearteam.createnuclear.CNBlocks;
import net.nuclearteam.createnuclear.CreateNuclear;

import static com.simibubi.create.infrastructure.ponder.AllCreatePonderTags.DISPLAY_SOURCES;
import static com.simibubi.create.infrastructure.ponder.AllCreatePonderTags.KINETIC_SOURCES;


public class CNCreateNuclearPonderTags {
    private static ResourceLocation loc(String id) {
        return CreateNuclear.asResource(id);
    }

    public static void register(PonderTagRegistrationHelper<ResourceLocation> helper) {
        PonderTagRegistrationHelper<RegistryEntry<?>> HELPER = helper.withKeyFunction(RegistryEntry::getId);
        PonderTagRegistrationHelper<ItemLike> itemHelper = helper.withKeyFunction(
                CatnipServices.REGISTRIES::getKeyOrThrow);

        helper.registerTag(KINETIC_SOURCES)
                .addToIndex()
                .item(CNBlocks.REACTOR_CONTROLLER.asItem())
                .title("Kinetic Nuclear")
                .register();

        HELPER.addToTag(KINETIC_SOURCES)
                .add(CNBlocks.REACTOR_CONTROLLER)
        ;

        HELPER.addToTag(DISPLAY_SOURCES)
                .add(CNBlocks.REACTOR_CONTROLLER)
        ;

    }

}
