package net.nuclearteam.createnuclear.potion;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.nuclearteam.createnuclear.CreateNuclear;
import net.nuclearteam.createnuclear.effects.CNEffects;
import net.nuclearteam.createnuclear.item.CNItems;

public class CNPotions {

    public static final Potion potion_1 = registerPotion("potion_of_radiation_1", new Potion(new MobEffectInstance(CNEffects.RADIATION.get(), 900)));
    public static final Potion potion_augment_1 = registerPotion("potion_of_radiation_augment_1", new Potion(new MobEffectInstance(CNEffects.RADIATION.get(), 1800)));
    public static final Potion potion_2 = registerPotion("potion_of_radiation_2", new Potion(new MobEffectInstance(CNEffects.RADIATION.get(), 410, 1)));

    public static Potion registerPotion(String name, Potion potion) {
        return Registry.register(BuiltInRegistries.POTION, CreateNuclear.asResource(name), potion);
    }

    public static void registerPotionsRecipes() {
        PotionBrewing.addMix(Potions.AWKWARD, CNItems.ENRICHED_YELLOWCAKE.get(), CNPotions.potion_1);
        PotionBrewing.addMix(potion_1, Items.REDSTONE, CNPotions.potion_augment_1);
        PotionBrewing.addMix(potion_1, Items.GLOWSTONE_DUST, CNPotions.potion_2);
    }


}
