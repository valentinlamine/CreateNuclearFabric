package net.nuclearteam.createnuclear.content.radiation;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.nuclearteam.createnuclear.api.radiation.IRadiationSource;

public class RadiationItem extends Item implements IRadiationSource {

    private final double radiation;

    public RadiationItem(Item.Properties settings, double radiation) {
        super(settings);
        this.radiation = radiation;
    }

    @Override
    public double getRadiation(ItemStack stack, LivingEntity entity) {
        return this.radiation * stack.getCount();
    }
}