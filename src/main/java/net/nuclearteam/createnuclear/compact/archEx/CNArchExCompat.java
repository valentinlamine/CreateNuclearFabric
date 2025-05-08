package net.nuclearteam.createnuclear.compact.archEx;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator.Pack;
import net.nuclearteam.createnuclear.CreateNuclear;

public class CNArchExCompat {
    public static void init(Pack pack) {
        CNGroupProvider groupProvider = pack.addProvider(CNGroupProvider::new);
        new CNLangProvider(CreateNuclear.MOD_ID, groupProvider.groups, CreateNuclear.REGISTRATE::addRawLang).run();
    }
}
