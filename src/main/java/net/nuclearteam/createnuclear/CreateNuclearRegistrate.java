package net.nuclearteam.createnuclear;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.nuclearteam.createnuclear.potion.PotionBuilder;
import net.nuclearteam.createnuclear.potion.PotionBuilder.PotionFactory;

public class CreateNuclearRegistrate extends AbstractRegistrate<CreateNuclearRegistrate> {

    protected CreateNuclearRegistrate(String modid) {
        super(modid);
    }

    public static CreateNuclearRegistrate create(String modid) {
        return new CreateNuclearRegistrate(modid);
    }

    public <T extends Potion> PotionBuilder<T, CreateNuclearRegistrate> potion(String name, PotionFactory<T> factory) {
        return entry(name, callback -> PotionBuilder.create(this, self(), name, callback, factory));
    }

}
