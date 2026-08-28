package net.nuclearteam.createnuclear;

import com.simibubi.create.api.behaviour.display.DisplaySource;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.nuclearteam.createnuclear.content.redstone.displayLink.source.*;

import java.util.function.Supplier;

public class CNDisplaySources {
    private static final CreateRegistrate REGISTRATE = CreateNuclear.REGISTRATE;

    public static final RegistryEntry<HeatDisplaySource> HEAT = simple("heat", HeatDisplaySource::new);
    public static final RegistryEntry<LiquidLevelDisplaySource> LIQUID_LEVEL = simple("liquid_level", LiquidLevelDisplaySource::new);
    public static final RegistryEntry<ReactorSummaryDisplaySource> REACTOR_SUMMARY = simple("reactor_summary", ReactorSummaryDisplaySource::new);
    public static final RegistryEntry<FuelDisplaySource> FUEL = simple("fuel", FuelDisplaySource::new);
    public static final RegistryEntry<CoolerDisplaySource> COOLER = simple("cooler", CoolerDisplaySource::new);
    public static final RegistryEntry<ReactorSizeDisplaySource> REACTOR_SIZE = simple("reactor_size", ReactorSizeDisplaySource::new);

    private static <T extends DisplaySource> RegistryEntry<T> simple(String name, Supplier<T> supplier) {
        return REGISTRATE.displaySource(name, supplier).register();
    }

    public static void register() {
    }
}
