package net.nuclearteam.createnuclear.content.uraniumOre;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.nuclearteam.createnuclear.api.radiation.IRadiationSource;

public class UraniumOreItem extends BlockItem implements IRadiationSource {
    private final double radiation;

    public UraniumOreItem(Block block, Properties properties, double radiation) {
        super(block, properties);
        this.radiation = radiation;
    }

    @Override
    public double getRadiation(ItemStack stack, LivingEntity entity) {
        return this.radiation * stack.getCount();
    }
}
