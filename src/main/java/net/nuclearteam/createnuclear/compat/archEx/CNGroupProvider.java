package net.nuclearteam.createnuclear.compat.archEx;

import com.google.gson.JsonObject;

import java.nio.file.Path;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataProvider;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.nuclearteam.createnuclear.content.decoration.palettes.CNPaletteStoneTypes;
import net.nuclearteam.createnuclear.content.decoration.palettes.PaletteBlockPattern;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class CNGroupProvider implements DataProvider {
    public static final Set<PaletteBlockPattern> EXTENDABLE_PATTERNS = Set.of(
            PaletteBlockPattern.CUT, PaletteBlockPattern.POLISHED, PaletteBlockPattern.BRICKS, PaletteBlockPattern.SMALL_BRICKS
    );

    private final FabricDataOutput out;
    public final List<CNArchExGroup> groups;

    public CNGroupProvider(FabricDataOutput out) {
        this.out = out;
        this.groups = generateGroups();
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        Path outputDir = out.getOutputFolder()
                .resolve("staticdata")
                .resolve("architecture_extensions");
        List<CompletableFuture<?>> saveFutures = new ArrayList<>();

        this.groups.forEach(group -> {
            JsonObject json = group.toJson();
            Path outputFile = outputDir.resolve(group.name() + ".json");
            CompletableFuture<?> future = DataProvider.saveStable(output, json, outputFile);
            saveFutures.add(future);
        });
        return CompletableFuture.allOf(saveFutures.toArray(CompletableFuture[]::new));
    }

    private List<CNArchExGroup> generateGroups() {
        List<CNArchExGroup> groups = new ArrayList<>();
        for (CNPaletteStoneTypes stoneTypes : CNPaletteStoneTypes.values()) {
            for (PaletteBlockPattern blockPattern : stoneTypes.variantTypes) {
                if (EXTENDABLE_PATTERNS.contains(blockPattern)) {
                    CNArchExGroup group = CNArchExGroup.builder()
                            .fromStoneTypeAndPattern(stoneTypes, blockPattern)
                            .build();
                    groups.add(group);
                }
            }
        }
        return groups;
    }

    @Override
    public @NotNull String getName() {
        return "CreateNuclear ArchEx compat";
    }
}
