package net.ynov.createnuclear.datagen;

import com.simibubi.create.compat.archEx.GroupProvider;
import com.simibubi.create.compat.archEx.LangProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator.Pack;
import net.ynov.createnuclear.CreateNuclear;


public class CNArchExCompat {
    public static void init(Pack pack) {
        GroupProvider groupProvider = pack.addProvider(GroupProvider::new);
        new LangProvider(CreateNuclear.MOD_ID, groupProvider.groups, CreateNuclear.REGISTRATE::addRawLang).run();
    }
}
