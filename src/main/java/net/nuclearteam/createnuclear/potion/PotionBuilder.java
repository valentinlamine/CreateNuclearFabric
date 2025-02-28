package net.nuclearteam.createnuclear.potion;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.AbstractBuilder;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonnullType;
import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistry;
import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.alchemy.Potion;
import net.nuclearteam.createnuclear.CreateNuclear;

@SuppressWarnings("null")
public class PotionBuilder<T extends Potion, P> extends AbstractBuilder<Potion, T, P, PotionBuilder<T, P>> {

    private MobEffectInstance effects;
    private final PotionFactory<T> factory;

    @FunctionalInterface
    public interface PotionFactory<T extends Potion> {
        T create(Potion potion);
    }

    public static <T extends Potion, P> PotionBuilder<T, P> create(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, PotionFactory<T> factory) {
        return new PotionBuilder<>(owner, parent, name, callback, factory);
    }


    protected PotionBuilder(AbstractRegistrate<?> owner, P parent, String name,
                            BuilderCallback callback, PotionFactory<T> factory) {
        super(owner, parent, name, callback, Registries.POTION);
        this.factory = factory;

    }


    public PotionBuilder<T, P> effect(MobEffect effect, int duration, int amplifier) {
        this.effects = new MobEffectInstance(effect, duration, amplifier);
        return this;
    }

    public PotionBuilder<T, P> effect(MobEffect effect, int duration) {
        this.effects = new MobEffectInstance(effect, duration, 0);
        return this;
    }

    public PotionBuilder<T, P> recipe(NonNullBiConsumer<FabricBrewingRecipeRegistry, T> brew) {
        return this;
    }


    @Override
    protected @NonnullType T createEntry() {
        return factory.create(new Potion(getName(), this.effects));
    }

    public T registerTest() {
        return Registry.register(BuiltInRegistries.POTION, CreateNuclear.asResource(getName()), createEntry());
    }
}
