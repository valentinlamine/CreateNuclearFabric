package net.nuclearteam.createnuclear.multiblock.gauge;

import com.simibubi.create.Create;
import com.simibubi.create.content.equipment.clipboard.ClipboardOverrides;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import io.github.fabricators_of_create.porting_lib.models.generators.ConfiguredModel;
import io.github.fabricators_of_create.porting_lib.models.generators.ModelFile;
import io.github.fabricators_of_create.porting_lib.models.generators.item.ItemModelBuilder;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.nuclearteam.createnuclear.CreateNuclear;

public class ReactorGaugeBlockOverrides {
    public static ItemModelBuilder addOverrideModels(DataGenContext<Item, ReactorGaugeBlockItem> c, RegistrateItemModelProvider p) {
        ItemModelBuilder builder = p.generated(c);
        return builder.override()
                .model(p.getBuilder("block/reactor_main_frame/reactor_gauge_none")
                        .parent(p.getExistingFile(p.modLoc("block/reactor_main_frame/reactor_gauge_none"))))
                .end();
                //.predicate(CreateNuclear.asResource("reactor_main_frame"), 0)
    }


}
