package net.nuclearteam.createnuclear.config;

import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry;
import fuzs.forgeconfigapiport.api.config.v2.ModConfigEvents;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;

import net.nuclearteam.createnuclear.CreateNuclear;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
@SuppressWarnings("unused")
public class CNConfigs {
    private static final Map<ModConfig.Type, CNConfigBase> CONFIGS = new EnumMap<ModConfig.Type, CNConfigBase>(ModConfig.Type.class);

    private static CNConfigCommon common;

    public static CNConfigCommon common() {
        return common;
    }

    public static CNConfigBase getType(ModConfig.Type type) {
        return CONFIGS.get(type);
    }

    private static <T extends CNConfigBase> T register(Supplier<T> factory, ModConfig.Type side) {
        Pair<T, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(builder ->  {
            T config = factory.get();
            config.registerAll(builder);
            return config;
        });

        T config = specPair.getLeft();
        config.specification = specPair.getRight();
        CONFIGS.put(side, config);
        return config;
    }

    public static void register() {
        common = register(CNConfigCommon::new, ModConfig.Type.COMMON);

        for (Map.Entry<ModConfig.Type, CNConfigBase> pair : CONFIGS.entrySet())
            ForgeConfigRegistry.INSTANCE.register(CreateNuclear.MOD_ID, pair.getKey(), pair.getValue().specification);


        ModConfigEvents.loading(CreateNuclear.MOD_ID).register(CNConfigs::onLoad);
        ModConfigEvents.reloading(CreateNuclear.MOD_ID).register(CNConfigs::onReload);
    }

    public static void onLoad(ModConfig modConfig) {
        for (CNConfigBase config : CONFIGS.values())
            if (config.specification == modConfig
                    .getSpec())
                config.onLoad();
    }

    public static void onReload(ModConfig modConfig) {
        for (CNConfigBase config : CONFIGS.values())
            if (config.specification == modConfig
                    .getSpec())
                config.onReload();
    }
}
