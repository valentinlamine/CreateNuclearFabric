package net.nuclearteam.createnuclear.foundation.data;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.DyeColor;
import net.minecraft.resources.ResourceLocation;
import io.github.fabricators_of_create.porting_lib.models.generators.item.ItemModelBuilder;
import io.github.fabricators_of_create.porting_lib.util.LazySpawnEggItem;
import net.nuclearteam.createnuclear.CreateNuclear;
import net.nuclearteam.createnuclear.content.biome.BiomeIrradiationExtractorItem;

import java.util.List;
import java.util.function.Supplier;

public class CNBuilderTransformers {

    /**
     * Generates the item model for a dyeable anti-radiation armor piece.
     * <p>
     * The undyed ("default") piece uses a flat 2D icon ({@code item/generated} with the
     * {@code item/armors/default_anti_radiation_<slot>} sprite) instead of the 3D Blockbench
     * model, since the dropped/inventory 3D rendering was problematic. The worn armor on the
     * body is unaffected (handled by {@code AntiRadiationArmorModel}).
     * <p>
     * One model override per {@link DyeColor} is added, driven by the client-side
     * {@code createnuclear:cloth_color} item property (see {@code CreateNuclearClient}). Each
     * per-color child model still re-textures the 3D Blockbench geometry ({@code item/<name>/item})
     * with the matching {@code item/armors/<color>_anti_radiation_suit} sheet.
     * <p>
     * The texture keys differ per slot: the helmet geometry uses {@code layer0}/{@code particle},
     * the other pieces use {@code 14} — hence {@code textureKeys} is passed explicitly.
     */
    public static <T extends Item> NonNullBiConsumer<DataGenContext<Item, T>, RegistrateItemModelProvider> coloredArmorModel(String slot, String... textureKeys) {
        return (c, p) -> {
            ResourceLocation baseParent = p.modLoc("item/" + c.getName() + "/item");
            ItemModelBuilder outer = p.generated(c, CreateNuclear.asResource("item/armors/default_anti_radiation_" + slot));
            // DyeColor.values() is ordered by id (0..15); overrides must be ascending by predicate
            // value so vanilla override resolution selects the exact color (returns last match <= value).cloth
            // The cloth_color property is clamped to [0,1] (see CreateNuclearClient), hence the
            // (id+1)/16 normalization here must match the value returned there exactly.
            for (DyeColor color : DyeColor.values()) {
                String colorName = color.getSerializedName();
                ItemModelBuilder child = p.withExistingParent("item/colored/" + colorName + "_anti_radiation_" + slot, baseParent);
                for (String key : textureKeys) {
                    child.texture(key, "item/armors/" + colorName + "_anti_radiation_suit");
                }
                outer.override()
                        .predicate(CreateNuclear.asResource("cloth_color"), (color.getId() + 1) / 16f)
                        .model(child)
                        .end();
            }
        };
    }

    /**
     * Generates the per-tier model overrides for the biome restore cell, driven by the
     * client-side {@code createnuclear:charge} item property (see {@code CreateNuclearClient}).
     * Overrides must stay ascending by predicate value (vanilla resolves to the last match <= value),
     * and the thresholds here must match the ratio returned by {@code biomeRestoreItemProperties}.
     */
    public static <T extends Item> NonNullBiConsumer<DataGenContext<Item, T>, RegistrateItemModelProvider> biomeRestoreModel() {
        return (c, p) -> {
            ItemModelBuilder outer = p.generated(c, CreateNuclear.asResource("item/biome_irradiation_extractor/empty"));

            record Tier(String name, float threshold) {}
            List<Tier> tiers = List.of(
                    new Tier("quarter", 0.25f),
                    new Tier("half", 0.5f),
                    new Tier("three_quarters", 0.75f),
                    new Tier("full", 1.0f)
            );

            for (Tier tier : tiers) {
                ItemModelBuilder child = p.withExistingParent("item/biome_irradiation_extractor/" + tier.name(), p.mcLoc("item/generated"))
                    .texture("layer0", CreateNuclear.asResource("item/biome_irradiation_extractor/" + tier.name()));

                outer.override()
                    .predicate(CreateNuclear.asResource(BiomeIrradiationExtractorItem.TAG), tier.threshold())
                    .model(child)
                    .end();
            }
        };
    }

    public static ItemEntry<LazySpawnEggItem> spawnEgg(String name, Supplier<? extends EntityType<? extends Mob>> entity, int backgroundColor, int highlightColor, String nameItems) {
        return CreateNuclear.REGISTRATE
                .item(name, p -> new LazySpawnEggItem(entity, backgroundColor, highlightColor, p))
                .lang(nameItems)
                .model((c, p) -> p.withExistingParent(c.getName(), new ResourceLocation("item/template_spawn_egg")))
                .register();
    }
}
