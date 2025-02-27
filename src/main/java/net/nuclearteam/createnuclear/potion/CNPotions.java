package net.nuclearteam.createnuclear.potion;

import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.nuclearteam.createnuclear.CreateNuclear;
import net.nuclearteam.createnuclear.effects.CNEffects;
import net.nuclearteam.createnuclear.item.CNItems;

public class CNPotions {

    public static final Potion POTION_RADIATION_1 = CreateNuclear.NUCLEAR_REGISTRATE
            .potion("potion_of_radiation_1", potion -> potion)
            .effect(CNEffects.RADIATION.get(), 900)
            .registerTest();

    public static final Potion POTION_AUGMENT_1 = CreateNuclear.NUCLEAR_REGISTRATE
            .potion("potion_of_radiation_augment_1", potion -> potion)
            .effect(CNEffects.RADIATION.get(), 1800)
            .registerTest();

    public static final Potion POTION_RADIATION_2 = CreateNuclear.NUCLEAR_REGISTRATE
            .potion("potion_of_radiation_2", potion -> potion)
            .effect(CNEffects.RADIATION.get(), 410, 1)
            .registerTest();

    public static void registerPotionRecipes() {
        FabricBrewingRecipeRegistry.registerPotionRecipe(Potions.AWKWARD, Ingredient.of(CNItems.ENRICHED_YELLOWCAKE.get()), POTION_RADIATION_1);
        FabricBrewingRecipeRegistry.registerPotionRecipe(POTION_RADIATION_1, Ingredient.of(Items.REDSTONE), POTION_AUGMENT_1);
        FabricBrewingRecipeRegistry.registerPotionRecipe(POTION_RADIATION_1, Ingredient.of(Items.REDSTONE), POTION_RADIATION_2);
    }

    public static void init() {
        CreateNuclear.LOGGER.info("Registering Potion Effects");
    }


}
