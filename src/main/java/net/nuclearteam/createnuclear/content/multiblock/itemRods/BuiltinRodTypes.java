package net.nuclearteam.createnuclear.content.multiblock.itemRods;

import net.nuclearteam.createnuclear.CNItems;
import net.nuclearteam.createnuclear.CNTags.CNItemTags;
import net.nuclearteam.createnuclear.CreateNuclear;
import net.nuclearteam.createnuclear.infrastructure.config.CNConfigs;
import net.nuclearteam.createnuclear.content.multiblock.itemRods.ItemRodType.RodComponentType;

public class BuiltinRodTypes {
    public static final ItemRodType
        URANIUM = create("uranium")
            .type(RodComponentType.FUEL)
            .baseValueRod(CNConfigs.common().rods.baseValueUranium.get())
            .lifeTime(CNConfigs.common().rods.uraniumRodLifetime.get())
            .proxyBonus(CNConfigs.common().rods.BoProxyUranium.get())
            .addTags(CNItemTags.FUEL.tag)
            .registerAndAssign(CNItems.URANIUM_ROD),

        GRAPHITE = create("graphite")
            .type(RodComponentType.COOLER)
            .baseValueRod(CNConfigs.common().rods.baseValueGraphite.get())
            .lifeTime(CNConfigs.common().rods.graphiteRodLifetime.get())
            .proxyBonus(CNConfigs.common().rods.MaProxigraphite.get())
            .addTags(CNItemTags.COOLER.tag)
            .registerAndAssign(CNItems.GRAPHITE_ROD),

        FALLBACK = create("fallback")
            .type(RodComponentType.NONE)
            .baseValueRod(0)
            .lifeTime(0)
            .proxyBonus(0)
            .register()
    ;

    private static ItemRodType.Builder create(String name) {
        return new ItemRodType.Builder(CreateNuclear.asResource(name));
    }
    public static void register() {}
}
