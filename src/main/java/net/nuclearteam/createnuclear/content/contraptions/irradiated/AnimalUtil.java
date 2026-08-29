package net.nuclearteam.createnuclear.content.contraptions.irradiated;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.Level;
import net.nuclearteam.createnuclear.CNItems;
import net.nuclearteam.createnuclear.foundation.utility.CreateNuclearLang;

import java.util.function.Predicate;

public class AnimalUtil {
    private AnimalUtil() {}

    public static boolean isFood(ItemStack stack, Ingredient foodItems) {
        return isFood(stack, foodItems, $ -> true);
    }

    public static boolean isFood(ItemStack stack, Ingredient foodItems, Predicate<ItemStack> extraTest) {
        if (stack.is(CNItems.YELLOWCAKE.get())) {
            return true;
        }

        return foodItems.test(stack) || extraTest.test(stack);
    }

    /**
     * Taming for irradiated creatures is still work in progress: rather than actually
     * taming the mob, this informs the player and consumes the interaction.
     */
    public static InteractionResult blockTamingWip(Player player, Level level) {
        if (!level.isClientSide) {
            player.displayClientMessage(Component.translatable("irradiated.taming.wip"), false);
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}