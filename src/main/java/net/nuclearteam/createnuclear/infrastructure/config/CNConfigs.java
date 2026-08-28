package net.nuclearteam.createnuclear.infrastructure.config;

import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry;
import fuzs.forgeconfigapiport.api.config.v2.ModConfigEvents;
import net.createmod.catnip.config.ConfigBase;
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
    private static final Map<ModConfig.Type, ConfigBase> CONFIGS = new EnumMap<>(ModConfig.Type.class);

    private static CNCClient client;
    private static CNCCommon common;
    private static CNCServer server;

    public static CNCClient client() {
        return client;
    }

    public static CNCCommon common() {
        return common;
    }

    public static CNCServer server() {
        return server;
    }

    public static ConfigBase getType(ModConfig.Type type) {
        return CONFIGS.get(type);
    }

    public static ConfigBase byType(ModConfig.Type type) {
        return CONFIGS.get(type);
    }

    private static <T extends ConfigBase> T register(Supplier<T> factory, ModConfig.Type side) {
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
        common = register(CNCCommon::new, ModConfig.Type.COMMON);
        server = register(CNCServer::new, ModConfig.Type.SERVER);
        client = register(CNCClient::new, ModConfig.Type.CLIENT);

        for (Map.Entry<ModConfig.Type, ConfigBase> pair : CONFIGS.entrySet())
            ForgeConfigRegistry.INSTANCE.register(CreateNuclear.MOD_ID, pair.getKey(), pair.getValue().specification);


        ModConfigEvents.loading(CreateNuclear.MOD_ID).register(CNConfigs::onLoad);
        ModConfigEvents.reloading(CreateNuclear.MOD_ID).register(CNConfigs::onReload);
    }

    public static void onLoad(ModConfig modConfig) {
        for (ConfigBase config : CONFIGS.values())
            if (config.specification == modConfig
                    .getSpec())
                config.onLoad();
    }

    public static void onReload(ModConfig modConfig) {
        for (ConfigBase config : CONFIGS.values())
            if (config.specification == modConfig
                    .getSpec())
                config.onReload();
    }
}
