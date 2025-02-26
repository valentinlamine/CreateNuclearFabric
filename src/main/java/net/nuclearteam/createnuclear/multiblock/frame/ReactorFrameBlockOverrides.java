package net.nuclearteam.createnuclear.multiblock.frame;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import io.github.fabricators_of_create.porting_lib.models.generators.item.ItemModelBuilder;
import net.minecraft.world.item.Item;
import net.nuclearteam.createnuclear.CreateNuclear;

public class ReactorFrameBlockOverrides {
    public static ItemModelBuilder addOverrideModels(DataGenContext<Item, ReactorFrameItem> c, RegistrateItemModelProvider p) {
        ItemModelBuilder builder = p.generated(c);

        return builder.override()
                .model(p.getBuilder("block/reactor_frame/reactor_frame_none")
                        .parent(p.getExistingFile(p.modLoc("block/reactor_frame/reactor_frame_none"))))
                .end();
                //.predicate(CreateNuclear.asResource("reactor_main_frame"), 0)
    }


}
