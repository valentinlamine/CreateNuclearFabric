package net.nuclearteam.createnuclear.entity;

import com.google.common.collect.Sets;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.nuclearteam.createnuclear.CreateNuclear;

import java.util.Set;

public class CNModelLayers {
    private static final Set<ModelLayerLocation> ALL_MODELS = Sets.newHashSet();

    public static final ModelLayerLocation IRRADIATED_CHICKEN = register("irradiated_chicken");
    public static final ModelLayerLocation IRRADIATED_WOLF = register("irradiated_wolf");
    public static final ModelLayerLocation IRRADIATED_CAT = register("irradiated_cat");

    private static ModelLayerLocation register(String path) {
        return register(path, "main");
    }

    private static ModelLayerLocation register(String path, String model) {
        ModelLayerLocation modelLayerLocation = createLocation(path, model);
        if (!ALL_MODELS.add(modelLayerLocation)) {
            throw new IllegalStateException("Duplicate registration for " + modelLayerLocation);
        } else {
            return modelLayerLocation;
        }
    }

    private static ModelLayerLocation createLocation(String path, String model) {
        return new ModelLayerLocation(CreateNuclear.asResource(path), model);
    }
}