package net.nuclearteam.createnuclear.foundation.item;

import com.simibubi.create.foundation.item.TooltipModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.world.level.Level;
import net.nuclearteam.createnuclear.CNItems;
import net.nuclearteam.createnuclear.CreateNuclear;
import net.nuclearteam.createnuclear.api.multiblock.rods.RodType;
import net.nuclearteam.createnuclear.foundation.utility.CreateNuclearLang;

import java.util.ArrayList;
import java.util.List;

public record RodsStats(Item item) implements TooltipModifier {

    public static RodsStats create(Item item) {
        return new RodsStats(item);
    }

    @Override
    public void modify(ItemStack stack, Player player, TooltipFlag flags, List<Component> tooltip) {
        if (player == null) return;
        List<Component> rodTypesStat = getRodTypeStats(item, player);
        if (!rodTypesStat.isEmpty()) {
            tooltip.add(CommonComponents.EMPTY);
            tooltip.addAll(rodTypesStat);
        }
    }

    private static List<Component> getRodTypeStats(Item item, Player player) {
        Level world = player.level();
        List<Component> components = new ArrayList<>();

        RodType rodType = RodType.resolveRodType(item, world);

        if (rodType.isNotEmptyItem()) {
            CreateNuclearLang.translate("tooltip.statRod")
                .style(ChatFormatting.GRAY)
                .addTo(components);

            CreateNuclearLang.builder()
                .add(CreateNuclearLang.translate("tooltip.baseRodHeat", rodType.baseRodHeat().get()))
                .addTo(components);
            CreateNuclearLang.builder()
                .add(CreateNuclearLang.translate("tooltip.proximityRodHeat", rodType.proximityRodHeat().get()))
                .addTo(components);
            CreateNuclearLang.builder()
                .add(CreateNuclearLang.translate("tooltip.rodTimer", rodType.rodTimer().get()))
                .addTo(components);
            CreateNuclearLang.builder()
                .add(CreateNuclearLang.translate("tooltip.ratio", rodType.ratio().get()))
                .addTo(components);
            CreateNuclearLang.builder()
                .add(CreateNuclearLang.translate("tooltip.typeRod", rodType.type().name()))
                .style(rodType.type().equals(RodType.TypeRod.FUEL) ? ChatFormatting.GREEN : ChatFormatting.AQUA)
                .addTo(components);
        }

        return components;

    }
}
