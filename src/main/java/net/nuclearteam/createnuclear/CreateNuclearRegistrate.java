package net.nuclearteam.createnuclear;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.Builder;
import com.tterrag.registrate.fabric.RegistryObject;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.alchemy.Potion;
import net.nuclearteam.createnuclear.potion.PotionBuilder;
import net.nuclearteam.createnuclear.potion.PotionBuilder.PotionFactory;

@MethodsReturnNonnullByDefault
public class CreateNuclearRegistrate extends AbstractRegistrate<CreateNuclearRegistrate> {

    protected CreateNuclearRegistrate(String modid) {
        super(modid);
    }

    public static CreateNuclearRegistrate create(String modid) {
        return new CreateNuclearRegistrate(modid);
    }

    @Override
    protected <R, T extends R> RegistryEntry<T> accept(String name, ResourceKey<? extends Registry<R>> type,
                                                       Builder<R, T, ?, ?> builder, NonNullSupplier<? extends T> creator,
                                                       NonNullFunction<RegistryObject<T>, ? extends RegistryEntry<T>> entryFactory) {

        return super.accept(name, type, builder, creator, entryFactory);
    }

    public <T extends Potion> PotionBuilder<T, CreateNuclearRegistrate> potion(String name, PotionFactory<T> factory) {
        return entry(name, callback -> PotionBuilder.create(this, self(), name, callback, factory));
    }

}
