package net.nuclearteam.createnuclear.content.radiation;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.nuclearteam.createnuclear.api.radiation.IRadiationSource;

import java.util.function.Supplier;

public class RadiationBucketItem extends BucketItem implements IRadiationSource {
    private final double radiation;

    public RadiationBucketItem(Supplier<? extends Fluid> supplier, Properties builder, double radiation) {
        super(supplier.get(), builder);
        this.radiation = radiation;
    }

    @Override
    public double getRadiation(ItemStack stack, LivingEntity entity) {
        return this.radiation;
    }
}
