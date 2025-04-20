package net.nuclearteam.createnuclear.world.biome;

import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.nuclearteam.createnuclear.CreateNuclear;

public enum d {
    HOT_RED,
    COLD_BLUE
    ;

    private final ResourceKey<Biome> biome;

    d() {
        this.biome = ResourceKey.create(Registries.BIOME, CreateNuclear.asResource(Lang.asId(name())));
    }

    d(String name) {
        this.biome = ResourceKey.create(Registries.BIOME, CreateNuclear.asResource(name));
    }

    public ResourceKey<Biome> getBiome() {
        return this.biome;
    }

    public static void bootstrap(BootstapContext<Biome> context) {
        context.register(HOT_RED.getBiome(), testBiome());
    }

    public static Biome testBiome() {

        return new Biome.BiomeBuilder()
                .build();
    }
}
