package net.nuclearteam.createnuclear.advancement;

import com.google.common.collect.Sets;
import com.simibubi.create.AllItems;

import com.simibubi.create.foundation.advancement.CreateAdvancement;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.advancements.Advancement;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.nuclearteam.createnuclear.advancement.CreateNuclearAdvancement.Builder;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;


import static net.nuclearteam.createnuclear.advancement.CreateNuclearAdvancement.TaskType.SILENT;

public class CNAdvancement implements DataProvider {

    public static final List<CreateNuclearAdvancement> ENTRIES = new ArrayList<>();
    public static final CreateNuclearAdvancement START = null,

    TEST = create("test", b -> b.icon(AllItems.BRASS_HAND)
            .title("Welcome to Create Nuclear")
			.description("Here Be Contraptions")
			.awardedForFree()
			.special(SILENT));

    private final PackOutput output;

    private static CreateNuclearAdvancement create(String id, UnaryOperator<Builder> b) {
        return new CreateNuclearAdvancement(id, b);
    }

    public CNAdvancement(FabricDataOutput output) {
        this.output = output;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        PackOutput.PathProvider pathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, "advancements");
        List<CompletableFuture<?>> futures = new ArrayList<>();

        Set<ResourceLocation> set = Sets.newHashSet();
        Consumer<Advancement> consumer = (advancement) -> {
            ResourceLocation id = advancement.getId();
            if (!set.add(id))
                throw new IllegalStateException("Duplicate advancement " + id);
            Path path = pathProvider.json(id);
            futures.add(DataProvider.saveStable(cache, advancement.deconstruct()
                    .serializeToJson(), path));
        };

        for (CreateNuclearAdvancement advancement : ENTRIES)
            advancement.save(consumer);

        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    @Override
    public String getName() {
        return "Create Nuclear Advancements";
    }

    public static void provideLang(BiConsumer<String, String> consumer) {
        for (CreateNuclearAdvancement advancement : ENTRIES)
            advancement.provideLang(consumer);
    }
}
