package net.nuclearteam.createnuclear.api.radiation;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public interface IRadiationSource {
    double getRadiation(ItemStack stack, LivingEntity entity);
}
