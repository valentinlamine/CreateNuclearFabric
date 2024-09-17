package net.nuclearteam.createnuclear.effects;

import net.minecraft.world.effect.MobEffect;
import net.nuclearteam.createnuclear.CreateNuclear;
import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.minecraft.core.registries.Registries;



public class CNEffects {
    public static final LazyRegistrar<MobEffect> EFFECTS = LazyRegistrar.create(Registries.MOB_EFFECT, CreateNuclear.MOD_ID);
    public static final RegistryObject<MobEffect> RADIATION = EFFECTS.register("radiation", RadiationEffect::new);

    public static void register() {
        EFFECTS.register();
    }

}