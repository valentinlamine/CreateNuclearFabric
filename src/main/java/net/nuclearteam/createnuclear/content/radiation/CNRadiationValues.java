package net.nuclearteam.createnuclear.content.radiation;

import com.simibubi.create.AllItems;
import net.nuclearteam.createnuclear.api.radiation.RadiationRegistry;
import net.nuclearteam.createnuclear.infrastructure.worldgen.biome.CNBiomes;

public class CNRadiationValues {
    public static void register() {
        RadiationRegistry.register()
            .item(AllItems.CRUSHED_URANIUM.asItem())
            .value(.5D)
            .build();

        RadiationRegistry.register()
            .biome(CNBiomes.Irradiated.PLAIN)
            .value(25D)
            .build();
    }
}
